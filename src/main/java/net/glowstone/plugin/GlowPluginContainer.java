package net.glowstone.plugin;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

public class GlowPluginContainer implements PluginContainer {
    private final String id;
    private final String name;
    private final String version;
    private final Object instance;

    public GlowPluginContainer(Plugin plugin, Object instance) {
        this.id = plugin.id();
        this.name = plugin.name();
        this.version = plugin.version();
        this.instance = instance;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
}
