package net.glowstone.block.entity;

import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.util.DataQueries;
import net.glowstone.util.Locations;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.world.GlowChunk;
import net.glowstone.world.GlowWorld;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.world.Location;

/**
 * Base class for tile entities (blocks with NBT data) in the world.
 * Most access to tile entities should occur through the Bukkit BlockState API.
 */
public abstract class GlowTileEntity implements TileEntity {
    protected final GlowWorld world;
    protected final int x, y, z;
    private String saveId;
    private boolean isValid;

    /**
     * Create a new TileEntity at the given location.
     */
    public GlowTileEntity(Location location) {
        this.world = Locations.world(location);
        location = Locations.worldLoc(location);
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Utility stuff

    public boolean isPosition(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    @Override
    public Location getBlock() {
        return new Location(world, x, y, z);
    }

    /**
     * Update this TileEntity's visible state to all players in range.
     */
    public final void updateInRange() {
        GlowChunk.Key key = new GlowChunk.Key(x >> 4, z >> 4);
        for (GlowPlayer player : world.getRawPlayers()) {
            if (player.canSeeChunk(key)) {
                update(player);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // World I/O

    /**
     * Set the text ID this tile entity is saved to disk with. If this is not
     * set, then load and save of the "id" tag must be performed manually.
     * @param saveId The ID.
     */
    protected final void setSaveId(String saveId) {
        if (this.saveId != null) {
            throw new IllegalStateException("Can only set saveId once");
        }
        this.saveId = saveId;
    }

    /**
     * Read this TileEntity's data from the saved tag.
     * @param tag The tag to load from.
     */
    public void loadNbt(CompoundTag tag) {
        // verify id and coordinates
        if (saveId != null) {
            if (!tag.isString("id") || !tag.getString("id").equals(saveId)) {
                throw new IllegalArgumentException("Expected tile entity id of " + saveId + ", got " + tag.getString("id"));
            }
        }

        // verify coordinates if provided
        if (tag.isInt("x")) {
            int x = tag.getInt("x");
            int y = tag.getInt("y");
            int z = tag.getInt("z");
            if (x != this.x || y != this.y || z != this.z) {
                throw new IllegalArgumentException("Tried to load tile entity with coords (" + x + "," + y + "," + z + ") into (" + this.x + "," + this.y + "," + this.z + ")");
            }
        }
    }

    /**
     * Save this TileEntity's data to NBT.
     * @param tag The tag to save to.
     */
    public void saveNbt(CompoundTag tag) {
        if (saveId != null) {
            tag.putString("id", saveId);
        }
        tag.putInt("x", this.x);
        tag.putInt("y", this.y);
        tag.putInt("z", this.z);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Overridable stuff

    /**
     * Destroy this TileEntity.
     */
    public void destroy() {
        // nothing by default
    }

    /**
     * Update this TileEntity's visible state to the given player.
     * @param player The player to update.
     */
    public void update(GlowPlayer player) {
        // nothing by default
    }

    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = new MemoryDataContainer();
        dataContainer.set(DataQueries.WORLD, world.getUniqueId().toString());
        dataContainer.set(DataQueries.X, x);
        dataContainer.set(DataQueries.Y, y);
        dataContainer.set(DataQueries.Z, z);
        dataContainer.set(DataQuery.of("tileType"), getClass().getSimpleName());
        return dataContainer;
    }
}
