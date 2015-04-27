package com.jbee;

import com.jbee.positioning.LatLon;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
public interface BeeWorld {

    void setOrigin(LatLon origin);

    Optional<LatLon> getOrigin();

    Optional<Position> calculatePosition(LatLon latlon, Distance height);

    Optional<LatLon> calculateLatLon(Position position);
}
