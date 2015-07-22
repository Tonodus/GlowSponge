package net.glowstone.service.scheduler;

import net.glowstone.GlowGame;
import org.apache.commons.lang3.Validate;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.service.scheduler.TaskBuilder;

import java.util.concurrent.TimeUnit;

public class GlowTaskBuilder implements TaskBuilder {
    private final GlowScheduler scheduler;
    private final GlowGame game;

    private boolean async = false;
    private long delayMs = -1, delayTicks = 0;
    private long intervalMs = -1, intervalTicks = 0;
    private String name = "";
    private Runnable task = null;

    public GlowTaskBuilder(GlowScheduler scheduler, GlowGame game) {
        this.scheduler = scheduler;
        this.game = game;
    }

    @Override
    public GlowTaskBuilder async() {
        this.async = true;
        return this;
    }

    @Override
    public GlowTaskBuilder execute(Runnable runnable) {
        this.task = runnable;
        return this;
    }

    @Override
    public GlowTaskBuilder delay(long delay, TimeUnit unit) {
        this.delayMs = unit.toMillis(delay);
        this.delayTicks = -1;
        return this;
    }

    @Override
    public GlowTaskBuilder delay(long delay) {
        if (async) {
            this.delayMs = delay;
            this.delayTicks = -1;
        } else {
            this.delayTicks = delay;
            this.delayMs = -1;
        }
        return this;
    }

    @Override
    public GlowTaskBuilder interval(long interval, TimeUnit unit) {
        this.intervalMs = unit.toMillis(interval);
        this.intervalTicks = -1;
        return this;
    }

    @Override
    public GlowTaskBuilder interval(long interval) {
        if (async) {
            this.intervalMs = interval;
            this.intervalTicks = -1;
        } else {
            this.intervalTicks = interval;
            this.intervalMs = -1;
        }
        return this;
    }

    @Override
    public GlowTaskBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Task submit(Object plugin) {
        Validate.notNull(plugin);
        Validate.notNull(task);
        PluginContainer container = game.getPluginManager().fromInstance(plugin).orNull();
        if (container == null) {
            throw new IllegalArgumentException("Argument plugin " + plugin + " must be a valid plugin instance!");
        }

        GlowTask task = new GlowTask(container, this.name, this.task, async, delayMs, intervalMs);
        scheduler.schedule(task);

        return task;
    }

    /**
     * Submits this task as a server task (no owning plugin).
     * @return the generated Task
     */
    public Task submitServer() {
        GlowTask task = new GlowTask(null, this.name, this.task, async, delayMs, intervalMs);
        scheduler.schedule(task);
        return task;
    }
}
