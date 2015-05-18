package com.jbee.device.ardrone2.internal.controllers;

import com.jbee.BeeState;
import com.jbee.ControlStateMachine;
import com.jbee.RotationDirection;
import com.jbee.buses.BeeStateBus;
import com.jbee.buses.PrincipalAxesBus;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.concurrent.CallbackWrapper;
import com.jbee.device.ardrone2.internal.commands.AT_CommandSender;
import com.jbee.device.ardrone2.internal.commands.AT_PCMD;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import static java.lang.Math.PI;
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
            openLoop.control(command, state);
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

        static final double EPSILON_HORIZONTAL = .1;
        static final double EPSILON_ALTITUDE = .1;
        static final double EPSILON_YAW = 5.0 / 180.0 * PI;

        static final int REQUIRED_ACCEPTABLE_COUNT = 2;

        PID xPID = new PID(.5, 0, .35);
        PID yPID = new PID(.5, 0, .35);
        PID zPID = new PID(.8, 0, .35);
        PID yawPID = new PID(1, 0, .3);

        double targetX, targetY, targetZ, targetYAW;
        int acceptableCount = 0;
        RotationDirection rotationDirection = RotationDirection.CLOCKWISE;

        @Override
        protected void handle() {
            beeStateBus.subscripe(state -> {
                double yaw = state.getPrincipalAxes().getYaw().toRadians();

                double eX = targetX - state.getPosition().getX();
                double eY = targetY - state.getPosition().getY();
                double eZ = targetZ - state.getPosition().getZ();
                double eYaw = yaw - targetYAW;

                if (acceptable(eX, eY, eZ, eYaw)) {

                    if (++acceptableCount >= REQUIRED_ACCEPTABLE_COUNT) {
                        submit(CommandResult.COMPLETED);
                    }
                }

                double rX = xPID.getRawCommand(eX);
                double rY = yPID.getRawCommand(eY);

                double dX = deflection(Math.cos(yaw) * rX + Math.sin(yaw) * rY);
                double dY = deflection(-Math.sin(yaw) * rX + Math.cos(yaw) * rY);
                double dZ = deflection(zPID.getRawCommand(eZ));
                double dYaw = deflection(yawPID.getRawCommand(eYaw));

                try {
                    commandSender.send(new AT_PCMD(false, (float) dX, (float) dY, (float) dZ, (float) dYaw));
                } catch (InterruptedException e) {
                }

            });
        }

        boolean acceptable(double eX, double eY, double eZ, double eYaw) {
            return Math.abs(eX) < EPSILON_HORIZONTAL
                    && Math.abs(eY) < EPSILON_HORIZONTAL
                    && Math.abs(eZ) < EPSILON_ALTITUDE
                    && Math.abs(eYaw) < EPSILON_YAW;
        }

        public void control(FlyCommand command, BeeState state) {
            reset();
            configTarget(state, command);
        }

        void configTarget(BeeState state, FlyCommand command) {
            Angle initialYAW = state.getPrincipalAxes().getYaw();
            Position targetPosition = command.calculateTargetPosition(state.getPosition(), initialYAW);
            targetX = targetPosition.getX();
            targetY = targetPosition.getY();
            targetZ = targetPosition.getZ();

            Angle deltaYAW = command.calculateDeltaYAW(initialYAW);
            targetYAW = initialYAW.add(deltaYAW).normalize().abs().toRadians();
            rotationDirection = deltaYAW.isNegative() ? RotationDirection.COUNTERCLOCKWISE : RotationDirection.CLOCKWISE;
        }

        void reset() {
            xPID.reset();
            yPID.reset();
            zPID.reset();
            yawPID.reset();
            acceptableCount = 0;
        }

        double deflection(double value) {
            return Math.max(MIN_DEFLECTION, Math.min(MAX_DEFLECTION, value));
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

        public double getRawCommand(double error) {

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
