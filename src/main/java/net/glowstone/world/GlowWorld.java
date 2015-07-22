package net.glowstone.world;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import net.glowstone.GlowGame;
import net.glowstone.GlowServer;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.entity.EntityManager;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.objects.GlowItem;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.event.world.GlowWorldEvent;
import net.glowstone.id.BlockIdManger;
import net.glowstone.id.OldBlockIdManager;
import net.glowstone.io.WorldMetadataService;
import net.glowstone.io.WorldStorageProvider;
import net.glowstone.world.gen.GlowWorldGenerator;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.ScheduledBlockUpdate;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.*;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.service.persistence.InvalidDataException;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.*;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.api.world.storage.WorldStorage;
import org.spongepowered.api.world.weather.Weather;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class GlowWorld extends GlowExtent implements World {
    private static final Vector3i BLOCK_MIN = new Vector3i(-30000000, 0, -30000000);
    private static final Vector3i BLOCK_MAX = new Vector3i(30000000, 256, 30000000).sub(1, 1, 1);
    private static final Vector3i BLOCK_SIZE = BLOCK_MAX.sub(BLOCK_MIN).add(1, 1, 1);
    private static final Vector2i BIOME_MIN = BLOCK_MIN.toVector2(true);
    private static final Vector2i BIOME_MAX = BLOCK_MAX.toVector2(true);
    private static final Vector2i BIOME_SIZE = BIOME_MAX.sub(BIOME_MIN).add(1, 1);

    /**
     * The length in ticks of one Minecraft day.
     */
    public static final long DAY_LENGTH = 24000;

    /**
     * The length in ticks between autosaves (5 minutes).
     */
    private static final int AUTOSAVE_TIME = 20 * 60 * 5;

    private final GlowServer server;
    private final net.glowstone.world.ChunkManager chunkManager;
    private final EntityManager entityManager;
    private final Random random;

    private final net.glowstone.world.ChunkManager.ChunkLock spawnChunkLock;

    private final WorldStorageProvider worldStorageProvider;
    private final Dimension dimension;
    private WorldGenerator worldGenerator;
    private final GlowWorldProperties properties;

    private Weather weather;
    private long weatherRemainingTime;
    private long weatherRunningDuration;

    private boolean autosave;
    private long saveTimer;
    private Context context;

    private final BlockIdManger blockIdManer = new OldBlockIdManager();

    public GlowWorld(GlowServer server, WorldCreationSettings settings, WorldStorageProvider storageProvider) {
        this.server = server;
        this.dimension = new GlowDimension(settings.getDimensionType());
        this.worldStorageProvider = storageProvider;
        this.random = new Random(); //TODO
        this.worldGenerator = GlowWorldGenerator.from(settings);

        this.chunkManager = new ChunkManager(this, storageProvider.getChunkIoService(), worldGenerator.getBaseGeneratorPopulator());
        entityManager = null;

        //Load world
        GlowGame.getInstance().getEventManager().post(new GlowWorldEvent.GlowWorldLoadEvent(this));

        long seed;
        UUID uid;

        WorldMetadataService.WorldFinalValues values = null;
        try {
            values = storageProvider.getMetadataService().readWorldData();
        } catch (IOException e) {
            server.getLogger().log(Level.SEVERE, "Error reading world for creation", e);
        }
        if (values != null) {
            if (values.getSeed() == 0L) {
                seed = settings.getSeed();
            } else {
                seed = values.getSeed();
            }
            uid = values.getUuid();
        } else {
            seed = settings.getSeed();
            uid = UUID.randomUUID();
        }

        this.properties = new GlowWorldProperties(settings.getWorldName(), seed, uid);

        // begin loading spawn area
        spawnChunkLock = newChunkLock("spawn");
        server.addWorld(this);
        server.getLogger().info("Preparing spawn for " + properties.getWorldName() + "...");


        // determine the spawn location if we need to
        if (properties.getSpawnPosition() == null) {
            TeleportHelper teleportHelper = server.getGame().getTeleportHelper();

            // no location loaded, look for fixed spawn
            properties.setSpawnPosition(new Vector3i(0, 100, 0)); //TODO

            if (properties.getSpawnPosition() == null) {
                // determine a location randomly
                int spawnX = random.nextInt(128) - 64, spawnZ = random.nextInt(128) - 64;
                GlowChunk chunk = getChunk(spawnX >> 4, spawnZ >> 4);
                //GlowServer.logger.info("determining spawn: " + chunk.getX() + " " + chunk.getZ());
                chunk.load();  // I'm not sure there's a sane way around this
                for (int tries = 0; tries < 10 && !teleportHelper.getSafeLocation(new Location(this, spawnX, getHighestBlockYAt(spawnX, spawnZ), spawnZ), 0, 0).isPresent(); ++tries) {
                    spawnX += random.nextInt(128) - 64;
                    spawnZ += random.nextInt(128) - 64;
                }
                properties.setSpawnPosition(new Vector3i(spawnX, getHighestBlockYAt(spawnX, spawnZ), spawnZ));
            }
        }

        // load up chunks around the spawn location
        spawnChunkLock.clear();
        if (properties.doesKeepSpawnLoaded()) {
            int centerX = properties.getSpawnPosition().getX() >> 4;
            int centerZ = properties.getSpawnPosition().getZ() >> 4;
            int radius = 4 * server.getViewDistance() / 3;

            long loadTime = System.currentTimeMillis();

            int total = (radius * 2 + 1) * (radius * 2 + 1), current = 0;
            for (int x = centerX - radius; x <= centerX + radius; ++x) {
                for (int z = centerZ - radius; z <= centerZ + radius; ++z) {
                    ++current;
                    loadChunk(x, z);
                    spawnChunkLock.acquire(new GlowChunk.Key(x, z));

                    if (System.currentTimeMillis() >= loadTime + 1000) {
                        int progress = 100 * current / total;
                        GlowServer.logger.info("Preparing spawn for " + properties.getWorldName() + ": " + progress + "%");
                        loadTime = System.currentTimeMillis();
                    }
                }
            }
        }
        server.getLogger().info("Preparing spawn for " + properties.getWorldName() + ": done");
    }

    private double getHighestBlockYAt(int x, int z) {
        return getChunk(x >> 4, z >> 4).getHeight(x & 0xf, z & 0xf);
    }

    public GlowServer getServer() {
        return server;
    }

    /**
     * Get a new chunk lock object a player or other party can use to keep chunks loaded.
     * @return The ChunkLock.
     */
    public net.glowstone.world.ChunkManager.ChunkLock newChunkLock(String desc) {
        return new net.glowstone.world.ChunkManager.ChunkLock(chunkManager, properties.getUniqueId().toString() + ": " + desc);
    }

    public void pulse() {
        List<GlowEntity> temp = new ArrayList<>(entityManager.getAll());
        List<GlowEntity> players = new LinkedList<>();

        // pulse players last so they actually see that other entities have
        // moved. unfortunately pretty hacky. not a problem for players b/c
        // their position is modified by session ticking.
        for (GlowEntity entity : temp) {
            if (entity instanceof GlowPlayer) {
                players.add(entity);
            } else {
                entity.pulse();
            }
        }
        for (GlowEntity entity : players) {
            entity.pulse();
        }

        for (GlowEntity entity : temp) {
            entity.reset();
        }

        // Tick the world age and time of day
        // Modulus by 24000, the tick length of a day
        properties.setWorldTime(properties.getWorldTime() + 1);
        if (properties.getGameRuleManager().getBoolean("doDaylightCycle")) {
            //  time = (time + 1) % DAY_LENGTH;
        }
        if (properties.getWorldTime() % (30 * 20) == 0) {
            // Only send the time every so often; clients are smart.
            for (GlowPlayer player : getRawPlayers()) {
                player.sendTime(properties.getWorldTime(), properties.getTotalTime());
            }
        }

        // only tick weather in a NORMAL world
       /* if (dimension.getType() == DimensionTypes.OVERWORLD) {
            if (--rainingTicks <= 0) {
                setStorm(!currentlyRaining);
            }

            if (--thunderingTicks <= 0) {
                setThundering(!currentlyThundering);
            }

            updateWeather();

            if (currentlyRaining && currentlyThundering) {
                if (random.nextDouble() < .01) {
                    GlowChunk[] chunkList = chunkManager.getLoadedChunks();
                    if (chunkList.length > 0) {
                        BukkitChunk chunk = chunkList[random.nextInt(chunkList.length)];

                        int x = (chunk.getX() << 4) + random.nextInt(16);
                        int z = (chunk.getZ() << 4) + random.nextInt(16);
                        int y = getHighestBlockYAt(x, z);

                        strikeLightning(new Location(this, x, y, z));
                    }
                }
            }
        }*/

        if (--saveTimer <= 0) {
            saveTimer = AUTOSAVE_TIME;
            chunkManager.unloadOldChunks();
            if (autosave) {
                save(true);
            }
        }
    }

    //////////////////////////////
    // World saving

    public void save(boolean async) {
        // save metadata
        writeWorldData(async);

        // save chunks
        maybeAsync(async, new Runnable() {
            @Override
            public void run() {
                for (GlowChunk chunk : chunkManager.getLoadedChunks()) {
                    chunkManager.performSave(chunk);
                }
            }
        });

        // save players
        for (GlowPlayer player : getRawPlayers()) {
            player.saveData(async);
        }
    }

    /**
     * Save the world data using the metadata service.
     * @param async Whether to write asynchronously.
     */
    private void writeWorldData(boolean async) {
        maybeAsync(async, new Runnable() {
            @Override
            public void run() {
                try {
                    worldStorageProvider.getMetadataService().writeWorldData();
                } catch (IOException e) {
                    server.getLogger().severe("Could not save metadata for world: " + getName());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Execute a runnable, optionally asynchronously.
     * @param async Whether to run the runnable in an asynchronous task.
     * @param runnable The runnable to run.
     */
    private void maybeAsync(boolean async, Runnable runnable) {
        if (async) {
            server.getGame().getScheduler().getTaskBuilder().async().execute(runnable).submit(null);
        } else {
            runnable.run();
        }
    }

    /////////////////////////////////////
    // Internals

    /////////////////
    // IdManagers
    public BlockIdManger getBlockIdManer() {
        return blockIdManer;
    }

    public boolean unload() {
        try {
            worldStorageProvider.getChunkIoService().unload();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private Collection<GlowEntity> getRawEntities() {
        return entityManager.getAll();
    }

    public boolean isChunkInUse(int x, int z) {
        return chunkManager.isChunkInUse(x, z);
    }

    public Collection<GlowPlayer> getRawPlayers() {
        return null;
    }

    public net.glowstone.world.ChunkManager getChunkManager() {
        return chunkManager;
    }

    public WorldStorageProvider getStorageProvider() {
        return worldStorageProvider;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    ///////////////////////
    // Accessors

    @Override
    public Difficulty getDifficulty() {
        return properties.getDifficulty();
    }

    @Override
    public String getName() {
        return properties.getWorldName();
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Optional<Chunk> getChunk(Vector3i position) {
        return getChunk(position.getX(), 0, position.getZ());
    }

    @Override
    public Optional<Chunk> getChunk(int x, int y, int z) {
        return Optional.fromNullable((Chunk) chunkManager.getChunk(x >> 4, z >> 4));
    }

    @Override
    public Optional<Chunk> loadChunk(Vector3i position, boolean shouldGenerate) {
        if (chunkManager.loadChunk(position.getX() >> 4, position.getZ() >> 4, shouldGenerate)) {
            return getChunk(position);
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<Chunk> loadChunk(int x, int y, int z, boolean shouldGenerate) {
        return null;
    }

    public void loadChunk(int x, int z) {
        chunkManager.loadChunk(x >> 4, z >> 4, true);
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return chunk.unloadChunk();
    }

    @Override
    public Iterable<Chunk> getLoadedChunks() {
        return (Iterable) Arrays.asList(chunkManager.getLoadedChunks());
    }

    @Override
    public Optional<Entity> getEntity(UUID uuid) {
        for (Entity entity : entityManager.getAll()) {
            if (entity.getUniqueId().equals(uuid))
                return Optional.of(entity);
        }

        return Optional.absent();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return properties.getWorldBorder();
    }

    @Override
    public Optional<String> getGameRule(String gameRule) {
        return properties.getGameRule(gameRule);
    }

    @Override
    public Map<String, String> getGameRules() {
        return properties.getGameRules();
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public WorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    @Override
    public void setWorldGenerator(WorldGenerator generator) {
        this.worldGenerator = generator;
    }

    @Override
    public boolean doesKeepSpawnLoaded() {
        return properties.doesKeepSpawnLoaded();
    }

    @Override
    public void setKeepSpawnLoaded(boolean keepLoaded) {
        properties.setKeepSpawnLoaded(keepLoaded);
    }

    @Override
    public WorldStorage getWorldStorage() {
        return null;
    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {

    }

    @Override
    public WorldCreationSettings getCreationSettings() {
        return null;
    }

    @Override
    public WorldProperties getProperties() {
        return properties;
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this, properties.getSpawnPosition());
    }

    @Override
    public Context getContext() {
        if (context == null) {
            context = new Context(Context.WORLD_KEY, getName());
        }

        return context;
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        return getChunk(x, z).getBlockType(x >> 4, y, z >> 4);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        getChunk(x, z).setBlockType(x >> 4, y, z >> 4, type);
    }

    private GlowChunk getChunk(int x, int z) {
        return chunkManager.getChunk(x >> 4, z >> 4);
    }

    @Override
    public BlockSnapshot getBlockSnapshot(int x, int y, int z) {
        return getChunk(x, z).getBlockSnapshot(x >> 4, y, z >> 4);
    }

    @Override
    public void setBlockSnapshot(int x, int y, int z, BlockSnapshot snapshot) {
        getChunk(x, z).setBlockSnapshot(x >> 4, y, z >> 4, snapshot);
    }

    @Override
    public <T> Optional<T> getBlockData(int x, int y, int z, Class<T> dataClass) {
        return getChunk(x, z).getBlockData(x >> 4, y, z >> 4, dataClass);
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
        return getChunk(x, z).getLuminance(x >> 4, y, z >> 4);
    }

    @Override
    public int getLuminanceFromSky(int x, int y, int z) {
        return getChunk(x, z).getLuminanceFromSky(x >> 4, y, z >> 4);
    }

    @Override
    public int getLuminanceFromGround(int x, int y, int z) {
        return getChunk(x, z).getLuminanceFromGround(x >> 4, y, z >> 4);
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
    public boolean containsBiome(int x, int z) {
        return false;
    }

    @Override
    public BiomeType getBiome(int x, int z) {
        return getChunk(x, z).getBiome(x >> 4, z >> 4);
    }

    @Override
    public void setBiome(int x, int z, BiomeType biome) {
        getChunk(x, z).setBiome(x >> 4, z >> 4, biome);
    }

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> filter) {
        ImmutableList.Builder<Entity> builder = ImmutableList.builder();
        for (Entity entity : getRawEntities()) {
            if (filter.apply(entity)) {
                builder.add(entity);
            }
        }
        return builder.build();
    }

    @Override
    public boolean spawnEntity(Entity entity) {
        if (entity instanceof GlowEntity) {
            ((GlowEntity) entity).spawn();
            return true;
        } else {
            return false;
        }
    }

    public void dropItem(Location location, ItemStack item) {
        GlowItem entity = new GlowItem(location, item);
        spawnEntity(entity);
    }

    @Override
    public UUID getUniqueId() {
        return properties.getUniqueId();
    }

    @Override
    public Collection<TileEntity> getTileEntities(Predicate<TileEntity> filter) {
        ImmutableList.Builder<TileEntity> builder = ImmutableList.builder();
        for (GlowChunk chunk : chunkManager.getLoadedChunks()) {
            Collection<GlowTileEntity> tileEntities = chunk.getRawTileEntities();
            for (TileEntity tileEntity : tileEntities) {
                if (filter.apply(tileEntity)) {
                    builder.add(tileEntity);
                }
            }
        }
        return builder.build();
    }

    @Override
    public Optional<TileEntity> getTileEntity(int x, int y, int z) {
        return getChunk(x, z).getTileEntity(x >> 4, y, z >> 4);
    }

    @Override
    public Vector3i getBlockMin() {
        return BLOCK_MIN;
    }

    @Override
    public Vector3i getBlockMax() {
        return BLOCK_MAX;
    }

    @Override
    public Vector3i getBlockSize() {
        return BLOCK_SIZE;
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return getChunk(x, z).getBlock(x >> 4, y, z >> 4);
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState block) {
        getChunk(x, z).setBlock(x >> 4, y, z >> 4, block);
    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position) {

    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position, int radius) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch) {

    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch, double minVolume) {

    }

    @Override
    public void sendMessage(ChatType type, String... messages) {
        for (Player player : getRawPlayers()) {
            player.sendMessage(type, messages);
        }
    }

    @Override
    public void sendMessage(ChatType type, Text... messages) {
        for (Player player : getRawPlayers()) {
            player.sendMessage(type, messages);
        }
    }

    @Override
    public void sendMessage(ChatType type, Iterable<Text> messages) {
        for (Player player : getRawPlayers()) {
            player.sendMessage(type, messages);
        }
    }

    @Override
    public void sendTitle(Title title) {
        for (Player player : getRawPlayers()) {
            player.sendTitle(title);
        }
    }

    @Override
    public void resetTitle() {
        for (Player player : getRawPlayers()) {
            player.resetTitle();
        }
    }

    @Override
    public void clearTitle() {
        for (Player player : getRawPlayers()) {
            player.clearTitle();
        }
    }

    @Override
    public Weather getWeather() {
        return weather;
    }

    @Override
    public long getRemainingDuration() {
        return weatherRemainingTime;
    }

    @Override
    public long getRunningDuration() {
        return weatherRunningDuration;
    }

    @Override
    public void forecast(Weather weather) {
        //TODO: random time
        forecast(weather, 5 * 60 * 20);
    }

    @Override
    public void forecast(Weather weather, long duration) {
        this.weather = weather;
        this.weatherRunningDuration = 0;
        this.weatherRemainingTime = duration;
    }

    @Override
    public WorldLocation convertToWorldLocation(Location location) {
        return new WorldLocation(this, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
