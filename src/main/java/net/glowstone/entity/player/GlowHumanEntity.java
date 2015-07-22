package net.glowstone.entity.player;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.networking.Message;
import com.google.common.base.Optional;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowAgent;
import net.glowstone.util.MutableVector;
import net.glowstone.entity.meta.profile.PlayerProfile;
import net.glowstone.entity.objects.GlowItem;
import net.glowstone.inventory.inventories.GlowContainer;
import net.glowstone.inventory.inventories.GlowPlayerInventory;
import net.glowstone.io.PlayerDataService;
import net.glowstone.item.GlowItemStack;
import net.glowstone.net.message.play.entity.EntityEquipmentMessage;
import net.glowstone.net.message.play.entity.EntityHeadRotationMessage;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import net.glowstone.util.Position;
import net.glowstone.world.GlowWorld;
import org.apache.commons.lang3.Validate;
import org.bukkit.util.Vector;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.type.CarriedInventory;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a human entity, such as an NPC or a player.
 */
public abstract class GlowHumanEntity extends GlowAgent implements Human {

    /**
     * The User containing all the entity's data.
     */
    private final GlowUser user;

    /**
     * The inventory of this human.
     */
    private final GlowPlayerInventory inventory;

    /**
     * The inventory this human is looking at.
     */
    private Inventory openInventory = null;

    /**
     * The ender chest inventory of this human.
     */
    private final GlowContainer enderChest = new GlowContainer(this, Inventory.ENDER_CHEST);

    /**
     * The item the player has on their cursor.
     */
    private ItemStack itemOnCursor;

    /**
     * Whether this human is sleeping or not.
     */
    protected boolean sleeping = false;

    /**
     * How long this human has been sleeping.
     */
    private int sleepingTicks = 0;

    /**
     * The player's active game mode
     */
    private GameMode gameMode;


    /**
     * Creates a human within the specified world and with the specified name.
     * @param location The location.
     * @param profile The human's profile with name and UUID information.
     */
    public GlowHumanEntity(GlowServer server, GlowWorld world, Vector3d location, PlayerProfile profile, PlayerDataService.PlayerReader reader) {
        super(server, world, location);
        this.user = new GlowUser(server, profile, reader);
        gameMode = server.getDefaultGameMode();
        this.inventory = new GlowPlayerInventory(this);
        addViewer(inventoryView.getTopInventory());
        addViewer(inventoryView.getBottomInventory());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    @Override
    public void setUniqueId(UUID uuid) {
        // silently allow setting the same UUID again
        if (!user.getUniqueId().equals(uuid)) {
            throw new IllegalStateException("UUID of " + this + " is already " + user.getUniqueId());
        }
    }

    @Override
    public List<Message> createSpawnMessage() {
        List<Message> result = new LinkedList<>();

        // spawn player
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);
        int yaw = Position.getIntYaw(this.yaw);
        int pitch = Position.getIntPitch(this.pitch);
        result.add(new SpawnPlayerMessage(id, user.getUniqueId(), x, y, z, yaw, pitch, 0, metadata.getEntryList()));

        // head facing
        result.add(new EntityHeadRotationMessage(id, yaw));

        // equipment
        result.add(new EntityEquipmentMessage(id, 0, getItemInHand().orNull()));
        result.add(new EntityEquipmentMessage(id, 1, getHelmet().orNull()));
        result.add(new EntityEquipmentMessage(id, 2, getChestplate().orNull()));
        result.add(new EntityEquipmentMessage(id, 3, getLeggings().orNull()));
        result.add(new EntityEquipmentMessage(id, 4, getBoots().orNull()));

        return result;
    }

    @Override
    public void pulse() {
        super.pulse();
        if (sleeping) {
            ++sleepingTicks;
        } else {
            sleepingTicks = 0;
        }
    }

