package net.glowstone.util;

import org.spongepowered.api.data.DataQuery;

import static org.spongepowered.api.data.DataQuery.of;

public class DataQueries {
    public static final DataQuery X = of("x");
    public static final DataQuery Y = of("y");
    public static final DataQuery Z = of("z");
    public static final DataQuery WORLD = of("world");

    private DataQueries() {
    }
}
