package net.glowstone.data.type;

import net.glowstone.GlowCatalogType;
import org.spongepowered.api.data.type.Fish;

public class GlowFishType extends GlowCatalogType implements Fish {
    public GlowFishType(String id, String name, int numericId) {
        super(id, name, numericId);
    }
}
