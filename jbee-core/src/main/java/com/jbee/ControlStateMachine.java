package com.jbee;

/**
 *
 * @author weinpau
 */
public class ControlStateMachine {

    ControlState controlState;

    ControlStateMachine(ControlState controlState) {
        this.controlState = controlState;
    }

    public ControlState getControlState() {
        return controlState;
    }

    public synchronized boolean changeState(ControlState state) {
        if (checkTransition(state)) {
            controlState = state;
            return true;
        }
        return false;

    }

    public void changeStateForced(ControlState state) {
        controlState = state;
    }

    boolean checkTransition(ControlState state) {
        if (state == controlState) {
            return true;
        }

        switch (state) {
            case DISCONNECTED:
                return permitFrom(ControlState.FLYING, ControlState.LANDING, ControlState.READY_FOR_TAKE_OFF, ControlState.TAKING_OFF);
            case FLYING:
                return permitFrom(ControlState.TAKING_OFF);
            case LANDING:
                return permitFrom(ControlState.FLYING);
            case READY_FOR_TAKE_OFF:
                return permitFrom(ControlState.DISCONNECTED, ControlState.LANDING);
            case TAKING_OFF:
                return permitFrom(ControlState.READY_FOR_TAKE_OFF);

        }

        return false;

    }

    boolean permitFrom(ControlState... state) {
        for (ControlState s : state) {
            if (s == this.controlState) {
                return true;
            }
        }
        return false;
    }

    public static ControlStateMachine init(ControlState controlState) {
        return new ControlStateMachine(controlState);
    }

}
