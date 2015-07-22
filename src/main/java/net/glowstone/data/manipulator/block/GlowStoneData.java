package net.glowstone.data.manipulator.block;

import net.glowstone.data.manipulator.GlowSingleValueData;
import org.spongepowered.api.data.manipulator.block.StoneData;
import org.spongepowered.api.data.type.StoneType;
import org.spongepowered.api.data.type.StoneTypes;

public class GlowStoneData extends GlowSingleValueData<StoneType, StoneData> implements StoneData {
    public GlowStoneData() {
        this(StoneTypes.STONE);
    }

    public GlowStoneData(StoneType stoneType) {
        super(StoneData.class, stoneType);
    }

    @Override
    public StoneData copy() {
        return new GlowStoneData(getValue());
    }

    @Override
    public int compareTo(StoneData o) {
        return 0;
    }
}
