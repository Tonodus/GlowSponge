package net.glowstone.entity.objects;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.networking.Message;
import net.glowstone.entity.GlowEntity;
import net.glowstone.util.MutableVector;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.item.GlowItemStackBuilder;
import net.glowstone.net.message.play.entity.*;
import net.glowstone.util.Position;
import org.spongepowered.api.data.manipulator.RepresentedItemData;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an item that is also an {@link net.glowstone.entity.GlowEntity} within the world.
 * @author Graham Edgecombe
 */
public final class GlowItem extends GlowEntity implements Item {

    /**
     * The number of ticks (equal to 5 minutes) that item entities should live for.
     */
    private static final int LIFETIME = 5 * 60 * 20;

    /**
     * The remaining delay until this item may be picked up.
     */
    private int pickupDelay;

    /**
     * A player to bias this item's pickup selection towards.
     */
    private GlowPlayer biasPlayer;

    /**
     * Creates a new item entity.
     * @param location The location of the entity.
     * @param item The item stack the entity is carrying.
     */
    public GlowItem(Location location, ItemStack item) {
        super(location);
        setItemStack(item);
        setBoundingBox(0.25, 0.25);
        pickupDelay = 40;
    }

    private boolean getPickedUp(GlowPlayer player) {
        // todo: fire PlayerPickupItemEvent in a way that allows for 'remaining' calculations

        ItemStack stack = getItemStack();
        int oldQuantity = stack.getQuantity();
        player.getInventory().offer(stack);
        //player.updateInventory(); // workaround for player editing slot & it immediately being filled again
        if (stack.getQuantity() > 0) {
            setItemStack(stack);
        }

        if (oldQuantity > stack.getQuantity()) {
            CollectItemMessage message = new CollectItemMessage(getEntityId(), player.getEntityId());
            world.playSound(SoundTypes.ITEM_PICKUP, location.toVector3d(), 0.3f, (float) (1 + Math.random()));
            for (GlowPlayer other : world.getRawPlayers()) {
                if (other.canSeeEntity(this)) {
                    other.getSession().send(message);
                }
            }
            remove();
            return true;
        } else {
            return false;
        }
    }

    public void setBias(GlowPlayer player) {
        biasPlayer = player;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Overrides

    @Override
    public EntityType getType() {
        return EntityTypes.DROPPED_ITEM;
    }

    @Override
    public void pulse() {
        super.pulse();

        // decrement pickupDelay if it's less than the NBT maximum
        if (pickupDelay > 0) {
            if (pickupDelay < Short.MAX_VALUE) {
                --pickupDelay;
            }
            if (pickupDelay < 20 && biasPlayer != null) {
                // check for the bias player
                for (Entity entity : getNearbyEntities(1, 0.5, 1)) {
                    if (entity == biasPlayer && getPickedUp((GlowPlayer) entity)) {
                        break;
                    }
                }
            }
        } else {
            // check for nearby players
            for (Entity entity : getNearbyEntities(1, 0.5, 1)) {
                if (entity instanceof GlowPlayer && getPickedUp((GlowPlayer) entity)) {
                    break;
                }
            }
        }

        // teleport to actual position fairly frequently in order to account
        // for missing/incorrect physics simulation
        if (getTicksLived() % (2 * 20) == 0) {
            teleported = true;
        }

        // disappear if we've lived too long
        if (getTicksLived() >= LIFETIME) {
            remove();
        }
    }

    @Override
    protected void pulsePhysics() {
        // simple temporary gravity - should eventually be improved to be real
        // physics and moved up to GlowEntity

        // continuously set velocity to 0 to make things look more normal
        setVelocity(new Vector3d(0, 0, 0));

        if (world.getBlockType(location.toVector3i()).isSolidCube()) {
            // float up out of solid blocks
            location.add(0, 0.2, 0);
        } else {
            // fall down on top of solid blocks
            MutableVector down = location.addNew(0, -0.1, 0);
            if (!world.getBlockType(down.toVector3i()).isSolidCube()) {
                setRawLocation(down);
            }
        }

        super.pulsePhysics();
    }

    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);

        int yaw = Position.getIntYaw(this.yaw);
        int pitch = Position.getIntPitch(this.pitch);

        return Arrays.asList(
                new SpawnObjectMessage(id, SpawnObjectMessage.ITEM, x, y, z, pitch, yaw),
                new EntityMetadataMessage(id, metadata.getEntryList()),
                // these keep the client from assigning a random velocity
                new EntityTeleportMessage(id, x, y, z, yaw, pitch),
                new EntityVelocityMessage(id, getVelocity())
        );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Item stuff

    public int getPickupDelay() {
        return pickupDelay;
    }

    public void setPickupDelay(int delay) {
        pickupDelay = delay;
    }

    public ItemStack getItemStack() {
        return metadata.getItem(MetadataIndex.ITEM_ITEM);
    }

    public void setItemStack(ItemStack stack) {
        // stone is the "default state" for the item stack according to the client
        metadata.set(MetadataIndex.ITEM_ITEM, stack == null ? GlowItemStackBuilder.build(ItemTypes.STONE) : GlowItemStackBuilder.copy(stack));
    }

    @Override
    public RepresentedItemData getItemData() {
        return null;
    }
}
