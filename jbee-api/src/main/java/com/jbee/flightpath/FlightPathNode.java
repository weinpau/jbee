package com.jbee.flightpath;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public class FlightPathNode {

    static final FlightPathNode ZERO = new FlightPathNode(Position.ORIGIN, Angle.ZERO);

    Position deltaPosition;
    Angle deltaYaw;

    public FlightPathNode(Position deltaPosition, Angle deltaYaw) {
        this.deltaPosition = deltaPosition;
        this.deltaYaw = deltaYaw;
    }

    public Position getDeltaPosition() {
        return deltaPosition;
    }

    public Angle getDeltaYaw() {
        return deltaYaw;
    }

    FlightPathNode add(FlightPathNode node) {
        return new FlightPathNode(
                deltaPosition.add(node.deltaPosition),
                deltaYaw.add(node.deltaYaw));
    }

    FlightPathNode sub(FlightPathNode node) {
        return new FlightPathNode(
                deltaPosition.sub(node.deltaPosition),
                deltaYaw.sub(node.deltaYaw));
    }

    FlightPathNode multiply(double factor) {
        return new FlightPathNode(
                deltaPosition.multiply(factor),
                deltaYaw.multiply(factor));
    }

    FlightPathNode combine(FlightPathNode node) {

        Position pos;
        if (deltaPosition.isOrigin()) {
            pos = node.deltaPosition;
        } else if (node.deltaPosition.isOrigin()) {
            pos = deltaPosition;
        } else {
            pos = deltaPosition.add(node.deltaPosition).multiply(.5);
        }

        Angle yaw;
        if (deltaYaw.isZero()) {
            yaw = node.deltaYaw;

        } else if (node.deltaYaw.isZero()) {
            yaw = deltaYaw;
        } else {
            yaw = deltaYaw.add(node.deltaYaw).multiply(.5);
        }
        
        return new FlightPathNode(pos, yaw);
    }

    double getMetersToOrigin() {
        return deltaPosition.distance(Position.ORIGIN).toMeters();
    }

    long calculateDuration(Speed speed, RotationalSpeed rotationalSpeed) {
        return Math.max(deltaPosition.distance(Position.ORIGIN).divide(speed).toMillis(),
                deltaYaw.divide(rotationalSpeed.toAngularSpeed()).toMillis());
    }

    @Override
    public String toString() {
        return "FlightPathNode{" + "deltaPosition=" + deltaPosition + ", deltaYaw=" + deltaYaw + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.deltaPosition);
        hash = 53 * hash + Objects.hashCode(this.deltaYaw);
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
        final FlightPathNode other = (FlightPathNode) obj;
        if (!Objects.equals(this.deltaPosition, other.deltaPosition)) {
            return false;
        }
        return Objects.equals(this.deltaYaw, other.deltaYaw);
    }

}
