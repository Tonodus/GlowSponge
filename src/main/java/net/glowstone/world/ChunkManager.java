package net.glowstone.world;

import net.glowstone.GlowGame;
import net.glowstone.GlowServer;
import net.glowstone.event.world.GlowChunkEvent;
import net.glowstone.io.ChunkIoService;
import net.glowstone.world.gen.GlowMutableBiomeBuffer;
import net.glowstone.world.gen.GlowMutableBlockBuffer;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.api.world.gen.Populator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

/**
 * A class which manages the {@link GlowChunk}s currently loaded in memory.
 * @author Graham Edgecombe
 */
public final class ChunkManager {

    /**
     * The world this ChunkManager is managing.
     */
    private final GlowWorld world;

    /**
     * The chunk I/O service used to read chunks from the disk and write them to
     * the disk.
     */
    private final ChunkIoService service;

    /**
     * The chunk generator used to generate new chunks.
     */
    private final GeneratorPopulator generator;

    /**
     * A map of chunks currently loaded in memory.
     */
    private final ConcurrentMap<GlowChunk.Key, GlowChunk> chunks = new ConcurrentHashMap<>();

    /**
     * A map of chunks which are being kept loaded by players or other factors.
     */
    private final ConcurrentMap<GlowChunk.Key, Set<ChunkLock>> locks = new ConcurrentHashMap<>();

    /**
     * Creates a new chunk manager with the specified I/O service and world
     * generator.
     * @param service The I/O service.
     * @param generator The world generator.
     */
    public ChunkManager(GlowWorld world, ChunkIoService service, GeneratorPopulator generator) {
        this.world = world;
        this.service = service;
        this.generator = generator;
    }

    /**
     * Get the chunk generator.
     */
    public GeneratorPopulator getGenerator() {
        return generator;
    }

    /**
     * Gets a chunk object representing the specified coordinates, which might
     * not yet be loaded.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return The chunk.
     */
    public GlowChunk getChunk(int x, int z) {
        GlowChunk.Key key = new GlowChunk.Key(x, z);
        if (chunks.containsKey(key)) {
            return chunks.get(key);
        } else {
            // only create chunk if it's not in the map already
            GlowChunk chunk = new GlowChunk(world, x, z);
            GlowChunk prev = chunks.putIfAbsent(key, chunk);
            // if it was created in the intervening time, the earlier one wins
            return prev == null ? chunk : prev;
        }
    }

    /**
     * Checks if the Chunk at the specified coordinates is loaded.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return true if the chunk is loaded, otherwise false.
     */
    public boolean isChunkLoaded(int x, int z) {
        GlowChunk.Key key = new GlowChunk.Key(x, z);
        return chunks.containsKey(key) && chunks.get(key).isLoaded();
    }

    /**
     * Check whether a chunk has locks on it preventing it from being unloaded.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return Whether the chunk is in use.
     */
    public boolean isChunkInUse(int x, int z) {
        GlowChunk.Key key = new GlowChunk.Key(x, z);
        Set<ChunkLock> lockSet = locks.get(key);
        return lockSet != null && lockSet.size() != 0;
    }

    /**
     * Call the ChunkIoService to load a chunk, optionally generating the chunk.
     * @param x The X coordinate of the chunk to load.
     * @param z The Y coordinate of the chunk to load.
     * @param generate Whether to generate the chunk if needed.
     * @return True on success, false on failure.
     */
    public boolean loadChunk(int x, int z, boolean generate) {
        GlowChunk chunk = getChunk(x, z);

        // try to load chunk
        try {
            if (service.read(chunk)) {
                GlowGame.getInstance().getEventManager().post(new GlowChunkEvent.GlowChunkLoadEvent(chunk));
                return true;
            }
        } catch (Exception e) {
            GlowServer.logger.log(Level.SEVERE, "Error while loading chunk (" + x + "," + z + ")", e);
            // an error in chunk reading may have left the chunk in an invalid state
            // (i.e. double initialization errors), so it's forcibly unloaded here
            chunk.unloadChunk(false, false);
        }

        // stop here if we can't generate
        if (!generate) {
            return false;
        }

        // get generating
        try {
            generateChunk(chunk, x, z);
        } catch (Throwable ex) {
            GlowServer.logger.log(Level.SEVERE, "Error while generating chunk (" + x + "," + z + ")", ex);
            return false;
        }

        GlowGame.getInstance().getEventManager().post(new GlowChunkEvent.GlowChunkLoadEvent(chunk));

        // right now, forcePopulate takes care of populating chunks that players actually see.
        /*for (int x2 = x - 1; x2 <= x + 1; ++x2) {
            for (int z2 = z - 1; z2 <= z + 1; ++z2) {
                populateChunk(x2, z2, false);
            }
        }*/
        return true;
    }

    /**
     * Unload chunks with no locks on them.
     */
    public void unloadOldChunks() {
        for (Map.Entry<GlowChunk.Key, GlowChunk> entry : chunks.entrySet()) {
            Set<ChunkLock> lockSet = locks.get(entry.getKey());
            if (lockSet == null || lockSet.size() == 0) {
                if (!entry.getValue().unloadChunk(true, true)) {
                    GlowServer.logger.warning("Failed to unload chunk " + world.getName() + ":" + entry.getKey());
                }
            }
            // cannot remove old chunks from cache - GlowBlock and GlowBlockState keep references.
            // they must either be changed to look up the chunk again all the time, or this code left out.
            /*if (!entry.getValue().isLoaded()) {
                //GlowServer.logger.info("Removing from cache " + entry.getKey());
                chunks.entrySet().remove(entry);
                locks.remove(entry.getKey());
            }*/
        }
    }

