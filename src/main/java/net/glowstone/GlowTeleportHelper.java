package net.glowstone;

import com.google.common.base.Optional;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;

public class GlowTeleportHelper implements TeleportHelper{
    @Override
    public Optional<Location> getSafeLocation(Location location) {
        return null;
    }

    @Override
    public Optional<Location> getSafeLocation(Location location, int height, int width) {
        return null;
    }
}
