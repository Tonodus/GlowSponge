package net.glowstone.block.entity;

import net.glowstone.data.manipulator.tileentity.GlowFurnaceData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.FurnaceInventory;
import net.glowstone.inventory.serializer.OrderedContentInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.Furnace;
import org.spongepowered.api.data.manipulator.tileentity.FurnaceData;
import org.spongepowered.api.world.Location;

public class TEFurnace extends TEContainer<FurnaceInventory, FurnaceData> implements Furnace {
    public TEFurnace(Location block) {
        super(block, FurnaceData.class);
        setSaveId("Furnace");
    }


    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putShort("BurnTime", getRawData().getRemainingBurnTime());
        tag.putShort("CookTime", getRawData().getRemainingCookTime());
    }

    @Override
    protected InventorySerializer<FurnaceInventory> getSerializer() {
        return new OrderedContentInventorySerializer<FurnaceInventory>() {
            @Override
            protected FurnaceInventory create(CompoundTag tag) {
                return new FurnaceInventory(TEFurnace.this);
            }
        };
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        if (tag.isShort("BurnTime")) {
            getRawData().setRemainingBurnTime(tag.getShort("BurnTime"));
        }
        if (tag.isShort("CookTime")) {
            getRawData().setRemainingCookTime(tag.getShort("CookTime"));
        }
    }


    @Override
    public boolean smelt() {
        return false;
    }

    @Override
    protected FurnaceData createNew() {
        return new GlowFurnaceData();
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.FURNACE;
    }
}
