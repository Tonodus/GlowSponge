package net.glowstone.net.message.play.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.spongepowered.api.world.Location;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class PlayerPositionMessage extends PlayerUpdateMessage {

    private final double x, y, z;

    public PlayerPositionMessage(boolean onGround, double x, double y, double z) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Location updateLocation(Location location) {
        return new Location(location.getExtent(), x, y, z);
    }

    @Override
    public String toString() {
        return "PlayerPositionMessage(" +
                "onGround=" + isOnGround() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ')';
    }

}
