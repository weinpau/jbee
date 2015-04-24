package com.jbee.commands;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public interface HoverCommand extends Command {
    
    void hover(Duration duration);
    
}
