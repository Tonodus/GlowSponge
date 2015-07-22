package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataManipulator;

public interface StateResolver {
    BlockState getDefaultState();

    boolean accepts(Class<? extends DataManipulator<?>> dataClass);

    byte getDataValueFromState(GlowBlockState state);

    GlowBlockState getStateFromDataValue(byte data);
}
