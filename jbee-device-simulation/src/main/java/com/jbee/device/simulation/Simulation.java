package com.jbee.device.simulation;

import com.jbee.TargetDevice;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyToCommand;
import com.jbee.commands.HoverCommand;
import com.jbee.commands.LandCommand;
import com.jbee.commands.RotationCommand;
import com.jbee.commands.TakeOffCommand;

/**
 *
 * @author weinpau
 */
public class Simulation implements TargetDevice {

    @Override
    public String getId() {
        return "simulation";
    }

    @Override
    public FlyCommand getFlyCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FlyToCommand getFlyToCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HoverCommand getHoverCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LandCommand getLandCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RotationCommand getRotationCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TakeOffCommand getTakeOffCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
