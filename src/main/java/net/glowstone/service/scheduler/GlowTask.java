package net.glowstone.service.scheduler;

import net.glowstone.GlowServer;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a task which is executed periodically.
 * @author Graham Edgecombe
 */
public class GlowTask extends FutureTask<Void> implements Task, Runnable {
    /**
     * The next task ID pending.
     */
    private static final AtomicInteger nextTaskId = new AtomicInteger(0);

    /**
     * The name of this task.
     */
    private final String name;

    /**
     * The ID of this task.
     */
    private final UUID uuid;

    /**
     * The Plugin that owns this task
     */
    private final PluginContainer owner;

    /**
     * The number of ticks or ms before the call to the Runnable.
     */
    private final long delay;

    /**
     * The number of ticks or ms between each call to the Runnable.
     */
    private final long period;

    /**
     * The current number of ticks or ms since last initialization.
     */
    private long counter;

    /**
     * A flag indicating whether this task is to be run asynchronously
     */
    private final boolean async;

    /**
     * The thread this task has been last executed on, if this task is async.
     */
    private Thread executionThread;

    /**
     * Return the last state returned by {@link #shouldExecute()}
     */
    private volatile TaskExecutionState lastExecutionState = TaskExecutionState.WAIT;
    private Runnable runnable;

    /**
     * Creates a new task with the specified number of ticks between
     * consecutive calls to execute().
     */
    public GlowTask(PluginContainer owner, String name, Runnable task, boolean async, long delay, long period) {
        super(task, null);
        this.uuid = UUID.nameUUIDFromBytes(("glowstone-task-" + nextTaskId.getAndIncrement()).getBytes());
        this.name = name;
        this.owner = owner;
        this.delay = delay;
        this.period = period;
        this.counter = 0;
        this.async = async;
        this.runnable = task;
    }

    @Override
    public String toString() {
        return "GlowTask{" +
                "uid=" + uuid +
                ", plugin=" + owner +
                ", async=" + async +
                '}';
    }

    /**
     * Gets the ID of this task.
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public PluginContainer getOwner() {
        return owner;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public long getInterval() {
        return getInterval();
    }

    @Override
    public boolean cancel() {
        return super.cancel(false);
    }

    @Override
    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public boolean isAsynchronous() {
        return async;
    }

    /**
     * Called every 'pulse' which is around 50ms in Minecraft. This method
     * updates the counters and returns whether execute() should be called
     * @return Execution state for this task
     */
    TaskExecutionState shouldExecute() {
        final TaskExecutionState execState = shouldExecuteUpdate();
        lastExecutionState = execState;
        return execState;
    }

    private TaskExecutionState shouldExecuteUpdate() {
        if (isDone()) // Stop running if cancelled, exception, or not repeating
            return TaskExecutionState.STOP;

        ++counter;
        if (counter >= delay) {
            if (period == -1 || (counter - delay) % period == 0) {
                return TaskExecutionState.RUN;
            }
        }

        return TaskExecutionState.WAIT;
    }

    /**
     * Return the last execution state returned by {@link #shouldExecute()}
     * @return the last state (most likely the state the task is currently in)
     */
    public TaskExecutionState getLastExecutionState() {
        return lastExecutionState;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public void run() {
        executionThread = Thread.currentThread();
        if (period == -1) {
            super.run();
        } else {
            runAndReset();
        }
    }

    @Override
    protected void done() {
        super.done();
        if (isCancelled()) {
            return;
        }

        try {
            get();
        } catch (ExecutionException ex) {
            //TODO: use plugin's logger
            Logger log = GlowServer.logger;
            log.log(Level.SEVERE, "Error while executing " + this, ex.getCause());
        } catch (InterruptedException e) {
            // Task is already done, see the fact that we're in done() method
        }
    }
}
