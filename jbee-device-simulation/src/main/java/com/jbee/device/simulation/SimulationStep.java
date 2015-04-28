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
   
    public static final SimulationStep START_STEP = new SimulationStep(null, CommandResult.COMPLETED, State.START_STATE, State.START_STATE, 0);

    private final Command command;
    private final CommandResult result;

    private final State startState, followingState;

    private final int timeSpent;
    
    private final long timestamp;

    public SimulationStep(Command command, CommandResult result, State startState, State followingState, int timeSpent) {
        this.command = command;
        this.result = result;
        this.startState = startState;
        this.followingState = followingState;
        this.timeSpent = timeSpent;
        this.timestamp = System.currentTimeMillis();
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

    public State simulateState(long timestamp) {

        
        double t = timestamp - this.timestamp;

        Velocity velocity = startState.getVelocity();
        Position position = followingState.getPosition().
                sub(startState.getPosition()).
                normalize().
                multiply(velocity.mps() * (t / 1000d)).
                add(startState.getPosition());        
        YAW yaw = followingState.getYAW().
                sub(startState.getYAW()).
                multiply(1d / (timeSpent)).
                multiply(t).
                add(startState.getYAW());

        return new State(position, velocity, yaw);

    }

}
