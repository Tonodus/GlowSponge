package net.glowstone.data.manipulator.block;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.block.AttachedData;

public class GlowAttachedData extends GlowDataManipulator<AttachedData> implements AttachedData {
    public GlowAttachedData() {
        super(AttachedData.class);
    }

    @Override
    public AttachedData copy() {
        return new GlowAttachedData();
    }

    @Override
    public int compareTo(AttachedData o) {
        return 0;
    }
}
