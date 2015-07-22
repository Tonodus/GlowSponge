package net.glowstone.service.scheduler;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.glowstone.GlowServer;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.scheduler.SchedulerService;
import org.spongepowered.api.service.scheduler.Task;

import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * A scheduler for managing server ticks, Bukkit tasks, and other synchronization.
 */
public final class GlowScheduler implements SchedulerService {
    private static class GlowThreadFactory implements ThreadFactory {
        public static final GlowThreadFactory INSTANCE = new GlowThreadFactory();
        private final AtomicInteger threadCounter = new AtomicInteger();

        private GlowThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "Glowstone-scheduler-" + threadCounter.getAndIncrement());
        }
    }

    /**
     * The number of milliseconds between pulses.
     */
    static final int PULSE_EVERY = 50;

    /**
     * The server this scheduler is managing for.
     */
    private final GlowServer server;

    /**
     * The scheduled executor service which backs this worlds.
     */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(GlowThreadFactory.INSTANCE);

    /**
     * Executor to handle execution of async tasks
     */
    private final ExecutorService asyncTaskExecutor = Executors.newCachedThreadPool(GlowThreadFactory.INSTANCE);

    /**
     * A list of active tasks.
     */
    private final ConcurrentMap<UUID, GlowTask> tasks = new ConcurrentHashMap<>();

    /**
     * The primary worlds thread in which pulse() is called.
     */
    private Thread primaryThread;

    /**
     * World tick scheduler
     */
    private final WorldScheduler worlds;

    /**
     * Tasks to be executed during the tick
     */
    private final Deque<Runnable> inTickTasks = new ConcurrentLinkedDeque<>();

    /**
     * Condition to wait on when processing in tick tasks
     */
    private final Object inTickTaskCondition;

    /**
     * Runnable to run at end of tick
     */
    private final Runnable tickEndRun;

    /**
     * Creates a new task scheduler.
     */
    public GlowScheduler(GlowServer server, WorldScheduler worlds) {
        this.server = server;
        this.worlds = worlds;
        inTickTaskCondition = worlds.getAdvanceCondition();
        tickEndRun = new Runnable() {
            @Override
            public void run() {
                GlowScheduler.this.worlds.doTickEnd();
            }
        };
        primaryThread = Thread.currentThread();
    }

    public WorldScheduler getWorldScheduler() {
        return worlds;
    }

    public void start() {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    pulse();
                } catch (Exception ex) {
                    GlowServer.logger.log(Level.SEVERE, "Error while pulsing", ex);
                }
            }
        }, 0, PULSE_EVERY, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the scheduler and all tasks.
     */
    public void stop() {
        cancelAllTasks();
        worlds.stop();
        executor.shutdownNow();
        asyncTaskExecutor.shutdown();

        synchronized (inTickTaskCondition) {
            for (Runnable task : inTickTasks) {
                if (task instanceof Future) {
                    ((Future) task).cancel(false);
                }
            }
            inTickTasks.clear();
        }
    }

    /**
     * Schedules the specified task.
     * @param task The task.
     */
    GlowTask schedule(GlowTask task) {
        tasks.put(task.getUniqueId(), task);
        return task;
    }

    /**
     * Returns true if the current {@link Thread} is the server's primary thread.
     */
    public boolean isPrimaryThread() {
        return Thread.currentThread() == primaryThread;
    }

    public void scheduleInTickExecution(Runnable run) {
        if (isPrimaryThread() || executor.isShutdown()) {
            run.run();
        } else {
            synchronized (inTickTaskCondition) {
                inTickTasks.addFirst(run);
                inTickTaskCondition.notifyAll();
            }
        }
    }

    /**
     * Adds new tasks and updates existing tasks, removing them if necessary.
     * <p/>
     * todo: Add watchdog system to make sure ticks advance
     */
    private void pulse() {
        primaryThread = Thread.currentThread();

        // Process player packets
        server.getSessionRegistry().pulse();

        // Run the relevant tasks.
        for (Iterator<GlowTask> it = tasks.values().iterator(); it.hasNext(); ) {
            GlowTask task = it.next();
            switch (task.shouldExecute()) {
                case RUN:
                    if (!task.isAsynchronous()) {
                        task.run();
                    } else {
                        asyncTaskExecutor.submit(task);
                    }
                    break;
                case STOP:
                    it.remove();
            }
        }
        try {
            int currentTick = worlds.beginTick();
            try {
                asyncTaskExecutor.submit(tickEndRun);
            } catch (RejectedExecutionException ex) {
                worlds.stop();
                return;
            }

            Runnable tickTask;
            synchronized (inTickTaskCondition) {
                while (!worlds.isTickComplete(currentTick)) {
                    while ((tickTask = inTickTasks.poll()) != null) {
                        tickTask.run();
                    }

                    inTickTaskCondition.wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }


    public <T> Future<T> callSyncMethod(PluginContainer plugin, Callable<T> task) {
        FutureTask<T> future = new FutureTask<>(task);
        schedule(new GlowTask(plugin, "", future, false, 0, -1));
        return future;
    }

    public <T> T syncIfNeeded(Callable<T> task) throws Exception {
        if (isPrimaryThread()) {
            return task.call();
        } else {
            return callSyncMethod(null, task).get();
        }
    }
/*
    public void cancelTask(GlowTask task) {
        tasks.remove(task.getUniqueId());
    }*/


    public void cancelAllTasks() {
        tasks.clear();
    }

/*
    public boolean isCurrentlyRunning(int taskId) {
        GlowTask task = tasks.get(taskId);
        return task != null && task.getLastExecutionState() == TaskExecutionState.RUN;
    }


    public boolean isQueued(int taskId) {
        return tasks.containsKey(taskId);
    }*/

    /**
     * Returns active async tasks
     * @return active async tasks
     */
    /*    public List<BukkitWorker> getActiveWorkers() {
        return ImmutableList.<BukkitWorker>copyOf(Collections2.filter(tasks.values(), new Predicate<GlowTask>() {
            @Override
            public boolean apply(@Nullable GlowTask glowTask) {
                return glowTask != null && !glowTask.isSync() && glowTask.getLastExecutionState() == TaskExecutionState.RUN;
            }
        }));
    }*/

    /**
     * Returns tasks that still have at least one run remaining
     * @return the tasks to be run
     */

   /* public List<BukkitTask> getPendingTasks() {
        return new ArrayList<BukkitTask>(tasks.values());
    }*/
    @Override
    public GlowTaskBuilder getTaskBuilder() {
        return new GlowTaskBuilder(this, server.getGame());
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        return Optional.fromNullable((Task) tasks.get(id));
    }

    @Override
    public Set<Task> getTasksByName(String pattern) {
        ImmutableSet.Builder<Task> builder = ImmutableSet.builder();
        for (Task task : tasks.values()) {
            if (task.getName().matches(pattern)) {
                builder.add(task);
            }
        }

        return builder.build();
    }

    @Override
    public Set<Task> getScheduledTasks() {
        return (Set) ImmutableSet.copyOf(tasks.values());
    }

    @Override
    public Set<Task> getScheduledTasks(boolean async) {
        ImmutableSet.Builder<Task> builder = ImmutableSet.builder();
        for (Task task : tasks.values()) {
            if (task.isAsynchronous() == async) {
                builder.add(task);
            }
        }

        return builder.build();
    }

    @Override
    public Set<Task> getScheduledTasks(Object plugin) {
        ImmutableSet.Builder<Task> builder = ImmutableSet.builder();
        for (Task task : tasks.values()) {
            if (task.getOwner().getInstance() == plugin) {
                builder.add(task);
            }
        }

        return builder.build();
    }
}
