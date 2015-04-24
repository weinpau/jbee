package com.jbee.commands;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public interface HoverCommand extends Command {
    
    CommandPromise hover(Duration duration);
    
}
