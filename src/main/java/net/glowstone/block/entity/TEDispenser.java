package net.glowstone.block.entity;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.data.manipulator.item.GlowInventoryItemData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.DispenserInventory;
import net.glowstone.inventory.serializer.OrderedContentInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.Dispenser;
import org.spongepowered.api.data.manipulator.item.InventoryItemData;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.world.Location;

public class TEDispenser extends TEContainer<DispenserInventory, InventoryItemData> implements Dispenser {

    public TEDispenser(Location block) {
        super(block, InventoryItemData.class);
        setSaveId("Trap");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass, Vector3d velocity) {
        return null;
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.DISPENSER;
    }

    @Override
    protected InventoryItemData createNew() {
        return new GlowInventoryItemData(getInventory());
    }

    @Override
    protected InventorySerializer<DispenserInventory> getSerializer() {
        return new OrderedContentInventorySerializer<DispenserInventory>() {
            @Override
            protected DispenserInventory create(CompoundTag tag) {
                return new DispenserInventory(TEDispenser.this);
            }
        };
    }
}
