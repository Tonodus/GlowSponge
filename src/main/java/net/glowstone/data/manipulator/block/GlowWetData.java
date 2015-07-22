package net.glowstone.data.manipulator.block;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.WetData;

public class GlowWetData extends GlowDataManipulator<WetData> implements WetData {
    public GlowWetData() {
        super(WetData.class);
    }

    @Override
    public WetData copy() {
        return new GlowWetData();
    }

    @Override
    public int compareTo(WetData o) {
        return 0;
    }
}
