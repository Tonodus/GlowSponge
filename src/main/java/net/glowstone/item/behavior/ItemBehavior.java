package net.glowstone.item.behavior;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.entity.player.GlowPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

public interface ItemBehavior {
    /**
     * Called when a player right-clicks in midair while holding this item.
     * Also called by default if rightClickBlock is not overridden.
     * @param player The player
     * @param holding The ItemStack the player was holding
     */
    void rightClickAir(GlowPlayer player, ItemStack holding);

    /**
     * Called when a player right-clicks on a block while holding this item.
     * @param player The player
     * @param target The block the player right-clicked
     * @param face The face on which the click occurred
     * @param holding The ItemStack the player was holding
     * @param clickedLoc The coordinates at which the click occurred
     */
    void rightClickBlock(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc);
}
