package com.jbee.commands;

/**
 *
 * @author weinpau
 */
public interface Command {
    
    void init(int commandNumber);

    int getCommandNumber();
    
}
