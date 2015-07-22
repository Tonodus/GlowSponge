package net.glowstone.item.behavior;

import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.net.message.play.game.PluginMessage;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemWrittenBook extends BaseItemBehavior {

    private static final byte[] EMPTY = new byte[0];

    @Override
    public void rightClickBlock(GlowPlayer player, BukkitBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        openBook(player);
    }

    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        openBook(player);
    }

    private void openBook(GlowPlayer player) {
        player.getSession().send(new PluginMessage("MC|BOpen", EMPTY));
    }
}
