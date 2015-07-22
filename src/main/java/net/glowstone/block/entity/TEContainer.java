package net.glowstone.block.entity;

import com.google.common.base.Optional;
import net.glowstone.inventory.InventorySerializer;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.*;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for container tile entities (those with inventories).
 */
public abstract class TEContainer<I extends TileEntityInventory, D extends DataManipulator<D>> extends GlowSingleDataTileEntity<D> implements TileEntityCarrier {
    private final InventorySerializer<I> inventorySerializer;

    private I inventory;

    public TEContainer(Location location, Class<D> dataClazz) {
        super(location, dataClazz);
        this.inventorySerializer = getSerializer();
    }

    protected abstract InventorySerializer<I> getSerializer();

    @Override
    public TileEntityInventory<TileEntityCarrier> getInventory() {
        return inventory;
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        inventory = inventorySerializer.deserialize(tag);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        inventorySerializer.serialize(inventory, tag);
    }

    // Not applicable for this by default

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        return Optional.absent();
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators() {
        return new ArrayList<>();
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        return Optional.absent();
    }

    @Override
    public Collection<Property<?, ?>> getProperties() {
        return new ArrayList<>();
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return Optional.absent();
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {

    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return false;
    }
}
