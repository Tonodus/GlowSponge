package net.glowstone.service.scheduler;

import com.google.common.base.Optional;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.service.scheduler.Task;

import java.util.Collection;
import java.util.UUID;

public class GlowSynchronousScheduler implements SynchronousScheduler {
    @Override
    public Optional<Task> runTask(Object plugin, Runnable task) {
        return null;
    }

    @Override
    public Optional<Task> runTaskAfter(Object plugin, Runnable task, long delay) {
        return null;
    }

    @Override
    public Optional<Task> runRepeatingTask(Object plugin, Runnable task, long interval) {
        return null;
    }

    @Override
    public Optional<Task> runRepeatingTaskAfter(Object plugin, Runnable task, long interval, long delay) {
        return null;
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        return null;
    }

    @Override
    public Optional<UUID> getUuidOfTaskByName(String name) {
        return null;
    }

    @Override
    public Collection<Task> getTasksByName(String pattern) {
        return null;
    }

    @Override
    public Collection<Task> getScheduledTasks() {
        return null;
    }

    @Override
    public Collection<Task> getScheduledTasks(Object plugin) {
        return null;
    }
}
