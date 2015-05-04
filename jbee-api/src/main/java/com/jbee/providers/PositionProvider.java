package com.jbee.providers;

import com.jbee.Provider;
import com.jbee.positioning.Position;

/**
 *
 * @author weinpau
 */
public interface PositionProvider extends Provider<Position> {

    default boolean isOnly2D() {
        return false;
    }
    
    default boolean isRelative() {
        return false;
    }

}
