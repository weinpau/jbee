package com.jbee.device.simulation;

import com.jbee.BeeState;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyToCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.InterruptCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.RotationCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import com.jbee.units.Velocity;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public class StateMachine {

    volatile SimulationStep step = SimulationStep.START_STEP;
    Velocity defaultVelocity;
    Distance takeOffHeight;

    public StateMachine(Velocity defaultVelocity, Distance takeOffHeight) {
        this.defaultVelocity = defaultVelocity;
        this.takeOffHeight = takeOffHeight;
    }

    public BeeState getCurrentState() {
        return step.simulateState(System.currentTimeMillis());
    }

    public RunnableFuture<CommandResult> execute(Command command) {
        return new FutureTask<>(() -> {
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
            if (command instanceof InterruptCommand) {
                simulationStep = exec((InterruptCommand) command);
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
        });
    }

    SimulationStep exec(TakeOffCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), defaultVelocity, lastState().getYAW());
        BeeState followingState = new SimulationState(startState.getPosition().withZ(takeOffHeight.toMeters()), Velocity.ZERO, startState.getYAW());

        if (!lastState().equals(BeeState.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            int timeSpent = calculateTimeSpent(defaultVelocity, takeOffHeight);
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }

    }

    SimulationStep exec(LandCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), defaultVelocity, lastState().getYAW());
        Position newPos = startState.getPosition().withZ(0);
        BeeState followingState = new SimulationState(newPos, Velocity.ZERO, startState.getYAW());

        if (lastState().equals(BeeState.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            int timeSpent = calculateTimeSpent(defaultVelocity, startState.getPosition().distance(newPos));
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }
    }

    SimulationStep exec(FlyToCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), command.getVelocity(), lastState().getYAW());
        BeeState followingState = new SimulationState(command.getPosition(), Velocity.ZERO,
                command.getYAW() == null ? lastState().getYAW() : command.getYAW());

        if (lastState().equals(BeeState.START_STATE)) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            Distance distance = startState.getPosition().distance(command.getPosition());
            int timeSpent = calculateTimeSpent(command.getVelocity(), distance);

            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }
    }

    SimulationStep exec(RotationCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), Velocity.ZERO, lastState().getYAW());
        BeeState followingState = new SimulationState(lastState().getPosition(), Velocity.ZERO, command.getYAW());

        if (lastState().getPosition().getZ() <= 0) {
            return new SimulationStep(command, CommandResult.FAILED, startState, followingState, 0);
        } else {
            int timeSpent = (int) (100 * command.getYAW().sub(lastState().getYAW()).abs().toRadians());
            return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, timeSpent);
        }
    }

    SimulationStep exec(HoverCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), Velocity.ZERO, lastState().getYAW());
        BeeState followingState = new SimulationState(lastState().getPosition(), Velocity.ZERO, lastState().getYAW());

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

    SimulationStep exec(InterruptCommand command) {
        BeeState startState = new SimulationState(lastState().getPosition(), Velocity.ZERO, lastState().getYAW());
        BeeState followingState = new SimulationState(lastState().getPosition(), Velocity.ZERO, lastState().getYAW());

        return new SimulationStep(command, CommandResult.COMPLETED, startState, followingState, 0);

    }

    BeeState lastState() {
        return step.getFollowingState();
    }

    Position calculatePosition(FlyCommand command) {
        Position p = lastState().getPosition();
        double yaw = lastState().getYAW().toDegrees();
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

    static int calculateTimeSpent(Velocity velocity, Distance distance) {
        return (int) (1000 * distance.toMeters() / velocity.mps());
    }

}