    /**
     * Gets the User with this Human's data.
     * @return the user object
     */
    public final GlowUser getUser() {
        return user;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Properties

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return user.getUniqueId();
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public int getSleepTicks() {
        return sleepingTicks;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode mode) {
        gameMode = mode;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Inventory

    @Override
    public boolean canEquip(EquipmentType type) {
        return type == EquipmentTypes.HEADWEAR ||
                type == EquipmentTypes.CHESTPLATE ||
                type == EquipmentTypes.LEGGINGS ||
                type == EquipmentTypes.BOOTS ||
                type == EquipmentTypes.EQUIPPED;
    }

    @Override
    public boolean canEquip(EquipmentType type, ItemStack equipment) {
        return false;
    }

    @Override
    public boolean equip(EquipmentType type, ItemStack equipment) {
        return false;
    }

    @Override
    public Optional<ItemStack> getEquipped(EquipmentType type) {
        return null;
    }

    @Override
    public CarriedInventory<? extends Carrier> getInventory() {
        return inventory;
    }

    @Override
    public Optional<Inventory> getOpenInventory() {
        return Optional.fromNullable(openInventory);
    }

    @Override
    public void openInventory(Inventory inventory) {
        this.openInventory = inventory;
    }

    @Override
    public void closeInventory() {
        this.openInventory = null;
    }

    @Override
    public boolean isViewingInventory() {
        return openInventory != null;
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return inventory.getEquipped(EquipmentTypes.HEADWEAR);
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        inventory.equip(EquipmentTypes.HEADWEAR, helmet);
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return inventory.getEquipped(EquipmentTypes.CHESTPLATE);
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        inventory.equip(EquipmentTypes.CHESTPLATE, chestplate);
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return inventory.getEquipped(EquipmentTypes.LEGGINGS);
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        inventory.equip(EquipmentTypes.LEGGINGS, leggings);
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return inventory.getEquipped(EquipmentTypes.BOOTS);
    }

    @Override
    public void setBoots(ItemStack boots) {
        inventory.equip(EquipmentTypes.BOOTS, boots);
    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return inventory.getEquipped(EquipmentTypes.EQUIPPED);
    }

    @Override
    public void setItemInHand(ItemStack itemInHand) {
        inventory.equip(EquipmentTypes.EQUIPPED, itemInHand);
    }

    private void addViewer(Inventory inventory) {
        if (inventory instanceof GlowContainer) {
            ((GlowContainer) inventory).addViewer(this);
        }
    }

    private void removeViewer(Inventory inventory) {
        if (inventory instanceof GlowContainer) {
            ((GlowContainer) inventory).removeViewer(this);
        }
    }

    /**
     * Drops the item this entity currently has in its hands and remove the
     * item from the HumanEntity's inventory.
     * @param wholeStack True if the whole stack should be dropped
     */
    public void dropItemInHand(boolean wholeStack) {
        ItemStack stack = getItemInHand().orNull();
        if (stack == null || stack.getQuantity() < 1) {
            return;
        }

        ItemStack dropping = new GlowItemStack(stack);
        if (!wholeStack) {
            dropping.setQuantity(1);
        }

        GlowItem dropped = drop(dropping);
        if (dropped == null) {
            return;
        }

        if (stack.getQuantity() == 1 || wholeStack) {
            setItemInHand(null);
        } else {
            ItemStack now = new GlowItemStack(stack);
            now.setQuantity(now.getQuantity() - 1);
            setItemInHand(now);
        }
    }

    /**
     * Spawns a new {@link GlowItem} in the world, as if this HumanEntity had
     * dropped it. Note that this does NOT remove the item from the inventory.
     * @param stack The item to drop
     * @return the GlowItem that was generated, or null if the spawning was cancelled
     * @throws IllegalArgumentException if the stack is null or has an amount less than one
     */
    public GlowItem drop(ItemStack stack) {
        Validate.notNull(stack, "stack must not be null");
        Validate.isTrue(stack.getQuantity() > 0, "stack amount must be greater than zero");

        MutableVector dropLocation = location.clone();
        dropLocation.add(0, getEyeHeight(true) - 0.3, 0);
        GlowItem dropItem = world.dropItem(dropLocation, stack);
        Vector vel = location.getDirection().multiply(0.3f);
        vel.setY(vel.getY() + 0.1F);
        dropItem.setVelocity(vel);
        return dropItem;
    }
}
