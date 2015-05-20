package com.jbee;

import com.jbee.positioning.LatLon;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
class DefaultBee implements Bee {

    final TargetDevice device;

    final CommandExecutor commandExecutor;
    LatLon origin;
    final DefaultBeeControl control;
    final DefaultBeeMonitor monitor = new DefaultBeeMonitor();

    public DefaultBee(TargetDevice device) {
        this.device = device;
        commandExecutor = new CommandExecutor(device);
        control = new DefaultBeeControl(commandExecutor, monitor, device.getDefaultSpeed(), device.getDefaultRotationalSpeed());
    }

    public void init(BusRegistry busRegistry) throws BeeBootstrapException {
        try {
            origin = determineOrigin(busRegistry);
        } catch (InterruptedException e) {
            throw new BeeBootstrapException(e);
        }

        busRegistry.get(DefaultBeeStateBus.class).
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
    public Optional<LatLon> getOrigin() {
        return Optional.ofNullable(origin);
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

    private LatLon determineOrigin(BusRegistry busRegistry) throws InterruptedException {

        OriginDeterminer originDeterminer = new OriginDeterminer(busRegistry);
        if (originDeterminer.isDeterminable()) {
            return originDeterminer.determineOrigin();
        } else {
            return null;
        }
    }

}
