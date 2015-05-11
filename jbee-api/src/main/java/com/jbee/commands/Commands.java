package com.jbee.commands;

import com.jbee.Direction;
import com.jbee.RotationDirection;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
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

    public static FlyCommandBuilder.RotationalSpeedStep rotate(Angle angle, RotationDirection rotationDirection) {
        return new FlyCommandBuilder().rotate(angle, rotationDirection);
    }

    public static FlyCommandBuilder.RotationalSpeedStep rotateTo(Angle angle, RotationDirection rotationDirection) {
        return new FlyCommandBuilder().rotateTo(angle, rotationDirection);
    }

    public static FlyCommandBuilder.SpeedStep flyTo(Position position) {
        return new FlyCommandBuilder().flyTo(position);
    }

    public static FlyCommandBuilder.RelativePositionStep fly(Direction direction, Distance distance) {
        return new FlyCommandBuilder().fly(direction, distance);
    }

    public static FlyCommandBuilder.RelativePositionStep forward(Distance distance) {
        return new FlyCommandBuilder().forward(distance);
    }

    public static FlyCommandBuilder.RelativePositionStep right(Distance distance) {
        return new FlyCommandBuilder().right(distance);
    }

    public static FlyCommandBuilder.RelativePositionStep left(Distance distance) {
        return new FlyCommandBuilder().left(distance);
    }

    public static FlyCommandBuilder.RelativePositionStep backward(Distance distance) {
        return new FlyCommandBuilder().backward(distance);
    }

    public static FlyCommandBuilder.RelativePositionStep up(Distance distance) {
        return new FlyCommandBuilder().up(distance);
    }

    public static FlyCommandBuilder.RelativePositionStep down(Distance distance) {
        return new FlyCommandBuilder().down(distance);
    }

}
