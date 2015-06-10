package com.jbee.device.simulation;

import com.jbee.Velocity;
import com.jbee.commands.LandCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Speed;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
class LandSimulation implements CommandSimulation<LandCommand> {

    Speed landSpeed;

    public LandSimulation(Speed landSpeed) {
        this.landSpeed = landSpeed;
    }

    @Override
    public State simulateState(State initialState, LandCommand command, long time) {

        Position p = initialState.getPosition().
                addZ(landSpeed.multiply(Duration.ofMillis(time)).
                        multiply(-1).toMeters());

        if (time < calculateDuration(initialState, command)) {
            return new State(p, new Velocity(Speed.ZERO, Speed.ZERO, landSpeed.multiply(-1)), initialState.getYaw());
        } else {
            return new State(p, Velocity.ZERO, initialState.getYaw());
        }

    }

    @Override
    public long calculateDuration(State initialState, LandCommand command) {
        return (long) (1000d * initialState.getPosition().getZ() / landSpeed.mps());
    }

}
