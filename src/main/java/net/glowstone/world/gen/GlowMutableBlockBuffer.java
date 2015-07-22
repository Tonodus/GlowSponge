package net.glowstone.world.gen;

import com.flowpowered.math.vector.Vector3i;
import net.glowstone.id.BlockIdManger;
import net.glowstone.world.ChunkSection;
import net.glowstone.world.ChunkUtils;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.gen.ImmutableBlockBuffer;
import org.spongepowered.api.util.gen.MutableBlockBuffer;
import org.spongepowered.api.world.extent.BlockVolume;

import java.util.Arrays;

public class GlowMutableBlockBuffer extends GlowBlockBuffer implements MutableBlockBuffer {
    public GlowMutableBlockBuffer(BlockVolume blockVolume, ChunkSection[] sections, BlockIdManger idManger) {
        super(blockVolume, sections, idManger);
    }

    @Override
    public void fill(BlockState block) {
        createAllSections();

        char type = idManger.getIdData(block);
        for (ChunkSection section : sections) {
            Arrays.fill(section.types, type);
        }
    }

    private void createAllSections() {
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] == null) {
                sections[i] = new ChunkSection();
            }
        }
    }

    @Override
    public void setHorizontalLayer(final int startY, final int height, BlockState block) {
        //TODO: increase performance

        char type = idManger.getIdData(block);
        int endY = startY + height - 1;

        for (int y = startY; y <= endY; y++) {
            ChunkSection section = ChunkUtils.getOrCreateSection(y, sections);
            for (int x = 0; x < ChunkUtils.WIDTH; x++) {
                for (int z = 0; z < ChunkUtils.DEPTH; z++) {
                    section.types[section.index(x, y, z)] = type;
                }
            }
        }
    }

    @Override
    public ImmutableBlockBuffer getImmutableClone() {
        return GlowImmutableBlockBuffer.copyOf(this);
    }

    @Override
    public boolean containsBlock(Vector3i position) {
        return blockVolume.containsBlock(position);
    }

    @Override
    public boolean containsBlock(int x, int y, int z) {
        return blockVolume.containsBlock(x, y, z);
    }

    @Override
    public void setBlock(Vector3i position, BlockState block) {
        setBlock(position.getX(), position.getY(), position.getZ(), block);
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState block) {
        ChunkSection section = ChunkUtils.getOrCreateSection(y, sections);
        section.types[section.index(x, y, z)] = idManger.getIdData(block);
    }

    @Override
    public BlockType getBlockType(Vector3i position) {
        return getBlockType(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        ChunkSection section = ChunkUtils.getSection(y, sections);
        return section == null ? BlockTypes.AIR : idManger.getBlockType(section.types[section.index(x, y, z)]);
    }

    @Override
    public void setBlockType(Vector3i position, BlockType type) {
        setBlockType(position.getX(), position.getY(), position.getZ(), type);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        ChunkSection section = ChunkUtils.getOrCreateSection(y, sections);
        section.types[section.index(x, y, z)] = idManger.getIdData(type);
    }
}
