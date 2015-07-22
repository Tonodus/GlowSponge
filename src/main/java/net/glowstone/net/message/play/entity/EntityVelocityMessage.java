package net.glowstone.net.message.play.entity;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public final class EntityVelocityMessage implements Message {

    private final int id, velocityX, velocityY, velocityZ;

    public EntityVelocityMessage(int id, Vector3d velocity) {
        this(id, convert(velocity.getX()), convert(velocity.getY()), convert(velocity.getZ()));
    }

    private static int convert(double val) {
        return (int) (val * 8000);
    }

}
