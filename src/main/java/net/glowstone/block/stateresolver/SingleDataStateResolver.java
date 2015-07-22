package net.glowstone.block.stateresolver;

import com.google.common.base.Optional;
import net.glowstone.GlowCatalogType;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.GlowBlockType;
import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.manipulator.SingleValueData;


public abstract class SingleDataStateResolver<D extends SingleValueData<T, D>, T extends CatalogType> implements StateResolver {
    private GlowBlockState defaultState = null;
    private final Class<D> dataClass;
    private GlowBlockState[] states = null;
    private T defaultData;

    public SingleDataStateResolver(Class<D> dataClass, T defaultData) {
        this.dataClass = dataClass;
        this.defaultData = defaultData;
    }

    protected abstract T[] getTypes();

    protected abstract GlowDataManipulator<D> create(T type);

    protected abstract GlowBlockType getBlockType(T type);

    @Override
    public GlowBlockState getDefaultState() {
        if (defaultState == null) {
            defaultState = new GlowBlockState(getBlockType(defaultData), create(defaultData));
            defaultData = null;
        }

        return defaultState;
    }

    @Override
    public boolean accepts(Class<? extends DataManipulator<?>> dataClass) {
        return this.dataClass == dataClass;
    }

    @Override
    public byte getDataValueFromState(GlowBlockState state) {
        Optional<D> manipulator = state.getManipulator(dataClass);
        if (!manipulator.isPresent()) {
            return 0;
        }

        return (byte) ((GlowCatalogType) manipulator.get().getValue()).getNumericId();
    }

    @Override
    public GlowBlockState getStateFromDataValue(byte data) {
        if (states == null) {
            T[] types = getTypes();
            states = new GlowBlockState[types.length];
            for (int i = 0; i < types.length; i++) {
                states[i] = new GlowBlockState(getBlockType(types[i]), create(types[i]));
            }
        }

        return states[data];
    }
}
