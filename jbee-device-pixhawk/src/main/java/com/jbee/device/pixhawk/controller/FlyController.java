package com.jbee.device.pixhawk.controller;

import com.MAVLink.common.msg_attitude;
import com.MAVLink.common.msg_local_position_ned;
import com.jbee.device.pixhawk.controller.helper.Vector3D;
import com.jbee.RotationDirection;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
import com.jbee.commands.FlyCommandBuilder;
import com.jbee.device.pixhawk.Pixhawk;
import com.jbee.device.pixhawk.controller.helper.PID;
import com.jbee.device.pixhawk.internal.PixhawkController;
import com.jbee.positioning.Position;
import com.jbee.units.Angle;
import com.jbee.units.RotationalSpeed;
import com.jbee.units.Speed;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erik Jähne
 */
public class FlyController extends BasicController{

    private final PixhawkController pixhawk;
    
    private Angle angle;
    private Position position;
    private RotationDirection rotationDirection;
    private Speed speed;
    private boolean realtivePosition;
    private boolean absoluteRotation;
    private RotationalSpeed rotationalSpeed;
    private FlyCommand.Mode mode;
    private PID yawPID = new PID(1, 0.0, 0.0);
    private double yawSpeed = 0;
    private double cumYaw = 0;
    private State state;
    private Vector3D localTravelDiv = new Vector3D(0.0,0.0,0.0);
    Vector3D directionVector;
    
