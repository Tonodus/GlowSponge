package net.glowstone.data;

import com.google.common.base.Optional;
import org.spongepowered.api.data.*;
import org.spongepowered.api.service.persistence.InvalidDataException;

import java.util.Arrays;
import java.util.Collection;

public abstract class GlowSingleDataDataHolder<T extends DataManipulator<T>> implements DataHolder {
    private final Class<? super T> dataClass;
    protected T data;
    private final boolean canRemove;

    public GlowSingleDataDataHolder(Class<? super T> dataClass) {
        this(dataClass, null);
    }

    public GlowSingleDataDataHolder(Class<? super T> dataClass, T value) {
        this.dataClass = dataClass;
        this.data = value;
        canRemove = false;
    }

    public Optional<T> getData() {
        return Optional.fromNullable(data);
    }

    public T getRawData() {
        return data;
    }

    public void setRawData(T data) {
        this.data = data;
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        if (this.dataClass == dataClass) {
            if (data != null) {
                return (Optional<T>) Optional.of(data.copy());
            } else {
                return Optional.absent();
            }
        } else {
            return Optional.absent();
        }
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        if (manipulatorClass == dataClass) {
            if (data == null) {
                return (Optional<T>) Optional.of(createNew());
            } else {
                return (Optional<T>) Optional.of(data);
            }
        } else {
            return Optional.absent();
        }
    }

    protected abstract T createNew();

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        if (canRemove && manipulatorClass == dataClass) {
            data = null;
            return true;
        }
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return manipulatorClass == dataClass;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        //TODO: correct priority?
        return offer(manipulatorData, DataPriority.PRE_MERGE);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        return null;
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators() {
        return (Collection) Arrays.asList(data.copy());
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return null;
    }

    @Override
    public Collection<Property<?, ?>> getProperties() {
        return null;
    }

    @Override
    public boolean validateRawData(DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {

    }
}
