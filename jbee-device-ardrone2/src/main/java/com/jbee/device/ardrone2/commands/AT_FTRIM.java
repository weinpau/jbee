package com.jbee.device.ardrone2.commands;

/**
 *
 * @author weinpau
 */
public class AT_FTRIM extends AT_Command {

    @Override
    public String getId() {
        return "FTRIM";
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

}
