package net.glowstone.id;

import com.google.common.collect.BiMap;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;

public class BiomeIdManager extends BasicIdManager<Byte, BiomeType> {

    @Override
    protected void fill(BiMap<Byte, BiomeType> map) {
        map.put((byte) 0, BiomeTypes.OCEAN);
        map.put((byte) 1, BiomeTypes.PLAINS);
        map.put((byte) 2, BiomeTypes.DESERT);
        map.put((byte) 3, BiomeTypes.EXTREME_HILLS);
        map.put((byte) 4, BiomeTypes.FOREST);
        map.put((byte) 5, BiomeTypes.TAIGA);
        map.put((byte) 6, BiomeTypes.SWAMPLAND);
        map.put((byte) 7, BiomeTypes.RIVER);
        map.put((byte) 8, BiomeTypes.HELL);
        map.put((byte) 9, BiomeTypes.SKY);
        map.put((byte) 10, BiomeTypes.FROZEN_OCEAN);
        map.put((byte) 11, BiomeTypes.FROZEN_RIVER);
        map.put((byte) 12, BiomeTypes.ICE_PLAINS);
        map.put((byte) 13, BiomeTypes.ICE_MOUNTAINS);
        map.put((byte) 14, BiomeTypes.MUSHROOM_ISLAND);
        map.put((byte) 15, BiomeTypes.MUSHROOM_ISLAND_SHORE);
        map.put((byte) 16, BiomeTypes.BEACH);
        map.put((byte) 17, BiomeTypes.DESERT_HILLS);
        map.put((byte) 18, BiomeTypes.FOREST_HILLS);
        map.put((byte) 19, BiomeTypes.TAIGA_HILLS);
        map.put((byte) 20, BiomeTypes.EXTREME_HILLS_EDGE);
        map.put((byte) 21, BiomeTypes.JUNGLE);
        map.put((byte) 22, BiomeTypes.JUNGLE_HILLS);
        map.put((byte) 23, BiomeTypes.JUNGLE_EDGE);
        map.put((byte) 24, BiomeTypes.DEEP_OCEAN);
        map.put((byte) 25, BiomeTypes.STONE_BEACH);
        map.put((byte) 26, BiomeTypes.COLD_BEACH);
        map.put((byte) 27, BiomeTypes.BIRCH_FOREST);
        map.put((byte) 28, BiomeTypes.BIRCH_FOREST_HILLS);
        map.put((byte) 29, BiomeTypes.ROOFED_FOREST);
        map.put((byte) 30, BiomeTypes.COLD_TAIGA);
        map.put((byte) 31, BiomeTypes.COLD_TAIGA_HILLS);
        map.put((byte) 32, BiomeTypes.MEGA_TAIGA);
        map.put((byte) 33, BiomeTypes.MEGA_TAIGA_HILLS);
        map.put((byte) 34, BiomeTypes.EXTREME_HILLS_PLUS);
        map.put((byte) 35, BiomeTypes.SAVANNA);
        map.put((byte) 36, BiomeTypes.SAVANNA_PLATEAU);
        map.put((byte) 37, BiomeTypes.MESA);
        map.put((byte) 38, BiomeTypes.MESA_PLATEAU_FOREST);
        map.put((byte) 39, BiomeTypes.MESA_PLATEAU);
        map.put((byte) 129, BiomeTypes.SUNFLOWER_PLAINS);
        map.put((byte) 130, BiomeTypes.DESERT_MOUNTAINS);
        map.put((byte) 131, BiomeTypes.EXTREME_HILLS_MOUNTAINS);
        map.put((byte) 132, BiomeTypes.FLOWER_FOREST);
        map.put((byte) 133, BiomeTypes.TAIGA_MOUNTAINS);
        map.put((byte) 134, BiomeTypes.SWAMPLAND_MOUNTAINS);
        map.put((byte) 140, BiomeTypes.ICE_PLAINS_SPIKES);
        map.put((byte) 149, BiomeTypes.JUNGLE_MOUNTAINS);
        map.put((byte) 151, BiomeTypes.JUNGLE_EDGE_MOUNTAINS);
        map.put((byte) 155, BiomeTypes.BIRCH_FOREST_MOUNTAINS);
        map.put((byte) 156, BiomeTypes.BIRCH_FOREST_HILLS_MOUNTAINS);
        map.put((byte) 157, BiomeTypes.ROOFED_FOREST_MOUNTAINS);
        map.put((byte) 158, BiomeTypes.COLD_TAIGA_MOUNTAINS);
        map.put((byte) 160, BiomeTypes.MEGA_SPRUCE_TAIGA);
        map.put((byte) 161, BiomeTypes.MEGA_SPRUCE_TAIGA_HILLS);
        map.put((byte) 162, BiomeTypes.EXTREME_HILLS_PLUS_MOUNTAINS);
        map.put((byte) 163, BiomeTypes.SAVANNA_MOUNTAINS);
        map.put((byte) 164, BiomeTypes.SAVANNA_PLATEAU_MOUNTAINS);
        map.put((byte) 165, BiomeTypes.MESA_BRYCE);
        map.put((byte) 166, BiomeTypes.MESA_PLATEAU_FOREST_MOUNTAINS);
        map.put((byte) 167, BiomeTypes.MESA_PLATEAU_MOUNTAINS);
        //...
    }
}
