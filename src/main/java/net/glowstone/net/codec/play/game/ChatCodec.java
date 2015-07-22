package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.text.TextUtils;
import org.spongepowered.api.text.Text;

import java.io.IOException;

public final class ChatCodec implements Codec<ChatMessage> {
    @Override
    public ChatMessage decode(ByteBuf buf) throws IOException {
        Text message = GlowBufUtils.readChat(buf);
        int mode = buf.readByte();
        return new ChatMessage(message, mode);
    }

    @Override
    public ByteBuf encode(ByteBuf buf, ChatMessage message) throws IOException {
        GlowBufUtils.writeChat(buf, TextUtils.toJSON(message.getText()));
        buf.writeByte(message.getMode());
        return buf;
    }
}
