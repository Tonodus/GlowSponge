package net.glowstone.item.behavior;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.block.GlowBlockType;
import net.glowstone.entity.player.GlowPlayer;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

public class ItemBanner extends BaseItemBehavior {

    @Override
    public void rightClickBlock(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        GlowBlockType placeAs;
        if (face == Direction.UP) {
            placeAs = BlockTypes.STANDING_BANNER;
        } else if (face == Direction.DOWN) {
            return;
        } else {
            placeAs = BlockTypes.WALL_BANNER;
        }

        placeAs.getBehavior().performPlace(player, target, face, holding, clickedLoc);
    }
}
