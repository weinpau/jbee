package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlStateMachine;
import com.jbee.commands.CommandResult;
import com.jbee.commands.LandCommand;
import com.jbee.concurrent.CallbackWrapper;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_REF;
import com.jbee.device.ardrone2.internal.navdata.NavDataClient;
import com.jbee.device.ardrone2.internal.navdata.options.ControlState;
import com.jbee.device.ardrone2.internal.navdata.options.Demo;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author weinpau
 */
public class LandController implements CommandController<LandCommand> {

    AT_CommandSender commandSender;
    NavDataClient navdataClient;
    ControlStateMachine controlStateMachine;
    ExecutorService commandExecutorService;

    static final int TIMEOUT_MILLIS = 10_000;

    public LandController(AT_CommandSender commandSender,
            NavDataClient navdataClient,
            ControlStateMachine controlStateMachine,
            ExecutorService commandExecutorService) {

        this.commandSender = commandSender;
        this.navdataClient = navdataClient;
        this.controlStateMachine = controlStateMachine;
        this.commandExecutorService = commandExecutorService;
    }

    @Override
    public CommandResult execute(LandCommand command) {
        if (controlStateMachine.getControlState() != com.jbee.ControlState.FLYING) {
            return CommandResult.NOT_EXECUTED;
        }

        String navdataReveiverId = String.format("land-%d", command.getCommandNumber());
        try {
            return commandExecutorService.submit(new CallbackWrapper<CommandResult>() {

                @Override
                protected void handle() {
                    try {
                        commandSender.send(AT_REF.LAND);
                        navdataClient.onNavDataReceived(navdataReveiverId, navdata -> {

                            Demo demo = navdata.getOption(Demo.class);
                            if (demo != null) {
                                if (demo.getControlState() == ControlState.CTRL_TRANS_LOOPING) {
                                    controlStateMachine.changeStateForced(com.jbee.ControlState.LANDING);
                                } else if (demo.getControlState() == ControlState.CTRL_LANDED) {
                                    controlStateMachine.changeStateForced(com.jbee.ControlState.READY_FOR_TAKE_OFF);
                                    submit(CommandResult.COMPLETED);
                                }
                            }

                        });
                    } catch (InterruptedException ex) {
                        submit(CommandResult.FAILED);
                    }
                }
            }).get(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            return CommandResult.FAILED;
        } finally {
            navdataClient.removeNavDataReceiver(navdataReveiverId);
        }

    }
}
