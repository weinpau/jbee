package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
import java.time.Duration;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {


    BeeAction takeOff(Distance height);

    BeeAction land();

    BeeAction hover(Duration duration);

    BeeAction rotate(YAW yaw);

    BeeAction flyTo(Position position, YAW yaw, Velocity velocity);

    BeeAction flyTo(Position position, Velocity velocity);

    BeeAction fly(Direction direction, Distance distance, Velocity velocity);

    default BeeAction foreward(Distance distance, Velocity velocity) {
        return fly(Direction.FOREWARD, distance, velocity);
    }

    default BeeAction right(Distance distance, Velocity velocity) {
        return fly(Direction.RIGHT, distance, velocity);
    }

    default BeeAction left(Distance distance, Velocity velocity) {
        return fly(Direction.LEFT, distance, velocity);
    }

    default BeeAction backward(Distance distance, Velocity velocity) {
        return fly(Direction.BACKWARD, distance, velocity);
    }

    default BeeAction up(Distance distance, Velocity velocity) {
        return fly(Direction.UP, distance, velocity);
    }

    default BeeAction down(Distance distance, Velocity velocity) {
        return fly(Direction.DOWN, distance, velocity);
    }

}
