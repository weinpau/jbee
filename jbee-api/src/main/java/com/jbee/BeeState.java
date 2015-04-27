package com.jbee;

import com.jbee.positioning.Position;
import com.jbee.units.Velocity;
import com.jbee.units.YAW;
import java.util.Objects;

/**
 *
 * @author weinpau
 */
public interface BeeState {

    long getTimestamp();

    Position getPosition();

    Velocity getVelocity();

    YAW getYAW();

    static final BeeState START_STATE = new BeeState() {

        @Override
        public long getTimestamp() {
            return 0;
        }

        @Override
        public Position getPosition() {
            return Position.ORIGIN;
        }

        @Override
        public Velocity getVelocity() {
            return Velocity.ZERO;
        }

        @Override
        public YAW getYAW() {
            return YAW.ZERO;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + (int) (this.getTimestamp() ^ (this.getTimestamp() >>> 32));
            hash = 89 * hash + Objects.hashCode(this.getPosition());
            hash = 89 * hash + Objects.hashCode(this.getVelocity());
            hash = 89 * hash + Objects.hashCode(this.getYAW());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof BeeState)) {
                return false;
            }
            final BeeState other = (BeeState) obj;
            if (this.getTimestamp() != other.getTimestamp()) {
                return false;
            }
            if (!Objects.equals(this.getPosition(), other.getPosition())) {
                return false;
            }
            if (!Objects.equals(this.getVelocity(), other.getVelocity())) {
                return false;
            }
            if (Objects.equals(this.getYAW(), other.getYAW())) {
            } else {
                return false;
            }
            return true;
        }

    };

}
