package com.jbee.commands;

import java.time.Duration;

/**
 *
 * @author weinpau
 */
public class HoverCommand extends AbstractCommand {

    private final Duration duration;

    HoverCommand(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

}
