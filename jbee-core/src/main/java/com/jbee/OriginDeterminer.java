package com.jbee;

import com.jbee.buses.LatLonBus;
import com.jbee.positioning.LatLon;

/**
 *
 * @author weinpau
 */
class OriginDeterminer {

    static final int WAIT_TIME = 200;
    static final int MAX_WAIT_CYCLES = 150;

    final BusRegistry busRegistry;

    public OriginDeterminer(BusRegistry busRegistry) {
        this.busRegistry = busRegistry;
    }

    public boolean isDeterminable() {
        return !busRegistry.getAll(LatLonBus.class).isEmpty();
    }

    @SuppressWarnings("SleepWhileInLoop")
    public LatLon determineOrigin() throws InterruptedException {

        for (int i = 0; i < MAX_WAIT_CYCLES; i++) {
            for (LatLonBus bus : busRegistry.getAll(LatLonBus.class)) {
                LatLon position = bus.getLastKnownValue().orElse(null);
                if (position != null) {
                    return position;
                }
            }
            Thread.sleep(WAIT_TIME);

        }
        throw new InterruptedException("No GPS signal");
    }

}
