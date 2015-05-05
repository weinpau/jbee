package com.jbee;

import java.io.IOException;

/**
 *
 * @author weinpau
 */
class DefaultBee implements Bee {

    final TargetDevice device;

    final CommandExecutor commandExecutor;
    final BeeWorld world = new BeeWorldImpl();
    final DefaultBeeControl control;
    final DefaultBeeMonitor monitor = new DefaultBeeMonitor();

    public DefaultBee(TargetDevice device, BeeStateBus beeStateBus) {
        this.device = device;
        commandExecutor = new CommandExecutor(device);
        control = new DefaultBeeControl(commandExecutor, monitor);

        beeStateBus.subscripe(state -> monitor.changeState(state));
    }

    @Override
    public BeeControl control() {
        return control;
    }

    @Override
    public BeeMonitor monitor() {
        return monitor;
    }

    @Override
    public BeeWorld getWorld() {
        return world;
    }

    @Override
    public void close() {
        try {
            device.disconnect();
        } catch (IOException iOException) {
        }
        commandExecutor.shutdown();
        control.close();
    }

}
