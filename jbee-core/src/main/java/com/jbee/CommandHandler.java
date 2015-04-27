package com.jbee;

import com.jbee.commands.Command;

/**
 *
 * @author weinpau
 */
interface CommandHandler {

    void start(Command command);

    void stop();

}
