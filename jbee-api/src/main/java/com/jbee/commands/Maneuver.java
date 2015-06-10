package com.jbee.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author weinpau
 */
public class Maneuver extends AbstractCommand {

    private final List<Command> commands = new ArrayList<>();
    
    
    Maneuver(List<Command> commands) {
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

   
    public static Maneuver of(Command... command) {
        return new Maneuver(Arrays.asList(command));
        
    }
    
}
