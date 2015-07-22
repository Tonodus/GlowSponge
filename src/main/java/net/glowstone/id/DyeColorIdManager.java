package net.glowstone.id;

import com.google.common.collect.BiMap;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;

public class DyeColorIdManager extends BasicIdManager<Byte, DyeColor> {
    @Override
    protected void fill(BiMap<Byte, DyeColor> map) {
        map.put((byte) 0, DyeColors.BLACK);
        //...
    }
}
