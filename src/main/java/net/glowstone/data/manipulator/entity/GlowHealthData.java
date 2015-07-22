package net.glowstone.data.manipulator.entity;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.entity.HealthData;

public class GlowHealthData extends GlowDataManipulator<HealthData> implements HealthData {
    private double health;
    private double maxHealth;

    public GlowHealthData() {
        super(HealthData.class);
    }

    @Override
    public HealthData damage(double amount) {
        health -= amount;
        return this;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public HealthData setHealth(double health) {
        this.health = health;
        return this;
    }

    @Override
    public double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public HealthData setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    @Override
    public HealthData copy() {
        return new GlowHealthData().setHealth(health).setMaxHealth(maxHealth);
    }

    @Override
    public int compareTo(HealthData o) {
        return Double.compare(health, o.getHealth());
    }
}
