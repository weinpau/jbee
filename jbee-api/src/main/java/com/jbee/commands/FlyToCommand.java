package com.jbee.commands;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.Angle;

/**
 *
 * @author weinpau
 */
public class FlyToCommand extends AbstractCommand {

    private final Position position;

    private final Velocity velocity;
    private final Angle yaw;

    public FlyToCommand(Position position, Velocity velocity, Angle yaw) {
        this.position = position;
        this.velocity = velocity;
        this.yaw = yaw;
    }

    public FlyToCommand(Position position, Velocity velocity) {
        this(position, velocity, null);
    }

    public Position getPosition() {
        return position;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Angle getYAW() {
        return yaw;
    }
    
}
