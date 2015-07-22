package net.glowstone.block.stateresolver;

import net.glowstone.block.GlowBlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;

public class LeavesResolver extends TreeTypeResolver {
    @Override
    protected GlowBlockType getBlockType(TreeType type) {
        if (type == TreeTypes.ACACIA || type == TreeTypes.DARK_OAK) {
            return BlockTypes.LEAVES2;
        } else {
            return BlockTypes.LEAVES;
        }
    }
}
