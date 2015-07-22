package net.glowstone.block.entity;

import com.google.common.base.Optional;
import net.glowstone.data.GlowSingleDataDataHolder;
import org.spongepowered.api.data.*;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.world.Location;

import java.util.Collection;

public abstract class GlowSingleDataTileEntity<T extends DataManipulator<T>> extends GlowTileEntity {
    private final GlowSingleDataDataHolder<T> dataHolder;

    public GlowSingleDataTileEntity(Location location, Class<T> dataClass) {
        super(location);
        dataHolder = new GlowSingleDataDataHolder<T>(dataClass) {
            @Override
            protected T createNew() {
                return GlowSingleDataTileEntity.this.createNew();
            }

            @Override
            public DataContainer toContainer() {
                return GlowSingleDataTileEntity.this.toContainer();
            }
        };
    }

    protected abstract T createNew();

    protected T getRawData() {
        return dataHolder.getRawData();
    }

    protected void setRawData(T data) {
        dataHolder.setRawData(data);
    }

    @Override
    public Optional<T> getData() {
        return dataHolder.getData();
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        return dataHolder.getData(dataClass);
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        return dataHolder.getOrCreate(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        return dataHolder.remove(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return dataHolder.isCompatible(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return dataHolder.offer(manipulatorData);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        return dataHolder.offer(manipulatorData, priority);
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators() {
        return dataHolder.getManipulators();
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return dataHolder.getProperty(propertyClass);
    }

    @Override
    public Collection<Property<?, ?>> getProperties() {
        return dataHolder.getProperties();
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return dataHolder.validateRawData(container);
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {
        dataHolder.setRawData(container);
    }
}
