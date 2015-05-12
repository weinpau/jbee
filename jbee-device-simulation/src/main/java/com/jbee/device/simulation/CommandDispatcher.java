package com.jbee.device.simulation;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.CancelCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.units.Distance;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
class CommandDispatcher {

    volatile State state = State.INITIAL_STATE;
    volatile Command currentCommand;
    volatile CommandSimulation currentCmdSimulation;
    Speed defaultSpeed;
    Distance takeOffAltitude;
    long startTime = System.currentTimeMillis();

    CommandDispatcher(Speed defaultSpeed, Distance takeOffAltitude) {
        this.defaultSpeed = defaultSpeed;
        this.takeOffAltitude = takeOffAltitude;
    }

    public State getCurrentState() {

        if (currentCmdSimulation == null) {
            return state;
        } else {
            return currentCmdSimulation.simulateState(
                    state,
                    currentCommand,
                    System.currentTimeMillis() - startTime);

        }
    }

    public CommandResult execute(Command command) throws InterruptedException {

        CommandSimulation cmdSimulation = getCommandSimulation(command);
        currentCommand = command;
        currentCmdSimulation = cmdSimulation;
        long timeSpent = cmdSimulation.calculateTimeSpent(state, command);
        startTime = System.currentTimeMillis();
        Thread.sleep(timeSpent);
        state = cmdSimulation.simulateState(state, command, timeSpent);
        currentCmdSimulation = null;
        currentCommand = null;
        return CommandResult.COMPLETED;

    }

    CommandSimulation getCommandSimulation(Command command) throws RuntimeException {

        if (command instanceof TakeOffCommand) {
            return new TakeOffSimulation(defaultSpeed, takeOffAltitude);
        }
        if (command instanceof LandCommand) {
            return new LandSimulation(defaultSpeed);
        }
        if (command instanceof HoverCommand) {
            return new HoverSimulation();
        }
        if (command instanceof CancelCommand) {
            return new CancelSimulation();
        }
        if (command instanceof FlyCommand) {
            return new FlySimulation();
        }

        throw new RuntimeException("unknown command");
    }

}
