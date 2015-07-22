package net.glowstone.event.entity;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.entity.ExplosionPrimeEvent;

public class GlowExplosionPrimeEvent extends GlowEntityEvent implements ExplosionPrimeEvent {
    private double radius;
    private boolean isFlammable;
    private boolean isCancelled;

    public GlowExplosionPrimeEvent(Entity entity, double radius, boolean isFlammable) {
        super(entity);
        this.radius = radius;
        this.isFlammable = isFlammable;
        this.isCancelled = false;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean isFlammable() {
        return isFlammable;
    }

    @Override
    public void setFlammable(boolean flammable) {
        this.isFlammable = flammable;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
