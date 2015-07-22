package net.glowstone.util;

import net.glowstone.world.GlowChunk;
import net.glowstone.world.GlowWorld;
import org.spongepowered.api.world.Location;

public class Locations {
    public static Location worldLoc(Location location) {
        if (location.getExtent() instanceof GlowWorld) {
            return location;
        } else if (location.getExtent() instanceof GlowChunk) {
            return new Location(((GlowChunk) location.getExtent()).getWorld(), location.getX(), location.getY(), location.getZ());
        } else {
            throw new IllegalArgumentException("Invalid Location Extent: Must be GlowWorld or GlowChunk!");
        }
    }

    public static GlowWorld world(Location location) {
        if (location.getExtent() instanceof GlowWorld) {
            return (GlowWorld) location.getExtent();
        } else if (location.getExtent() instanceof GlowChunk) {
            return ((GlowChunk) location.getExtent()).getWorld();
        } else {
            throw new IllegalArgumentException("Invalid Location Extent: Must be GlowWorld or GlowChunk!");
        }
    }
}
