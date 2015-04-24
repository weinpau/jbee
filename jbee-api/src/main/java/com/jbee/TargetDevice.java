package com.jbee;

import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyToCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.RotationCommand;
import com.jbee.commands.TakeOffCommand;

/**
 *
 * @author weinpau
 */
public interface TargetDevice {

    String getId();
    
    BeeState getCurrentState();
    
    FlyCommand getFlyCommand();
    
    FlyToCommand getFlyToCommand();
    
    HoverCommand getHoverCommand();
    
    LandCommand getLandCommand();
    
    RotationCommand getRotationCommand();
    
    TakeOffCommand getTakeOffCommand();

}
