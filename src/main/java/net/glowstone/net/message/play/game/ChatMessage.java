package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.text.Text;

@Data
@RequiredArgsConstructor
public final class ChatMessage implements Message {

    private final Text text;
    private final int mode;

    public ChatMessage(Text json) {
        this(json, 0);
    }
}
