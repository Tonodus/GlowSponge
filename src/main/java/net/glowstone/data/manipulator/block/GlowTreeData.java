package net.glowstone.data.manipulator.block;

import net.glowstone.data.manipulator.GlowSingleValueData;
import org.spongepowered.api.data.manipulator.block.TreeData;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;

public class GlowTreeData extends GlowSingleValueData<TreeType, TreeData> implements TreeData {
    public GlowTreeData() {
        super(TreeData.class, TreeTypes.OAK);
    }

    public GlowTreeData(TreeType defaultValue) {
        super(TreeData.class, defaultValue);
    }

    @Override
    public TreeData copy() {
        return new GlowTreeData(value);
    }

    @Override
    public int compareTo(TreeData o) {
        return 0;
    }
}
