package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.providers.*;
import com.jbee.units.Velocity;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
class StateFactory {

    PositionProvider positionProvider;
    YAWProvider yawProvider;
    VelocityProvider velocityProvider;
    BeeContext context;

    public StateFactory(BeeContext context) {

        this.context = context;

        positionProvider = context.getProviders(PositionProvider.class).stream().
                findAny().
                orElseThrow(() -> new RuntimeException("A position provider is missing."));

        yawProvider = context.getProviders(YAWProvider.class).stream().
                findAny().
                orElseThrow(() -> new RuntimeException("A YAW provider is missing."));

        velocityProvider = context.getProviders(VelocityProvider.class).stream().
                findAny().
                orElseThrow(() -> new RuntimeException("A velocity provider is missing."));

    }

    public BeeState getCurrentState() {
        Position position = positionProvider.get();
        Angle yaw = yawProvider.get();
        Velocity velocity = velocityProvider.get();
        BatteryState batteryState = context.getTargetDevice().getBatteryState();
        ControlState controlState = context.getTargetDevice().getControlState();
        return new BeeState(System.currentTimeMillis(), position, velocity, yaw, batteryState, controlState);
    }

}
