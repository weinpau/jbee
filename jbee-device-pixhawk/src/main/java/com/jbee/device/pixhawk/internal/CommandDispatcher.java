package com.jbee.device.pixhawk.internal;

import com.jbee.commands.CancelCommand;
import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.TakeOffCommand;
import com.jbee.device.pixhawk.connection.network.NetworkConnection;
import com.jbee.device.pixhawk.controller.CancelController;
import com.jbee.device.pixhawk.controller.FlyController;
import com.jbee.device.pixhawk.controller.HoverController;
import com.jbee.device.pixhawk.controller.LandController;
import com.jbee.device.pixhawk.controller.TakeOffController;
import com.jbee.device.pixhawk.mavlink.MavlinkModule;

/**
 *
 * @author Erik Jähne
 */
public class CommandDispatcher {
    
    private final TakeOffController takeOffController;
    private final LandController landController;
    private final HoverController hoverController;
    private final CancelController cancelController;
    private final FlyController flyController;

    public CommandDispatcher(PixhawkController pixhawk) {        
        takeOffController = new TakeOffController(pixhawk);
        landController = new LandController(pixhawk);
        hoverController = new HoverController(pixhawk);
        cancelController = new CancelController(pixhawk);
        flyController = new FlyController(pixhawk);
    }
    
    public CommandResult dispatch(Command command,MavlinkModule myModule,NetworkConnection network){
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

        throw new RuntimeException("unknown command");
    }
}
