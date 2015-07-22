package net.glowstone.world.gen.populators;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populator.Ore;

import java.util.Random;

public class GlowOre implements Ore {
    @Override
    public BlockState getOreBlock() {
        return null;
    }

    @Override
    public void setOreBlock(BlockState block) {

    }

    @Override
    public int getDepositSize() {
        return 0;
    }

    @Override
    public void setDepositSize(int size) {

    }

    @Override
    public int getDepositsPerChunk() {
        return 0;
    }

    @Override
    public void setDepositsPerChunk(int count) {

    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public void setMinHeight(int min) {

    }

    @Override
    public int getMaxHeight() {
        return 0;
    }

    @Override
    public void setMaxHeight(int max) {

    }

    @Override
    public void populate(Chunk chunk, Random random) {

    }
}
