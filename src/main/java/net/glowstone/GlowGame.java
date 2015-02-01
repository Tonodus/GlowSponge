package net.glowstone;

import com.google.common.base.Optional;
import net.glowstone.plugin.GlowPluginManager;
import net.glowstone.service.GlowServiceManager;
import net.glowstone.service.command.GlowCommandService;
import net.glowstone.service.event.GlowEventManager;
import net.glowstone.service.scheduler.GlowAsynchronousScheduler;
import net.glowstone.service.scheduler.GlowSynchronousScheduler;
import net.glowstone.util.ServerConfig;
import org.spongepowered.api.*;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.event.EventManager;
import org.spongepowered.api.service.scheduler.AsynchronousScheduler;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;

import java.net.BindException;

public class GlowGame implements Game {
    private final String apiVersion = "1.1-SNAPSHOT", implVersion = "0.1-SNAPSHOT";
    private final MinecraftVersion mcVersion = new GlowMinecraftVersion(1, 8);

    private final GlowServer server;
    private final GlowPluginManager pluginManager;
    private final GlowEventManager eventManager;
    private final GlowGameRegistry gameRegistry;
    private final GlowServiceManager serviceManager;
    private final GlowSynchronousScheduler syncScheduler;
    private final GlowAsynchronousScheduler asyncScheduler;
    private final GlowCommandService commandService;

    public GlowGame() {
        this.server = new GlowServer(this, new ServerConfig(null, null, null));
        this.pluginManager = new GlowPluginManager(this);
        this.eventManager = new GlowEventManager(this);
        this.gameRegistry = new GlowGameRegistry(this);
        this.serviceManager = new GlowServiceManager(this);
        this.syncScheduler = new GlowSynchronousScheduler();
        this.asyncScheduler = new GlowAsynchronousScheduler();
        this.commandService = new GlowCommandService();
    }

    public void startGame() {
        this.server.start();
        try {
            this.server.bindAll();
        } catch (BindException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Platform getPlatform() {
        return Platform.SERVER;
    }

    @Override
    public Optional<Server> getServer() {
        return (Optional) Optional.of(server);
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public GameRegistry getRegistry() {
        return gameRegistry;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public SynchronousScheduler getSyncScheduler() {
        return syncScheduler;
    }

    @Override
    public AsynchronousScheduler getAsyncScheduler() {
        return asyncScheduler;
    }

    @Override
    public CommandService getCommandDispatcher() {
        return commandService;
    }

    @Override
    public String getAPIVersion() {
        return apiVersion;
    }

    @Override
    public String getImplementationVersion() {
        return implVersion;
    }

    @Override
    public MinecraftVersion getMinecraftVersion() {
        return mcVersion;
    }
}
