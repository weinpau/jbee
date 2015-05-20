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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
    LoopControl loopControl = new OpenLoopControl();

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
        long timeout = calculateTimeout(command, state);
        loopControl.control(command, state);
        Future<CommandResult> task = commandExecutorService.submit(loopControl);
        try {

            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {

            return CommandResult.FAILED;
        } finally {
            task.cancel(true);
            loopControl.stop();
        }

    }

    long calculateTimeout(FlyCommand command, BeeState state) {
        long timeout = 100 + command.calculateDuration(
                state.getPosition(),
                state.getPrincipalAxes().getYaw()).toMillis();
        return timeout;
    }

    interface LoopControl extends Callable<CommandResult> {

        void control(FlyCommand command, BeeState state);

        void stop();

    }

    class OpenLoopControl extends CallbackWrapper<CommandResult> implements LoopControl {

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

        long lastTime;

        double targetX, targetY, targetZ, targetYAW;
        int acceptableCount = 0;
        RotationDirection rotationDirection = RotationDirection.CLOCKWISE;

        boolean enable = false;

        @Override
        protected void handle() {
            beeStateBus.subscripe(state -> {
                if (!enable) {
                    return;
                }
                long time = System.currentTimeMillis();
                if (time - lastTime <= 1) {
                    return;
                } else {
                    lastTime = time;
                }

                double yaw = state.getPrincipalAxes().getYaw().toRadians();

                double eX = targetX - state.getPosition().getX();
                double eY = targetY - state.getPosition().getY();
                double eZ = targetZ - state.getPosition().getZ();
                double eYaw = yaw - targetYAW;

                System.out.println("yaw: " + state.getPrincipalAxes().getYaw() + ", pos: " + state.getPosition());

                if (acceptable(eX, eY, eZ, eYaw)) {

                    if (++acceptableCount >= REQUIRED_ACCEPTABLE_COUNT) {
                        submit(CommandResult.COMPLETED);
                    }
                }

                double rX = xPID.getRawCommand(eX);
                double rY = yPID.getRawCommand(eY);
                double rZ = zPID.getRawCommand(eZ);
                double rYaw = yawPID.getRawCommand(eYaw);

                double dX = deflection(rX);
                double dY = deflection(rY);
                double dZ = deflection(rZ);
                double dYaw = deflection(rYaw);

                System.out.println(String.format("ex: %f, ey: %f, ez: %f, eyaw: %f", eX, eY, eZ, eYaw));
                System.out.println(String.format("rx: %f, ry: %f, rz: %f, ryaw: %f", rX, rY, rZ, rYaw));
                System.out.println(String.format("dx: %f, dy: %f, dz: %f, dyaw: %f", dX, dY, dZ, dYaw));
              
                try {
                    commandSender.send(new AT_PCMD(false, (float) dX, (float) dY, (float) dZ, (float) dYaw));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }

        boolean acceptable(double eX, double eY, double eZ, double eYaw) {
            return Math.abs(eX) < EPSILON_HORIZONTAL
                    && Math.abs(eY) < EPSILON_HORIZONTAL
                    && Math.abs(eZ) < EPSILON_ALTITUDE
                    && Math.abs(eYaw) < EPSILON_YAW;
        }

        @Override
        public void control(FlyCommand command, BeeState state) {
            reset();
            configTarget(state, command);
            enable = true;
        }

        @Override
        public void stop() {
            enable = false;
        }

        void configTarget(BeeState state, FlyCommand command) {
            Angle initialYAW = state.getPrincipalAxes().getYaw();
            Position targetPosition = command.calculateTargetPosition(state.getPosition(), initialYAW);

            System.out.println("currentPosition: " + state.getPosition());
            System.out.println("targetPosition: " + targetPosition);

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

}