    public FlyController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }
    
    private boolean inRange(CSD csd,double range){
        return inRange(csd.desired, csd.current,range);
    }

    private boolean inRange(double desired,double current,double range){
        return current < desired + range && current > desired - range;
    }
    
    public CommandResult execute(FlyCommand flyCommand) {
        
        position = flyCommand.getPosition();
        angle = flyCommand.getAngle();
        rotationDirection = flyCommand.getRotationDirection();
        rotationalSpeed = flyCommand.getRotationalSpeed();
        speed = flyCommand.getSpeed();
        realtivePosition = flyCommand.isRealtivePosition();
        absoluteRotation = !flyCommand.isRealtiveRotation();
        mode = flyCommand.getMode();
        
        if(speed == null){
            speed = Speed.mps(10);
        }
        if(rotationalSpeed == null){
            rotationalSpeed = RotationalSpeed.rps(0.25);
        }
        
        state = new State().withPixhawkValues();
        directionVector = new Vector3D(position.getY(), position.getX());
        
        yawPID.reset();
        cancle= false;
        yawSpeed = 0;

        //prepare an set desired Values
        boolean isFlyMode = mode == FlyCommand.Mode.FLY || mode == FlyCommand.Mode.FLY_AND_ROTATE;
        boolean isRotateMode = (mode == FlyCommand.Mode.ROTATE || mode == FlyCommand.Mode.FLY_AND_ROTATE || mode == FlyCommand.Mode.ROTATE_AND_FLY);
        
        if(isFlyMode) prepareFly();
        if(isRotateMode) prepareRotation();
        
        while(!cancle){
                 
            
            state.update(pixhawk.getLocalPosition(),pixhawk.getAttitude());

            if(isRotateMode){
                updateRotation();
            }
            if(mode == FlyCommand.Mode.ROTATE_AND_FLY){
                updateRelativeFly();
            }
                        
            //Send Command
            pixhawk.setPositionTargetLocal(state.x.desired , state.y.desired, state.z.desired,
                speed.mps(),speed.mps(),speed.mps(),
                state.yaw.desired,yawSpeed,absoluteRotation,false);
            
            //Check Target == Current
            if(     inRange(state.x, 0.1) && 
                    inRange(state.y, 0.1) &&
                    inRange(state.z, 0.1)&&
                    ( (inRange(state.yaw, Math.PI / 180) && absoluteRotation) || ( inRange(0,yawSpeed, 0.05) && !absoluteRotation)))
                    {                
                return CommandResult.COMPLETED;
            }
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(FlyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Cancled
        
        pixhawk.setPositionTargetLocal(state.x.current , state.y.current, state.z.current,
            speed.mps(),speed.mps(),speed.mps(),
            pixhawk.getAttitude().yaw,yawSpeed,true,false);
        
        return CommandResult.CANCELLED;
    }

    private void updateRelativeFly() {
        localTravelDiv.x = (state.x.current - state.x.lastStep);
        localTravelDiv.y = (state.y.current - state.y.lastStep);
        localTravelDiv.rotate2D(-state.yaw.lastStep);
        
        
        //directionVector.sub(localTravelDiv);
        Vector3D globalPos = new Vector3D(directionVector);
        globalPos.rotate2D(state.yaw.current);
        
        state.y.desired = state.y.current + globalPos.y * -yawSpeed;
        state.x.desired = state.x.current + globalPos.x * -yawSpeed;
    }

    private void updateRotation() {
        double dAngle = (state.yaw.lastStep - state.yaw.current);
        if(dAngle > 0.9 * (Math.PI * 2.0)){
            dAngle -= (Math.PI * 2.0);
        }
        if(dAngle < -0.9 * (Math.PI * 2.0)){
            dAngle += (Math.PI * 2.0);
        }

        cumYaw += dAngle;

        yawSpeed = rotationalSpeed.toAngularSpeed().toRadiansPerSecond();
        double error = (cumYaw - angle.toRadians())/yawSpeed;
        double yawSpeedFaktor = yawPID.getLimitedRawCommand(error,-1,1);
        yawSpeed *= yawSpeedFaktor;
    }
    
    private void prepareRotation() {
        if(absoluteRotation){
            state.yaw.desired = angle.normalize().toRadians();
            return;
        }
        //relative rotation
        if(angle.isZero()){
            state.yaw.desired = pixhawk.getAttitude().yaw;
            absoluteRotation = true;
        }
        
        cumYaw = 0;
        if(rotationDirection == RotationDirection.CW){
            angle = angle.multiply(-1);
        }
    }

    private void prepareFly() {
        if(realtivePosition){
            //rotate position Vector to current heading
            Vector3D vector = new Vector3D(position.getX(), position.getY(), 0.0);
            vector.rotate2D(-state.yaw.current);
            state.addDesired(vector.y, vector.x, position.getZ(), null);
        }
        else{
            state.setDesired(position.getY(), position.getX(), position.getZ(), null);
        }
    }

    class CSD{
        public double current;
        public double lastStep;
        public double start;
        public double desired;

        public CSD() {
        }
        
        public CSD(double init){
            current = init;
            start = init;
            desired = init;
            lastStep = init;
        }
        
        public CSD(CSD copy) {
            this.current = copy.current;
            this.start = copy.start;
            this.desired = copy.desired;
            this.lastStep = copy.lastStep;
        }
    }
    
    class State{
        public CSD x;
        public CSD y;
        public CSD z;
        public CSD yaw;

        public State(){
        }
        
        public State withPixhawkValues() {
            x = new CSD(pixhawk.getLocalPosition().x);
            y = new CSD(pixhawk.getLocalPosition().y);
            z = new CSD(-pixhawk.getLocalPosition().z);
            yaw = new CSD(pixhawk.getAttitude().yaw);
            return this;
        }
        
        public void setDesired(Double x,Double y,Double z,Double yaw){
            if(x != null) this.x.desired = x;
            if(y != null) this.y.desired = y;
            if(z != null) this.z.desired = z;
            if(yaw != null) this.yaw.desired = yaw;
        }
        
        public void addDesired(Double x,Double y,Double z,Double yaw){
            if(x != null) this.x.desired += x;
            if(y != null) this.y.desired += y;
            if(z != null) this.z.desired += z;
            if(yaw != null) this.yaw.desired += yaw;
        }
        
        public void setCurrent(Double x,Double y,Double z,Double yaw){
            if(x != null) this.x.current = x;
            if(y != null) this.y.current = y;
            if(z != null) this.z.current = z;
            if(yaw != null) this.yaw.current = yaw;
        }
        
        public void addCurrent(Double x,Double y,Double z,Double yaw){
            if(x != null) this.x.current += x;
            if(y != null) this.y.current += y;
            if(z != null) this.z.current += z;
            if(yaw != null) this.yaw.current += yaw;
        }
        
        public void update(msg_local_position_ned position, msg_attitude attitude){
            updateCurrentPosition(position);
            updateAttitude(attitude);
            
        }
        
        public void updateAttitude(msg_attitude attitude){
            yaw.lastStep = yaw.current;
            yaw.current = attitude.yaw;
        }
        
        public void updateCurrentPosition(msg_local_position_ned position){
            x.lastStep = x.current;
            y.lastStep = y.current;
            z.lastStep = z.current;
            
            x.current = position.x;
            y.current = position.y;
            z.current = -position.z;
        }
    }
}
