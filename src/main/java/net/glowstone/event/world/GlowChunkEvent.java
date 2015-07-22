package net.glowstone.event.world;

import net.glowstone.event.GlowGameEvent;
import org.spongepowered.api.event.world.ChunkEvent;
import org.spongepowered.api.event.world.ChunkLoadEvent;
import org.spongepowered.api.event.world.ChunkUnloadEvent;
import org.spongepowered.api.world.Chunk;

public class GlowChunkEvent extends GlowGameEvent implements ChunkEvent {
    private final Chunk chunk;

    public GlowChunkEvent(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public Chunk getChunk() {
        return chunk;
    }


    public static class GlowChunkLoadEvent extends GlowChunkEvent implements ChunkLoadEvent {
        public GlowChunkLoadEvent(Chunk chunk) {
            super(chunk);
        }
    }

    public static class GlowUnloadChunkEvent extends GlowChunkEvent implements ChunkUnloadEvent {
        public GlowUnloadChunkEvent(Chunk chunk) {
            super(chunk);
        }
    }
}
