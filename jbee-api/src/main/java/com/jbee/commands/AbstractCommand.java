package com.jbee.commands;

import com.jbee.BeeControl;

/**
 * Abstract implementation of a command
 *
 * @author weinpau
 */
public class AbstractCommand implements Command {

    private int commandNumber;
    private BeeControl control;

    @Override
    public void init(int commandNumber, BeeControl executiveControl) {
        this.commandNumber = commandNumber;
        this.control = executiveControl;
    }

    @Override
    public int getCommandNumber() {
        return commandNumber;
    }

    @Override
    public boolean cancel() {
        if (control == null) {
            return false;
        } else {
            return control.execute(new CancelCommand(commandNumber)) == CommandResult.COMPLETED;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.commandNumber;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractCommand other = (AbstractCommand) obj;
        return this.commandNumber == other.commandNumber;
    }

}
