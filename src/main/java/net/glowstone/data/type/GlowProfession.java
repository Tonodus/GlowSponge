package net.glowstone.data.type;

import net.glowstone.GlowCatalogType;
import org.spongepowered.api.data.type.Profession;
import org.spongepowered.api.text.translation.Translation;

public class GlowProfession extends GlowCatalogType implements Profession {

    public GlowProfession(String id, String name, int numericalId) {
        super(id, name, numericalId);
    }

    @Override
    public Translation getTranslation() {
        return null;
    }
}
