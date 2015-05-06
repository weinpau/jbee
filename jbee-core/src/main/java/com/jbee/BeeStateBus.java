package com.jbee;

import com.jbee.buses.PositionBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.buses.TranslationalVelocityBus;
import com.jbee.positioning.Position;
import com.jbee.units.Velocity3D;

/**
 *
 * @author weinpau
 */
class BeeStateBus extends Bus<BeeState> {

    Position position = Position.ORIGIN;
    Velocity3D translationalVelocity = Velocity3D.ZERO;
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

        context.getBus(TranslationalVelocityBus.class).
                orElseThrow(() -> new RuntimeException("A translational velocity bus is missing.")).
                subscripe(v -> {
                    translationalVelocity = v;
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
        return new BeeState(System.currentTimeMillis(), position, translationalVelocity, principalAxes,
                device.getBatteryState(),
                device.getControlState());
    }

}
