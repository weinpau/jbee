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

    public DefaultBee(TargetDevice device, BusRegistry busRegistry) {
        this.device = device;
        commandExecutor = new CommandExecutor(device);
        control = new DefaultBeeControl(commandExecutor, monitor, device.getDefaultSpeed(), device.getDefaultRotationalSpeed());

        busRegistry.get(BeeStateBus.class).
                orElseThrow(() -> new RuntimeException("BeeStateBus has not been initialized")).
                subscripe(state -> monitor.changeState(state));
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
        } catch (IOException ioe) {
        }
        commandExecutor.shutdown();
        control.close();
    }

}
