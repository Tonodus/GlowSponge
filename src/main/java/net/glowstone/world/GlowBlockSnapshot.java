package net.glowstone.world;

import com.flowpowered.math.vector.Vector3i;
import net.glowstone.block.GlowBlockState;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;

public class GlowBlockSnapshot implements BlockSnapshot {
    private BlockState state;
    private Vector3i location;

    public GlowBlockSnapshot(BlockState state, Vector3i location) {
        this.state = state;
        this.location = location;
    }

    @Override
    public BlockState getState() {
        return state;
    }

    @Override
    public void setBlockState(BlockState blockState) {
        this.state = blockState;
    }

    @Override
    public Vector3i getLocation() {
        return location;
    }

    @Override
    public void setLocation(Vector3i location) {
        this.location = location;
    }

    @Override
    public BlockSnapshot copy() {
        return new GlowBlockSnapshot(GlowBlockState.from(state.getType(), state.getManipulators()), location);
    }

    @Override
    public DataContainer toContainer() {
        return new MemoryDataContainer();
    }
}
