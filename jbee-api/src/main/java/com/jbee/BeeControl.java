package com.jbee;

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

    void onStateChange(Consumer<BeeState> beeStateListener);

    BeeState getLastKnownState();

    BeeAction takeOff(Distance height);

    BeeAction land();

    BeeAction hover(Duration duration);
    
    BeeAction flyTo(Position position, YAW yaw, Velocity velocity);

    BeeAction flyTo(Position position, Velocity velocity);

    BeeAction foreward(Distance distance, Velocity velocity);

    BeeAction right(Distance distance, Velocity velocity);

    BeeAction left(Distance distance, Velocity velocity);

    BeeAction backward(Distance distance, Velocity velocity);

    BeeAction up(Distance distance, Velocity velocity);

    BeeAction down(Distance distance, Velocity velocity);
    
    BeeAction rotate(YAW yaw);

}
