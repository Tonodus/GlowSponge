package net.glowstone.data.manipulator;

import org.spongepowered.api.data.manipulator.IntData;

public abstract class GlowIntData<T extends IntData<T>> extends GlowSingleValueData<Integer, T> implements IntData<T> {
    private final int min, max;

    public GlowIntData(Class<T> manipulatorClass, int defaultValue, int min, int max) {
        super(manipulatorClass, defaultValue);
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer getMinValue() {
        return min;
    }

    @Override
    public Integer getMaxValue() {
        return max;
    }

    @Override
    public int compareTo(T o) {
        return o.getValue().compareTo(getValue());
    }
}
