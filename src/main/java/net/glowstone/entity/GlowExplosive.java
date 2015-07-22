package net.glowstone.entity;

import org.spongepowered.api.data.manipulator.entity.ExplosiveRadiusData;
import org.spongepowered.api.entity.explosive.Explosive;
import org.spongepowered.api.world.Location;

public abstract class GlowExplosive extends GlowEntity implements Explosive {
    public GlowExplosive(Location location) {
        super(location);
    }

    @Override
    public void detonate() {

    }

    @Override
    public ExplosiveRadiusData getExplosiveRadiusData() {
        return getData(ExplosiveRadiusData.class).get();
    }
}
