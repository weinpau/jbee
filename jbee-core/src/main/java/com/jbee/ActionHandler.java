package com.jbee;

import com.jbee.commands.Command;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class ActionHandler implements CommandHandler {

    private final Consumer<Command> actionListener;
    private final Duration period;
    private final Duration delay;
    final TimerTask task;
    final Timer timer;

    ActionHandler(Command command, Consumer<Command> listener, Duration delay, Duration period) {
        this.actionListener = listener;
        this.period = period;
        this.delay = delay;
        timer = new Timer("action-handler-" + command.getCommandNumber(), true);
        task = new TimerTask() {

            @Override
            public void run() {
                actionListener.accept(command);
            }
        };
    }

    @Override
    public void start() {
        timer.scheduleAtFixedRate(task, delay.toMillis(), period.toMillis());
    }

    @Override
    public void stop() {
        task.cancel();
    }

}
