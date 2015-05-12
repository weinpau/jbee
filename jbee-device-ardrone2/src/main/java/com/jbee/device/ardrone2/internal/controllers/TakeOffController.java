package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlStateMachine;
import com.jbee.commands.CommandResult;
import com.jbee.commands.TakeOffCommand;
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
public class TakeOffController implements CommandController<TakeOffCommand> {

    AT_CommandSender commandSender;
    NavDataClient navdataClient;
    ExecutorService commandExecutorService;
    ControlStateMachine controlStateMachine;

    public static final int TIMEOUT_MILLIS = 5000;

    public TakeOffController(AT_CommandSender commandSender,
            NavDataClient navdataClient,
            ControlStateMachine controlStateMachine,
            ExecutorService commandExecutorService) {

        this.commandSender = commandSender;
        this.navdataClient = navdataClient;
        this.controlStateMachine = controlStateMachine;
        this.commandExecutorService = commandExecutorService;
    }

    @Override
    public CommandResult execute(TakeOffCommand command) {
        if (!controlStateMachine.changeState(com.jbee.ControlState.TAKING_OFF)) {
            return CommandResult.NOT_EXECUTED;
        }

        String navdataReveiverId = "takeoff-" + command.getCommandNumber();
        try {
            return commandExecutorService.submit(new CallbackWrapper<CommandResult>() {

                @Override
                protected void handle() {
                    try {
                        commandSender.send(AT_REF.TAKE_OFF);
                        navdataClient.onNavDataReceived(navdataReveiverId, navdata -> {

                            Demo demo = navdata.getOption(Demo.class);
                            if (demo != null && demo.getControlState() == ControlState.CTRL_HOVERING) {
                                controlStateMachine.changeState(com.jbee.ControlState.FLYING);
                                submit(CommandResult.COMPLETED);
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