package net.glowstone.net.handler.play.game;

import com.flowpowered.networking.MessageHandler;
import com.google.common.base.Optional;
import net.glowstone.GlowServer;
import net.glowstone.block.entity.TESign;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.game.UpdateSignMessage;
import net.glowstone.text.TextUtils;
import org.spongepowered.api.block.tileentity.Sign;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.world.Location;

public final class UpdateSignHandler implements MessageHandler<GlowSession, UpdateSignMessage> {
    @Override
    public void handle(GlowSession session, UpdateSignMessage message) {
        final GlowPlayer player = session.getPlayer();

        // filter out json messages that aren't plaintext
        String[] lines = new String[4];
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = TextUtils.strip(message.getMessage()[i]);
        }

        Location location = new Location(player.getWorld(), message.getX(), message.getY(), message.getZ());
        if (player.checkSignLocation(location)) {
            // update the sign if it's actually still there
            Optional<TileEntity> te = location.getTileEntity();
            if (te.isPresent() && te.get() instanceof Sign) {
                TESign sign = (TESign) te.get();
                SignData data = sign.getOrCreate(SignData.class).get();
                for (int i = 0; i < lines.length; i++) {
                    data.setLine(i, Texts.of(lines[i]));
                }
                sign.offer(data);
                sign.updateInRange();
            }
        } else {
            // player shouldn't be editing this sign
            GlowServer.logger.warning(session + " tried to edit sign at " + location);
        }
    }
}
