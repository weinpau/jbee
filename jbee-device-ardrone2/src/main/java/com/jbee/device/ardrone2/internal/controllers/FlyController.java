package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.BeeState;
import com.jbee.ControlStateMachine;
import com.jbee.buses.BeeStateBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.concurrent.CallbackWrapper;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author weinpau
 */
public class FlyController implements CommandController<FlyCommand> {

    AT_CommandSender commandSender;
    BeeStateBus beeStateBus;
    PrincipalAxesBus principalAxesBus;
    ControlStateMachine controlStateMachine;
    ExecutorService commandExecutorService;
    OpenLoopControl openLoop = new OpenLoopControl();

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
        if (controlStateMachine.getControlState() != com.jbee.ControlState.FLYING) {
            return CommandResult.NOT_EXECUTED;
        }

        BeeState state = beeStateBus.getLastKnownValue().orElse(BeeState.START_STATE);

        try {
            long timeout = calculateTimeout(command, state);
            openLoop.control(command);
            return commandExecutorService.submit(openLoop).get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            return CommandResult.FAILED;

        }

    }

    long calculateTimeout(FlyCommand command, BeeState state) {
        long timeout = 10 + command.calculateDuration(
                state.getPosition(),
                state.getPrincipalAxes().getYaw()).toMillis();
        return timeout * 2;
    }

    class OpenLoopControl extends CallbackWrapper<CommandResult> {

        static final double MIN_DEFLECTION = -1;
        static final double MAX_DEFLECTION = 1;

        PID xPID = new PID(.5, 0, .35);
        PID yPID = new PID(.5, 0, .35);
        PID zPID = new PID(.8, 0, .35);
        PID yawPID = new PID(1, 0, .3);

        @Override
        protected void handle() {
            beeStateBus.subscripe(state -> {    

               
                
            });
        }

        public void control(FlyCommand command) {
            reset();
        }

        void reset() {
            xPID.reset();
            yPID.reset();
            zPID.reset();
            yawPID.reset();
        }

    };

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
