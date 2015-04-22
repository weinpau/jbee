package com.jbee;

import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeAction {

    BeeAction onSuccess(Consumer<BeeControl> onSuccess);

    BeeAction onFailed(Consumer<BeeControl> onFailed);

    BeeAction onPositionChange(Consumer<Position> positionChange, double deltaDistance);
    
    void stop();
    
    
}
