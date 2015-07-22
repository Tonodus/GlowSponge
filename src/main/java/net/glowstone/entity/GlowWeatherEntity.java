package net.glowstone.entity;

import org.spongepowered.api.world.Location;

/**
 * Represents a Weather related entity, such as a storm.
 */
public abstract class GlowWeatherEntity extends GlowEntity {

    public GlowWeatherEntity(Location location) {
        super(location);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}
