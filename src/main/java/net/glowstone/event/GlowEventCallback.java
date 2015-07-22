package net.glowstone.event;

import org.spongepowered.api.event.Order;
import org.spongepowered.api.util.event.callback.EventCallback;

public abstract class GlowEventCallback implements EventCallback {
    private final boolean baseGame;
    private final Order order;

    public GlowEventCallback(boolean baseGame, Order order) {
        this.baseGame = baseGame;
        this.order = order;
    }

    @Override
    public boolean isBaseGame() {
        return baseGame;
    }

    @Override
    public Order getOrder() {
        return order;
    }
}
