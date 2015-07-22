package net.glowstone.entity;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.networking.Message;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.glowstone.GlowServer;
import net.glowstone.data.GlowDataHolder;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.MetadataMap;
import net.glowstone.entity.physics.BoundingBox;
import net.glowstone.entity.physics.EntityBoundingBox;
import net.glowstone.net.message.play.entity.*;
import net.glowstone.util.Locations;
import net.glowstone.util.MutableVector;
import net.glowstone.util.Position;
import net.glowstone.world.GlowChunk;
import net.glowstone.world.GlowWorld;
import org.apache.commons.lang3.Validate;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.Property;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.RelativePositions;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Represents some entity in the world such as an item on the floor or a player.
 * @author Graham Edgecombe
 */
public abstract class GlowEntity extends GlowDataHolder implements Entity {
    /**
     * The server this entity belongs to.
     */
    protected final GlowServer server;

    /**
     * The entity's metadata.
     */
    protected final MetadataMap metadata = new MetadataMap(getClass());

    /**
     * The world this entity belongs to.
     */
    protected GlowWorld world;

    /**
     * Whether this entity was spawned.
     */
    protected boolean spawned = false;

    /**
     * A flag indicating if this entity is currently active.
     */
    protected boolean active = true;

    /**
     * This entity's unique id.
     */
    private UUID uuid;

    /**
     * This entity's current identifier for its world.
     */
    protected int id;

    /**
     * The current position.
     */
    protected final MutableVector location;

    /**
     * The position in the last cycle.
     */
    protected final MutableVector previousLocation;

    /**
     * The entity's rotation.
     */
    protected double yaw, pitch;

    /**
     * The entity's velocity, applied each tick.
     */
    protected Vector3d velocity = new Vector3d();

    /**
     * The entity's bounding box, or null if it has no physical presence.
     */
    private EntityBoundingBox boundingBox;

    /**
     * Whether the entity should have its position resent as if teleported.
     */
    protected boolean teleported = false;

    /**
     * Whether the entity should have its velocity resent.
     */
    protected boolean velocityChanged = false;

    /**
     * A flag indicting if the entity is on the ground
     */
    private boolean onGround = true;

    /**
     * The distance the entity is currently falling without touching the ground.
     */
    private float fallDistance;

    /**
     * A counter of how long this entity has existed
     */
    private int ticksLived = 0;

    /**
     * How long the entity has been on fire, or 0 if it is not.
     */
    private int fireTicks = 0;

    /**
     * The entity's properties.
     */
    private final ImmutableMap<Class<?>, Property<?, ?>> properties;

