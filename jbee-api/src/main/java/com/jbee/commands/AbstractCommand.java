package com.jbee.commands;

/**
 *
 * @author weinpau
 */
public class AbstractCommand implements Command {

    private int commandNumber;

    @Override
    public void init(int commandNumber) {
        this.commandNumber = commandNumber;
    }

    @Override
    public int getCommandNumber() {
        return commandNumber;
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
