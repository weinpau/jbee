package com.jbee.device.simulation;

import com.jbee.Velocity;
import com.jbee.commands.TakeOffCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Speed;
import java.time.Duration;

/**
 *
 * @author weinpau
 */
class TakeOffSimulation implements CommandSimulation<TakeOffCommand> {

    Speed takeOffSpeed;
    Distance takeOffAltitude;

    public TakeOffSimulation(Speed takeOffSpeed, Distance takeOffAltitude) {
        this.takeOffSpeed = takeOffSpeed;
        this.takeOffAltitude = takeOffAltitude;
    }

    @Override
    public State simulateState(State initialState, TakeOffCommand command, long time) {
        Position p = initialState.getPosition().
                addZ(takeOffSpeed.multiply(Duration.ofMillis(time)).toMeters());

        if (time < calculateTimeSpent(initialState, command)) {
            return new State(p, new Velocity(Speed.ZERO, Speed.ZERO, takeOffSpeed), initialState.getYAW());
        } else {
            return new State(p, Velocity.ZERO, initialState.getYAW());
        }
    }

    @Override
    public long calculateTimeSpent(State initialState, TakeOffCommand command) {
        return takeOffAltitude.divide(takeOffSpeed).toMillis();
    }

}