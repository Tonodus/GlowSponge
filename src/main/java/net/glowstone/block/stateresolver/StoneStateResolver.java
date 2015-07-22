package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockType;
import net.glowstone.data.manipulator.GlowDataManipulator;
import net.glowstone.data.manipulator.block.GlowStoneData;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.block.StoneData;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.StoneTypes;

public class StoneStateResolver extends SingleDataStateResolver<StoneData, StoneType> {

    public StoneStateResolver() {
        super(StoneData.class, StoneTypes.STONE);
    }

    @Override
    protected StoneType[] getTypes() {
        return new StoneType[]{
                StoneTypes.STONE
                //...
        };
    }

    @Override
    protected GlowDataManipulator<StoneData> create(StoneType type) {
        return new GlowStoneData(type);
    }

    @Override
    protected GlowBlockType getBlockType(StoneType type) {
        return BlockTypes.STONE;
    }
}
