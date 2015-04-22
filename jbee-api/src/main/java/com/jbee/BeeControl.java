package com.jbee;

import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {

    void onStateChange(Consumer<BeeState> beeStateListener);
     
    BeeState getLastKnownState();

    BeeAction takeOff();

    BeeAction land();

    BeeAction flyTo(Position position);
    
    BeeAction foreward(double distance);
        
    BeeAction right(double distance);
    
    BeeAction left(double distance);
    
    BeeAction backward(double distance);
    
    

}
