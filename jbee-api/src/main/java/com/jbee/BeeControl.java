package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.Commands;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import com.jbee.units.Angle;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {
    
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
        return execute(Commands.rotate(angle, rotationDirection));
    }
    
    default CommandResult rotateTo(Angle angle, RotationDirection rotationDirection) {
        return execute(Commands.rotateTo(angle, rotationDirection));
    }
    
    default CommandResult flyTo(Position position, Velocity velocity) {
        return execute(Commands.flyTo(position, velocity));
    }
    
    default CommandResult fly(Direction direction, Distance distance, Velocity velocity) {
        return execute(Commands.fly(direction, distance, velocity));
    }
    
    default CommandResult forward(Distance distance, Velocity velocity) {
        return fly(Direction.FORWARD, distance, velocity);
    }
    
    default CommandResult right(Distance distance, Velocity velocity) {
        return fly(Direction.RIGHT, distance, velocity);
    }
    
    default CommandResult left(Distance distance, Velocity velocity) {
        return fly(Direction.LEFT, distance, velocity);
    }
    
    default CommandResult backward(Distance distance, Velocity velocity) {
        return fly(Direction.BACKWARD, distance, velocity);
    }
    
    default CommandResult up(Distance distance, Velocity velocity) {
        return fly(Direction.UP, distance, velocity);
    }
    
    default CommandResult down(Distance distance, Velocity velocity) {
        return fly(Direction.DOWN, distance, velocity);
    }
    
}
