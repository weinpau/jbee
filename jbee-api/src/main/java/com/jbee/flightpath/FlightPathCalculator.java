package com.jbee.flightpath;

import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class FlightPathCalculator {

    long[] durations;
    List<FlightPathNode> chain;

    FlightPathCalculator(List<FlightPathNode> chain, Speed speed, RotationalSpeed rotationalSpeed) {
        durations = calculateDurations(chain, speed, rotationalSpeed);
        this.chain = chain;

    }

    public FlightPathNode calculateNode(long millis) {
        for (int i = 0; i < chain.size(); i++) {
            if (durations[i] > millis) {
                return chain.get(i).multiply(millis / (double) durations[i]);
            } else {
                millis -= durations[i];
            }
        }
        int last = chain.size() - 1;
        return chain.get(last).multiply((durations[last] + millis) / (double) durations[last]);
    }

    private long[] calculateDurations(List<FlightPathNode> chain, Speed speed, RotationalSpeed rotationalSpeed) {
        long[] result = new long[chain.size()];
        for (int i = 0; i < chain.size(); i++) {
            result[i] = chain.get(i).calculateDuration(speed, rotationalSpeed);
        }
        return result;

    }

}
