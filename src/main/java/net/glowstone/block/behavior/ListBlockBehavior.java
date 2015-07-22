package net.glowstone.block.behavior;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import io.netty.util.Signal;
import net.glowstone.block.BlockBehavior;
import net.glowstone.block.entity.GlowTileEntity;
import net.glowstone.entity.player.GlowPlayer;
import net.glowstone.world.GlowChunk;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * BlockBehavior which uses the first child implementing each method.
 */
public final class ListBlockBehavior implements BlockBehavior {

    private static final Signal NEXT = BaseBlockBehavior.NEXT;
    private static final BlockBehavior fallback = DefaultBlockBehavior.INSTANCE;

    private final List<BlockBehavior> children;

    public ListBlockBehavior(List<BlockBehavior> children) {
        this.children = new ArrayList<>(children);
    }

    @Override
    public String toString() {
        return children.toString();
    }

    @Override
    public Collection<? extends ItemStack> getDrops(Location block, ItemStack tool) {
        for (BlockBehavior child : children) {
            try {
                return child.getDrops(block, tool);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.getDrops(block, tool);
    }

    @Override
    public GlowTileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        for (BlockBehavior child : children) {
            try {
                return child.createTileEntity(chunk, cx, cy, cz);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.createTileEntity(chunk, cx, cy, cz);
    }

    @Override
    public boolean canPlaceAt(Location block, Direction against) {
        for (BlockBehavior child : children) {
            try {
                return child.canPlaceAt(block, against);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.canPlaceAt(block, against);
    }

    @Override
    public void afterPlace(GlowPlayer player, Location block, ItemStack holding) {
        for (BlockBehavior child : children) {
            try {
                child.afterPlace(player, block, holding);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.afterPlace(player, block, holding);
    }

    @Override
    public boolean blockInteract(GlowPlayer player, Location block, Direction face, Vector2d clickedLoc) {
        for (BlockBehavior child : children) {
            try {
                return child.blockInteract(player, block, face, clickedLoc);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.blockInteract(player, block, face, clickedLoc);
    }

    @Override
    public void blockDestroy(GlowPlayer player, Location block, Direction face) {
        for (BlockBehavior child : children) {
            try {
                child.blockDestroy(player, block, face);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.blockDestroy(player, block, face);
    }

    @Override
    public boolean canAbsorb(Location block, Direction face, ItemStack holding) {
        for (BlockBehavior child : children) {
            try {
                return child.canAbsorb(block, face, holding);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.canAbsorb(block, face, holding);
    }

    @Override
    public boolean canOverride(Location block, Direction face, ItemStack holding) {
        for (BlockBehavior child : children) {
            try {
                return child.canOverride(block, face, holding);
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        return fallback.canOverride(block, face, holding);
    }

    @Override
    public void onNearBlockChanged(Location block, Direction face, Location changedBlock, BlockType oldType, byte oldData, BlockType newType, byte newData) {
        for (BlockBehavior child : children) {
            try {
                child.onNearBlockChanged(block, face, changedBlock, oldType, oldData, newType, newData);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.onNearBlockChanged(block, face, changedBlock, oldType, oldData, newType, newData);
    }

    @Override
    public void onBlockChanged(Location block, BlockType oldType, byte oldData, BlockType newType, byte data) {
        for (BlockBehavior child : children) {
            try {
                child.onBlockChanged(block, oldType, oldData, newType, data);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.onBlockChanged(block, oldType, oldData, newType, data);
    }

    @Override
    public void updatePhysics(Location me) {
        for (BlockBehavior child : children) {
            try {
                child.updatePhysics(me);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.updatePhysics(me);
    }

    @Override
    public void performPlace(GlowPlayer player, Location target, Direction face, ItemStack holding, Vector3d clickedLoc) {
        for (BlockBehavior child : children) {
            try {
                child.performPlace(player, target, face, holding, clickedLoc);
                return;
            } catch (Signal signal) {
                signal.expect(NEXT);
            }
        }
        fallback.performPlace(player, target, face, holding, clickedLoc);
    }
}
