package net.glowstone.world.gen;

import com.flowpowered.math.vector.Vector2i;
import net.glowstone.id.IdManagers;
import org.spongepowered.api.util.gen.ImmutableBiomeBuffer;
import org.spongepowered.api.util.gen.MutableBiomeBuffer;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.BiomeArea;

import java.util.Arrays;

public class GlowMutableBiomeBuffer extends GlowBiomeBuffer implements MutableBiomeBuffer {
    public GlowMutableBiomeBuffer(byte[] biomes, BiomeArea biomeArea) {
        super(biomes, biomeArea);
    }

    @Override
    public void fill(BiomeType biome) {
        byte type = IdManagers.BIOMES.getId(biome);
        Arrays.fill(biomes, type);
    }

    @Override
    public ImmutableBiomeBuffer getImmutableClone() {
        return GlowImmutableBiomeBuffer.copyOf(this);
    }

    @Override
    public boolean containsBiome(Vector2i position) {
        return containsBiome(position.getX(), position.getY());
    }

    @Override
    public boolean containsBiome(int x, int z) {
        return biomeArea.containsBiome(x, z);
    }

    @Override
    public void setBiome(Vector2i position, BiomeType biome) {
        setBiome(position.getX(), position.getY(), biome);
    }

    @Override
    public void setBiome(int x, int z, BiomeType biome) {
        biomes[z * 16 + x] = IdManagers.BIOMES.getId(biome);
    }
}
