package com.jbee;

import java.util.function.Consumer;

/**
 *
 * @author weinpau
 */
public interface BeeControl {

    void onStateChange(Consumer<BeeState> beeStateListener);
     
    BeeState getLastKnownState();

    BeeAction takeOff(double height);

    BeeAction land();

    BeeAction flyTo(Position position, Velocity velocity);
    
    BeeAction foreward(double distance, Velocity velocity);
        
    BeeAction right(double distance, Velocity velocity);
    
    BeeAction left(double distance, Velocity velocity);
    
    BeeAction backward(double distance, Velocity velocity);
    
    

}
