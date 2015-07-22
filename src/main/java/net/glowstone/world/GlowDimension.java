package net.glowstone.world;

import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.DimensionType;

public class GlowDimension implements Dimension {
    private final DimensionType type;
    private Context context;

    public GlowDimension(DimensionType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean allowsPlayerRespawns() {
        return false;
    }

    @Override
    public void setAllowsPlayerRespawns(boolean allow) {

    }

    @Override
    public int getMinimumSpawnHeight() {
        return 0;
    }

    @Override
    public boolean doesWaterEvaporate() {
        return false;
    }

    @Override
    public void setWaterEvaporates(boolean evaporates) {

    }

    @Override
    public boolean hasSky() {
        return false;
    }

    @Override
    public DimensionType getType() {
        return type;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getBuildHeight() {
        return 0;
    }

    @Override
    public Context getContext() {
        if (context == null) {
            context = new Context("dimension", getName());
        }

        return context;
    }
}
