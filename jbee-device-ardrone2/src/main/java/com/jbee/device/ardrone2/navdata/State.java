package com.jbee.device.ardrone2.navdata;

/**
 *
 * @author weinpau
 */
public final class State {

    private final int state;

    State(int state) {
        this.state = state;
    }

    public boolean isFlying() {
        return (state & 1) != 0;
    }

    public boolean isVideoEnabled() {
        return (state & (1 << 1)) != 0;
    }

    public boolean isVisionEnabled() {
        return (state & (1 << 2)) != 0;
    }

    public ControlAlgorithm getControlAlgorithm() {
        return (state & (1 << 3)) != 0
                ? ControlAlgorithm.ANGULAR_SPEED_CONTROL
                : ControlAlgorithm.EULER_ANGELS_CONTROL;
    }

    public boolean isAltitudeControlActive() {
        return (state & (1 << 4)) != 0;
    }

    public boolean isUserFeedbackOn() {
        return (state & (1 << 5)) != 0;
    }

    public boolean isControlReceived() {
        return (state & (1 << 6)) != 0;
    }

    public boolean isTrimReceived() {
        return (state & (1 << 7)) != 0;
    }

    public boolean isTrimRunning() {
        return (state & (1 << 8)) != 0;
    }

    public boolean isTrimSucceeded() {
        return (state & (1 << 9)) != 0;
    }

    public boolean isNavDataDemoOnly() {
        return (state & (1 << 10)) != 0;
    }

    public boolean isNavDataBootstrap() {
        return (state & (1 << 11)) != 0;
    }

    public boolean isMotorsDown() {
        return (state & (1 << 12)) != 0;
    }

    public boolean isCommunicationLost() {
        return (state & (1 << 13)) != 0;
    }

    public boolean isGyrometersDown() {
        return (state & (1 << 14)) != 0;
    }

    public boolean isBatteryTooLow() {
        return (state & (1 << 15)) != 0;
    }

    public boolean isBatteryTooHigh() {
        return (state & (1 << 16)) != 0;
    }

    public boolean isTimerElapsed() {
        return (state & (1 << 17)) != 0;
    }

    public boolean isNotEnoughPower() {
        return (state & (1 << 18)) != 0;
    }

    public boolean isAngelsOutOufRange() {
        return (state & (1 << 19)) != 0;
    }

    public boolean isTooMuchWind() {
        return (state & (1 << 20)) != 0;
    }

    public boolean isUltrasonicSensorDeaf() {
        return (state & (1 << 21)) != 0;
    }

    public boolean isCutoutSystemDetected() {
        return (state & (1 << 22)) != 0;
    }

    public boolean isPICVersionNumberOK() {
        return (state & (1 << 23)) != 0;
    }

    public boolean isATCodedThreadOn() {
        return (state & (1 << 24)) != 0;
    }

    public boolean isNavDataThreadOn() {
        return (state & (1 << 25)) != 0;
    }

    public boolean isVideoThreadOn() {
        return (state & (1 << 26)) != 0;
    }

    public boolean isAcquisitionThreadOn() {
        return (state & (1 << 27)) != 0;
    }

    public boolean isControlWatchdogDelayed() {
        return (state & (1 << 28)) != 0;
    }

    public boolean isADCWatchdogDelayed() {
        return (state & (1 << 29)) != 0;
    }

    public boolean isCommunicationProblemOccurred() {
        return (state & (1 << 30)) != 0;
    }

    public boolean isEmergency() {
        return (state & (1 << 31)) != 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.state;
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
        return this.state == ((State) obj).state;
    }

}
