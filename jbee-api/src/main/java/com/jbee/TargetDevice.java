package com.jbee;

import com.jbee.commands.Command;
import com.jbee.commands.CommandResult;
import com.jbee.units.Frequency;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.io.IOException;
import java.util.concurrent.RunnableFuture;

/**
 *
 * @author weinpau
 */
public interface TargetDevice {

    String getId();

    RunnableFuture<CommandResult> execute(Command command);

    void bootstrap(BusRegistry busRegistry) throws BeeBootstrapException;

    void disconnect() throws IOException;

    ControlState getControlState();

    BatteryState getBatteryState();

    Frequency getTransmissionRate();

    Speed getMaxSpeed();

    Speed getDefaultSpeed();

    RotationalSpeed getMaxRotationalSpeed();

    RotationalSpeed getDefaultRotationalSpeed();
}
