package net.glowstone.event.entity;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.world.Location;

public class GlowPlayerMoveEvent extends GlowEntityEvent implements PlayerMoveEvent {
    private boolean cancelled;

    public GlowPlayerMoveEvent(Player entity) {
        super(entity);
    }

    @Override
    public Location getOldLocation() {
        return null;
    }

    @Override
    public Location getNewLocation() {
        return null;
    }

    @Override
    public void setNewLocation(Location location) {

    }

    @Override
    public Vector3d getRotation() {
        return null;
    }

    @Override
    public void setRotation(Vector3d rotation) {

    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Player getUser() {
        return getEntity();
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }
}
