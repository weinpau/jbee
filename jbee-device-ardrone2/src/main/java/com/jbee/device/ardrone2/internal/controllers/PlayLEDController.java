package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.commands.CommandResult;
import com.jbee.device.ardrone2.commands.PlayLEDAnimation;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_LED;

/**
 *
 * @author weinpau
 */
public class PlayLEDController implements CommandController<PlayLEDAnimation> {

    AT_CommandSender commandSender;

    public PlayLEDController(AT_CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public CommandResult execute(PlayLEDAnimation command) {
        try {
            int animation = command.getAnimation().ordinal();
            float frequency = command.getFrequency().toMilliHz() / 1000f;
            int duration = StrictMath.toIntExact(command.getDuration().getSeconds());
            commandSender.send(new AT_LED(animation, frequency, duration));
            return CommandResult.COMPLETED;
        } catch (Exception e) {
            return CommandResult.FAILED;
        }

    }

}
