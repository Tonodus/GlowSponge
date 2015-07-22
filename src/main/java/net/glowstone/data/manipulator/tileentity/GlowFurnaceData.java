package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowDataManipulator;
import org.spongepowered.api.data.manipulator.tileentity.FurnaceData;

public class GlowFurnaceData extends GlowDataManipulator<FurnaceData> implements FurnaceData {
    private int burnTime, cookTime;

    public GlowFurnaceData() {
        this(0, 0);
    }

    public GlowFurnaceData(int burnTime, int cookTime) {
        super(FurnaceData.class);
        this.burnTime = burnTime;
        this.cookTime = cookTime;
    }

    @Override
    public int getRemainingBurnTime() {
        return burnTime;
    }

    @Override
    public FurnaceData setRemainingBurnTime(int time) {
        this.burnTime = time;
        return this;
    }

    @Override
    public int getRemainingCookTime() {
        return cookTime;
    }

    @Override
    public FurnaceData setRemainingCookTime(int time) {
        this.cookTime = time;
        return this;
    }

    @Override
    public FurnaceData copy() {
        return new GlowFurnaceData(burnTime, cookTime);
    }

    @Override
    public int compareTo(FurnaceData o) {
        return 0;
    }
}
