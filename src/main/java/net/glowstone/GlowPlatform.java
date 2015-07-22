package net.glowstone;

import org.spongepowered.api.MinecraftVersion;

import java.util.Map;

public class GlowPlatform implements org.spongepowered.api.Platform {
    private final String apiVersion = "1.1-SNAPSHOT", implVersion = "0.1-SNAPSHOT";
    private final MinecraftVersion mcVersion = new GlowMinecraftVersion(1, 8);

    @Override
    public Type getType() {
        return Type.SERVER;
    }

    @Override
    public Type getExecutionType() {
        return Type.SERVER;
    }

    @Override
    public String getName() {
        return "Glowstone";
    }

    @Override
    public String getVersion() {
        return implVersion;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public MinecraftVersion getMinecraftVersion() {
        return mcVersion;
    }

    @Override
    public Map<String, Object> asMap() {
        return null;
    }
}
