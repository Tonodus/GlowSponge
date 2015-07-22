package net.glowstone.event.world;

import net.glowstone.event.GlowGameEvent;
import org.spongepowered.api.event.world.WorldEvent;
import org.spongepowered.api.event.world.WorldLoadEvent;
import org.spongepowered.api.event.world.WorldUnloadEvent;
import org.spongepowered.api.world.World;

public class GlowWorldEvent extends GlowGameEvent implements WorldEvent {
    private final World world;

    public GlowWorldEvent(World world) {
        this.world = world;
    }

    @Override
    public World getWorld() {
        return world;
    }

    public static class GlowWorldLoadEvent extends GlowWorldEvent implements WorldLoadEvent {
        public GlowWorldLoadEvent(World world) {
            super(world);
        }
    }

    public static class GlowWorldUnloadEvent extends GlowWorldEvent implements WorldUnloadEvent {
        public GlowWorldUnloadEvent(World world) {
            super(world);
        }
    }
}
