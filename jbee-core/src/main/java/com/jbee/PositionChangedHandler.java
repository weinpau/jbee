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
class PositionChangedHandler implements CommandHandler {

    final BiConsumer<Command, Position> onPositionChange;
    final Distance deltaDistance;
    final DefaultBeeMonitor monitor;
    final Command command;

    volatile Position currentPosition;

    PositionChangedHandler(Command command, DefaultBeeMonitor monitor, BiConsumer<Command, Position> onPositionChange, Distance deltaDistance) {
        this.monitor = monitor;
        this.onPositionChange = onPositionChange;
        this.deltaDistance = deltaDistance;
        this.command = command;

        currentPosition = monitor.getLastKnownState().getPosition();
    }

    @Override
    public void start() {

        currentPosition = monitor.getLastKnownState().getPosition();
        monitor.onStateChange(stateListener);

    }

    @Override
    public void stop() {
        monitor.removeStateChangeListener(stateListener);
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
