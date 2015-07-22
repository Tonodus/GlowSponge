package net.glowstone.world.gen.base;

import net.glowstone.block.GlowBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.util.gen.MutableBiomeBuffer;
import org.spongepowered.api.util.gen.MutableBlockBuffer;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.gen.GeneratorPopulator;

public class EndGenerator implements GeneratorPopulator {
    @Override
    public void populate(World world, MutableBlockBuffer buffer, BiomeBuffer biomes) {
        BlockState end = new GlowBlockState(BlockTypes.END_STONE);
        buffer.setHorizontalLayer(0, 50, end);
    }

    public static class BiomeGenerator implements org.spongepowered.api.world.gen.BiomeGenerator {
        @Override
        public void generateBiomes(MutableBiomeBuffer buffer) {
            buffer.fill(BiomeTypes.SKY);
        }
    }
}
