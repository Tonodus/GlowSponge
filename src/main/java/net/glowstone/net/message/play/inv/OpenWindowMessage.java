package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

@Data
@RequiredArgsConstructor
public final class OpenWindowMessage implements Message {

    private final int id;
    private final String type;
    private final Text title;
    private final int slots, entityId;

    public OpenWindowMessage(int id, String type, String title, int slots) {
        this(id, type, Texts.of(title), slots, 0);
    }

}
