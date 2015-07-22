package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.TreeType;

public class PlanksResolver extends TreeTypeResolver {

    @Override
    protected GlowBlockType getBlockType(TreeType type) {
        return BlockTypes.PLANKS;
    }
}
