package com.jbee.commands;

import com.jbee.units.Distance;

/**
 *
 * @author weinpau
 */
public class TakeOffCommand extends AbstractCommand {

    private final Distance height;

    public TakeOffCommand(Distance height) {
        this.height = height;

    }

    public Distance getHeight() {
        return height;
    }

}
