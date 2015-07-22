package net.glowstone.data.manipulator.tileentity;

import net.glowstone.data.manipulator.GlowIntData;
import org.spongepowered.api.data.manipulator.tileentity.BrewingData;

public class GlowBrewingData extends GlowIntData<BrewingData> implements BrewingData {
    public GlowBrewingData() {
        super(BrewingData.class, 0, 0, 100);
    }

    @Override
    public int getRemainingBrewTime() {
        return getValue();
    }

    @Override
    public BrewingData setRemainingBrewTime(int time) {
        return setValue(time);
    }

    @Override
    public BrewingData copy() {
        return new GlowBrewingData().setRemainingBrewTime(getRemainingBrewTime());
    }
}
