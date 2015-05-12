package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlStateMachine;
import com.jbee.commands.CommandResult;
import com.jbee.commands.HoverCommand;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_PCMD;

/**
 *
 * @author weinpau
 */
public class HoverController implements CommandController<HoverCommand> {

    AT_CommandSender commandSender;
    ControlStateMachine controlStateMachine;

    public HoverController(AT_CommandSender commandSender, ControlStateMachine controlStateMachine) {
        this.commandSender = commandSender;
        this.controlStateMachine = controlStateMachine;
    }

    @Override
    public CommandResult execute(HoverCommand command) {
        if (!controlStateMachine.changeState(com.jbee.ControlState.FLYING)) {
            return CommandResult.NOT_EXECUTED;
        }
        try {
            commandSender.send(AT_PCMD.HOVER);
            Thread.sleep(command.getDuration().toMillis());
            return CommandResult.COMPLETED;
        } catch (InterruptedException e) {
            return CommandResult.FAILED;
        }
    }

}
