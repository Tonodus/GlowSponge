package net.glowstone.id;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

public interface BlockIdManger {
    char getIdData(BlockType type);

    char getIdData(BlockState block);

    int getId(BlockState state);

    BlockState getBlock(char idData);

    BlockType getBlockType(char idData);
}
