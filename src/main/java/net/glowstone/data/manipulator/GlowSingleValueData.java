package net.glowstone.data.manipulator;

import org.spongepowered.api.data.manipulator.SingleValueData;

public abstract class GlowSingleValueData<V, T extends SingleValueData<V, T>> extends GlowDataManipulator<T> implements SingleValueData<V, T> {
    protected V value;

    public GlowSingleValueData(Class<T> manipulatorClass, V defaultValue) {
        super(manipulatorClass);
        this.value = defaultValue;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public T setValue(V value) {
        this.value = value;
        return (T) this;
    }
}
