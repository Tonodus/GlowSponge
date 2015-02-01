package net.glowstone.plugin;

import com.google.common.base.Optional;
import net.glowstone.GlowGame;
import org.slf4j.Logger;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;

import java.util.Collection;

public class GlowPluginManager implements PluginManager {
    private final GlowGame game;

    public GlowPluginManager(GlowGame game) {
        this.game = game;
    }

    @Override
    public Optional<PluginContainer> fromInstance(Object instance) {
        return null;
    }

    @Override
    public Optional<PluginContainer> getPlugin(String id) {
        return null;
    }

    @Override
    public Logger getLogger(PluginContainer plugin) {
        return null;
    }

    @Override
    public Collection<PluginContainer> getPlugins() {
        return null;
    }

    @Override
    public boolean isLoaded(String id) {
        return false;
    }
}
