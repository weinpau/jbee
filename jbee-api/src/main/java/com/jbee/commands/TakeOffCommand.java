package com.jbee.commands;

import com.jbee.units.Distance;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class TakeOffCommand implements Command {

    private final Distance height;

    public TakeOffCommand(Distance height) {
        this.height = height;
    }

    public Distance getHeight() {
        return height;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.height);
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
        final TakeOffCommand other = (TakeOffCommand) obj;
        if (!Objects.equals(this.height, other.height)) {
            return false;
        }
        return true;
    }

}
