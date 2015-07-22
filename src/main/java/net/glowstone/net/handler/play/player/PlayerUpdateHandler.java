package net.glowstone.net.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.event.entity.GlowPlayerMoveEvent;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.game.PositionRotationMessage;
import net.glowstone.net.message.play.player.PlayerUpdateMessage;
import net.glowstone.util.MutableVector;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spongepowered.api.world.Location;

public final class PlayerUpdateHandler implements MessageHandler<GlowSession, PlayerUpdateMessage> {

    @Override
    public void handle(GlowSession session, PlayerUpdateMessage message) {
        MutableVector oldLocation = session.getPlayer().getRawLocation();
        Location newLocation = message.updateLocation(session.getPlayer().getLocation());

        // don't let players move more than 16 blocks in a single packet.
        // this is NOT robust hack prevention - only to prevent client
        // confusion about where its actual location is (e.g. during login)
        if (newLocation.distanceSquared(oldLocation) > 16 * 16) {
            return;
        }

        // call move event if movement actually occurred and there are handlers registered
        if (!oldLocation.equals(newLocation) && PlayerMoveEvent.getHandlerList().getRegisteredListeners().length > 0) {
            final PlayerMoveEvent event = new GlowPlayerMoveEvent();
            if (event.isCancelled()) {
                // tell client they're back where they started
                session.send(new PositionRotationMessage(oldLocation));
                return;
            }

            if (!event.getTo().equals(newLocation)) {
                // teleport to the set destination: fires PlayerTeleportEvent and
                // handles if the destination is in another world
                session.getPlayer().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                return;
            }
        }

        // move event was not fired or did nothing, simply update location
        session.getPlayer().setRawLocation(newLocation);
    }
}
