package net.glowstone.net.message.play.player;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.networking.Message;
import lombok.Data;
import org.spongepowered.api.world.Location;

/**
 * Base class for player update messages.
 */
@Data
public class PlayerUpdateMessage implements Message {

    private final boolean onGround;

    public Location updateLocation(Location location) {
        // do nothing
        return location;
    }

    public Vector2f updateRotation(Vector2f rotation) {
        //do nothing
        return rotation;
    }

}
