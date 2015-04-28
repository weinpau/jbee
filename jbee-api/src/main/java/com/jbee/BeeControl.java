package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyToCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.RotationCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {

    BeeControl onFailed(Consumer<Command> onFailed);

    BeeControl onInterrupt(Consumer<Command> onInterrupt);

    BeeControl onAction(Consumer<Command> onAction, Duration period);

    BeeControl onPositionChange(BiConsumer<Command, Position> positionChange, Distance deltaDistance);
  
    CommandResult execute(Command command);
    
    CommandResult interrupt();

    default CommandResult takeOff(Distance height) {
        return execute(new TakeOffCommand(height));
    }

    default CommandResult land() {
        return execute(new LandCommand());
    }

    default CommandResult hover(Duration duration) {
        return execute(new HoverCommand(duration));
    }

    default CommandResult rotate(YAW yaw) {
       return execute(new RotationCommand(yaw)); 
    }

    default CommandResult flyTo(Position position, Velocity velocity, YAW yaw) {
        return execute(new FlyToCommand(position,velocity, yaw));
    }

    default CommandResult flyTo(Position position, Velocity velocity) {
        return execute(new FlyToCommand(position, velocity, null));
    }

    default CommandResult fly(Direction direction, Distance distance, Velocity velocity) {
        return execute(new FlyCommand(direction, distance, velocity));
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
