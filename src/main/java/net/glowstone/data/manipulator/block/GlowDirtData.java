package net.glowstone.data.manipulator.block;

import net.glowstone.data.manipulator.GlowSingleValueData;
import org.spongepowered.api.data.manipulator.block.DirtData;
import org.spongepowered.api.data.type.DirtType;
import org.spongepowered.api.data.type.DirtTypes;

public class GlowDirtData extends GlowSingleValueData<DirtType, DirtData> implements DirtData {
    public GlowDirtData() {
        this(DirtTypes.DIRT);
    }

    public GlowDirtData(DirtType type) {
        super(DirtData.class, type);
    }

    @Override
    public DirtData copy() {
        return new GlowDirtData().setValue(getValue());
    }

    @Override
    public int compareTo(DirtData o) {
        return o.getValue().getId().compareTo(getValue().getId());
    }
}
