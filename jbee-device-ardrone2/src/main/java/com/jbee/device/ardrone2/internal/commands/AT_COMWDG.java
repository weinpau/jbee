package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_COMWDG extends AT_Command {

    @Override
    public String getId() {
        return "COMWDG";
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

}
