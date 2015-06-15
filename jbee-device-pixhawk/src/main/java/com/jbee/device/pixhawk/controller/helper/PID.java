/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller.helper;

/**
 *
 * @author Trader
 */
public class PID {

        private double kp, ki, kd;

        private long lastNanoTime;
        private double lastError, errorSum;

        public PID(double kp, double ki, double kd) {
            config(kp, ki, kd);
            reset();
        }

        public final void config(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
        }

        public final void reset() {
            lastNanoTime = 0;
            lastError = Double.POSITIVE_INFINITY;
            errorSum = 0;
        }

        public double getLimitedRawCommand(double error,double lowerLimit,double upperLimit) {
            double result = getRawCommand(error);
            if(result < lowerLimit) result = lowerLimit;
            if(result > upperLimit) result = upperLimit;
           return result;
        }
        
        public double getRawCommand(double error) {

            long nanoTime = System.nanoTime();
            double dt = (nanoTime - lastNanoTime) / 10e9d;
            double de = 0;
            if (lastNanoTime != 0) {
                if (lastError < Double.POSITIVE_INFINITY) {
                    de = (error - lastError) / dt;
                }
                errorSum += error * dt;
            }
            lastNanoTime = nanoTime;
            lastError = error;
            return kp * error + ki * errorSum + kd * de;
        }
    }