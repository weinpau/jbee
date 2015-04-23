package com.jbee;

import java.time.Duration;
import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeAction {

    BeeAction onFailed(Consumer<BeeControl> onFailed);
    
    BeeAction onInterrupt(Consumer<BeeControl> onInterrupt);

    BeeAction onAction(Consumer<BeeAction> onAction, Duration interval);

    BeeAction onPositionChange(Consumer<Position> positionChange, double deltaDistance);

    void interrupt();

}
