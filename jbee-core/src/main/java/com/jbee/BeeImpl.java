package com.jbee;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author weinpau
 */
class BeeImpl implements Bee {

    final BeeWorld world = new BeeWorldImpl();
    final BeeControl control;
    final DefaultBeeMonitor monitor = new DefaultBeeMonitor();

    private static final int CLOCK = 100;

    Timer timer = new Timer(true);

    public BeeImpl(TargetDevice device) {
        control = new BeeControlImpl(device, monitor);

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

}
