package net.glowstone.event;

import net.glowstone.GlowGame;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.util.event.callback.CallbackList;

public class GlowGameEvent implements GameEvent {
    private final CallbackList callbacks;

    public GlowGameEvent(CallbackList callbacks) {
        this.callbacks = callbacks;
    }

    public GlowGameEvent() {
        callbacks = new CallbackList();
    }

    @Override
    public Game getGame() {
        return GlowGame.getInstance();
    }

    @Override
    public CallbackList getCallbacks() {
        return callbacks;
    }
}
