package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.spongepowered.api.text.Text;

@Data
public final class UserListHeaderFooterMessage implements Message {

    private final Text header, footer;

}
