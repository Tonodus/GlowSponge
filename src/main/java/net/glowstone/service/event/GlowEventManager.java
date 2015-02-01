package net.glowstone.service.event;

import net.glowstone.GlowGame;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.util.event.Event;

public class GlowEventManager implements EventManager {
    private final GlowGame game;

    public GlowEventManager(GlowGame game) {
        this.game = game;
    }

    @Override
    public void register(Object plugin, Object obj) {

    }

    @Override
    public void unregister(Object obj) {

    }

    @Override
    public boolean post(Event event) {
        return false;
    }
}
