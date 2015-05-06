package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Speed;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class Commands {

    private Commands() {
    }

    public static Command cancel() {
        return new CancelCommand();
    }

    public static Command takeOff() {
        return new TakeOffCommand();
    }

    public static Command land() {
        return new LandCommand();
    }

    public static Command hover(Duration duration) {
        return new HoverCommand(duration);
    }

    public static Command rotate(Angle angle, RotationDirection rotationDirection) {
        return new RotationCommand(angle, rotationDirection, false);
    }

    public static Command rotateTo(Angle angle, RotationDirection rotationDirection) {
        return new RotationCommand(angle, rotationDirection, true);
    }

    public static Command flyTo(Position position, Speed velocity) {
        return new FlyToCommand(position, velocity);
    }

    public static Command fly(Direction direction, Distance distance, Speed velocity) {
        return new FlyCommand(direction, distance, velocity);
    }

    public static Command forward(Distance distance, Speed velocity) {
        return fly(Direction.FORWARD, distance, velocity);
    }

    public static Command right(Distance distance, Speed velocity) {
        return fly(Direction.RIGHT, distance, velocity);
    }

    public static Command left(Distance distance, Speed velocity) {
        return fly(Direction.LEFT, distance, velocity);
    }

    public static Command backward(Distance distance, Speed velocity) {
        return fly(Direction.BACKWARD, distance, velocity);
    }

    public static Command up(Distance distance, Speed velocity) {
        return fly(Direction.UP, distance, velocity);
    }

    public static Command down(Distance distance, Speed velocity) {
        return fly(Direction.DOWN, distance, velocity);
    }
}
