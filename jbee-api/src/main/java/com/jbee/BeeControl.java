package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.Commands;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Speed;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {

    BeeControl speed(Speed speed);

    Speed getSpeed();

    BeeControl rotationalSpeed(RotationalSpeed rotationalSpeed);

    RotationalSpeed getRotationalSpeed();

    BeeControl onFailed(Consumer<Command> onFailed);

    BeeControl onCompleted(Consumer<Command> onCompleted);

    BeeControl onExecute(Consumer<Command> onExecute);

    BeeControl onCanceled(Consumer<Command> onCanceled);

    BeeControl onAction(Consumer<Command> onAction, Duration delay, Duration period);

    default BeeControl onAction(Consumer<Command> onAction, Duration period) {
        return onAction(onAction, Duration.ZERO, period);
    }

    BeeControl onPositionChanged(BiConsumer<Command, Position> positionChanged, Distance deltaDistance);

    CommandResult execute(Command command);

    default CommandResult cancel() {
        return execute(Commands.cancel());
    }

    default CommandResult takeOff() {
        return execute(Commands.takeOff());
    }

    default CommandResult land() {
        return execute(Commands.land());
    }

    default CommandResult hover(Duration duration) {
        return execute(Commands.hover(duration));
    }

    default CommandResult rotate(Angle angle, RotationDirection rotationDirection) {
        return execute(Commands.rotate(angle, rotationDirection).with(getRotationalSpeed()).build());
    }

    default CommandResult rotateTo(Angle angle, RotationDirection rotationDirection) {
           return execute(Commands.rotateTo(angle, rotationDirection).with(getRotationalSpeed()).build());
    }

    default CommandResult flyTo(Position position) {
        return execute(Commands.flyTo(position).with(getSpeed()).build());
    }

    default CommandResult fly(Direction direction, Distance distance) {
        return execute(Commands.fly(direction, distance).with(getSpeed()).build());
    }

    default CommandResult forward(Distance distance) {
        return fly(Direction.FORWARD, distance);
    }

    default CommandResult right(Distance distance) {
        return fly(Direction.RIGHT, distance);
    }

    default CommandResult left(Distance distance) {
        return fly(Direction.LEFT, distance);
    }

    default CommandResult backward(Distance distance) {
        return fly(Direction.BACKWARD, distance);
    }

    default CommandResult up(Distance distance) {
        return fly(Direction.UP, distance);
    }

    default CommandResult down(Distance distance) {
        return fly(Direction.DOWN, distance);
    }

}
