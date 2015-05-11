package com.jbee.commands;

import com.jbee.BeeControl;

/**
 *
 * @author weinpau
 */
public interface Command {
    
    void init(int commandNumber, BeeControl executiveControl);
    
    int getCommandNumber();
        
}
