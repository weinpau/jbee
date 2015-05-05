package com.jbee.device.simulation;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Velocity3D;
import com.jbee.units.Velocity;

/**
 *
 * @author weinpau
 */
class SimulationStep {

    static final SimulationStep START_STEP = new SimulationStep(null, CommandResult.COMPLETED, State.START_STATE, State.START_STATE, 0);

    final Command command;
    final CommandResult result;

    final State startState, followingState;

    final int timeSpent;

    final long timestamp;

    SimulationStep(Command command, CommandResult result, State startState, State followingState, int timeSpent) {
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

    public Position simulatePosition(long timestamp) {

        double t = timestamp - this.timestamp;
        Velocity v = startState.getVelocity();

        return followingState.getPosition().
                sub(startState.getPosition()).
                normalize().
                multiply(v.mps() * (t / 1000d)).
                add(startState.getPosition());
    }

    public Angle simulateYAW(long timestamp) {

        double t = timestamp - this.timestamp;
        return followingState.getYAW().
                sub(startState.getYAW()).
                multiply(1d / (timeSpent)).
                multiply(t).
                add(startState.getYAW());

    }

    public Velocity3D simulateTranslationalVelocity(long timestamp) {
     
        Velocity v = startState.getVelocity();        
        Position p = followingState.getPosition().
                sub(startState.getPosition()).
                normalize().
                multiply(v.mps());
               
        return new Velocity3D(
                Velocity.mps(p.getX()), 
                Velocity.mps(p.getY()),
                Velocity.mps(p.getZ()));
    }

}
