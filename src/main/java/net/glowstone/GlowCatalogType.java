package net.glowstone;

import org.spongepowered.api.CatalogType;

/**
 * Helper class for {@link CatalogType} implementations
 */
public class GlowCatalogType implements CatalogType {
    private final String id;
    private final String name;
    private final int nid;

    public GlowCatalogType(String id, String name) {
        this(id, name, -1);
    }

    public GlowCatalogType(String id, String name, int numericalId) {
        this.id = id;
        this.name = name;
        this.nid = numericalId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the numerical id of this {@link CatalogType}.
     * If this CatalogType hasn't a numeric id, a {@link RuntimeException} is thrown.
     * @return the numerical id
     */
    public int getNumericId() {
        if (nid == -1) {
            throw new RuntimeException("This CatalogType(" + name + ", " + id + ") doesn't have a numerical id!");
        }

        return nid;
    }
}
