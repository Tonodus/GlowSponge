package net.glowstone.net.message.play.player;

import com.flowpowered.math.vector.Vector2f;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.spongepowered.api.world.Location;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class PlayerPositionLookMessage extends PlayerUpdateMessage {

    private final double x, y, z;
    private final float yaw, pitch;

    public PlayerPositionLookMessage(boolean onGround, double x, double y, double z, float yaw, float pitch) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = (yaw % 360 + 360) % 360;
        this.pitch = pitch;
    }

    @Override
    public Location updateLocation(Location location) {
        return new Location(location.getExtent(), x, y, z);
    }

    @Override
    public Vector2f updateRotation(Vector2f rotation) {
        return new Vector2f(yaw, pitch);
    }

    @Override
    public String toString() {
        return "PlayerPositionLookMessage(" +
                "onGround=" + isOnGround() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ')';
    }

}
