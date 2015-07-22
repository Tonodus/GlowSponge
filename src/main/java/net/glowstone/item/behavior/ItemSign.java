package net.glowstone.item.behavior;

import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.entity.player.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemSign extends BaseItemBehavior {

    @Override
    public void rightClickBlock(GlowPlayer player, BukkitBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        BlockType placeAs;
        if (face == BlockFace.UP) {
            placeAs = ItemTable.instance().getBlock(Material.SIGN_POST);
        } else if (face == BlockFace.DOWN) {
            return;
        } else {
            placeAs = ItemTable.instance().getBlock(Material.WALL_SIGN);
        }
        placeAs.rightClickBlock(player, target, face, holding, clickedLoc);
    }

}
