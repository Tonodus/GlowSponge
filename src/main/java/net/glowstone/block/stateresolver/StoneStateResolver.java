package net.glowstone.block.stateresolver;

import net.glowstone.data.manipulator.GlowDataManipulator;
import net.glowstone.data.manipulator.block.GlowStoneData;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.block.StoneData;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.StoneTypes;

public class StoneStateResolver extends SingleDataStateResolver<StoneData, StoneType> {

    public StoneStateResolver() {
        super(BlockTypes.STONE, StoneData.class, new GlowStoneData(StoneTypes.STONE));
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
}
