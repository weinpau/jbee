package com.jbee;

import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeAction {

    BeeAction onSuccess(Consumer<BeeAction> onSuccess);

    BeeAction onFailed(Consumer<BeeAction> onFailed);

    BeeAction onPositionChange(Consumer<Position> positionChange, double deltaDistance);
    
    void stop();
    
    
}
