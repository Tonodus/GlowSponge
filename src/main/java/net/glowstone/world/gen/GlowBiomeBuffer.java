package net.glowstone.world.gen;

import com.flowpowered.math.vector.Vector2i;
import net.glowstone.id.IdManagers;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.BiomeArea;

public class GlowBiomeBuffer implements BiomeBuffer {
    protected final BiomeArea biomeArea;
    protected final byte[] biomes;

    public GlowBiomeBuffer(byte[] biomes, BiomeArea biomeArea) {
        this.biomeArea = biomeArea;
        this.biomes = biomes;
    }

    @Override
    public Vector2i getBiomeMin() {
        return biomeArea.getBiomeMin();
    }

    @Override
    public Vector2i getBiomeMax() {
        return biomeArea.getBiomeMax();
    }

    @Override
    public Vector2i getBiomeSize() {
        return biomeArea.getBiomeSize();
    }

    @Override
    public BiomeType getBiome(Vector2i position) {
        return getBiome(position.getX(), position.getY());
    }

    @Override
    public BiomeType getBiome(int x, int z) {
        return IdManagers.BIOMES.getById(biomes[z * 16 + x]);
    }

    public byte[] getBiomes() {
        return biomes;
    }
}
