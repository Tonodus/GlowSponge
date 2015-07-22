package net.glowstone.data;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.data.*;
import org.spongepowered.api.service.persistence.InvalidDataException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GlowDataHolder implements DataHolder {
    private final Map<Class<?>, DataManipulatorBuilder<?>> builders;
    private final Map<Class<?>, DataManipulator<?>> manipulatorMap;
    private final Map<Class<?>, Property<?, ?>> properties;

    public GlowDataHolder() {
        manipulatorMap = new HashMap<>();

        properties = new HashMap<>();
        fillProperties(properties);

        builders = new HashMap<>();
        fillAcceptedData(builders);
    }

    protected void fillProperties(Map<Class<?>, Property<?, ?>> map) {
        //To override by sub-classes
        //do nothing => no properties
    }

    protected void fillAcceptedData(Map<Class<?>, DataManipulatorBuilder<?>> map) {
        //To override by sub-classes
        //do nothing => no accepted data
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Class<T> dataClass) {
        return (Optional) Optional.fromNullable(manipulatorMap.get(dataClass));
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Class<T> manipulatorClass) {
        DataManipulator<?> dm = manipulatorMap.get(manipulatorClass);
        if (dm != null) {
            return (Optional) Optional.of(dm);
        }
        if (!isCompatible(manipulatorClass)) {
            return Optional.absent();
        }
        return Optional.fromNullable(createNew(manipulatorClass));
    }

    private <T extends DataManipulator<T>> T createNew(Class<T> manipulatorClass) {
        DataManipulatorBuilder<?> builder = builders.get(manipulatorClass);
        if (builder == null) {
            return null;
        }
        return (T) builder.create();
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Class<T> manipulatorClass) {
        if (manipulatorMap.containsKey(manipulatorClass)) {
            manipulatorMap.remove(manipulatorClass);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Class<T> manipulatorClass) {
        return builders.containsKey(manipulatorClass);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData) {
        return offer(manipulatorData, DataPriority.PRE_MERGE);
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(T manipulatorData, DataPriority priority) {
        Class clazz = manipulatorData.getClass();
        DataManipulator old = manipulatorMap.get(clazz); //TODO: don't rely on getClass?
        if (old == null) {
            manipulatorMap.put(clazz, manipulatorData);
            return GlowDataTransactionResult.builder(DataTransactionResult.Type.SUCCESS).build();
        }
        switch (priority) {
            case DATA_HOLDER:
                return GlowDataTransactionResult.builder(DataTransactionResult.Type.CANCELLED).rejected(manipulatorData).build();
            case DATA_MANIPULATOR:
                manipulatorMap.put(clazz, manipulatorData);
                return GlowDataTransactionResult.builder(DataTransactionResult.Type.SUCCESS).replaced(old).build();
            case PRE_MERGE:
            case POST_MERGE:
                return null; //TODO merge
        }
        return null;
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators() {
        return ImmutableList.copyOf(manipulatorMap.values());
    }


    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Class<T> propertyClass) {
        return (Optional) Optional.fromNullable(properties.get(propertyClass));
    }

    @Override
    public Collection<Property<?, ?>> getProperties() {
        return ImmutableList.copyOf(properties.values());
    }


    @Override
    public boolean validateRawData(DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(DataContainer container) throws InvalidDataException {
        Set<DataQuery> keys = container.getKeys(false);
        for (DataQuery key : keys) {
            Object o = container.get(key).get();
            //TODO affect data based on o
        }
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = new MemoryDataContainer();
        for (Map.Entry<Class<?>, DataManipulator<?>> manipulatorEntry : manipulatorMap.entrySet()) {
            container.set(DataQuery.of(manipulatorEntry.getKey().getSimpleName()), manipulatorEntry.getValue().toContainer());
        }
        return container;
    }
}
