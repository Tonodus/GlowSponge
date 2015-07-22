package net.glowstone.world;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.ScheduledBlockUpdate;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.Extent;

import java.util.Collection;

public abstract class GlowExtent implements Extent {
    public abstract WorldLocation convertToWorldLocation(Location location);

    ////////////////////////////////////////

    @Override
    public Location getLocation(Vector3i position) {
        return new Location(this, position);
    }

    @Override
    public Location getLocation(int x, int y, int z) {
        return new Location(this, x, y, z);
    }

    @Override
    public Location getLocation(Vector3d position) {
        return new Location(this, position);
    }

    @Override
    public Location getLocation(double x, double y, double z) {
        return new Location(this, x, y, z);
    }

    @Override
    public float getTemperature(Vector3i position) {
        return getTemperature(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public float getTemperature(int x, int y, int z) {
        return (float) getBiome(x, z).getTemperature();
    }


    @Override
    public boolean containsBiome(Vector2i position) {
        return containsBiome(position.getX(), position.getY());
    }

    @Override
    public boolean containsBlock(Vector3i position) {
        return containsBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public boolean containsBlock(int x, int y, int z) {
        Vector3i v = getBlockMin();
        if (v.getX() > x || v.getY() > y || v.getZ() > z) {
            return false;
        }
        v = getBlockMax();
        if (v.getX() < x || v.getY() < y || v.getZ() < z) {
            return false;
        }
        return true;
    }

    @Override
    public final BlockType getBlockType(Vector3i position) {
        return getBlockType(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final void setBlockType(Vector3i position, BlockType type) {
        setBlockType(position.getX(), position.getY(), position.getZ(), type);
    }

    @Override
    public final BlockSnapshot getBlockSnapshot(Vector3i position) {
        return getBlockSnapshot(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final void setBlockSnapshot(Vector3i position, BlockSnapshot snapshot) {
        setBlockSnapshot(position.getX(), position.getY(), position.getZ(), snapshot);
    }

    @Override
    public final <T> Optional<T> getBlockData(Vector3i position, Class<T> dataClass) {
        return getBlockData(position.getX(), position.getY(), position.getZ(), dataClass);
    }

    @Override
    public final void interactBlock(Vector3i position) {
        interactBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final void interactBlock(int x, int y, int z) {

    }

    @Override
    public final void interactBlockWith(Vector3i position, ItemStack itemStack) {
        interactBlockWith(position.getX(), position.getY(), position.getZ(), itemStack);
    }

    @Override
    public final void interactBlockWith(int x, int y, int z, ItemStack itemStack) {

    }

    @Override
    public final boolean digBlock(Vector3i position) {
        return digBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public boolean digBlockWith(Vector3i position, ItemStack itemStack) {
        return false;
    }

    @Override
    public int getBlockDigTime(Vector3i position) {
        return 0;
    }

    @Override
    public int getBlockDigTimeWith(Vector3i position, ItemStack itemStack) {
        return 0;
    }

    @Override
    public final int getLuminance(Vector3i position) {
        return getLuminance(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final int getLuminanceFromSky(Vector3i position) {
        return getLuminanceFromSky(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final int getLuminanceFromGround(Vector3i position) {
        return getLuminanceFromGround(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public boolean isBlockPowered(Vector3i position) {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered(Vector3i position) {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(Vector3i position, Direction direction) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(Vector3i position, Direction direction) {
        return false;
    }


    @Override
    public Collection<Direction> getPoweredBlockFaces(Vector3i position) {
        return null;
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredBlockFaces(Vector3i position) {
        return null;
    }

    @Override
    public boolean isBlockPassable(Vector3i position) {
        return false;
    }

    @Override
    public boolean isBlockFlammable(Vector3i position, Direction faceDirection) {
        return false;
    }

    @Override
    public Collection<ScheduledBlockUpdate> getScheduledUpdates(Vector3i position) {
        return null;
    }

    @Override
    public ScheduledBlockUpdate addScheduledUpdate(Vector3i position, int priority, int ticks) {
        return null;
    }

    @Override
    public void removeScheduledUpdate(Vector3i position, ScheduledBlockUpdate update) {

    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(Vector3i position, Class<T> dataClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(Vector3i position, Class<T> manipulatorClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(Vector3i position, Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(Vector3i position, Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(Vector3i position, T manipulatorData) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(Vector3i position, T manipulatorData, DataPriority priority) {
        return null;
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators(Vector3i position) {
        return getManipulators(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(Vector3i position, Class<T> propertyClass) {
        return null;
    }

    @Override
    public Collection<Property<?, ?>> getProperties(Vector3i position) {
        return null;
    }

    @Override
    public boolean validateRawData(Vector3i position, DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(Vector3i position, DataContainer container) throws InvalidDataException {

    }


    @Override
    public Vector2i getBiomeMin() {
        return null;
    }

    @Override
    public Vector2i getBiomeMax() {
        return null;
    }

    @Override
    public Vector2i getBiomeSize() {
        return null;
    }

    @Override
    public final BiomeType getBiome(Vector2i position) {
        return getBiome(position.getX(), position.getY());
    }

    @Override
    public final void setBiome(Vector2i position, BiomeType biome) {
        setBiome(position.getX(), position.getY(), biome);
    }

    @Override
    public BlockState getBlock(Vector3i position) {
        return getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(Vector3i position, BlockState block) {
        setBlock(position.getX(), position.getY(), position.getZ(), block);
    }

    @Override
    public final Collection<Entity> getEntities() {
        return getEntities(null);
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position) {
        return null;
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3i position) {
        return null;
    }

    @Override
    public Optional<Entity> createEntity(DataContainer entityContainer) {
        return null;
    }

    @Override
    public Optional<Entity> createEntity(DataContainer entityContainer, Vector3d position) {
        return null;
    }

    @Override
    public final Collection<TileEntity> getTileEntities() {
        return getTileEntities(null);
    }

    @Override
    public final Optional<TileEntity> getTileEntity(Vector3i position) {
        return getTileEntity(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public final Optional<TileEntity> getTileEntity(Location blockLoc) {
        return getTileEntity(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ());
    }
}
