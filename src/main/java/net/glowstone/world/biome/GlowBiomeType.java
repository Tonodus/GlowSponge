package net.glowstone.world.biome;

import net.glowstone.GlowCatalogType;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.api.world.gen.Populator;

import java.util.List;

public class GlowBiomeType extends GlowCatalogType implements BiomeType {
    private final double tmp;
    private final double hum;
    private final float minHeight;
    private final float maxHeight;

    public GlowBiomeType(String id, String name, int numericId, double tmp, double hum, float minHeight, float maxHeight) {
        super(id, name, numericId);
        this.tmp = tmp;
        this.hum = hum;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public double getTemperature() {
        return tmp;
    }

    @Override
    public double getHumidity() {
        return hum;
    }

    @Override
    public float getMinHeight() {
        return minHeight;
    }

    @Override
    public float getMaxHeight() {
        return maxHeight;
    }

    @Override
    public List<GroundCoverLayer> getGroundCover() {
        return null;
    }

    @Override
    public List<GeneratorPopulator> getGeneratorPopulators() {
        return null;
    }

    @Override
    public List<Populator> getPopulators() {
        return null;
    }
}
