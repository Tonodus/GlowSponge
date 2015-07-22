package net.glowstone.block.behavior;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import io.netty.util.Signal;
import net.glowstone.GlowServer;
import net.glowstone.block.BlockBehavior;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.world.GlowChunk;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.Collection;

/**
 * BlockBehavior for child behaviors to extend.
 */
public class BaseBlockBehavior implements BlockBehavior {

    static final Signal NEXT = Signal.valueOf("BaseBlockBehavior.NEXT");

    protected BaseBlockBehavior() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public Collection<? extends ItemStack> getDrops(Location block, ItemStack tool) {
        throw NEXT;
    }

    @Override
    public GlowTileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        throw NEXT;
    }

    @Override
    public boolean canPlaceAt(Location block, Direction against) {
        throw NEXT;
    }

    @Override
    public void placeBlock(GlowPlayer player, Location location, GlowBlockState state, Direction face, ItemStack holding, Vector3d clickedLoc) {
        throw NEXT;
    }

    @Override
    public void afterPlace(GlowPlayer player, Location block, ItemStack holding) {
        throw NEXT;
    }

    @Override
    public boolean blockInteract(GlowPlayer player, Location block, Direction face, Vector2d clickedLoc) {
        throw NEXT;
    }

    @Override
    public void blockDestroy(GlowPlayer player, Location block, Direction face) {
        throw NEXT;
    }

    @Override
    public boolean canAbsorb(Location block, Direction face, ItemStack holding) {
        throw NEXT;
    }

    @Override
    public boolean canOverride(Location block, Direction face, ItemStack holding) {
        throw NEXT;
    }

    @Override
    public void onNearBlockChanged(Location block, Direction face, Location changedBlock, BlockType oldType, byte oldData, BlockType newType, byte newData) {
        throw NEXT;
    }

    @Override
    public void onBlockChanged(Location block, BlockType oldType, byte oldData, BlockType newType, byte data) {
        throw NEXT;
    }

    @Override
    public void updatePhysics(Location me) {
        throw NEXT;
    }

    @Override
    public void performPlace(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        throw NEXT;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Helper methods

    /**
     * Display the warning for finding the wrong MaterialData subclass.
     * @param clazz The expected subclass of MaterialData.
     * @param data The actual MaterialData found.
     */
    protected void warnMaterialData(Class<?> clazz, BlockSnapshot data) {
        GlowServer.logger.warning("Wrong MaterialData for " + getClass().getSimpleName() + ": expected " + clazz.getSimpleName() + ", got " + data);
    }

    /**
     * Gets the Direction opposite of the direction the location is facing.
     * Usually used to set the way container blocks face when being placed.
     * @param location Location to get opposite of
     * @param inverted If up/down should be used
     * @return Opposite Direction or EAST if yaw is invalid
     */
    protected static Direction getOppositeDirection(double yaw, double pitch, boolean inverted) {
        double rot = yaw % 360;
        if (inverted) {
            // todo: Check the 67.5 pitch in source. This is based off of WorldEdit's number for this.
            if (pitch < -67.5D) {
                return Direction.DOWN;
            } else if (pitch > 67.5D) {
                return Direction.UP;
            }
        }
        if (rot < 0) {
            rot += 360.0;
        }
        if (0 <= rot && rot < 45) {
            return Direction.NORTH;
        } else if (45 <= rot && rot < 135) {
            return Direction.EAST;
        } else if (135 <= rot && rot < 225) {
            return Direction.SOUTH;
        } else if (225 <= rot && rot < 315) {
            return Direction.WEST;
        } else if (315 <= rot && rot < 360.0) {
            return Direction.NORTH;
        } else {
            return Direction.EAST;
        }
    }
}
