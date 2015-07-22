package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockType;
import net.glowstone.data.manipulator.GlowDataManipulator;
import net.glowstone.data.manipulator.block.GlowDirtData;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.block.DirtData;
import org.spongepowered.api.data.type.DirtType;
import org.spongepowered.api.data.type.DirtTypes;

public class DirtStateResolver extends SingleDataStateResolver<DirtData, DirtType> {
    public DirtStateResolver() {
        super(DirtData.class, DirtTypes.DIRT);
    }

    @Override
    protected DirtType[] getTypes() {
        return new DirtType[]{
                DirtTypes.DIRT,
                DirtTypes.PODZOL,
                DirtTypes.COARSE_DIRT
        };
    }

    @Override
    protected GlowDataManipulator<DirtData> create(DirtType type) {
        return new GlowDirtData(type);
    }

    @Override
    protected GlowBlockType getBlockType(DirtType type) {
        return BlockTypes.DIRT;
    }
}
