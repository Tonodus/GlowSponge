package net.glowstone.id;

import net.glowstone.block.GlowBlockState;
import net.glowstone.block.GlowBlockType;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

/**
 * The {@link BlockIdManger} implementation using defined
 * values for specific BlockTypes
 */
public class OldBlockIdManager implements BlockIdManger {
    private final GlowBlockType[] blocks = new GlowBlockType[]{
            BlockTypes.AIR,
            BlockTypes.STONE,
            BlockTypes.DIRT/*,
            ...
            */
    };

    @Override
    public char getIdData(BlockType type) {
        return ((char) (((GlowBlockType) type).getOldId() << 4));
    }

    @Override
    public char getIdData(BlockState block) {
        GlowBlockType type = (GlowBlockType) block.getType();
        byte dataValue = type.getDataValueFromState(((GlowBlockState) block));
        return (char) ((type.getOldId() << 4) | dataValue);
    }

    @Override
    public int getId(BlockState state) {
        return ((GlowBlockType) state.getType()).getOldId();
    }

    @Override
    public BlockState getBlock(char idData) {
        int type = idData >> 4;
        int data = idData & 0xF;

        GlowBlockType blockType = blocks[type];
        if (data == 0) {
            return blockType.getDefaultState();
        } else {
            return blockType.getStateFromDataValue((byte) data);
        }
    }

    @Override
    public BlockType getBlockType(char idData) {
        int type = idData >> 4;
        return blocks[type];
    }
}
