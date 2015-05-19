package com.jbee.device.ardrone2.internal;

import com.jbee.ControlStateMachine;
import com.jbee.buses.BeeStateBus;
import com.jbee.buses.PositionBus;
import com.jbee.commands.CancelCommand;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.device.ardrone2.commands.PlayLEDAnimation;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.controllers.CancelController;
import com.jbee.device.ardrone2.internal.controllers.FlyController;
import com.jbee.device.ardrone2.internal.controllers.HoverController;
import com.jbee.device.ardrone2.internal.controllers.LandController;
import com.jbee.device.ardrone2.internal.controllers.PlayLEDController;
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
    FlyController flyController;
    PlayLEDController playLEDController;

    public CommandDispatcher(AT_CommandSender commandSender, NavDataClient navdataClient, BeeStateBus beeStateBus, ControlStateMachine controlStateMachine) {
        takeOffController = new TakeOffController(commandSender, navdataClient, controlStateMachine, commandExecutorService);
        landController = new LandController(commandSender, navdataClient, controlStateMachine, commandExecutorService);
        hoverController = new HoverController(commandSender, controlStateMachine);
        cancelController = new CancelController(commandSender, controlStateMachine);
        flyController = new FlyController(commandSender, beeStateBus, controlStateMachine, commandExecutorService);
        playLEDController = new PlayLEDController(commandSender);
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
        if (command instanceof FlyCommand) {
            return flyController.execute((FlyCommand) command);
        }

        if (command instanceof PlayLEDAnimation) {
            return playLEDController.execute((PlayLEDAnimation) command);
        }

        throw new RuntimeException("unknown command");
    }

}
