package net.glowstone.event.entity;

import net.glowstone.event.GlowGameEvent;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.entity.EntityEvent;

public class GlowEntityEvent extends GlowGameEvent implements EntityEvent {
    private final Entity entity;

    public GlowEntityEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return null;
    }
}
