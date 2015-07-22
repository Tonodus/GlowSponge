package net.glowstone.block.entity;

import net.glowstone.data.manipulator.GlowMobSpawnerData;
import net.glowstone.id.IdManagers;
import net.glowstone.util.nbt.CompoundTag;
import org.spongepowered.api.block.tileentity.MobSpawner;
import org.spongepowered.api.block.tileentity.TileEntityType;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.data.manipulator.MobSpawnerData;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.util.weighted.WeightedEntity;
import org.spongepowered.api.world.Location;

public class TEMobSpawner extends GlowSingleDataTileEntity<MobSpawnerData> implements MobSpawner {
    private static final EntityType DEFAULT = EntityTypes.PIG;

    public TEMobSpawner(Location block) {
        super(block, MobSpawnerData.class);
        setSaveId("MobSpawner");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);

        EntityType spawning = DEFAULT;
        if (tag.isString("EntityId")) {
            spawning = IdManagers.ENTITY_TYPES.getById(tag.getString("EntityId"));
            if (spawning == null) {
                spawning = DEFAULT;
            }
        }

        int delay = 0;
        if (tag.isInt("Delay")) {
            delay = tag.getInt("Delay");
        }

        getRawData().setNextEntityToSpawn(new WeightedEntity(spawning, 1)).setRemainingDelay((short) delay);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        /*tag.putString("EntityId", spawning == null ? "" : spawning.getName());
        tag.putInt("Delay", delay);*/
    }

    @Override
    protected MobSpawnerData createNew() {
        return new GlowMobSpawnerData();
    }

    @Override
    public void spawnEntityBatchImmediately(boolean force) {

    }

    @Override
    public TileEntityType getType() {
        return TileEntityTypes.MOB_SPAWNER;
    }
}
