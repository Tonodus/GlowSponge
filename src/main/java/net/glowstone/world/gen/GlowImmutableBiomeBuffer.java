package net.glowstone.world.gen;

import net.glowstone.id.IdManager;
import org.spongepowered.api.util.gen.ImmutableBiomeBuffer;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.BiomeArea;

public class GlowImmutableBiomeBuffer extends GlowBiomeBuffer implements ImmutableBiomeBuffer {
    public GlowImmutableBiomeBuffer(byte[] biomes, BiomeArea biomeArea, IdManager<BiomeType> idManager) {
        super(biomes, biomeArea, idManager);
    }

    public static ImmutableBiomeBuffer copyOf(GlowBiomeBuffer biomeBuffer) {
        return new GlowImmutableBiomeBuffer(biomeBuffer.biomes.clone(), biomeBuffer.biomeArea, biomeBuffer.idManager);
    }
}
