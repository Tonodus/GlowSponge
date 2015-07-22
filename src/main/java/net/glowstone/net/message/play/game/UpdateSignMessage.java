package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import lombok.Data;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;

@Data
public final class UpdateSignMessage implements Message {

    private final int x, y, z;
    private final Text[] message;

    public UpdateSignMessage(int x, int y, int z, Text[] message) {
        if (message.length != 4) {
            throw new IllegalArgumentException();
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.message = message;
    }

    public static UpdateSignMessage fromPlainText(int x, int y, int z, String[] message) {
        if (message.length != 4) {
            throw new IllegalArgumentException();
        }

        Text[] encoded = new Text[4];
        for (int i = 0; i < 4; ++i) {
            encoded[i] = Texts.of(message[i]);
        }
        return new UpdateSignMessage(x, y, z, encoded);
    }

}
