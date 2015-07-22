package net.glowstone.data.type;

import net.glowstone.GlowCatalogType;
import org.spongepowered.api.data.type.DirtType;

public class GlowDirtType extends GlowCatalogType implements DirtType {
    public GlowDirtType(String id, String name, int numericId) {
        super(id, name, numericId);
    }
}
