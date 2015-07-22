package net.glowstone.item.behavior;

import com.flowpowered.math.vector.Vector3d;
import io.netty.util.Signal;
import net.glowstone.entity.player.GlowPlayer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

public class ListItemBehavior implements ItemBehavior {
    private static final ItemBehavior fallback = new DefaultItemBehavior();

    private final ItemBehavior[] behaviors;

    public ListItemBehavior(ItemBehavior... behaviors) {
        this.behaviors = behaviors;
    }

    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        for (ItemBehavior behavior : behaviors) {
            try {
                behavior.rightClickAir(player, holding);
                return;
            } catch (Signal signal) {
                BaseItemBehavior.NEXT.expect(signal);
            }
        }

        fallback.rightClickAir(player, holding);
    }

    @Override
    public void rightClickBlock(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        for (ItemBehavior behavior : behaviors) {
            try {
                behavior.rightClickAir(player, holding);
                return;
            } catch (Signal signal) {
                BaseItemBehavior.NEXT.expect(signal);
            }
        }

        fallback.rightClickBlock(player, target, face, holding, clickedLoc);
    }
}
