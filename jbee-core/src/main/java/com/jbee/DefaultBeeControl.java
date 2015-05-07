package com.jbee;

import com.jbee.commands.*;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class DefaultBeeControl implements BeeControl {

    final DefaultBeeControl parent;
    final CommandExecutor commandExecutor;
    final DefaultBeeMonitor monitor;
    Speed defaultSpeed;
    RotationalSpeed defaultRotationalSpeed;

    volatile boolean closed = false;

    final List<DefaultBeeControl> childs = Collections.synchronizedList(new ArrayList<>());
    Consumer<Command> failedListener, canceledListener, executeListener, completedListener;
    CommandHandlerFactory commandHandlerFactory;

    DefaultBeeControl(CommandExecutor commandExecutor, DefaultBeeMonitor monitor,
            Speed defaultSpeed, RotationalSpeed defaultRotationalSpeed, DefaultBeeControl parent) {
        this.commandExecutor = commandExecutor;
        this.monitor = monitor;
        this.defaultSpeed = defaultSpeed;
        this.defaultRotationalSpeed = defaultRotationalSpeed;
        this.parent = parent;
    }

    DefaultBeeControl(CommandExecutor commandExecutor, DefaultBeeMonitor monitor, Speed defaultSpeed, RotationalSpeed defaultRotationalSpeed) {
        this(commandExecutor, monitor, defaultSpeed, defaultRotationalSpeed, null);
    }

    @Override
    public BeeControl defaultSpeed(Speed speed) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.defaultSpeed = speed;
        return control;

    }

    @Override
    public Speed getDefaultSpeed() {
        return defaultSpeed;
    }

    @Override
    public BeeControl defaultRotationalSpeed(RotationalSpeed rotationalSpeed) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.defaultRotationalSpeed = rotationalSpeed;
        return control;
    }

    @Override
    public RotationalSpeed getDefaultRotationalSpeed() {
        return defaultRotationalSpeed;
    }

    @Override
    public BeeControl onFailed(Consumer<Command> onFailed) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.failedListener = onFailed;
        return control;
    }

    @Override
    public BeeControl onExecute(Consumer<Command> onExecute) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.executeListener = onExecute;
        return control;
    }

    @Override
    public BeeControl onCompleted(Consumer<Command> onCompleted) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.completedListener = onCompleted;
        return control;
    }

    @Override
    public BeeControl onCanceled(Consumer<Command> onCanceled) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.canceledListener = onCanceled;
        return control;
    }

    @Override
    public BeeControl onAction(Consumer<Command> onAction, Duration delay, Duration period) {
        if (delay.isNegative()) {
            throw new IllegalArgumentException("The delay must be greater or equal to zero.");
        }
        if (period.isZero() || period.isNegative()) {
            throw new IllegalArgumentException("The period must be greater than zero.");
        }
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.commandHandlerFactory = c -> new ActionHandler(c, onAction, delay, period);
        return control;
    }

    @Override
    public BeeControl onPositionChanged(BiConsumer<Command, Position> onPositionChanged, Distance deltaDistance) {
        checkControl();
        DefaultBeeControl control = createChildControl();
        control.commandHandlerFactory = c -> new PositionChangedHandler(c, monitor, onPositionChanged, deltaDistance);
        return control;
    }

    @Override
    public CommandResult execute(Command command) {
        checkControl();

        command.init(commandExecutor.newCommandNumber());

        List<CommandHandler> handlers = startHandlers(command);
        postExecute(command);
        CommandResult result = commandExecutor.execute(command);
        if (CommandResult.FAILED.equals(result)) {
            postFailed(command);
        }
        if (CommandResult.CANCELLED.equals(result)) {
            postCanceled(command);
        }
        if (CommandResult.COMPLETED.equals(result)) {
            postCompleted(command);
        }
        handlers.forEach(h -> h.stop());

        return result;
    }

    protected void postExecute(Command command) {
        if (executeListener != null) {
            executeListener.accept(command);
        }
        if (parent != null) {
            parent.postExecute(command);
        }
    }

    protected void postFailed(Command command) {
        if (failedListener != null) {
            failedListener.accept(command);
        }
        if (parent != null) {
            parent.postFailed(command);
        }
    }

    protected void postCanceled(Command command) {
        if (canceledListener != null) {
            canceledListener.accept(command);
        }
        if (parent != null) {
            parent.postCanceled(command);
        }
    }

    protected void postCompleted(Command command) {
        if (completedListener != null) {
            completedListener.accept(command);
        }
        if (parent != null) {
            parent.postCompleted(command);
        }
    }

    public void close() {
        childs.forEach(c -> c.close());
        closed = true;
    }

    private DefaultBeeControl createChildControl() {
        DefaultBeeControl control = new DefaultBeeControl(commandExecutor, monitor, defaultSpeed, defaultRotationalSpeed, this);
        childs.add(control);
        return control;
    }

    private List<CommandHandler> startHandlers(Command command) {
        List<CommandHandler> handlers = new ArrayList<>();
        createHandlers(command, handlers);
        handlers.forEach(h -> h.start());
        return handlers;
    }

    protected void createHandlers(Command command, Collection<CommandHandler> handlers) {

        if (commandHandlerFactory != null) {
            handlers.add(commandHandlerFactory.create(command));
        }
        if (parent != null) {
            parent.createHandlers(command, handlers);
        }
    }

    private void checkControl() {
        if (closed) {
            throw new RuntimeException("Illegal call, the control is already closed.");
        }
    }

    protected DefaultBeeControl root() {
        if (parent == null) {
            return this;
        } else {
            return parent.root();
        }
    }

}
