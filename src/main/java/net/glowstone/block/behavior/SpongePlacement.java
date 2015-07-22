package net.glowstone.block.behavior;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.glowstone.block.GlowBlockState;
import net.glowstone.data.manipulator.block.GlowWetData;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.util.BlockTypeValidator;
import net.glowstone.util.TaxicabBlockIterator;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.WetData;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.Set;

public class SpongePlacement extends BaseBlockBehavior {

    private static final Set<BlockType> WATER_MATERIALS = ImmutableSet.of((BlockType) BlockTypes.WATER, BlockTypes.FLOWING_WATER);

    @Override
    public void placeBlock(GlowPlayer player, Location location, GlowBlockState state, Direction face, ItemStack holding, Vector3d clickedLoc) {
        // TODO: Move this to a new method when physics works and run this on neighbour change too.

        Optional<WetData> data = state.getManipulator(WetData.class);
        boolean isWet = data.isPresent();

        if (!isWet) {
            TaxicabBlockIterator iterator = new TaxicabBlockIterator(location);
            iterator.setMaxDistance(7);
            iterator.setMaxBlocks(66);
            iterator.setValidator(new BlockTypeValidator(WATER_MATERIALS));

            if (iterator.hasNext()) {
                isWet = true;
                do {
                    iterator.next().removeBlock();
                } while (iterator.hasNext());
            }
        }

        location.setBlock(isWet ? state.withData((WetData) new GlowWetData()).get() : state);
    }
}
