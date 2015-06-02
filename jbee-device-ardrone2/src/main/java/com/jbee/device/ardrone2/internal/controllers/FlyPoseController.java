package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.commands.CommandResult;
import com.jbee.device.ardrone2.commands.FlyPose;
import com.jbee.device.ardrone2.internal.commands.AT_ANIM;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;

/**
 *
 * @author weinpau
 */
public class FlyPoseController implements CommandController<FlyPose> {

       
    AT_CommandSender commandSender;

    public FlyPoseController(AT_CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public CommandResult execute(FlyPose command) {
        try {
            int animation = command.getAnimation().ordinal();
            int duration = StrictMath.toIntExact(command.getDuration().toMillis());
            commandSender.send(new AT_ANIM(animation, duration));
            Thread.sleep(command.getDuration().toMillis());
            return CommandResult.COMPLETED;
        } catch (Exception e) {
            return CommandResult.FAILED;
        }

    }

}
