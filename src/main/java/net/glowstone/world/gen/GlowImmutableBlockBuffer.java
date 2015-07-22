package net.glowstone.world.gen;

import net.glowstone.id.BlockIdManger;
import net.glowstone.world.ChunkSection;
import org.spongepowered.api.util.gen.ImmutableBlockBuffer;
import org.spongepowered.api.world.extent.BlockVolume;

public class GlowImmutableBlockBuffer extends GlowBlockBuffer implements ImmutableBlockBuffer {
    public GlowImmutableBlockBuffer(BlockVolume blockVolume, ChunkSection[] sections, BlockIdManger idManger) {
        super(blockVolume, sections, idManger);
    }

    public static GlowImmutableBlockBuffer copyOf(GlowBlockBuffer buffer) {
        ChunkSection[] sections = new ChunkSection[buffer.sections.length];
        for (int i = 0; i < sections.length; i++) {
            if (buffer.sections[i] == null) {
                sections[i] = null;
            } else {
                sections[i] = buffer.sections[i].snapshot();
            }
        }
        return new GlowImmutableBlockBuffer(buffer.blockVolume, sections, buffer.idManger);
    }

}
