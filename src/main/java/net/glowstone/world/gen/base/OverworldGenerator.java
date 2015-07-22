package net.glowstone.world.gen.base;

import net.glowstone.block.GlowBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.gen.BiomeBuffer;
import org.spongepowered.api.util.gen.MutableBlockBuffer;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.gen.GeneratorPopulator;

public class OverworldGenerator implements GeneratorPopulator {
    @Override
    public void populate(World world, MutableBlockBuffer buffer, BiomeBuffer biomes) {
        BlockState dirt = new GlowBlockState(BlockTypes.DIRT);
        buffer.setHorizontalLayer(0, 50, dirt);
    }
}
