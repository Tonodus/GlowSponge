package net.glowstone.item.behavior;

import com.flowpowered.math.vector.Vector3d;
import io.netty.util.Signal;
import net.glowstone.entity.player.GlowPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

/**
 * Base class for item behavior.
 */
public class BaseItemBehavior implements ItemBehavior {
    public static final Signal NEXT = Signal.valueOf("ItemBehavior.NEXT");

    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        throw NEXT;
    }

    public void rightClickBlock(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        throw NEXT;
    }
}
