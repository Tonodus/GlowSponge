package net.glowstone.net.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import com.google.common.base.Optional;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.player.PlayerAbilitiesMessage;
import org.spongepowered.api.data.manipulator.entity.FlyingData;


public final class PlayerAbilitiesHandler implements MessageHandler<GlowSession, PlayerAbilitiesMessage> {
    @Override
    public void handle(GlowSession session, PlayerAbilitiesMessage message) {
        // player sends this when changing whether or not they are currently flying
        // other values should match what we've sent in the past but are ignored here

        final GlowPlayer player = session.getPlayer();
        boolean flying = (message.getFlags() & 0x02) != 0;

        Optional<FlyingData> flyingData = player.getData(FlyingData.class);
        if (/* player.isAllowFly() */ false && flying) {
            if (!flyingData.isPresent()) {
                player.offer(player.getOrCreate(FlyingData.class).get());
            }
        } else {
            player.remove(FlyingData.class);
        }
    }
}
