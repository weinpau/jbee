package com.jbee;

/**
 *
 * @author weinpau
 */
public class ControlStateMachine {

    volatile ControlState controlState;

    ControlStateMachine(ControlState controlState) {
        this.controlState = controlState;

    }

    public ControlState getControlState() {
        return controlState;
    }

    public boolean changeState(ControlState state) {
        if (checkTransition(state)) {
            controlState = state;
            return true;
        }
        return false;

    }

    boolean checkTransition(ControlState state) {
        if (state == controlState) {
            return true;
        }

        switch (state) {
            case DISCONNECTED:
                return permit(ControlState.FLYING, ControlState.LANDING, ControlState.READY_FOR_TAKE_OFF, ControlState.TAKING_OFF);
            case FLYING:
                return permit(ControlState.TAKING_OFF);
            case LANDING:
                return permit(ControlState.FLYING);
            case READY_FOR_TAKE_OFF:
                return permit(ControlState.DISCONNECTED, ControlState.LANDING);
            case TAKING_OFF:
                return permit(ControlState.READY_FOR_TAKE_OFF);

        }

        return false;

    }

    boolean permit(ControlState... state) {
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
