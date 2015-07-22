package net.glowstone.util;

import org.spongepowered.api.block.BlockType;

import java.util.Set;

public class BlockTypeValidator implements Validator<BlockType> {

    private final Set<BlockType> validMaterials;

    public BlockTypeValidator(Set<BlockType> validMaterials) {
        this.validMaterials = validMaterials;
    }

    @Override
    public boolean isValid(BlockType block) {
        return validMaterials.contains(block);
    }

}
