package net.glowstone.block.stateresolver;

import com.google.common.base.Optional;
import net.glowstone.GlowCatalogType;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.GlowBlockType;
import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.manipulator.SingleValueData;


public abstract class SingleDataStateResolver<D extends SingleValueData<T, D>, T extends CatalogType> implements StateResolver {
    private final BlockState defaultState;
    private final Class<D> dataClass;
    private final GlowBlockState[] states;

    public SingleDataStateResolver(GlowBlockType blockType, Class<D> dataClass, DataManipulator<D> defaultStateData) {
        this.dataClass = dataClass;
        this.defaultState = new GlowBlockState(blockType, (GlowDataManipulator) defaultStateData);
        T[] types = getTypes();
        this.states = new GlowBlockState[types.length];
        for (int i = 0; i < states.length; i++) {
            states[i] = new GlowBlockState(blockType, create(types[i]));
        }
    }

    protected abstract T[] getTypes();

    protected abstract GlowDataManipulator<D> create(T type);

    @Override
    public BlockState getDefaultState() {
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
        return states[data];
    }
}
