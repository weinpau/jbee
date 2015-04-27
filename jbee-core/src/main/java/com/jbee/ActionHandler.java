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

    private Timer timer;

    ActionHandler(Consumer<Command> listener, Duration period) {
        this.actionListener = listener;
        this.period = period;
    }

    @Override
    public void start(Command command) {
        stop();
        timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                actionListener.accept(command);
            }
        }, 0, period.toMillis());

    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

}
