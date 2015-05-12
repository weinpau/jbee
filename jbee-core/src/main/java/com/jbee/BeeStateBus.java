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

    BeeStateBus() {
        super(Priority.HIGH);
    }

    @Override
    public void bootstrap(TargetDevice device, BusRegistry busRegistry) throws BeeBootstrapException {
        busRegistry.get(PrincipalAxesBus.class).
                orElseThrow(() -> new BeeBootstrapException("A principal axes bus is missing.")).
                subscripe(pa -> {
                    principalAxes = pa;
                    publish(createBeeState(device));
                });

        busRegistry.get(VelocityBus.class).
                orElseThrow(() -> new BeeBootstrapException("A velocity bus is missing.")).
                subscripe(v -> {
                    velocity = v;
                    publish(createBeeState(device));
                });

        busRegistry.get(PositionBus.class).
                orElseThrow(() -> new BeeBootstrapException("A position bus is missing.")).
                subscripe(p -> {
                    position = p;
                    publish(createBeeState(device));
                });
    }

    final BeeState createBeeState(TargetDevice device) {
        return new BeeState(System.currentTimeMillis(), position, velocity, principalAxes,
                device.getBatteryState(),
                device.getControlState());
    }

}
