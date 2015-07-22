package net.glowstone.net.message.play.game;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.world.Location;

@Data
@RequiredArgsConstructor
public final class PositionRotationMessage implements Message {

    private final double x, y, z;
    private final float rotation, pitch;
    private final int flags;

    public PositionRotationMessage(double x, double y, double z, float rotation, float pitch) {
        this(x, y, z, rotation, pitch, 0);
    }

    public PositionRotationMessage(Location location, Vector2f rotation) {
        this(location.getX(), location.getY(), location.getZ(), rotation.getX(), rotation.getY());
    }

}
