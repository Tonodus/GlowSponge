package net.glowstone.world.gen;

import com.flowpowered.math.vector.Vector3i;
import net.glowstone.block.GlowBlockState;
import net.glowstone.id.BlockIdManger;
import net.glowstone.world.ChunkSection;
import net.glowstone.world.ChunkUtils;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.gen.BlockBuffer;
import org.spongepowered.api.world.extent.BlockVolume;

public class GlowBlockBuffer implements BlockBuffer {
    protected final BlockVolume blockVolume;
    protected final BlockIdManger idManger;
    protected final ChunkSection[] sections;

    public GlowBlockBuffer(BlockVolume blockVolume, ChunkSection[] sections, BlockIdManger idManger) {
        this.blockVolume = blockVolume;
        this.idManger = idManger;
        this.sections = sections;
    }

    @Override
    public Vector3i getBlockMin() {
        return blockVolume.getBlockMin();
    }

    @Override
    public Vector3i getBlockMax() {
        return blockVolume.getBlockMax();
    }

    @Override
    public Vector3i getBlockSize() {
        return blockVolume.getBlockSize();
    }

    @Override
    public BlockState getBlock(Vector3i position) {
        return getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        ChunkSection section = ChunkUtils.getSection(y, sections);
        if (section == null) {
            return new GlowBlockState(BlockTypes.AIR);
        } else {
            return idManger.getBlock(section.types[section.index(x, y, z)]);
        }
    }

    public ChunkSection[] getSections() {
        return sections;
    }
}
