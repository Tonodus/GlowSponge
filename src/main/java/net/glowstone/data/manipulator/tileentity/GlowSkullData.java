package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowSingleValueData;
import org.spongepowered.api.data.manipulator.SkullData;
import org.spongepowered.api.data.type.SkullType;
import org.spongepowered.api.data.type.SkullTypes;

public class GlowSkullData extends GlowSingleValueData<SkullType, SkullData> implements SkullData {
    public GlowSkullData() {
        super(SkullData.class, SkullTypes.SKELETON);
    }

    @Override
    public SkullData copy() {
        return new GlowSkullData().setValue(getValue());
    }

    @Override
    public int compareTo(SkullData o) {
        return 0;
    }
}
