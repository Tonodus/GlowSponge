package net.glowstone.block;

import com.google.common.base.Optional;
import net.glowstone.GlowCatalogType;
import net.glowstone.block.stateresolver.StateResolver;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.item.ItemBlock;
import org.spongepowered.api.text.translation.Translation;

/**
 * Implementation of {@link BlockType}.
 */
public final class GlowBlockType extends GlowCatalogType implements BlockType {
    private final BlockBehavior behavior;

    private final StateResolver resolver;

    private boolean tickRandomly;

    public GlowBlockType(String name, String id, int oldId, BlockBehavior behavior, StateResolver stateResolver) {
        super(name, id, oldId);

        this.behavior = behavior;
        this.resolver = stateResolver;
    }

    public BlockBehavior getBehavior() {
        return behavior;
    }

    @Override
    public GlowBlockState getDefaultState() {
        if (resolver != null) {
            return resolver.getDefaultState();
        } else {
            return new GlowBlockState(this);
        }
    }

    public boolean hasTypeOfData(Class<? extends DataManipulator<?>> dataClass) {
        if (resolver != null) {
            return resolver.accepts(dataClass);
        } else {
            return false;
        }
    }

    public byte getDataValueFromState(GlowBlockState state) {
        if (resolver != null) {
            return resolver.getDataValueFromState(state);
        } else {
            return 0;
        }
    }

    public GlowBlockState getStateFromDataValue(byte data) {
        if (resolver != null) {
            return resolver.getStateFromDataValue(data);
        } else {
            return getDefaultState();
        }
    }

    @Override
    public boolean getTickRandomly() {
        return tickRandomly;
    }

    @Override
    public void setTickRandomly(boolean tickRandomly) {
        this.tickRandomly = tickRandomly;
    }

    @Override
    public boolean isLiquid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSolidCube() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGaseous() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReplaceable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAffectedByGravity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean areStatisticsEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getEmittedLight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ItemBlock> getHeldItem() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String toString() {
        return "GlowBlockType{" + getNumericId() + "}";
    }

    @Override
    public Translation getTranslation() {
        throw new UnsupportedOperationException();
    }

    private static class Pair<A, B> {
        final A first;
        final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
}
