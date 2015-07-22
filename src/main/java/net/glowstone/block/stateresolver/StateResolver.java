package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockState;
import net.glowstone.block.GlowBlockType;
import org.spongepowered.api.data.DataManipulator;

public interface StateResolver {
    GlowBlockState getDefaultState();

    boolean accepts(Class<? extends DataManipulator<?>> dataClass);

    byte getDataValueFromState(GlowBlockState state);

    GlowBlockState getStateFromDataValue(byte data);
}
