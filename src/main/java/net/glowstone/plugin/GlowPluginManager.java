package net.glowstone.plugin;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.glowstone.GlowGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GlowPluginManager implements PluginManager {
    private final GlowGame game;
    private final Map<String, PluginContainer> id2plugin;
    private final Map<Object, PluginContainer> obj2plugin;

    public GlowPluginManager(GlowGame game) {
        this.game = game;
        id2plugin = new HashMap<>();
        obj2plugin = new HashMap<>();
    }

    public void register(Object plugin) {
        Plugin info = plugin.getClass().getAnnotation(Plugin.class);
        GlowPluginContainer container = new GlowPluginContainer(info, plugin);
        id2plugin.put(container.getId(), container);
        obj2plugin.put(plugin, container);
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        return Optional.fromNullable(obj2plugin.get(instance));
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return Optional.fromNullable(id2plugin.get(id));
    }

    @Override
    public Logger getLogger(PluginContainer plugin) {
        return LoggerFactory.getLogger("Plugin:" + plugin.getInstance().getClass().getName());
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return ImmutableList.copyOf(id2plugin.values());
    }

    @Override
    public boolean isLoaded(String id) {
        return id2plugin.containsKey(id);
    }
}
