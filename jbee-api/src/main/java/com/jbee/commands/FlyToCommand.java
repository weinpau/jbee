package com.jbee.commands;

import com.jbee.positioning.Position;
import com.jbee.units.Speed;

/**
 *
 * @author weinpau
 */
public class FlyToCommand extends AbstractCommand {

    private final Position position;

    private final Speed velocity;

    public FlyToCommand(Position position, Speed velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Position getPosition() {
        return position;
    }

    public Speed getVelocity() {
        return velocity;
    }

}
