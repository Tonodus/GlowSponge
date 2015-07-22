package net.glowstone.net.message.play.player;

import com.flowpowered.math.vector.Vector3i;
import com.flowpowered.networking.Message;
import lombok.Data;

@Data
public final class TabCompleteMessage implements Message {

    private final String text;
    private final Vector3i location;

}

