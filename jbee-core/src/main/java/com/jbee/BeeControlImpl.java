package com.jbee;

import com.jbee.commands.*;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RunnableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class BeeControlImpl implements BeeControl {

    ExecutorService commandExecutor;
    TargetDevice device;
    DefaultBeeMonitor monitor;

    List<Consumer<Command>> failedListeners = new ArrayList<>();
    List<Consumer<Command>> interruptListeners = new ArrayList<>();
    List<CommandHandler> commandHandlers = new ArrayList<>();

    WeakReference<Command> currentCommand;

    BeeControlImpl(ExecutorService commandExecutor, TargetDevice device, DefaultBeeMonitor monitor) {
        this.commandExecutor = commandExecutor;
        this.device = device;
        this.monitor = monitor;
    }

    @Override
    public BeeControl onFailed(Consumer<Command> onFailed) {
        BeeControlImpl control = new BeeControlImpl(commandExecutor, device, monitor);
        control.failedListeners.addAll(failedListeners);
        control.failedListeners.add(onFailed);
        return control;
    }

    @Override
    public BeeControl onInterrupt(Consumer<Command> onInterrupt) {
        BeeControlImpl control = new BeeControlImpl(commandExecutor, device, monitor);
        control.interruptListeners.addAll(interruptListeners);
        control.interruptListeners.add(onInterrupt);
        return control;
    }

    @Override
    public BeeControl onAction(Consumer<Command> onAction, Duration period) {
        BeeControlImpl control = new BeeControlImpl(commandExecutor, device, monitor);
        control.commandHandlers.addAll(commandHandlers);
        control.commandHandlers.add(new ActionHandler(onAction, period));
        return control;
    }

    @Override
    public BeeControl onPositionChange(BiConsumer<Command, Position> onPositionChange, Distance deltaDistance) {
        BeeControlImpl control = new BeeControlImpl(commandExecutor, device, monitor);
        control.commandHandlers.addAll(commandHandlers);
        control.commandHandlers.add(new PositionChangeHandler(monitor, onPositionChange, deltaDistance));
        return control;
    }

    @Override
    public CommandResult execute(Command command) {
        if (currentCommand != null) {
            clearCommand();
        }
        currentCommand = new WeakReference<>(command);
        command.init(monitor.newCommandNumber());
        startHandlers(command);
        CommandResult result = runByExecutor(command);
        if (CommandResult.FAILED.equals(result)) {
            failedListeners.forEach(listener -> listener.accept(currentCommand.get()));
        }
        stopHandlers();
        return result;
    }

    @Override
    public CommandResult interrupt() {
        if (currentCommand == null) {
            return CommandResult.NOT_EXECUTED;
        }
        CommandResult result = execute(new InterruptCommand());
        stopHandlers();
        if (CommandResult.FAILED.equals(result)) {
            failedListeners.forEach(listener -> listener.accept(currentCommand.get()));
        }
        clearCommand();
        return result;
    }

    private CommandResult runByExecutor(Command command) {

        try {
            RunnableFuture<CommandResult> future = device.execute(command);
            commandExecutor.execute(future);
            return future.get();
        } catch (InterruptedException | ExecutionException ex) {
            return CommandResult.FAILED;
        }

    }

    private void startHandlers(Command command) {
        commandHandlers.forEach(h -> h.start(command));
    }

    private void stopHandlers() {
        commandHandlers.forEach(handler -> handler.stop());
    }

    private void clearCommand() {
        interruptListeners.forEach(listener -> listener.accept(currentCommand.get()));
        currentCommand = null;
    }

}
