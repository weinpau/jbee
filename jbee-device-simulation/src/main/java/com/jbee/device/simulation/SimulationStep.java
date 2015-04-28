package com.jbee.device.simulation;

import com.jbee.BeeState;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;

/**
 *
 * @author weinpau
 */
public class SimulationStep {

    public static final SimulationStep START_STEP = new SimulationStep(null, CommandResult.COMPLETED, BeeState.START_STATE, BeeState.START_STATE, 0);

    private final Command command;
    private final CommandResult result;

    private final BeeState startState, followingState;

    private final int timeSpent;

    public SimulationStep(Command command, CommandResult result, BeeState startState, BeeState followingState, int timeSpent) {
        this.command = command;
        this.result = result;
        this.startState = startState;
        this.followingState = followingState;
        this.timeSpent = timeSpent;
    }

    public Command getCommand() {
        return command;
    }

    public CommandResult getResult() {
        return result;
    }

    public BeeState getStartState() {
        return startState;
    }

    public BeeState getFollowingState() {
        return followingState;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public BeeState simulateState(long timestamp) {

        
        double t = timestamp - startState.getTimestamp();

        Velocity velocity = startState.getVelocity();
        Position position = followingState.getPosition().
                sub(startState.getPosition()).
                normalize().
                multiply(velocity.mps() * (t / 1000d)).
                add(startState.getPosition());        
        YAW yaw = followingState.getYAW().
                sub(startState.getYAW()).
                multiply(1d / (followingState.getTimestamp() - startState.getTimestamp())).
                multiply(t).
                add(startState.getYAW());

        return new SimulationState(position, velocity, yaw);

    }

}
