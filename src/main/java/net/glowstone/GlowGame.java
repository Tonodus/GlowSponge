package net.glowstone;

import net.glowstone.plugin.GlowPluginManager;
import net.glowstone.service.GlowServiceManager;
import net.glowstone.service.command.GlowCommandService;
import net.glowstone.service.event.GlowEventManager;
import net.glowstone.service.profile.GlowGameProfileResolver;
import net.glowstone.service.scheduler.GlowScheduler;
import net.glowstone.service.scheduler.WorldScheduler;
import net.glowstone.util.ServerConfig;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.profile.GameProfileResolver;
import org.spongepowered.api.service.scheduler.SchedulerService;

import java.net.BindException;

public class GlowGame implements Game {
    private static GlowGame INSTANCE;

    public static final GlowGame instance() {
        return INSTANCE;
    }

    private final GlowServer server;
    private final GlowPluginManager pluginManager;
    private final GlowEventManager eventManager;
    private final GlowGameRegistry gameRegistry;
    private final GlowServiceManager serviceManager;
    private final GlowScheduler scheduler;
    private final GlowCommandService commandService;
    private final GlowTeleportHelper teleportHelper;
    private final Platform platform;

    public GlowGame() {
        INSTANCE = this;

        this.server = new GlowServer(this, new ServerConfig(null, null, null));
        this.pluginManager = new GlowPluginManager(this);
        this.eventManager = new GlowEventManager(this);
        this.gameRegistry = new GlowGameRegistry(this);
        this.serviceManager = new GlowServiceManager();
        this.scheduler = new GlowScheduler(server, new WorldScheduler());
        this.commandService = new GlowCommandService();
        this.teleportHelper = new GlowTeleportHelper();
        this.platform = new GlowPlatform();

        try {
            serviceManager.setProvider(null, SchedulerService.class, scheduler);
            serviceManager.setProvider(null, CommandService.class, commandService);
            serviceManager.setProvider(null, GameProfileResolver.class, new GlowGameProfileResolver());
        } catch (ProviderExistsException e) {
            e.printStackTrace();
        }
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
        return platform;
    }

    @Override
    public GlowServer getServer() {
        return server;
    }

    @Override
    public GlowPluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public GlowEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public GlowGameRegistry getRegistry() {
        return gameRegistry;
    }

    @Override
    public GlowServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public GlowScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public GlowCommandService getCommandDispatcher() {
        return commandService;
    }

    @Override
    public GlowTeleportHelper getTeleportHelper() {
        return teleportHelper;
    }
}
