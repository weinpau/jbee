package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.ControlStateMachine;
import com.jbee.buses.BeeStateBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author weinpau
 */
public class FlyController implements CommandController<FlyCommand> {

    static final double MIN_DEFLECTION = -1;
    static final double MAX_DEFLECTION = 1;

    AT_CommandSender commandSender;
    BeeStateBus beeStateBus;
    PrincipalAxesBus principalAxesBus;
    ControlStateMachine controlStateMachine;
    ExecutorService commandExecutorService;

    PID xPID = new PID(.5, 0, .35);
    PID yPID = new PID(.5, 0, .35);
    PID zPID = new PID(.8, 0, .35);
    PID yawPID = new PID(1, 0, .3);

    public FlyController(AT_CommandSender commandSender,
            BeeStateBus beeStateBus,
            ControlStateMachine controlStateMachine,
            ExecutorService commandExecutorService) {
        this.commandSender = commandSender;
        this.beeStateBus = beeStateBus;
        this.controlStateMachine = controlStateMachine;
        this.commandExecutorService = commandExecutorService;
    }

    @Override
    public CommandResult execute(FlyCommand command) {
        return CommandResult.NOT_EXECUTED;
    }

   
    static class PID {

        private final double kp, ki, kd;

        private long lastTime;
        private double lastError, errorSum;

        public PID(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
            reset();
        }

        public final void reset() {
            lastTime = 0;
            lastError = Double.POSITIVE_INFINITY;
            errorSum = 0;
        }

        public double getDeflection(double error) {

            long time = System.nanoTime();
            double dt = (time - lastTime) / 10e9d;
            double de = 0;
            if (lastTime != 0) {
                if (lastError < Double.POSITIVE_INFINITY) {
                    de = (error - lastError) / dt;
                }
                errorSum += error * dt;
            }
            lastTime = time;
            lastError = error;
            return kp * error + ki * errorSum + kd * de;
        }
    }

}
