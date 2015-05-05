package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.units.Frequency;
import java.io.IOException;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public interface TargetDevice {

    String getId();

    RunnableFuture<CommandResult> execute(Command command);

    void bootstrap() throws BeeBootstrapException;

    void disconnect() throws IOException;
    
    ControlState getControlState();
    
    BatteryState getBatteryState();
    
    Frequency getTransmissionRate();

}
