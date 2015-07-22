package net.glowstone.block.entity;

import net.glowstone.data.manipulator.item.GlowInventoryItemData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.HopperInventory;
import net.glowstone.inventory.serializer.OrderedContentInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.Hopper;
import org.spongepowered.api.data.manipulator.item.InventoryItemData;
import org.spongepowered.api.world.Location;

public class TEHopper extends TEContainer<HopperInventory, InventoryItemData> implements Hopper {

    public TEHopper(Location block) {
        super(block, InventoryItemData.class);
        setSaveId("Hopper");
    }

    @Override
    public void transferItem() {

    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.HOPPER;
    }

    @Override
    protected InventorySerializer<HopperInventory> getSerializer() {
        return new OrderedContentInventorySerializer<HopperInventory>() {
            @Override
            protected HopperInventory create(CompoundTag tag) {
                return new HopperInventory(TEHopper.this);
            }
        };
    }

    @Override
    protected InventoryItemData createNew() {
        return new GlowInventoryItemData(getInventory());
    }
}
