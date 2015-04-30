package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
class CommandExecutor {

    final TargetDevice device;
    final ExecutorService executor = Executors.newSingleThreadExecutor();

    volatile int currentCommandNumber;
    Queue<RunnableFuture<CommandResult>> commandQueue = new ConcurrentLinkedQueue<>();

    public CommandExecutor(TargetDevice device) {
        this.device = device;
    }

    @SuppressWarnings("UseSpecificCatch")
    public CommandResult execute(Command command) {
        commandQueue.forEach(r -> {
            r.cancel(true);

        });
        RunnableFuture<CommandResult> runnable = device.execute(command);

        commandQueue.offer(runnable);
        executor.execute(runnable);
        try {
            return runnable.get();
        } catch (Exception e) {
            return CommandResult.CANCELLED;
        } finally {
            commandQueue.remove(runnable);
        }
    }

    public int newCommandNumber() {
        return ++currentCommandNumber;
    }

    public int getCurrentCommandNumber() {
        return currentCommandNumber;
    }

    public void shutdown() {
        executor.shutdown();
    }

}
