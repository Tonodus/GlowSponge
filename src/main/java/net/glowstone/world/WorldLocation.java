package net.glowstone.world;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.Location;

/**
 * A location in a specific world.
 */
public class WorldLocation {
    private final GlowWorld world;
    private final Vector3d

    public WorldLocation(GlowWorld world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static WorldLocation fromLocation(Location location) {
        if (location.getExtent() instanceof GlowExtent) {
            return ((GlowExtent) location.getExtent()).convertToWorldLocation(location);
        }

        throw new IllegalArgumentException("Location's Extent is not a valid GlowExtent!");
    }
}
