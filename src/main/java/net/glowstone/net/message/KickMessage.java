package net.glowstone.net.message;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

@Data
@RequiredArgsConstructor
public final class KickMessage implements Message {

    private final Text text;

    public KickMessage(String text) {
        this(Texts.of(text));
    }

}
