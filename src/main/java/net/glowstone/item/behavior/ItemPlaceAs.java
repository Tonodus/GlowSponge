package net.glowstone.item.behavior;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.entity.player.GlowPlayer;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

public class ItemPlaceAs extends BaseItemBehavior {
    private final BlockType placeAs;

    public ItemPlaceAs(BlockType blockType) {
        this.placeAs = blockType;
    }

    @Override
    public void rightClickBlock(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        target.setBlockType(placeAs);
    }
}
