package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlStateMachine;
import com.jbee.buses.PositionBus;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author weinpau
 */
public class FlyController implements CommandController<FlyCommand> {

    AT_CommandSender commandSender;
    PositionBus positionBus;
    ControlStateMachine controlStateMachine;
    ExecutorService commandExecutorService;

    public FlyController(AT_CommandSender commandSender, 
            PositionBus positionBus, 
            ControlStateMachine controlStateMachine, 
            ExecutorService commandExecutorService) {        
        this.commandSender = commandSender;
        this.positionBus = positionBus;
        this.controlStateMachine = controlStateMachine;
        this.commandExecutorService = commandExecutorService;
    }

    @Override
    public CommandResult execute(FlyCommand command) {
        return CommandResult.NOT_EXECUTED;
    }

}
