package com.jbee;

import com.jbee.positioning.LatLon;
import com.jbee.positioning.Position;
import com.jbee.units.Distance;
import java.util.Optional;

/**
 *
 * @author weinpau
 */
class BeeWorldImpl implements BeeWorld {

    private LatLon origin;

    @Override
    public void setOrigin(LatLon origin) {
        this.origin = origin;
    }

    @Override
    public Optional<LatLon> getOrigin() {
        return Optional.ofNullable(origin);
    }

    @Override
    public Optional<Position> calculatePosition(LatLon latlon, Distance height) {
        return Optional.empty();
    }

    @Override
    public Optional<LatLon> calculateLatLon(Position position) {
        return Optional.empty();
    }

}
