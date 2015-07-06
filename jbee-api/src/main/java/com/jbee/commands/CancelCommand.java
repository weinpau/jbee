package com.jbee.commands;

/**
 * Representation of the cancel command.
 *
 * @author weinpau
 */
public class CancelCommand extends AbstractCommand {

    private final int cancelCommandNumber;

    /**
     * Creates a new cancel command for any current command.
     */
    public CancelCommand() {
        cancelCommandNumber = -1;
    }

    /**
     * Creates a new cancel command for the specified command number.
     *
     * @param cancelCommandNumber the command number of the command that is to
     * be canceled.
     */
    public CancelCommand(int cancelCommandNumber) {
        this.cancelCommandNumber = cancelCommandNumber;
    }

    /**
     * Returns the command number of the command that is to be canceled. If
     * {@code -1}, any current command is canceled.
     *
     * @return the command number of the command that is to be canceled.
     */
    public int getCancelCommandNumber() {
        return cancelCommandNumber;
    }

}
