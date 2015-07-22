package net.glowstone.world;

import net.glowstone.constants.GlowBiome;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

/**
 * Class representing a snapshot of a chunk.
 */
public class GlowChunkSnapshot {

    private final int x, z;
    private final String world;
    private final long time;

    private final ChunkSection[] sections;

    private final byte[] height;
    private final double[] temp, humid;
    private final byte[] biomes;

    public GlowChunkSnapshot(int x, int z, World world, ChunkSection[] sections, byte[] height, byte[] biomes, boolean svTemp) {
        this.x = x;
        this.z = z;
        this.world = world.getName();
        this.time = world.getProperties().getTotalTime();

        int numSections = sections != null ? sections.length : 0;
        this.sections = new ChunkSection[numSections];
        for (int i = 0; i < numSections; ++i) {
            if (sections[i] != null) {
                this.sections[i] = sections[i].snapshot();
            }
        }

        this.height = height;
        this.biomes = biomes;

        if (svTemp) {
            final int baseX = x << 4, baseZ = z << 4;
            temp = new double[16 * 16];
            humid = new double[16 * 16];
            for (int xx = 0; xx < 16; ++xx) {
                for (int zz = 0; zz < 16; ++zz) {
                    temp[coordToIndex(xx, zz)] = world.getTemperature(baseX + xx, 0, baseZ + zz);
                    humid[coordToIndex(xx, zz)] = 0; //world.getHumidity(baseX + xx, baseZ + zz);
                }
            }
        } else {
            temp = humid = null;
        }
    }

    private ChunkSection getSection(int y) {
        int idx = y >> 4;
        if (idx < 0 || idx >= sections.length) {
            return null;
        }
        return sections[idx];
    }

    /**
     * Get the ChunkSection array backing this snapshot. In general, it should not be modified.
     * @return The array of ChunkSections.
     */
    public ChunkSection[] getRawSections() {
        return sections;
    }

    public int[] getRawHeightmap() {
        int[] result = new int[height.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = height[i];
        }
        return result;
    }

    public byte[] getRawBiomes() {
        return biomes;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }


    public int getHighestBlockYAt(int x, int z) {
        return height[coordToIndex(x, z)];
    }

    public BiomeType getBiome(int x, int z) {
        return GlowBiome.getBiome(biomes[coordToIndex(x, z)]);
    }

    public double getRawBiomeTemperature(int x, int z) {
        return temp[coordToIndex(x, z)];
    }

    public double getRawBiomeRainfall(int x, int z) {
        return humid[coordToIndex(x, z)];
    }

    private int coordToIndex(int x, int z) {
        if (x < 0 || z < 0 || x >= ChunkUtils.WIDTH || z >= ChunkUtils.HEIGHT)
            throw new IndexOutOfBoundsException();

        return z * ChunkUtils.WIDTH + x;
    }

    public static class EmptySnapshot extends GlowChunkSnapshot {

        public EmptySnapshot(int x, int z, World world, boolean svBiome, boolean svTemp) {
            super(x, z, world, null, null, svBiome ? new byte[256] : null, svTemp);
        }

        @Override
        public int getHighestBlockYAt(int x, int z) {
            return 0;
        }

    }

}
