package com.jbee.positioning;

import com.jbee.units.Distance;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public interface BeeWorld {

    void setOrigin(LatLon latLon);

    Optional<LatLon> getOrigin();

    Optional<Position> calculatePosition(LatLon latlon, Distance height);

    Optional<LatLon> calculateLatLon(Position position);
}
