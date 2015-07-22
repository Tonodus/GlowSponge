package net.glowstone.world.gen.base;

import org.spongepowered.api.util.gen.MutableBiomeBuffer;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.gen.BiomeGenerator;

public class OverworldBiomeGenerator implements BiomeGenerator {
    @Override
    public void generateBiomes(MutableBiomeBuffer buffer) {
        buffer.fill(BiomeTypes.BEACH);
    }
}
