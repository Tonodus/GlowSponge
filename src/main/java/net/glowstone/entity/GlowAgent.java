package net.glowstone.entity;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.GlowServer;
import net.glowstone.world.GlowWorld;
import org.spongepowered.api.entity.living.Agent;

public abstract class GlowAgent extends GlowLivingEntity implements Agent {
    public GlowAgent(GlowServer server, GlowWorld world, Vector3d location) {
        super(server, world, location);
    }
}
