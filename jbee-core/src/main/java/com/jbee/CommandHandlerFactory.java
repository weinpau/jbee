package com.jbee;

import com.jbee.commands.Command;

/**
 *
 * @author weinpau
 */
interface CommandHandlerFactory {    
    
    CommandHandler create(Command command);
    
}
