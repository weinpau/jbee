package com.jbee.commands;

import java.time.Duration;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class HoverCommand implements Command {

    private final Duration duration;

    public HoverCommand(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.duration);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HoverCommand other = (HoverCommand) obj;
        if (!Objects.equals(this.duration, other.duration)) {
            return false;
        }
        return true;
    }

}
