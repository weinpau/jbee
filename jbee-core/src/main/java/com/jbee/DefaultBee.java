package com.jbee;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final int CLOCK = 50;

    Timer stateListener = new Timer("state-listener", true);

    public DefaultBee(TargetDevice device, StateFactory stateFactory) {
        this.device = device;
        commandExecutor = new CommandExecutor(device);
        control = new DefaultBeeControl(commandExecutor, monitor);

        stateListener.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                monitor.changeState(stateFactory.getCurrentState());
            }
        }, CLOCK, CLOCK);
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
        device.disconnect();
        stateListener.cancel();
        commandExecutor.shutdown();
        control.close();
    }

}
