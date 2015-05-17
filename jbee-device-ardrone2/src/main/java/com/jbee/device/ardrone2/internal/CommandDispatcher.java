package com.jbee.device.ardrone2.internal;

import com.jbee.ControlStateMachine;
import com.jbee.commands.CancelCommand;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.controllers.CancelController;
import com.jbee.device.ardrone2.internal.controllers.HoverController;
import com.jbee.device.ardrone2.internal.controllers.LandController;
import com.jbee.device.ardrone2.internal.controllers.TakeOffController;
import com.jbee.device.ardrone2.internal.navdata.NavDataClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author weinpau
 */
public class CommandDispatcher {

    ExecutorService commandExecutorService = Executors.newSingleThreadExecutor();

    LandController landController;
    TakeOffController takeOffController;
    HoverController hoverController;
    CancelController cancelController;

    public CommandDispatcher(AT_CommandSender commandSender, NavDataClient navdataClient, ControlStateMachine controlStateMachine) {

        takeOffController = new TakeOffController(commandSender, navdataClient, controlStateMachine, commandExecutorService);
        landController = new LandController(commandSender, navdataClient, controlStateMachine, commandExecutorService);
        hoverController = new HoverController(commandSender, controlStateMachine);
        cancelController = new CancelController(commandSender, controlStateMachine);
    }

    public CommandResult dispatch(Command command) {

        if (command instanceof TakeOffCommand) {
            return takeOffController.execute((TakeOffCommand) command);
        }
        if (command instanceof LandCommand) {
            return landController.execute((LandCommand) command);
        }
        if (command instanceof HoverCommand) {
            return hoverController.execute((HoverCommand) command);
        }
        if (command instanceof CancelCommand) {
            return cancelController.execute((CancelCommand) command);
        }

        throw new RuntimeException("unknown command");
    }

}
