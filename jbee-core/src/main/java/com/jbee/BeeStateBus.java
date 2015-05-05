package com.jbee;

import com.jbee.buses.PositionBus;
import com.jbee.buses.YAWBus;
import com.jbee.buses.TranslationalVelocityBus;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Velocity3D;

/**
 *
 * @author weinpau
 */
class BeeStateBus extends Bus<BeeState> {

    Position position = Position.ORIGIN;
    Velocity3D translationalVelocity = Velocity3D.ZERO;
    Angle yaw = Angle.ZERO;

    TargetDevice device;

    BeeStateBus(BeeContext context) {
        super(Priority.HIGH);
        
        device = context.getTargetDevice();

        context.getBus(YAWBus.class).stream().
                sorted().
                findFirst().
                orElseThrow(() -> new RuntimeException("A YAW bus is missing.")).
                subscripe(y -> {
                    yaw = y;
                    publish(createBeeState());
                });

        context.getBus(TranslationalVelocityBus.class).stream().
                sorted().
                findFirst().
                orElseThrow(() -> new RuntimeException("A velocity bus is missing.")).
                subscripe(v -> {
                    translationalVelocity = v;
                    publish(createBeeState());
                });

        context.getBus(PositionBus.class).stream().
                sorted().
                findFirst().
                orElseThrow(() -> new RuntimeException("A position bus is missing.")).
                subscripe(p -> {
                    position = p;
                    publish(createBeeState());
                });
    }

    final BeeState createBeeState() {
        return new BeeState(System.currentTimeMillis(), position, translationalVelocity, yaw,
                device.getBatteryState(),
                device.getControlState());
    }

}
