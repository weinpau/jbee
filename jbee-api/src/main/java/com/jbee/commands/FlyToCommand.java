package com.jbee.commands;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;

/**
 *
 * @author weinpau
 */
public class FlyToCommand extends AbstractCommand {

    private final Position position;

    private final Velocity velocity;

    public FlyToCommand(Position position, Velocity velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

}
