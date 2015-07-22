package net.glowstone.block.behavior;

import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.util.Locations;
import net.glowstone.world.GlowWorld;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

public class Falling extends BaseBlockBehavior {

    @Override
    public void afterPlace(GlowPlayer player, Location block, ItemStack holding) {
        updatePhysics(block);
    }

    @Override
    public void onNearBlockChanged(Location me, Direction face, Location other, BlockType oldType, byte oldData, BlockType newType, byte newData) {
        if (face == Direction.DOWN) {
            updatePhysics(me);
        }
    }

    @Override
    public void updatePhysics(Location me) {
        Location below = me.getRelative(Direction.DOWN);
        if (!supportingBlock(below.getBlockType())) {
            transformToFallingEntity(me);
        }
    }

    protected void transformToFallingEntity(final Location me) {
        final GlowWorld world = Locations.world(me);

        final BlockSnapshot snapshot = me.getBlockSnapshot();

        me.removeBlock();

        // todo: replace with me.getWorld().spawnFallingBlock(me.getLocation(), drop, me.getData());
        // on a delay to prevent the block not being visible because its new location was just dug
        world.getServer().getGame().getScheduler().getTaskBuilder().execute(new Runnable() {
            @Override
            public void run() {
                int x = me.getBlockX(), y = me.getBlockY(), z = me.getBlockZ();
                for (; y > 0; --y) {
                    BlockType check = world.getBlockType(x, y - 1, z);
                    if (supportingBlock(check)) {
                        world.setBlock(x, y, z, snapshot.getState());
                        break;
                    }
                }
                //me.applyPhysics(oldType, 0, data, (byte) 0);
            }
        }).submitServer();
    }

    private boolean supportingBlock(BlockType material) {
        return material != BlockTypes.AIR &&
                material != BlockTypes.FIRE &&
                material != BlockTypes.WATER &&
                material != BlockTypes.FLOWING_WATER &&
                material != BlockTypes.LAVA &&
                material != BlockTypes.FLOWING_LAVA;
    }
}
