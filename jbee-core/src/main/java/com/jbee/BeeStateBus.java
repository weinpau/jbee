package com.jbee;

import com.jbee.buses.PositionBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.buses.VelocityBus;
import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
class BeeStateBus extends Bus<BeeState> {

    Position position = Position.ORIGIN;
    Velocity velocity = Velocity.ZERO;
    PrincipalAxes principalAxes = PrincipalAxes.ZERO;

    TargetDevice device;

    BeeStateBus(BeeContext context) {
        super(Priority.HIGH);
        
        device = context.getTargetDevice();

        context.getBus(PrincipalAxesBus.class).
                orElseThrow(() -> new RuntimeException("A principal axes bus is missing.")).
                subscripe(pa -> {
                    principalAxes = pa;
                    publish(createBeeState());
                });

        context.getBus(VelocityBus.class).
                orElseThrow(() -> new RuntimeException("A velocity bus is missing.")).
                subscripe(v -> {
                    velocity = v;
                    publish(createBeeState());
                });

        context.getBus(PositionBus.class).
                orElseThrow(() -> new RuntimeException("A position bus is missing.")).
                subscripe(p -> {
                    position = p;
                    publish(createBeeState());
                });
    }

    final BeeState createBeeState() {
        return new BeeState(System.currentTimeMillis(), position, velocity, principalAxes,
                device.getBatteryState(),
                device.getControlState());
    }

}
