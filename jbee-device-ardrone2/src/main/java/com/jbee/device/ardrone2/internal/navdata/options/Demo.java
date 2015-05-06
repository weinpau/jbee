package com.jbee.device.ardrone2.internal.navdata.options;

import java.nio.ByteBuffer;

/**
 *
 * @author weinpau
 */
public class Demo implements Option {

    ControlState controlState;
    FlyState flyState;
    int batteryPercentage;
    float pitch, roll, yaw; //[degree]
    int altitude; //[mm]
    float speedX, speedY, speedZ; //[mm/s]
    int frameIndex;
    float[][] detectionCameraRotation;
    float[] detectionCameraTranslation;
    int detectionTagIndex;
    int detectionCameraType;
    float[][] droneCameraRotation;
    float[] droneCameraTranslation;

    @Override
    public OptionId getId() {
        return OptionId.DEMO;
    }

    public ControlState getControlState() {
        return controlState;
    }

    public FlyState getFlyState() {
        return flyState;
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

    public int getFrameIndex() {
        return frameIndex;
    }

    public float[][] getDetectionCameraRotation() {
        return detectionCameraRotation;
    }

    public float[] getDetectionCameraTranslation() {
        return detectionCameraTranslation;
    }

    public int getDetectionTagIndex() {
        return detectionTagIndex;
    }

    public int getDetectionCameraType() {
        return detectionCameraType;
    }

    public float[][] getDroneCameraRotation() {
        return droneCameraRotation;
    }

    public float[] getDroneCameraTranslation() {
        return droneCameraTranslation;
    }

    
    
    @Override
    public void parse(ByteBuffer buffer) {

        flyState = FlyState.values()[buffer.getShort()];
        controlState = ControlState.values()[buffer.getShort()];
        batteryPercentage = buffer.getInt();
        pitch = buffer.getFloat() / 1000;
        roll = buffer.getFloat() / 1000;
        yaw = buffer.getFloat() / 1000;
        altitude = buffer.getInt();
        speedX = buffer.getFloat();
        speedY = buffer.getFloat();
        speedZ = buffer.getFloat();
        frameIndex = buffer.getInt();
        detectionCameraRotation = matrix(buffer);
        detectionCameraTranslation = vector(buffer);
        detectionTagIndex = buffer.getInt();
        detectionCameraType = buffer.getInt();
        droneCameraRotation = matrix(buffer);
        droneCameraTranslation = vector(buffer);

    }

    private float[] vector(ByteBuffer buffer) {
        float[] vector = new float[3];
        vector[0] = buffer.getFloat();
        vector[1] = buffer.getFloat();
        vector[2] = buffer.getFloat();
        return vector;
    }

    private float[][] matrix(ByteBuffer buffer) {
        float[][] matrix = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = buffer.getFloat();
            }
        }
        return matrix;
    }
}
