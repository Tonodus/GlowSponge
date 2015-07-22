package net.glowstone.block.entity;

import net.glowstone.data.manipulator.tileentity.GlowBrewingData;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.inventory.inventories.tileentity.BrewingInventory;
import net.glowstone.inventory.serializer.BrewingInventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.BrewingStand;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.manipulator.tileentity.BrewingData;
import org.spongepowered.api.world.Location;

public class TEBrewingStand extends TEContainer<BrewingInventory, BrewingData> implements BrewingStand {
    public TEBrewingStand(Location location) {
        super(location, BrewingData.class);
        setSaveId("Cauldron");
    }

    @Override
    protected InventorySerializer<BrewingInventory> getSerializer() {
        return new BrewingInventorySerializer(this);
    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.BREWING_STAND;
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);

        if (tag.isInt("BrewTime")) {
            getRawData().setRemainingBrewTime(tag.getInt("BrewTime"));
        }
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putInt("BrewTime", getRawData().getRemainingBrewTime());
    }

    @Override
    public boolean brew() {
        return false;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = super.toContainer();
        dataContainer.set(DataQuery.of("BrewTime"), getRawData().getRemainingBrewTime());
        return dataContainer;
    }

    @Override
    protected BrewingData createNew() {
        return new GlowBrewingData();
    }
}
