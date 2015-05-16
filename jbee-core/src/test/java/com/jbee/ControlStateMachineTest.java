package com.jbee;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class ControlStateMachineTest {

    @Test
    public void testGetControlState() {
        ControlStateMachine instance = ControlStateMachine.init(ControlState.FLYING);
        ControlState expResult = ControlState.FLYING;
        ControlState result = instance.getControlState();
        assertEquals(expResult, result);
    }

    @Test
    public void testChangeState() {
        ControlStateMachine instance = ControlStateMachine.init(ControlState.DISCONNECTED);
        instance.changeState(ControlState.READY_FOR_TAKE_OFF);
        assertEquals(ControlState.READY_FOR_TAKE_OFF, instance.getControlState());
    }

    @Test
    public void testChangeState_DISCONNECTED() {
        assertTrue(ControlStateMachine.init(ControlState.DISCONNECTED).changeState(ControlState.DISCONNECTED));
        assertTrue(ControlStateMachine.init(ControlState.DISCONNECTED).changeState(ControlState.READY_FOR_TAKE_OFF));
        assertFalse(ControlStateMachine.init(ControlState.DISCONNECTED).changeState(ControlState.FLYING));
        assertFalse(ControlStateMachine.init(ControlState.DISCONNECTED).changeState(ControlState.LANDING));
        assertFalse(ControlStateMachine.init(ControlState.DISCONNECTED).changeState(ControlState.TAKING_OFF));
    }

    @Test
    public void testChangeState_READY_FOR_TAKE_OFF() {
        assertTrue(ControlStateMachine.init(ControlState.READY_FOR_TAKE_OFF).changeState(ControlState.DISCONNECTED));
        assertTrue(ControlStateMachine.init(ControlState.READY_FOR_TAKE_OFF).changeState(ControlState.READY_FOR_TAKE_OFF));
        assertFalse(ControlStateMachine.init(ControlState.READY_FOR_TAKE_OFF).changeState(ControlState.FLYING));
        assertFalse(ControlStateMachine.init(ControlState.READY_FOR_TAKE_OFF).changeState(ControlState.LANDING));
        assertTrue(ControlStateMachine.init(ControlState.READY_FOR_TAKE_OFF).changeState(ControlState.TAKING_OFF));
    }

    @Test
    public void testChangeState_FLYING() {
        assertTrue(ControlStateMachine.init(ControlState.FLYING).changeState(ControlState.DISCONNECTED));
        assertFalse(ControlStateMachine.init(ControlState.FLYING).changeState(ControlState.READY_FOR_TAKE_OFF));
        assertTrue(ControlStateMachine.init(ControlState.FLYING).changeState(ControlState.FLYING));
        assertTrue(ControlStateMachine.init(ControlState.FLYING).changeState(ControlState.LANDING));
        assertFalse(ControlStateMachine.init(ControlState.FLYING).changeState(ControlState.TAKING_OFF));
    }

    @Test
    public void testChangeState_LANDING() {
        assertTrue(ControlStateMachine.init(ControlState.LANDING).changeState(ControlState.DISCONNECTED));
        assertTrue(ControlStateMachine.init(ControlState.LANDING).changeState(ControlState.READY_FOR_TAKE_OFF));
        assertFalse(ControlStateMachine.init(ControlState.LANDING).changeState(ControlState.FLYING));
        assertTrue(ControlStateMachine.init(ControlState.LANDING).changeState(ControlState.LANDING));
        assertFalse(ControlStateMachine.init(ControlState.LANDING).changeState(ControlState.TAKING_OFF));
    }

    @Test
    public void testChangeState_TAKING_OFF() {
        assertTrue(ControlStateMachine.init(ControlState.TAKING_OFF).changeState(ControlState.DISCONNECTED));
        assertFalse(ControlStateMachine.init(ControlState.TAKING_OFF).changeState(ControlState.READY_FOR_TAKE_OFF));
        assertTrue(ControlStateMachine.init(ControlState.TAKING_OFF).changeState(ControlState.FLYING));
        assertFalse(ControlStateMachine.init(ControlState.TAKING_OFF).changeState(ControlState.LANDING));
        assertTrue(ControlStateMachine.init(ControlState.TAKING_OFF).changeState(ControlState.TAKING_OFF));
    }

}
