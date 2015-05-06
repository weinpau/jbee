package com.jbee.device.simulation;

import com.jbee.RotationDirection;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyToCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.CancelCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.RotationCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
class StateMachine {

    volatile SimulationStep step = SimulationStep.START_STEP;
    Speed defaultVelocity;
    Distance takeOffHeight;

    StateMachine(Speed defaultVelocity, Distance takeOffHeight) {
        this.defaultVelocity = defaultVelocity;
        this.takeOffHeight = takeOffHeight;
    }

    public SimulationStep getCurrentStep() {
        return step;
    }

    public CommandResult execute(Command command) throws InterruptedException {

        SimulationStep simulationStep = null;
        if (command instanceof TakeOffCommand) {
            simulationStep = exec((TakeOffCommand) command);
        }
        if (command instanceof LandCommand) {
            simulationStep = exec((LandCommand) command);
        }
        if (command instanceof FlyToCommand) {
            simulationStep = exec((FlyToCommand) command);
        }
        if (command instanceof RotationCommand) {
            simulationStep = exec((RotationCommand) command);
        }
        if (command instanceof HoverCommand) {
            simulationStep = exec((HoverCommand) command);
        }
        if (command instanceof CancelCommand) {
            simulationStep = exec((CancelCommand) command);
        }
        if (command instanceof FlyCommand) {
            simulationStep = exec((FlyCommand) command);
        }

        if (simulationStep == null) {
            throw new RuntimeException("unknown command");
        }

        if (simulationStep.getResult().equals(CommandResult.COMPLETED)) {
            step = simulationStep;
        }
        Thread.sleep(simulationStep.getTimeSpent());
        return simulationStep.getResult();

    }

    SimulationStep exec(TakeOffCommand command) {
        State startState = new State(lastState().getPosition(), defaultVelocity, lastState().getYAW());
        State followingState = new State(startState.getPosition().withZ(takeOffHeight.toMeters()), Speed.ZERO, startState.getYAW());

        if (!lastState().equals(State.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            int timeSpent = calculateTimeSpent(defaultVelocity, takeOffHeight);
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }

    }

    SimulationStep exec(LandCommand command) {
        State startState = new State(lastState().getPosition(), defaultVelocity, lastState().getYAW());
        Position newPos = startState.getPosition().withZ(0);
        State followingState = new State(newPos, Speed.ZERO, startState.getYAW());

        if (lastState().equals(State.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            int timeSpent = calculateTimeSpent(defaultVelocity, startState.getPosition().distance(newPos));
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }
    }

    SimulationStep exec(FlyToCommand command) {
        State startState = new State(lastState().getPosition(), command.getVelocity(), lastState().getYAW());
        State followingState = new State(command.getPosition(), Speed.ZERO, lastState().getYAW());

        if (lastState().equals(State.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            Distance distance = startState.getPosition().distance(command.getPosition());
            int timeSpent = calculateTimeSpent(command.getVelocity(), distance);

            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }
    }

    SimulationStep exec(RotationCommand command) {
        State startState = new State(lastState().getPosition(), Speed.ZERO, lastState().getYAW());

        Angle difference = Angle.ofDegrees(50);
        Angle followingYAW;
        if (command.isAbsolute()) {
            followingYAW = command.getAngle();
        } else {
            if (command.getRotationDirection() == RotationDirection.CLOCKWISE) {
                followingYAW = startState.getYAW().add(command.getAngle());
            } else {
                followingYAW = startState.getYAW().sub(command.getAngle());
            }
        }
        State followingState = new State(lastState().getPosition(), Speed.ZERO, followingYAW);

        if (lastState().getPosition().getZ() <= 0) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, command.getRotationDirection(), 0);
        } else {
            int timeSpent = (int) (100 * difference.abs().toRadians());
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, command.getRotationDirection(), timeSpent);
        }
    }

    SimulationStep exec(HoverCommand command) {
        State startState = new State(lastState().getPosition(), Speed.ZERO, lastState().getYAW());
        State followingState = new State(lastState().getPosition(), Speed.ZERO, lastState().getYAW());

        if (lastState().getPosition().getZ() <= 0) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, (int) command.getDuration().toMillis());
        }
    }

    SimulationStep exec(FlyCommand command) {
        Position position = calculatePosition(command);
        return exec(new FlyToCommand(position, command.getVelocity()));

    }

    SimulationStep exec(CancelCommand command) {
        State startState = new State(lastState().getPosition(), Speed.ZERO, lastState().getYAW());
        State followingState = new State(lastState().getPosition(), Speed.ZERO, lastState().getYAW());

        return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, 0);

    }

    State lastState() {
        return step.getFollowingState();
    }

    Position calculatePosition(FlyCommand command) {
        Position p = lastState().getPosition();
        double yaw = lastState().getYAW().toRadians();
        double distance = command.getDistance().toMeters();

        switch (command.getDirection()) {
            case DOWN:
                return p.addZ(-distance);
            case UP:
                return p.addZ(distance);
            case FORWARD:
                return p.withX(p.getX() + Math.cos(yaw) * distance).withY(p.getY() + Math.sin(yaw) * distance);
            case BACKWARD:
                return p.withX(p.getX() + Math.cos(yaw) * -distance).withY(p.getY() + Math.sin(yaw) * -distance);
            case RIGHT:
                return p.withX(p.getX() - Math.sin(yaw) * distance).withY(p.getY() + Math.cos(yaw) * distance);
            case LEFT:
                return p.withX(p.getX() - Math.sin(yaw) * -distance).withY(p.getY() + Math.cos(yaw) * -distance);
        }
        return p;
    }

    static int calculateTimeSpent(Speed velocity, Distance distance) {
        return (int) (1000 * distance.toMeters() / velocity.mps());
    }

}