    /**
     * Creates a new instance of this entity without spawning it.
     */
    public GlowEntity(Location location) {
        this.world = Locations.world(location);
        this.server = this.world.getServer();
        this.location = new MutableVector(Locations.worldLoc(location));
        this.yaw = this.pitch = 0;
        previousLocation = this.location.clone();
        properties = ImmutableMap.of();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Core properties

    public void spawn() {
        this.spawned = true;
        server.getEntityIdManager().allocate(this);
        world.getEntityManager().register(this);
    }

    @Override
    public final GlowWorld getWorld() {
        return world;
    }

    public final int getEntityId() {
        return id;
    }

    @Override
    public UUID getUniqueId() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    public boolean isDead() {
        return !active;
    }

    public boolean isValid() {
        return world.getEntityManager().getEntity(id) == this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Location stuff

    @Override
    public Location getLocation() {
        return new Location(world, location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void setLocation(Location newLocation) {
        Validate.notNull(newLocation, "location cannot be null");

        Location location = Locations.worldLoc(newLocation);
        if (location.getExtent() != world) {
            world.getEntityManager().unregister(this);
            world = (GlowWorld) location.getExtent();
            world.getEntityManager().register(this);
        }
        setRawLocation(location);
        teleported = true;
    }

    @Override
    public void setLocationAndRotation(Location location, Vector3d rotation) {
        setLocation(location);
        this.yaw = rotation.getX();
        this.pitch = rotation.getY();
    }

    @Override
    public void setLocationAndRotation(Location location, Vector3d rotation, EnumSet<RelativePositions> relativePositions) {

    }

    @Override
    public boolean setLocationSafely(Location location) {
        Optional<Location> newLocation = server.getGame().getTeleportHelper().getSafeLocation(location);
        if (newLocation.isPresent()) {
            setLocation(location);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setLocationAndRotationSafely(Location location, Vector3d rotation) {
        Optional<Location> newLocation = server.getGame().getTeleportHelper().getSafeLocation(location);
        if (newLocation.isPresent()) {
            setLocationAndRotation(location, rotation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setLocationAndRotationSafely(Location location, Vector3d rotation, EnumSet<RelativePositions> relativePositions) {
        Optional<Location> newLocation = server.getGame().getTeleportHelper().getSafeLocation(location);
        if (newLocation.isPresent()) {
            setLocationAndRotation(location, rotation, relativePositions);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean transferToWorld(String worldName, Vector3d position) {
        Optional<World> world = server.loadWorld(worldName);
        if (world.isPresent()) {
            setLocation(new Location(world.get(), position.getX(), position.getY(), position.getZ()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean transferToWorld(UUID uuid, Vector3d position) {
        Optional<World> world = server.loadWorld(uuid);
        if (world.isPresent()) {
            setLocation(new Location(world.get(), position.getX(), position.getY(), position.getZ()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Vector3d getRotation() {
        return new Vector3d(yaw, pitch, 0);
    }

    @Override
    public void setRotation(Vector3d rotation) {
        this.yaw = rotation.getX();
        this.pitch = rotation.getY();
        teleported = true;
    }

    /**
     * Get the direction (SOUTH, WEST, NORTH, or EAST) this entity is facing.
     * @return The cardinal BlockFace of this entity.
     */
    public Direction getDirection() {
        double rot = yaw % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 45) {
            return Direction.SOUTH;
        } else if (45 <= rot && rot < 135) {
            return Direction.WEST;
        } else if (135 <= rot && rot < 225) {
            return Direction.NORTH;
        } else if (225 <= rot && rot < 315) {
            return Direction.EAST;
        } else if (315 <= rot && rot < 360.0) {
            return Direction.SOUTH;
        } else {
            return Direction.EAST;
        }
    }

    /**
     * Gets the full direction (including SOUTH_SOUTH_EAST etc) this entity is facing.
     * @return The intercardinal BlockFace of this entity
     */
    public Direction getFacing() {
        long facing = Math.round(yaw / 22.5) + 8;
        return Position.getDirection((byte) (facing % 16));
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
        velocityChanged = true;
    }

    public void setVelocity(MutableVector velocity) {
        this.velocity = velocity.toVector3d();
        velocityChanged = true;
    }

    public Vector3d getVelocity() {
        return velocity.clone();
    }


    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Checks if this entity is within the visible radius of another.
     * @param other The other entity.
     * @return {@code true} if the entities can see each other, {@code false} if
     * not.
     */
    public boolean isWithinDistance(GlowEntity other) {
        return !other.isDead() && (isWithinDistance(other.world, other.location) || other instanceof GlowLightningStrike);
    }

    /**
     * Checks if this entity is within the visible radius of a location.
     * @param loc The location.
     * @return {@code true} if the entities can see each other, {@code false} if
     * not.
     */
    public boolean isWithinDistance(GlowWorld world, MutableVector loc) {
        double dx = Math.abs(location.getX() - loc.getX());
        double dz = Math.abs(location.getZ() - loc.getZ());
        return this.world == world && dx <= (server.getViewDistance() * GlowChunk.WIDTH) && dz <= (server.getViewDistance() * GlowChunk.HEIGHT);
    }

    /**
     * Checks whether this entity should be saved as part of the world.
     * @return True if the entity should be saved.
     */
    public boolean shouldSave() {
        return true;
    }

    /**
     * Called every game cycle. Subclasses should implement this to implement
     * periodic functionality e.g. mob AI.
     */
    public void pulse() {
        ticksLived++;

        if (fireTicks > 0) {
            --fireTicks;
        }
        metadata.setBit(MetadataIndex.STATUS, MetadataIndex.StatusFlags.ON_FIRE, fireTicks > 0);

        // resend position if it's been a while
        if (ticksLived % (30 * 20) == 0) {
            teleported = true;
        }

        pulsePhysics();

        if (hasMoved()) {
            checkEndTeleporting();
        }
    }

    private void checkEndTeleporting() {
       /* Block currentBlock = location.getBlock();
        if (currentBlock.getType() == Material.ENDER_PORTAL) {
            EventFactory.callEvent(new EntityPortalEnterEvent(this, currentBlock.getLocation()));
            if (server.getAllowEnd()) {
                Location previousLocation = location.clone();
                boolean success;
                if (getWorld().getEnvironment() == World.Environment.THE_END) {
                    success = teleportToSpawn();
                } else {
                    success = teleportToEnd();
                }
                if (success) {
                    EntityPortalExitEvent e = EventFactory.callEvent(new EntityPortalExitEvent(this, previousLocation, location.clone(), velocity.clone(), new Vector()));
                    if (!e.getAfter().equals(velocity)) {
                        setVelocity(e.getAfter());
                    }
                }
            }
        }*/
    }

    /**
     * Resets the previous location and other properties to their current value.
     */
    public void reset() {
        previousLocation.updateFrom(location);
        metadata.resetChanges();
        teleported = false;
        velocityChanged = false;
    }

    /**
     * Sets this entity's location.
     * @param location The new location.
     */
    public void setRawLocation(Location location) {
        setRawLocation(new MutableVector(location));
    }

    public void setRawLocation(MutableVector location) {
        world.getEntityManager().move(this, location);
        this.location.updateFrom(location);
    }

    /**
     * Sets this entity's unique identifier if possible.
     * @param uuid The new UUID. Must not be null.
     * @throws IllegalArgumentException if the passed UUID is null.
     * @throws IllegalStateException if a UUID has already been set.
     */
    public void setUniqueId(UUID uuid) {
        Validate.notNull(uuid, "uuid must not be null");
        if (this.uuid == null) {
            this.uuid = uuid;
        } else if (!this.uuid.equals(uuid)) {
            // silently allow setting the same UUID, since
            // it can't be checked with getUniqueId()
            throw new IllegalStateException("UUID of " + this + " is already " + this.uuid);
        }
    }

    /**
     * Creates a {@link Message} which can be sent to a client to spawn this
     * entity.
     * @return A message which can spawn this entity.
     */
    public abstract List<Message> createSpawnMessage();

    /**
     * Creates a {@link Message} which can be sent to a client to update this
     * entity.
     * @return A message which can update this entity.
     */
    public List<Message> createUpdateMessage() {
        boolean moved = hasMoved();
        boolean rotated = hasRotated();

        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);

        int dx = x - Position.getIntX(previousLocation);
        int dy = y - Position.getIntY(previousLocation);
        int dz = z - Position.getIntZ(previousLocation);

        boolean teleport = dx > Byte.MAX_VALUE || dy > Byte.MAX_VALUE || dz > Byte.MAX_VALUE || dx < Byte.MIN_VALUE || dy < Byte.MIN_VALUE || dz < Byte.MIN_VALUE;

        int yaw = Position.getIntYaw(this.yaw);
        int pitch = Position.getIntPitch(this.pitch);

        List<Message> result = new LinkedList<>();
        if (teleported || (moved && teleport)) {
            result.add(new EntityTeleportMessage(id, x, y, z, yaw, pitch));
        } else if (moved && rotated) {
            result.add(new RelativeEntityPositionRotationMessage(id, dx, dy, dz, yaw, pitch));
        } else if (moved) {
            result.add(new RelativeEntityPositionMessage(id, dx, dy, dz));
        } else if (rotated) {
            result.add(new EntityRotationMessage(id, yaw, pitch));
        }

        // todo: handle head rotation as a separate value
        if (rotated) {
            result.add(new EntityHeadRotationMessage(id, yaw));
        }

        // send changed metadata
        List<MetadataMap.Entry> changes = metadata.getChanges();
        if (changes.size() > 0) {
            result.add(new EntityMetadataMessage(id, changes));
        }

        // send velocity if needed
        if (velocityChanged) {
            result.add(new EntityVelocityMessage(id, velocity));
        }

        return result;
    }

    /**
     * Checks if this entity has moved this cycle.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasMoved() {
        return Position.hasMoved(location, previousLocation);
    }

    /**
     * Checks if this entity has rotated this cycle.
     * @return {@code true} if so, {@code false} if not.
     */
    public boolean hasRotated() {
        return Position.hasRotated(location, previousLocation);
    }

    /**
     * Teleport this entity to the spawn point of the main world.
     * This is used to teleport out of the End.
     * @return {@code true} if the teleport was successful.
     */
    protected boolean teleportToSpawn() {
        Location target = server.getWorlds().get(0).getSpawnLocation();

       /*
        TODO: event?
        EntityPortalEvent event = EventFactory.callEvent(new EntityPortalEvent(this, location.clone(), target, null));
        if (event.isCancelled()) {
            return false;
        }
        target = event.getTo();*/

        setLocation(target);
        return true;
    }

    /**
     * Teleport this entity to the End.
     * If no End world is loaded this does nothing.
     * @return {@code true} if the teleport was successful.
     */
    protected boolean teleportToEnd() {
        /*
        TODO: allowEnd
        if (!allowEnd) {
            return false;
        }*/
        Location target = null;
        for (World world : server.getWorlds()) {
            if (world.getDimension().getType() == DimensionTypes.END) {
                target = world.getSpawnLocation();
                break;
            }
        }
        if (target == null) {
            return false;
        }

       /*
       TODO: event?
       EntityPortalEvent event = EventFactory.callEvent(new EntityPortalEvent(this, location.clone(), target, null));
        if (event.isCancelled()) {
            return false;
        }
        target = event.getTo();*/

        setLocation(target);
        return true;
    }

    /**
     * Determine if this entity is intersecting a block of the specified type.
     * If the entity has a defined bounding box, that is used to check for
     * intersection. Otherwise,
     * @param material The material to check for.
     * @return True if the entity is intersecting
     */
    public boolean isTouchingMaterial(BlockType material) {
        if (boundingBox == null) {
            // less accurate calculation if no bounding box is present
            for (Direction face : new Direction[]{Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH, Direction.DOWN, Direction.NONE,
                    Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST}) {
                Vector3d adding = face.toVector3d();
                //TODO: correct rounding
                if (world.getBlockType((int) (location.getX() + adding.getX()), (int) (location.getY() + adding.getY()), (int) (location.getZ() + adding.getZ())).equals(material)) {
                    return true;
                }
            }
        } else {
            // bounding box-based calculation
            MutableVector min = boundingBox.minCorner, max = boundingBox.maxCorner;
            for (int x = min.getBlockX(); x <= max.getBlockX(); ++x) {
                for (int y = min.getBlockY(); y <= max.getBlockY(); ++y) {
                    for (int z = min.getBlockZ(); z <= max.getBlockZ(); ++z) {
                        if (world.getBlockType(x, y, z).equals(material)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Physics stuff

    protected final void setBoundingBox(double xz, double y) {
        boundingBox = new EntityBoundingBox(xz, y);
    }

    public boolean intersects(BoundingBox box) {
        return boundingBox != null && boundingBox.intersects(box);
    }

    protected void pulsePhysics() {
        // todo: update location based on velocity,
        // do gravity, all that other good stuff

        // make sure bounding box is up to date
        if (boundingBox != null) {
            boundingBox.setCenter(location.getX(), location.getY(), location.getZ());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Various properties
    public int getFireTicks() {
        return fireTicks;
    }

    public void setFireTicks(int ticks) {
        fireTicks = ticks;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public void setFallDistance(float distance) {
        fallDistance = Math.max(distance, 0);
    }

    public int getTicksLived() {
        return ticksLived;
    }

    public void setTicksLived(int value) {
        this.ticksLived = value;
    }

    @Override
    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public boolean isRemoved() {
        return active;
    }

    @Override
    public boolean isLoaded() {
        return !isRemoved();
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Miscellaneous actions

    @Override
    public void remove() {
        active = false;
        world.getEntityManager().unregister(this);
        server.getEntityIdManager().deallocate(this);
    }

    public List<Entity> getNearbyEntities(double x, double y, double z) {
        // This behavior is similar to CraftBukkit, where a call with args
        // (0, 0, 0) finds any entities whose bounding boxes intersect that of
        // this entity.

        BoundingBox searchBox;
        if (boundingBox == null) {
            searchBox = BoundingBox.fromPositionAndSize(location, new MutableVector(0, 0, 0));
        } else {
            searchBox = BoundingBox.copyOf(boundingBox);
        }
        Vector3d vec = new Vector3d(x, y, z);
        searchBox.minCorner.sub(vec);
        searchBox.maxCorner.add(vec);

        return world.getEntityManager().getEntitiesInside(searchBox, this);
    }

   /* @Override
    public void playEffect(Effect type) {
        EntityStatusMessage message = new EntityStatusMessage(id, type);
        for (GlowPlayer player : world.getRawPlayers()) {
            if (player.canSeeEntity(this)) {
                player.getSession().send(message);
            }
        }
    }*/

    @Override
    public final DataContainer toContainer() {
        DataContainer container = super.toContainer();
        serialize(container);
        return container;
    }

    protected void serialize(DataView data) {
        data.set(DataQuery.of("world"), world.getUniqueId().toString());
        data.set(DataQuery.of("x"), location.getX());
        data.set(DataQuery.of("y"), location.getY());
        data.set(DataQuery.of("z"), location.getZ());
    }
}
