package net.glowstone.plugin;

import com.google.inject.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.event.EventManager;

import java.util.List;

public class PluginRegistrar {
    private final Game game;

    public PluginRegistrar(Game game) {
        this.game = game;
    }

    public void register(List<Class<?>> plugins) {
        for (Class<?> plugin : plugins) {
            Injector injector = Guice.createInjector(Stage.PRODUCTION, new PluginModule(plugin.getAnnotation(Plugin.class).id()));
            Object instance = injector.getInstance(plugin);

            //Registering in PluginManager
            ((GlowPluginManager) game.getPluginManager()).register(instance);
            //Let it subscribe for events
            game.getEventManager().register(instance, instance);
        }
    }

    private class PluginModule implements Module {
        private final String name;

        private PluginModule(String name) {
            this.name = name;
        }

        @Override
        public void configure(Binder binder) {
            binder.bind(Logger.class).toInstance(LoggerFactory.getLogger("Plugin:" + name));
            binder.bind(Game.class).toInstance(game);
            binder.bind(PluginManager.class).toInstance(game.getPluginManager());
            binder.bind(GameRegistry.class).toInstance(game.getRegistry());
            binder.bind(EventManager.class).toInstance(game.getEventManager());
        }
    }
}
