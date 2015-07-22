package net.glowstone.item;

import net.glowstone.item.behavior.ItemBehavior;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemBlock;

public class GlowItemBlock extends GlowItemType implements ItemBlock {
    private final BlockType blockType;

    public GlowItemBlock(String id, String name, ItemBehavior behavior, BlockType blockType) {
        super(id, name, behavior);
        this.blockType = blockType;
    }

    @Override
    public BlockType getBlock() {
        return blockType;
    }
}
