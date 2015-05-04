package com.jbee.device.ardrone2.internal.commands;

/**
 *
 * @author weinpau
 */
public class AT_PCMD extends AT_Command {

    public static final AT_Command HOVER = new AT_PCMD(false, 0, 0, 0, 0);

    private final boolean combinedYawEnable;
    private final float leftRightTilt;
    private final float frontBackTilt;
    private final float verticalSpeed;
    private final float angularSpeed;

    public AT_PCMD(boolean combinedYawEnable, float leftRightTilt, float frontBackTilt, float verticalSpeed, float angularSpeed) {
        this.combinedYawEnable = combinedYawEnable;
        this.leftRightTilt = leftRightTilt;
        this.frontBackTilt = frontBackTilt;
        this.verticalSpeed = verticalSpeed;
        this.angularSpeed = angularSpeed;
    }

    @Override
    public String getId() {
        return "PCMD";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{
            combinedYawEnable ? 1 : 0,
            leftRightTilt,
            frontBackTilt,
            verticalSpeed,
            angularSpeed
        };
    }

}
