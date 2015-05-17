package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlState;
import com.jbee.ControlStateMachine;
import com.jbee.commands.CancelCommand;
import com.jbee.commands.CommandResult;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_PCMD;

/**
 *
 * @author weinpau
 */
public class CancelController implements CommandController<CancelCommand> {

    AT_CommandSender commandSender;
    ControlStateMachine controlStateMachine;

    public CancelController(AT_CommandSender commandSender, ControlStateMachine controlStateMachine) {
        this.commandSender = commandSender;
        this.controlStateMachine = controlStateMachine;
    }

    @Override
    public CommandResult execute(CancelCommand command) {
        try {
            if (controlStateMachine.getControlState() != ControlState.FLYING) {
                return CommandResult.NOT_EXECUTED;
            }
            commandSender.send(AT_PCMD.HOVER);
            return CommandResult.COMPLETED;
        } catch (InterruptedException e) {
            return CommandResult.FAILED;
        }

    }

}
