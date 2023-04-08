package com.github.cao.awa.apricot.thread.pool;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

@Stable
public final class ExecutorEntrust implements Executor {
    private final @NotNull Executor executor;

    public ExecutorEntrust(@NotNull Executor executor) {
        this.executor = executor;
    }

    public void schedule(long delay, long interval, @NotNull TimeUnit unit, @NotNull Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            if (interval > 0) {
                scheduled.scheduleAtFixedRate(
                        command,
                        delay,
                        interval,
                        unit
                );
            } else {
                scheduled.schedule(
                        command,
                        delay,
                        unit
                );
            }
        } else {
            if (interval > 0) {
                throw new IllegalStateException("The executor is not supports to schedule");
            }
            schedule(
                    delay,
                    unit,
                    command
            );
        }
    }


    public void schedule(long delay, @NotNull TimeUnit unit, @NotNull Runnable command) {
        if (this.executor instanceof ScheduledExecutorService scheduled) {
            scheduled.schedule(
                    command,
                    delay,
                    unit
            );
        } else {
            execute(() -> {
                TimeUtil.coma(unit.convert(
                        delay,
                        TimeUnit.MILLISECONDS
                ));
                command.run();
            });
        }
    }

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command
     *         the runnable task
     * @throws RejectedExecutionException
     *         if this task cannot be
     *         accepted for execution
     * @throws NullPointerException
     *         if command is null
     */
    @Override
    public void execute(@NotNull Runnable command) {
        this.executor.execute(command);
    }

    public Executor executor() {
        return this.executor;
    }

    public void shutdown() {
        if (this.executor instanceof ExecutorService service) {
            service.shutdown();
        }
    }
}
