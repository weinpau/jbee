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

    BeeStateBus(TargetDevice device) {
        super(Priority.HIGH);
        this.device = device;
    }

    @Override
    public void bootstrap(BusRegistry busRegistry) throws BeeBootstrapException {
        busRegistry.get(PrincipalAxesBus.class).
                orElseThrow(() -> new BeeBootstrapException("A principal axes bus is missing.")).
                subscripe(pa -> {
                    principalAxes = pa;
                    publish(createBeeState());
                });

        busRegistry.get(VelocityBus.class).
                orElseThrow(() -> new BeeBootstrapException("A velocity bus is missing.")).
                subscripe(v -> {
                    velocity = v;
                    publish(createBeeState());
                });

        busRegistry.get(PositionBus.class).
                orElseThrow(() -> new BeeBootstrapException("A position bus is missing.")).
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
