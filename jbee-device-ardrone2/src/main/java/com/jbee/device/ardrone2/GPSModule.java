package com.jbee.device.ardrone2;

import com.jbee.BeeBootstrapException;
import com.jbee.BeeModule;
import com.jbee.BusRegistry;
import com.jbee.TargetDevice;
import com.jbee.buses.LatLonBus;
import com.jbee.device.ardrone2.internal.navdata.options.GPS;
import com.jbee.positioning.LatLon;

/**
 *
 * @author weinpau
 */
public class GPSModule extends BeeModule {

    public GPSModule() {
        register(latLonBus);
    }

    LatLonBus latLonBus = new LatLonBus() {

        @Override
        public void bootstrap(TargetDevice targetDevice, BusRegistry busRegistry) throws BeeBootstrapException {
            
            if (targetDevice instanceof ARDrone2) {

                ARDrone2 arDrone = (ARDrone2) targetDevice;

                arDrone.navdataClient.onNavDataReceived("gps-receiver", navdata -> {

                    GPS gps = navdata.getOption(GPS.class);

                    if (gps != null && gps.getNbSatellites() >= 3) {
                        LatLon geo = new LatLon(gps.getLatitude(), gps.getLongitude());
                        latLonBus.publish(geo);
                    }

                });

            } else {
                throw new BeeBootstrapException("The device is incompatible.");
            }

        }
    };

}
