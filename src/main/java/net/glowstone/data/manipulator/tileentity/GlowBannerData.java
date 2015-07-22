package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowListData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulator.tileentity.BannerData;
import org.spongepowered.api.data.type.BannerPatternShape;
import org.spongepowered.api.data.type.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class GlowBannerData extends GlowListData<BannerData.PatternLayer, BannerData> implements BannerData {
    private DyeColor baseColor;
    private final List<BannerData.PatternLayer> patterns;

    public GlowBannerData() {
        super(BannerData.class);
        patterns = new ArrayList<>();
    }

    public GlowBannerData(DyeColor baseColor, List<PatternLayer> patterns) {
        super(BannerData.class);
        this.baseColor = baseColor;
        this.patterns = patterns;
    }

    @Override
    public DyeColor getBaseColor() {
        return baseColor;
    }

    @Override
    public BannerData setBaseColor(DyeColor color) {
        this.baseColor = color;
        return this;
    }

    @Override
    public List<PatternLayer> getPatternsList() {
        return patterns;
    }

    @Override
    public BannerData clearPatterns() {
        patterns.clear();
        return this;
    }

    @Override
    public BannerData addPatternLayer(PatternLayer pattern) {
        patterns.add(pattern);
        return this;
    }

    @Override
    public BannerData addPatternLayer(BannerPatternShape patternShape, DyeColor color) {
        patterns.add(new GlowPatternLayer(patternShape, color));
        return this;
    }

    @Override
    public int compareTo(BannerData o) {
        return 0;
    }

    @Override
    public BannerData copy() {
        return new GlowBannerData(baseColor, new ArrayList<>(patterns));
    }

    private static class GlowPatternLayer implements PatternLayer {
        private final BannerPatternShape patternShape;
        private final DyeColor color;

        public GlowPatternLayer(BannerPatternShape patternShape, DyeColor color) {
            this.patternShape = patternShape;
            this.color = color;
        }

        @Override
        public BannerPatternShape getId() {
            return patternShape;
        }

        @Override
        public DyeColor getColor() {
            return color;
        }

        @Override
        public DataContainer toContainer() {
            DataContainer container = new MemoryDataContainer();
            //TODO: verify names
            container.set(DataQuery.of("patternShape"), patternShape);
            container.set(DataQuery.of("color"), color);
            return container;
        }
    }
}
