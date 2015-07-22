package net.glowstone.block.entity;

import net.glowstone.data.manipulator.item.GlowInventoryItemData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.DropperInventory;
import net.glowstone.inventory.serializer.OrderedContentInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.Dropper;
import org.spongepowered.api.data.manipulator.item.InventoryItemData;
import org.spongepowered.api.world.Location;

public class TEDropper extends TEContainer<DropperInventory, InventoryItemData> implements Dropper {

    public TEDropper(Location block) {
        super(block, InventoryItemData.class);
        setSaveId("Dropper");
    }


    @Override
    protected InventorySerializer<DropperInventory> getSerializer() {
        return new OrderedContentInventorySerializer<DropperInventory>() {
            @Override
            protected DropperInventory create(CompoundTag tag) {
                return new DropperInventory(TEDropper.this);
            }
        };
    }

    @Override
    protected InventoryItemData createNew() {
        return new GlowInventoryItemData(getInventory());
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.DROPPER;
    }
}
