package net.glowstone.block;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;

public final class GlowBlockState implements BlockState {
    private static final ImmutableList<GlowDataManipulator<?>> EMPTY_LIST = ImmutableList.of();
    private final GlowBlockType type;
    private final ImmutableList<GlowDataManipulator<?>> properties;

    public GlowBlockState(GlowBlockType type, GlowDataManipulator<?>... properties) {
        this(type, ImmutableList.copyOf(properties));
    }

    private GlowBlockState(GlowBlockType type, ImmutableList<GlowDataManipulator<?>> properties) {
        this.type = type;
        this.properties = properties;
    }

    public static GlowBlockState from(BlockType type, Iterable<DataManipulator<?>> dataManipulators) {
        GlowBlockType blockType = (GlowBlockType) type;
        ImmutableList.Builder<GlowDataManipulator<?>> properties = ImmutableList.builder();
        for (DataManipulator<?> dm : dataManipulators) {
            if (dm instanceof GlowDataManipulator) {
                properties.add((GlowDataManipulator<?>) dm);
            }
        }
        return new GlowBlockState(blockType, properties.build());
    }

    public GlowBlockState(GlowBlockType type) {
        this(type, EMPTY_LIST);
    }

    @Override
    public GlowBlockType getType() {
        return type;
    }

    @Override
    public ImmutableCollection<DataManipulator<?>> getManipulators() {
        ImmutableList.Builder<DataManipulator<?>> builder = ImmutableList.builder();
        for (DataManipulator<?> manipulator : properties) {
            builder.add(manipulator.copy());
        }
        return builder.build();
    }

    @Override
    public <M extends DataManipulator<M>> Optional<M> getManipulator(Class<M> manipulatorClass) {
        for (GlowDataManipulator<?> property : properties) {
            if (property.getManipulatorClass() == manipulatorClass) {
                return (Optional) Optional.of(property.copy());
            }
        }

        return Optional.absent();
    }

    @Override
    public <M extends DataManipulator<M>> Optional<BlockState> withData(M newManipulator) {
        if (!(newManipulator instanceof GlowDataManipulator)) {
            return Optional.absent();
        }

        if (!type.hasTypeOfData(((GlowDataManipulator) newManipulator).getManipulatorClass())) {
            return Optional.absent();
        }

        ImmutableList.Builder<GlowDataManipulator<?>> builder = ImmutableList.builder();
        for (GlowDataManipulator<?> manipulator : properties) {
            if (manipulator.getManipulatorClass() == newManipulator.getClass()) {
                builder.add((GlowDataManipulator) newManipulator);
            } else {
                builder.add(manipulator);
            }
        }
        ImmutableList<GlowDataManipulator<?>> newProperties = builder.build();
        return Optional.of((BlockState) new GlowBlockState(type, newProperties));
    }

    @Override
    public <M extends DataManipulator<M>> Optional<BlockState> withoutData(Class<M> rmManipulator) {
        ImmutableList.Builder<GlowDataManipulator<?>> builder = ImmutableList.builder();
        for (GlowDataManipulator<?> manipulator : properties) {
            if (manipulator.getManipulatorClass() != rmManipulator) {
                builder.add(manipulator);
            }
        }
        ImmutableList<GlowDataManipulator<?>> newProperties = builder.build();
        return Optional.of((BlockState) new GlowBlockState(type, newProperties));
    }

    @Override
    public DataContainer toContainer() {
        MemoryDataContainer container = new MemoryDataContainer();
        container.set(DataQuery.of("BlockType"), getType().getId());
        container.set(DataQuery.of("Data"), properties);
        return container;
    }
}
