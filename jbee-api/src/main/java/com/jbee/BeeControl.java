package com.jbee;

import com.jbee.units.Distance;
import com.jbee.units.Velocity;
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

    BeeAction flyTo(Position position, Velocity velocity);

    BeeAction foreward(Distance distance, Velocity velocity);

    BeeAction right(Distance distance, Velocity velocity);

    BeeAction left(Distance distance, Velocity velocity);

    BeeAction backward(Distance distance, Velocity velocity);

}
