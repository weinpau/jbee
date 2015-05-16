package com.jbee.device.ardrone2.internal.navdata.options;

import java.nio.ByteBuffer;

/**
 *
 * @author weinpau
 */
public class Demo implements Option {

    ControlState controlState;
    int batteryPercentage;
    float pitch, roll, yaw; //[degree]
    int altitude; //[mm]
    float speedX, speedY, speedZ; //[mm/s]

    @Override
    public OptionId getId() {
        return OptionId.DEMO;
    }

    public ControlState getControlState() {
        return controlState;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float getYaw() {
        return yaw;
    }

    public int getAltitude() {
        return altitude;
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    @Override
    public void parse(ByteBuffer buffer) {

        controlState = ControlState.values()[buffer.getInt() >> 16];
        batteryPercentage = buffer.getInt();
        pitch = buffer.getFloat() / 1000f;
        roll = buffer.getFloat() / 1000f;
        yaw = buffer.getFloat() / 1000f;
        altitude = buffer.getInt();
        speedX = buffer.getFloat();
        speedY = buffer.getFloat();
        speedZ = buffer.getFloat();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("BeeState{").append("\n");
        stringBuilder.append("\t").append("controlState=").append(controlState).append("\n");
        stringBuilder.append("\t").append("batteryPercentage=").append(batteryPercentage).append("\n");
        stringBuilder.append("\t").append("pitch=").append(pitch).append("\n");
        stringBuilder.append("\t").append("roll=").append(roll).append("\n");
        stringBuilder.append("\t").append("yaw=").append(yaw).append("\n");
        stringBuilder.append("\t").append("altitude=").append(altitude).append("\n");
        stringBuilder.append("\t").append("speedX=").append(speedX).append("\n");
        stringBuilder.append("\t").append("speedY=").append(speedY).append("\n");
        stringBuilder.append("\t").append("speedZ=").append(speedZ).append("\n");
        stringBuilder.append("}");

        return stringBuilder.toString();

    }

}
