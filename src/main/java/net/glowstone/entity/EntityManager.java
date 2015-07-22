package net.glowstone.entity;

import net.glowstone.entity.physics.BoundingBox;
import net.glowstone.util.MutableVector;
import net.glowstone.world.GlowChunk;
import net.glowstone.world.GlowWorld;
import org.spongepowered.api.entity.Entity;

import java.util.*;

/**
 * A class which manages all of the entities within a world.
 * @author Graham Edgecombe
 */
public final class EntityManager implements Iterable<GlowEntity> {

    /**
     * A map of all the entity ids to the corresponding entities.
     */
    private final Map<Integer, GlowEntity> entities = new HashMap<>();

    /**
     * A map of entity types to a set containing all entities of that type.
     */
    private final Map<Class<? extends GlowEntity>, Set<? extends GlowEntity>> groupedEntities = new HashMap<>();

    /**
     * Gets all entities with the specified type.
     * @param type The {@link Class} for the type.
     * @param <T> The type of entity.
     * @return A collection of entities with the specified type.
     */
    @SuppressWarnings("unchecked")
    public <T extends GlowEntity> Collection<T> getAll(Class<T> type) {
        Set<T> set = (Set<T>) groupedEntities.get(type);
        if (set == null) {
            set = new HashSet<>();
            groupedEntities.put(type, set);
        }
        return set;
    }

    /**
     * Gets all entities.
     * @return A collection of entities.
     */
    public Collection<GlowEntity> getAll() {
        return entities.values();
    }

    /**
     * Gets an entity by its id.
     * @param id The id.
     * @return The entity, or {@code null} if it could not be found.
     */
    public GlowEntity getEntity(int id) {
        return entities.get(id);
    }

    /**
     * Registers the entity to this world.
     * @param entity The entity.
     */
    @SuppressWarnings("unchecked")
    void register(GlowEntity entity) {
        if (entity.id == 0) {
            throw new IllegalStateException("Entity has not been assigned an id.");
        }
        entities.put(entity.id, entity);
        ((Collection<GlowEntity>) getAll(entity.getClass())).add(entity);
        getChunk(entity.world, entity.location).getRawEntities().add(entity);
    }

    /**
     * Unregister the entity to this world.
     * @param entity The entity.
     */
    void unregister(GlowEntity entity) {
        entities.remove(entity.id);
        getAll(entity.getClass()).remove(entity);
        getChunk(entity.world, entity.location).getRawEntities().remove(entity);
    }

    /**
     * Notes that an entity has moved from one location to another for
     * physics and storage purposes.
     * @param entity The entity.
     * @param newLocation The new location.
     */
    void move(GlowEntity entity, MutableVector newLocation) {
        GlowChunk prevChunk = getChunk(entity.world, entity.location);
        GlowChunk newChunk = getChunk(entity.world, newLocation);
        if (prevChunk != newChunk) {
            prevChunk.getRawEntities().remove(entity);
            newChunk.getRawEntities().add(entity);
        }
    }

    private static GlowChunk getChunk(GlowWorld world, MutableVector position) {
        return (GlowChunk) world.getChunk(position.getBlockX(), position.getBlockY(), position.getBlockZ()).get();
    }

    @Override
    public Iterator<GlowEntity> iterator() {
        return entities.values().iterator();
    }

    public List<Entity> getEntitiesInside(BoundingBox searchBox, GlowEntity except) {
        // todo: narrow search based on the box's corners
        List<Entity> result = new LinkedList<>();
        for (GlowEntity entity : entities.values()) {
            if (entity != except && entity.intersects(searchBox)) {
                result.add(entity);
            }
        }
        return result;
    }
}
