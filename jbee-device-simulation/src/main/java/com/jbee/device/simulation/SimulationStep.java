package com.jbee.device.simulation;

import com.jbee.RotationDirection;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.Velocity;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
class SimulationStep {

    static final SimulationStep START_STEP = new SimulationStep(null, CommandResult.COMPLETED, State.START_STATE, State.START_STATE, 0);

    final Command command;
    final CommandResult result;

    final State startState, followingState;

    final RotationDirection rotationDirection;

    final int timeSpent;

    final long timestamp;

    SimulationStep(Command command, CommandResult result, State startState, State followingState, RotationDirection rotationDirection, int timeSpent) {
        this.command = command;
        this.result = result;
        this.startState = startState;
        this.followingState = followingState;
        this.timeSpent = timeSpent;
        this.rotationDirection = rotationDirection;
        this.timestamp = System.currentTimeMillis();
    }

    SimulationStep(Command command, CommandResult result, State startState, State followingState, int timeSpent) {
        this(command, result, startState, followingState, RotationDirection.CLOCKWISE, timeSpent);
    }

    public Command getCommand() {
        return command;
    }

    public CommandResult getResult() {
        return result;
    }

    public State getStartState() {
        return startState;
    }

    public State getFollowingState() {
        return followingState;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public Angle simulateYAW(long timestamp) {

        if (timeSpent == 0) {
            return startState.getYAW();
        }

        double t = timestamp - this.timestamp;
        return followingState.getYAW().
                sub(startState.getYAW()).
                multiply(1d / (timeSpent)).
                multiply(t).
                add(startState.getYAW());

    }

    public Velocity simulateVelocity(long timestamp) {

        Speed v = startState.getSpeed();
        Position p = followingState.getPosition().
                sub(startState.getPosition()).
                normalize().
                multiply(v.mps());

        return new Velocity(
                Speed.mps(p.getX()),
                Speed.mps(p.getY()),
                Speed.mps(p.getZ()));
    }

}
