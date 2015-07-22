package net.glowstone.block.behavior;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.glowstone.block.BlockBehavior;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.item.GlowItemStackBuilder;
import net.glowstone.world.GlowChunk;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.ItemBlock;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.Arrays;
import java.util.Collection;

/**
 * Default block behavior used when no other behavior is specified.
 */
public final class DefaultBlockBehavior extends BaseBlockBehavior implements BlockBehavior {

    public static final DefaultBlockBehavior INSTANCE = new DefaultBlockBehavior();

    private DefaultBlockBehavior() {
    }

    @Override
    public String toString() {
        return "DefaultBlockBehavior";
    }

    @Override
    public Collection<ItemStack> getDrops(Location block, ItemStack tool) {
        BlockType type = block.getBlockType();
        Optional<ItemBlock> itemType = type.getHeldItem();
        if (itemType.isPresent()) {
            return (Collection) Arrays.asList(GlowItemStackBuilder.build(itemType.get(), 1, block.getManipulators()));
        }
        return ImmutableList.of();
    }

    @Override
    public GlowTileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return null;
    }

    @Override
    public boolean canPlaceAt(Location block, Direction against) {
        return true;
    }

    @Override
    public void afterPlace(GlowPlayer player, Location block, ItemStack holding) {

    }

    @Override
    public void blockDestroy(GlowPlayer player, Location block, Direction face) {

    }

    @Override
    public boolean canAbsorb(Location block, Direction face, ItemStack holding) {
        return false;
    }

    @Override
    public boolean canOverride(Location block, Direction face, ItemStack holding) {
        return block.getBlockType().isLiquid();
    }

    @Override
    public void onNearBlockChanged(Location block, Direction face, Location changedBlock, BlockType oldType, byte oldData, BlockType newType, byte newData) {

    }

    @Override
    public void onBlockChanged(Location block, BlockType oldType, byte oldData, BlockType newType, byte data) {

    }

    @Override
    public void updatePhysics(Location me) {

    }

    @Override
    public void performPlace(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {

    }
}
