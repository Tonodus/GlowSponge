package net.glowstone.net.message.play.player;

import com.flowpowered.math.vector.Vector2f;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class PlayerLookMessage extends PlayerUpdateMessage {

    private final float yaw, pitch;

    public PlayerLookMessage(float yaw, float pitch, boolean onGround) {
        super(onGround);
        this.yaw = (yaw % 360 + 360) % 360;
        this.pitch = pitch;
    }

    @Override
    public Vector2f updateRotation(Vector2f rotation) {
        return new Vector2f(yaw, pitch);
    }

    @Override
    public String toString() {
        return "PlayerLookMessage(" +
                "yaw=" + yaw +
                ", pitch=" + pitch +
                ", onGround=" + isOnGround() +
                ')';
    }

}
