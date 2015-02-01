package net.glowstone;

import org.spongepowered.api.MinecraftVersion;

public class GlowMinecraftVersion implements MinecraftVersion {
    private final int a, b;

    public GlowMinecraftVersion(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String getName() {
        return "" + a + "." + b;
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public int compareTo(MinecraftVersion o) {
        return 0;
    }
}
