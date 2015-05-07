package com.jbee.flightpath;

import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class FlightPath {

    final List<FlightPathNode> chain = new ArrayList<>();

    FlightPath() {
    }

    FlightPath(FlightPathNode targetNode) {
        chain.add(targetNode);
    }

    public FlightPathNode getTargetNode() {
        FlightPathNode result = FlightPathNode.ZERO;
        for (FlightPathNode n : chain) {
            result = result.add(n);
        }
        return result;
    }

    public FlightPath chain(FlightPath path) {
        FlightPath result = new FlightPath();
        result.chain.addAll(chain);
        result.chain.addAll(path.chain);
        return result;

    }

    public FlightPath combine(FlightPath path) {
        double len = getTargetNode().getMetersToOrigin();
        if (len < path.getTargetNode().getMetersToOrigin()) {
            path.combine(this);
        }
        if (len == path.getTargetNode().getMetersToOrigin() && chain.size() < path.chain.size()) {
            path.combine(this);
        }
        FlightPath result = new FlightPath();

        FlightPathNode newTargetNode = getTargetNode().combine(path.getTargetNode());

        FlightPathNode delta = newTargetNode.sub(getTargetNode());

        chain.stream().
                map(n -> n.add(delta.multiply(n.getMetersToOrigin() / len))).
                forEach(result.chain::add);

        return result;
    }

    public Distance getLength() {
        Distance result = Distance.ZERO;
        for (FlightPathNode n : chain) {
            result = result.add(n.deltaPosition.distance(Position.ORIGIN));
        }
        return result;
    }

    public FlightPathCalculator createFlightPathCalculator(Speed speed, RotationalSpeed rotationalSpeed) {
        return new FlightPathCalculator(Collections.unmodifiableList(chain), speed, rotationalSpeed);
    }

    public static FlightPath of(FlightPathNode targetNode) {
        return new FlightPath(targetNode);
    }

    public static FlightPath of(Position deltaPosition, Angle deltaYaw) {
        return of(new FlightPathNode(deltaPosition, deltaYaw));
    }

}
