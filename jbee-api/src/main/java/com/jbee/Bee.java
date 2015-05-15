package com.jbee;

import com.jbee.positioning.LatLon;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public interface Bee {

    BeeControl control();

    BeeMonitor monitor();

    Optional<LatLon> getOrigin();

    void close();

}
