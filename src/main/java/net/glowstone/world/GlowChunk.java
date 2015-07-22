package net.glowstone.world;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import net.glowstone.GlowGame;
import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlockType;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.entity.GlowEntity;
import net.glowstone.event.world.GlowChunkEvent;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.ScheduledBlockUpdate;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.*;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class GlowChunk extends GlowExtent implements Chunk {
    /**
     * The dimensions of a chunk (width: x, height: z, depth: y).
     */
    public static final int WIDTH = ChunkUtils.WIDTH, HEIGHT = ChunkUtils.HEIGHT, DEPTH = ChunkUtils.DEPTH;

    /**
     * The Y depth of a single chunk section.
     */
    private static final int SEC_DEPTH = 16;

    /**
     * A chunk key represents the X and Z coordinates of a chunk and implements
     * the {@link #hashCode()} and {@link #equals(Object)} methods making it
     * suitable for use as a key in a hash table or set.
     * @author Graham Edgecombe
     */
    public static final class Key {

        /**
         * The coordinates.
         */
        private final int x, z;

        /**
         * Creates a new chunk key with the specified X and Z coordinates.
         * @param x The X coordinate.
         * @param z The Z coordinate.
         */
        public Key(int x, int z) {
            this.x = x;
            this.z = z;
        }

        /**
         * Gets the X coordinate.
         * @return The X coordinate.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the Z coordinate.
         * @return The Z coordinate.
         */
        public int getZ() {
            return z;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Key other = (Key) obj;
            return x == other.x && z == other.z;
        }

        @Override
        public String toString() {
            return "ChunkKey{" + x + ',' + z + '}';
        }
    }

    /**
     * The world of this chunk.
     */
    private final GlowWorld world;

    /**
     * The coordinates of this chunk.
     */
    private final int x, z;

    /**
     * The array of chunk sections this chunk contains, or null if it is unloaded.
     */
    private ChunkSection[] sections;

    /**
     * The array of biomes this chunk contains, or null if it is unloaded.
     */
    private byte[] biomes;

    /**
     * The height map values values of each column, or null if it is unloaded.
     * The height for a column is one plus the y-index of the highest non-air
     * block in the column.
     */
    private byte[] heightMap;

    /**
     * The tile entities that reside in this chunk.
     */
    private final HashMap<Integer, GlowTileEntity> tileEntities = new HashMap<>();

    /**
     * The entities that reside in this chunk.
     */
    private final Set<GlowEntity> entities = new HashSet<>(4);

    /**
     * Whether the chunk has been populated by special features.
     * Used in map generation.
     */
    private boolean populated = false;

    GlowChunk(GlowWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    ///////////////////////////////////
    // Internals

    public Set<GlowEntity> getRawEntities() {
        return entities;
    }

    /**
     * Initialize this chunk from the given sections.
     * @param initSections The ChunkSections to use.
     */
    public void initializeSections(ChunkSection[] initSections) {
        if (isLoaded()) {
            GlowServer.logger.log(Level.SEVERE, "Tried to initialize already loaded chunk (" + x + "," + z + ")", new Throwable());
            return;
        }
        //GlowServer.logger.log(Level.INFO, "Initializing chunk ({0},{1})", new Object[]{x, z});

        this.sections = initSections;

        biomes = new byte[WIDTH * HEIGHT];
        heightMap = new byte[WIDTH * HEIGHT];

        // tile entity initialization
        for (int i = 0; i < sections.length; ++i) {
            if (sections[i] == null) continue;
            int by = 16 * i;
            for (int cx = 0; cx < WIDTH; ++cx) {
                for (int cz = 0; cz < HEIGHT; ++cz) {
                    for (int cy = by; cy < by + 16; ++cy) {
                        createTileEntity(cx, cy, cz, getBlockType(cx, cz, cy));
                    }
                }
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    ///////////////////////////////////
    // Highmap - Functions

    public double getHeight(int x, int z) {
        if (heightMap == null && !load()) return 0;
        return heightMap[z * WIDTH + x] & 0xff;
    }

    /**
     * Scan downwards to determine the new height map value.
     */
    private int lowerHeightMap(int x, int y, int z) {
        for (--y; y >= 0; --y) {
            if (getBlockTD(x, z, y) != 0) {
                break;
            }
        }
        return y + 1;
    }

    /**
     * Automatically fill the height map after chunks have been initialized.
     */
    public void automaticHeightMap() {
        // determine max Y chunk section at a time
        int sy = sections.length - 1;
        for (; sy >= 0; --sy) {
            if (sections[sy] != null) {
                break;
            }
        }
        int y = (sy + 1) * 16;
        for (int x = 0; x < WIDTH; ++x) {
            for (int z = 0; z < HEIGHT; ++z) {
                heightMap[z * WIDTH + x] = (byte) lowerHeightMap(x, y, z);
            }
        }
    }

    public void setHeightMap(int[] map) {
        //this.heightMap = map;
    }

    @Override
    public String toString() {
        return "GlowChunk{world=" + world.getName() + ",x=" + x + ",z=" + z + '}';
    }

    @Override
    public Vector3i getPosition() {
        return new Vector3i(x << 4, 0, z << 4);
    }

    @Override
    public boolean isLoaded() {
        return sections != null;
    }

    @Override
    public GlowWorld getWorld() {
        return world;
    }

    @Override
    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public boolean load() {
        return loadChunk(true);
    }

    @Override
    public boolean loadChunk(boolean generate) {
        return isLoaded() || world.getChunkManager().loadChunk(x, z, generate);
    }

    public boolean unloadChunk(boolean b, boolean b1) {
        return false;
    }

    @Override
    public boolean unloadChunk() {
        if (!isLoaded()) {
            return true;
        }

        if (world.isChunkInUse(x, z)) {
            return false;
        }

        if (!world.getChunkManager().performSave(this)) {
            return false;
        }


        //Chunk unloading can't be cancelled
        GlowGame.getInstance().getEventManager().post(new GlowChunkEvent.GlowUnloadChunkEvent(this));

        sections = null;
        biomes = null;
        tileEntities.clear();
        return true;
    }

    @Override
    public boolean containsBiome(int x, int z) {
        return false;
    }

    @Override
    public BiomeType getBiome(int x, int z) {
        if (biomes == null && !load()) return BiomeTypes.OCEAN;
        return world.getBiomeIdManager().getById(biomes[z * WIDTH + x] & 0xFF);
    }

    @Override
    public void setBiome(int x, int z, BiomeType biome) {
        if (biomes == null) return;
        biomes[z * WIDTH + x] = (byte) world.getBiomeIdManager().getId(biome);
    }

    /**
     * Set the entire biome array of this chunk.
     * @param newBiomes The biome array.
     */
    public void setBiomes(byte[] newBiomes) {
        if (biomes == null) {
            throw new IllegalStateException("Must initialize chunk first");
        }
        if (newBiomes.length != biomes.length) {
            throw new IllegalArgumentException("Biomes array not of length " + biomes.length);
        }
        System.arraycopy(newBiomes, 0, biomes, 0, biomes.length);
    }

    @Override
    public GlowBlockType getBlockType(int x, int y, int z) {
        return (GlowBlockType) world.getBlockIdManer().getBlockType(getBlockTD(x, y, z));
    }

    /**
     * Returns the char representing block id and data.
     */
    private char getBlockTD(int x, int y, int z) {
        ChunkSection section = ChunkUtils.getSection(y, sections);
        return section == null ? (char) 0 : section.types[section.index(x, y, z)];
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType blockType) {
        if (!(blockType instanceof GlowBlockType)) {
            return;
        }

        char idData = world.getBlockIdManer().getIdData(blockType);
        setBlock(x, y, z, idData, blockType);
    }

    private void setBlock(int x, int y, int z, char idData, BlockType blockType) {
        if (idData < 0 || idData > 0xfff)
            throw new IllegalArgumentException("Block type out of range: " + idData);

        ChunkSection section = ChunkUtils.getSection(y, sections);
        if (section == null) {
            if (idData == 0) {
                // don't need to create chunk for air
                return;
            } else {
                // create new ChunkSection for this y coordinate
                int idx = y >> 4;
                if (y < 0 || y >= DEPTH || idx >= sections.length) {
                    // y is out of range somehow
                    return;
                }
                sections[idx] = section = new ChunkSection();
            }
        }

        // destroy any tile entity there
        int tileEntityIndex = ChunkUtils.coordToIndex(x, z, y);
        if (tileEntities.containsKey(tileEntityIndex)) {
            tileEntities.remove(tileEntityIndex).destroy();
        }

        // update the air count and height map
        int index = section.index(x, y, z);
        int heightIndex = z * WIDTH + x;
        if (idData == 0) {
            if (section.types[index] != 0) {
                section.count--;
            }
            if (heightMap[heightIndex] == y + 1) {
                // erased just below old height map -> lower
                heightMap[heightIndex] = (byte) lowerHeightMap(x, y, z);
            }
        } else {
            if (section.types[index] == 0) {
                section.count++;
            }
            if (heightMap[heightIndex] <= y) {
                // placed between old height map and top -> raise
                heightMap[heightIndex] = (byte) Math.min(y + 1, 255);
            }
        }
        // update the type - also sets metadata to 0
        section.types[index] = idData;

        if (idData == 0 && section.count == 0) {
            // destroy the empty section
            sections[y / SEC_DEPTH] = null;
            return;
        }

        // create a new tile entity if we need
        createTileEntity(x, y, z, blockType);
    }


    @Override
    public BlockSnapshot getBlockSnapshot(int x, int y, int z) {
        return new GlowBlockSnapshot(getBlock(x, y, z), new Vector3i(x, y, z));
    }

    @Override
    public void setBlockSnapshot(int x, int y, int z, BlockSnapshot snapshot) {
        setBlock(x, y, z, snapshot.getState());
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState block) {
        char idData = world.getBlockIdManer().getIdData(block);
        setBlock(x, y, z, idData, block.getType());
    }

    @Override
    public <T> Optional<T> getBlockData(int x, int y, int z, Class<T> dataClass) {
        //TODO
        return Optional.absent();
    }

    @Override
    public boolean digBlock(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean digBlockWith(int x, int y, int z, ItemStack itemStack) {
        return false;
    }

    @Override
    public int getBlockDigTime(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getBlockDigTimeWith(int x, int y, int z, ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getLuminance(int x, int y, int z) {
        return (byte) (getLuminanceFromGround(x, y, z) + getLuminanceFromSky(x, y, z));
    }

    @Override
    public int getLuminanceFromSky(int x, int y, int z) {
        load();
        ChunkSection section = ChunkUtils.getSection(y, sections);
        return section == null ? 0 : section.skyLight.get(section.index(x, y, z));
    }

    @Override
    public int getLuminanceFromGround(int x, int y, int z) {
        load();
        ChunkSection section = ChunkUtils.getSection(y, sections);
        return section == null ? 0 : section.blockLight.get(section.index(x, y, z));
    }

    @Override
    public boolean isBlockPowered(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockFacePowered(int x, int y, int z, Direction direction) {
        return false;
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(int x, int y, int z, Direction direction) {
        return false;
    }

    @Override
    public Collection<Direction> getPoweredBlockFaces(int x, int y, int z) {
        return null;
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredBlockFaces(int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isBlockPassable(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isBlockFlammable(int x, int y, int z, Direction faceDirection) {
        return false;
    }

    @Override
    public Collection<ScheduledBlockUpdate> getScheduledUpdates(int x, int y, int z) {
        return null;
    }

    @Override
    public ScheduledBlockUpdate addScheduledUpdate(int x, int y, int z, int priority, int ticks) {
        return null;
    }

    @Override
    public void removeScheduledUpdate(int x, int y, int z, ScheduledBlockUpdate update) {

    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getData(int x, int y, int z, Class<T> dataClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> Optional<T> getOrCreate(int x, int y, int z, Class<T> manipulatorClass) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> boolean remove(int x, int y, int z, Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> boolean isCompatible(int x, int y, int z, Class<T> manipulatorClass) {
        return false;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(int x, int y, int z, T manipulatorData) {
        return null;
    }

    @Override
    public <T extends DataManipulator<T>> DataTransactionResult offer(int x, int y, int z, T manipulatorData, DataPriority priority) {
        return null;
    }

    @Override
    public Collection<DataManipulator<?>> getManipulators(int x, int y, int z) {
        return null;
    }

    @Override
    public <T extends Property<?, ?>> Optional<T> getProperty(int x, int y, int z, Class<T> propertyClass) {
        return null;
    }

    @Override
    public Collection<Property<?, ?>> getProperties(int x, int y, int z) {
        return null;
    }

    @Override
    public boolean validateRawData(int x, int y, int z, DataContainer container) {
        return false;
    }

    @Override
    public void setRawData(int x, int y, int z, DataContainer container) throws InvalidDataException {

    }

    @Override
    public Vector3i getBlockMin() {
        return new Vector3i(0, 0, 0);
    }

    @Override
    public Vector3i getBlockMax() {
        return new Vector3i(WIDTH - 1, HEIGHT - 1, DEPTH - 1);
    }

    @Override
    public Vector3i getBlockSize() {
        return new Vector3i(WIDTH, HEIGHT, DEPTH);
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return world.getBlockIdManer().getBlock(getBlockTD(x, y, z));
    }

    ////////////////////////////////////
    // Entities

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> filter) {
        if (filter == null) {
            return (Collection) ImmutableList.copyOf(entities);
        } else {
            ImmutableList.Builder<Entity> builder = ImmutableList.builder();
            for (Entity entity : entities) {
                if (filter.apply(entity)) {
                    builder.add(entity);
                }
            }
            return builder.build();
        }
    }

    @Override
    public boolean spawnEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof GlowEntity) {
            ((GlowEntity) entity).spawn();
        }
        return true;
    }

    ///////////////////////////////////
    // TileEntity management

    /**
     * If needed, create a new tile entity at the given location.
     */
    private void createTileEntity(int x, int y, int z, BlockType blockType) {
        if (blockType == null || !(blockType instanceof GlowBlockType)) return;

        try {
            GlowTileEntity entity = ((GlowBlockType) blockType).getBehavior().createTileEntity(this, x, y, z);
            if (entity == null) return;

            tileEntities.put(ChunkUtils.coordToIndex(x, z, y), entity);
        } catch (Exception ex) {
            GlowServer.logger.log(Level.SEVERE, "Unable to initialize tile entity for " + blockType, ex);
        }
    }

    @Override
    public Optional<TileEntity> getTileEntity(int x, int y, int z) {
        if (y >= DEPTH || y < 0) return Optional.absent();
        load();
        return (Optional) Optional.fromNullable(tileEntities.get(ChunkUtils.coordToIndex(x, z, y)));
    }

    @Override
    public Collection<TileEntity> getTileEntities(Predicate<org.spongepowered.api.block.tileentity.TileEntity> filter) {
        ImmutableList.Builder<TileEntity> builder = ImmutableList.builder();
        for (TileEntity entity : tileEntities.values()) {
            if (filter.apply(entity)) {
                builder.add(entity);
            }
        }
        return builder.build();
    }

    public Collection<GlowTileEntity> getRawTileEntities() {
        return tileEntities.values();
    }

    ///////////////////////////////////
    // Weather
    @Override
    public Weather getWeather() {
        return world.getWeather();
    }

    @Override
    public long getRemainingDuration() {
        return world.getRemainingDuration();
    }

    @Override
    public long getRunningDuration() {
        return world.getRunningDuration();
    }

    @Override
    public void forecast(Weather weather) {
        world.forecast(weather);
    }

    @Override
    public void forecast(Weather weather, long duration) {
        world.forecast(weather, duration);
    }
}
