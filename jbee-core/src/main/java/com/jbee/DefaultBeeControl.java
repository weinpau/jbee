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
class DefaultBeeControl implements BeeControl {

    boolean closed = false;
    List<DefaultBeeControl> childs = new ArrayList<>();

    final ExecutorService commandExecutor;
    final TargetDevice device;
    final DefaultBeeMonitor monitor;

    List<Consumer<Command>> failedListeners = new ArrayList<>();
    List<Consumer<Command>> interruptListeners = new ArrayList<>();
    List<CommandHandler> commandHandlers = new ArrayList<>();

    WeakReference<Command> currentCommand;

    DefaultBeeControl(ExecutorService commandExecutor, TargetDevice device, DefaultBeeMonitor monitor) {
        this.commandExecutor = commandExecutor;
        this.device = device;
        this.monitor = monitor;
    }

    @Override
    public BeeControl onFailed(Consumer<Command> onFailed) {
        checkControl();
        DefaultBeeControl control = new DefaultBeeControl(commandExecutor, device, monitor);
        control.failedListeners.addAll(failedListeners);
        control.failedListeners.add(onFailed);
        childs.add(control);
        return control;
    }

    @Override
    public BeeControl onInterrupt(Consumer<Command> onInterrupt) {
        checkControl();
        DefaultBeeControl control = new DefaultBeeControl(commandExecutor, device, monitor);
        control.interruptListeners.addAll(interruptListeners);
        control.interruptListeners.add(onInterrupt);
        childs.add(control);
        return control;
    }

    @Override
    public BeeControl onAction(Consumer<Command> onAction, Duration period) {
        checkControl();
        DefaultBeeControl control = new DefaultBeeControl(commandExecutor, device, monitor);
        control.commandHandlers.addAll(commandHandlers);
        control.commandHandlers.add(new ActionHandler(onAction, period));
        childs.add(control);
        return control;
    }

    @Override
    public BeeControl onPositionChange(BiConsumer<Command, Position> onPositionChange, Distance deltaDistance) {
        checkControl();
        DefaultBeeControl control = new DefaultBeeControl(commandExecutor, device, monitor);
        control.commandHandlers.addAll(commandHandlers);
        control.commandHandlers.add(new PositionChangeHandler(monitor, onPositionChange, deltaDistance));
        childs.add(control);
        return control;
    }

    @Override
    public CommandResult execute(Command command) {
        checkControl();
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
        checkControl();
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

    public void close() {
        childs.forEach(c -> c.close());
        currentCommand = null;
        failedListeners.clear();
        commandHandlers.clear();
        interruptListeners.clear();
        closed = true;
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

    private void checkControl() {
        if (closed) {
            throw new RuntimeException("Illegal call, the control is already closed.");
        }
    }

}