    /**
     * Populate a single chunk if needed.
     */
    private void populateChunk(int x, int z, boolean force) {
        GlowChunk chunk = getChunk(x, z);
        // cancel out if it's already populated
        if (chunk.isPopulated()) {
            return;
        }

        // cancel out if the 3x3 around it isn't available
        for (int x2 = x - 1; x2 <= x + 1; ++x2) {
            for (int z2 = z - 1; z2 <= z + 1; ++z2) {
                if (!getChunk(x2, z2).isLoaded() && (!force || !loadChunk(x2, z2, true))) {
                    return;
                }
            }
        }

        // it might have loaded since before, so check again that it's not already populated
        if (chunk.isPopulated()) {
            return;
        }
        chunk.setPopulated(true);

        Random random = new Random(world.getProperties().getSeed());
        long xRand = random.nextLong() / 2 * 2 + 1;
        long zRand = random.nextLong() / 2 * 2 + 1;
        random.setSeed((long) x * xRand + (long) z * zRand ^ world.getProperties().getSeed());

        for (Populator p : world.getWorldGenerator().getPopulators()) {
            p.populate(chunk, random);
        }
    }

    /**
     * Force a chunk to be populated by loading the chunks in an area around it. Used when streaming chunks to players
     * so that they do not have to watch chunks being populated.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
    public void forcePopulation(int x, int z) {
        try {
            populateChunk(x, z, true);
        } catch (Throwable ex) {
            GlowServer.logger.log(Level.SEVERE, "Error while populating chunk (" + x + "," + z + ")", ex);
        }
    }

    /**
     * Initialize a single chunk from the chunk generator.
     */
    private void generateChunk(GlowChunk chunk, int x, int z) {
       // Random random = new Random((long) x * 341873128712L + (long) z * 132897987541L);
        GlowMutableBiomeBuffer biomeBuffer = new GlowMutableBiomeBuffer(new byte[ChunkUtils.WIDTH * ChunkUtils.HEIGHT], chunk);
        GlowMutableBlockBuffer blockBuffer = new GlowMutableBlockBuffer(chunk, new ChunkSection[ChunkUtils.DEPTH / ChunkUtils.SEC_DEPTH], world.getBlockIdManer());
        generator.populate(world, blockBuffer, biomeBuffer);

        ChunkSection[] sections = blockBuffer.getSections();
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] != null) {
                sections[i].recount();
                if (sections[i].count == 0) {
                    sections[i] = null;
                }
            }
        }

        chunk.initializeSections(sections);
        chunk.setBiomes(biomeBuffer.getBiomes());
        chunk.automaticHeightMap();
    }

    /**
     * Forces generation of the given chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return Whether the chunk was successfully regenerated.
     */
    public boolean forceRegeneration(int x, int z) {
        GlowChunk chunk = getChunk(x, z);

        if (chunk == null || !chunk.unloadChunk(false, false)) {
            return false;
        }

        chunk.setPopulated(false);
        try {
            generateChunk(chunk, x, z);
            populateChunk(x, z, false);  // should this be forced?
        } catch (Throwable ex) {
            GlowServer.logger.log(Level.SEVERE, "Error while regenerating chunk (" + x + "," + z + ")", ex);
            return false;
        }
        return true;
    }

    /**
     * Gets a list of loaded chunks.
     * @return The currently loaded chunks.
     */
    public GlowChunk[] getLoadedChunks() {
        ArrayList<GlowChunk> result = new ArrayList<>();
        for (GlowChunk chunk : chunks.values()) {
            if (chunk.isLoaded()) {
                result.add(chunk);
            }
        }
        return result.toArray(new GlowChunk[result.size()]);
    }

    /**
     * Performs the save for the given chunk using the storage provider.
     * @param chunk The chunk to save.
     */
    public boolean performSave(GlowChunk chunk) {
        if (chunk.isLoaded()) {
            try {
                service.write(chunk);
                return true;
            } catch (IOException ex) {
                GlowServer.logger.log(Level.SEVERE, "Error while saving " + chunk, ex);
                return false;
            }
        }
        return false;
    }

    /**
     * Look up the set of locks on a given chunk.
     * @param key The chunk key.
     * @return The set of locks for that chunk.
     */
    private Set<ChunkLock> getLockSet(GlowChunk.Key key) {
        if (locks.containsKey(key)) {
            return locks.get(key);
        } else {
            // only create chunk if it's not in the map already
            Set<ChunkLock> set = new HashSet<>();
            Set<ChunkLock> prev = locks.putIfAbsent(key, set);
            // if it was created in the intervening time, the earlier one wins
            return prev == null ? set : prev;
        }
    }

    /**
     * A group of locks on chunks to prevent them from being unloaded while in use.
     */
    public static class ChunkLock implements Iterable<GlowChunk.Key> {
        private final ChunkManager cm;
        private final String desc;
        private final Set<GlowChunk.Key> keys = new HashSet<>();

        public ChunkLock(ChunkManager cm, String desc) {
            this.cm = cm;
            this.desc = desc;
        }

        public void acquire(GlowChunk.Key key) {
            if (keys.contains(key)) return;
            keys.add(key);
            cm.getLockSet(key).add(this);
            //GlowServer.logger.info(this + " acquires " + key);
        }

        public void release(GlowChunk.Key key) {
            if (!keys.contains(key)) return;
            keys.remove(key);
            cm.getLockSet(key).remove(this);
            //GlowServer.logger.info(this + " releases " + key);
        }

        public void clear() {
            for (GlowChunk.Key key : keys) {
                cm.getLockSet(key).remove(this);
                //GlowServer.logger.info(this + " clearing " + key);
            }
            keys.clear();
        }

        @Override
        public String toString() {
            return "ChunkLock{" + desc + "}";
        }

        @Override
        public Iterator<GlowChunk.Key> iterator() {
            return keys.iterator();
        }
    }
}
