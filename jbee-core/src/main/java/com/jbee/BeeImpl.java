package com.jbee;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author weinpau
 */
class BeeImpl implements Bee {

    final ExecutorService commandExecutor = Executors.newFixedThreadPool(1);
    final BeeWorld world = new BeeWorldImpl();
    final DefaultBeeControl control;
    final DefaultBeeMonitor monitor = new DefaultBeeMonitor();

    private static final int CLOCK = 50;

    Timer timer = new Timer(true);

    public BeeImpl(TargetDevice device) {
        control = new DefaultBeeControl(commandExecutor, device, monitor);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                monitor.changeState(device.getCurrentState());
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
        timer.cancel();
        commandExecutor.shutdown();
        control.close();
    }

}
