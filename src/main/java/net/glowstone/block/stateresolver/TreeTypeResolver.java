package net.glowstone.block.stateresolver;

import net.glowstone.data.manipulator.GlowDataManipulator;
import net.glowstone.data.manipulator.block.GlowTreeData;
import org.spongepowered.api.data.manipulator.block.TreeData;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;

public abstract class TreeTypeResolver extends SingleDataStateResolver<TreeData, TreeType> {
    public TreeTypeResolver() {
        super(TreeData.class, TreeTypes.OAK);
    }

    @Override
    protected TreeType[] getTypes() {
        return new TreeType[]{
                TreeTypes.OAK,
                //...
        };
    }

    @Override
    protected GlowDataManipulator<TreeData> create(TreeType type) {
        return new GlowTreeData(type);
    }
}
