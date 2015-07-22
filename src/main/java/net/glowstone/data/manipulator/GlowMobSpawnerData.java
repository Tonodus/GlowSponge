package net.glowstone.data.manipulator;

import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.manipulator.MobSpawnerData;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.weighted.WeightedCollection;
import org.spongepowered.api.util.weighted.WeightedEntity;

import java.util.Collection;

public class GlowMobSpawnerData extends GlowDataManipulator<MobSpawnerData> implements MobSpawnerData {
    private short remainingDelay;
    private short minimumSpawnDelay;
    private short maximumSpawnDelay;

    public GlowMobSpawnerData() {
        super(MobSpawnerData.class);
    }

    @Override
    public short getRemainingDelay() {
        return remainingDelay;
    }

    @Override
    public MobSpawnerData setRemainingDelay(short delay) {
        this.remainingDelay = delay;
        return this;
    }

    @Override
    public short getMinimumSpawnDelay() {
        return minimumSpawnDelay;
    }

    @Override
    public MobSpawnerData setMinimumSpawnDelay(short delay) {
        this.minimumSpawnDelay = delay;
        return this;
    }

    @Override
    public short getMaximumSpawnDelay() {
        return maximumSpawnDelay;
    }

    @Override
    public MobSpawnerData setMaximumSpawnDelay(short delay) {
        this.maximumSpawnDelay = delay;
        return this;
    }

    @Override
    public short getSpawnCount() {
        return 0;
    }

    @Override
    public MobSpawnerData setSpawnCount(short count) {
        return null;
    }

    @Override
    public short getMaximumNearbyEntities() {
        return 0;
    }

    @Override
    public MobSpawnerData setMaximumNearbyEntities(short count) {
        return null;
    }

    @Override
    public short getRequiredPlayerRange() {
        return 0;
    }

    @Override
    public MobSpawnerData setRequiredPlayerRange(short range) {
        return null;
    }

    @Override
    public short getSpawnRange() {
        return 0;
    }

    @Override
    public MobSpawnerData setSpawnRange(short range) {
        return null;
    }

    @Override
    public MobSpawnerData setNextEntityToSpawn(EntityType type, Collection<DataManipulator<?>> additionalProperties) {
        return null;
    }

    @Override
    public MobSpawnerData setNextEntityToSpawn(WeightedEntity entity) {
        return null;
    }

    @Override
    public WeightedCollection<WeightedEntity> getPossibleEntitiesToSpawn() {
        return null;
    }

    @Override
    public MobSpawnerData copy() {
        return new GlowMobSpawnerData();
    }

    @Override
    public int compareTo(MobSpawnerData o) {
        return 0;
    }
}
