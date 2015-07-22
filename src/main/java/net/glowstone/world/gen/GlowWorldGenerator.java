package net.glowstone.world.gen;

import net.glowstone.world.gen.base.EndGenerator;
import net.glowstone.world.gen.base.NetherGenerator;
import net.glowstone.world.gen.base.OverworldBiomeGenerator;
import net.glowstone.world.gen.base.OverworldGenerator;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.gen.BiomeGenerator;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.WorldGenerator;

import java.util.ArrayList;
import java.util.List;

public class GlowWorldGenerator implements WorldGenerator {
    private GeneratorPopulator base;
    private final List<GeneratorPopulator> generatorPopulators;
    private final List<Populator> populators;
    private BiomeGenerator biomeGenerator;

    public GlowWorldGenerator(GeneratorPopulator base, BiomeGenerator biomeGenerator) {
        this.base = base;
        this.biomeGenerator = biomeGenerator;
        generatorPopulators = new ArrayList<>();
        populators = new ArrayList<>();
    }

    @Override
    public GeneratorPopulator getBaseGeneratorPopulator() {
        return base;
    }

    @Override
    public void setBaseGeneratorPopulator(GeneratorPopulator generator) {
        this.base = generator;
    }

    @Override
    public List<GeneratorPopulator> getGeneratorPopulators() {
        return generatorPopulators;
    }

    @Override
    public List<Populator> getPopulators() {
        return populators;
    }

    @Override
    public BiomeGenerator getBiomeGenerator() {
        return biomeGenerator;
    }

    @Override
    public void setBiomeGenerator(BiomeGenerator biomeGenerator) {
        this.biomeGenerator = biomeGenerator;
    }

    public static WorldGenerator from(WorldCreationSettings settings) {
        GeneratorType generator = settings.getGeneratorType();
        boolean useDefault = settings.getGeneratorType() == GeneratorTypes.DEFAULT;
        DimensionType dimension = settings.getDimensionType();

        if (generator == GeneratorTypes.OVERWORLD || (useDefault && dimension == DimensionTypes.OVERWORLD)) {
            return new GlowWorldGenerator(new OverworldGenerator(), new OverworldBiomeGenerator());
        } else if (generator == GeneratorTypes.NETHER || (useDefault && dimension == DimensionTypes.NETHER)) {
            return new GlowWorldGenerator(new NetherGenerator(), new NetherGenerator.BiomeGenerator());
        } else if (generator == GeneratorTypes.END || (useDefault && dimension == DimensionTypes.END)) {
            return new GlowWorldGenerator(new EndGenerator(), new EndGenerator.BiomeGenerator());
        }

        return null;
    }
}
