package com.jbee;

import com.jbee.commands.Command;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
class PositionChangeHandler implements CommandHandler {

    private final BiConsumer<Command, Position> onPositionChange;
    private final Distance deltaDistance;
    private final DefaultBeeMonitor monitor;
    private Command command;

    private Position currentPosition;

    PositionChangeHandler(DefaultBeeMonitor monitor, BiConsumer<Command, Position> onPositionChange, Distance deltaDistance) {
        this.monitor = monitor;
        this.onPositionChange = onPositionChange;
        this.deltaDistance = deltaDistance;

        currentPosition = monitor.getLastKnownState().getPosition();
    }

    @Override
    public void start(Command command) {
        this.command = command;
        currentPosition = monitor.getLastKnownState().getPosition();
        monitor.onStateChange(stateListener);

    }

    @Override
    public void stop() {
        monitor.removeStateChangeListener(stateListener);
        currentPosition = null;
    }

    @SuppressWarnings("Convert2Lambda")
    Consumer<BeeState> stateListener = new Consumer<BeeState>() {

        @Override
        public void accept(BeeState state) {
            Position newPos = state.getPosition();
            if (newPos.distance(currentPosition).compareTo(deltaDistance) > 0) {
                currentPosition = newPos;
                onPositionChange.accept(command, newPos);
            }
        }
    };

}
