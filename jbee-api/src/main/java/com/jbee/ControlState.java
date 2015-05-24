package com.jbee;

/**
 *
 * @author weinpau
 */
public enum ControlState {

    DISCONNECTED,
    READY_FOR_TAKE_OFF,
    TAKING_OFF,
    FLYING,
    CRITICAL,  /* System is in a non-normal flight mode. It can however still navigate. | */
    EMERGENCY, /* System is in a non-normal flight mode. It lost control over parts or over the whole airframe. It is in mayday and going down. | */
    LANDING

}
