package com.jbee.device.pixhawk.controller;

import com.jbee.device.pixhawk.controller.helper.Vector3D;
import com.jbee.RotationDirection;
import com.jbee.commands.CommandResult;
import com.jbee.commands.FlyCommand;
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
    private Integer state = 0;
    private final Boolean ready = true;
    private CommandResult result;
    
    Angle angle;
    Position position;
    RotationDirection rotationDirection;
    Speed speed;
    boolean realtivePosition;
    boolean absoluteRotation;
    RotationalSpeed rotationalSpeed;
    
    private boolean cancle= false;
    
    public FlyController(PixhawkController pixhawk) {
        this.pixhawk = pixhawk;
    }
    
    private boolean inRange(CSD csd,double range){
        return csd.current < csd.desired + range && csd.current > csd.desired - range;
    }

    public CommandResult execute(FlyCommand flyCommand) {
        
        position = flyCommand.getPosition();
        angle = flyCommand.getAngle();
        rotationDirection = flyCommand.getRotationDirection();
        rotationalSpeed = flyCommand.getRotationalSpeed();
        speed = flyCommand.getSpeed();
        realtivePosition = flyCommand.isRealtivePosition();
        absoluteRotation = !flyCommand.isRealtiveRotation();
        
        cancle= false;
        double dAngle;
        double yawSpeedFaktor;
        double yawSpeed = rotationalSpeed.toAngularSpeed().toRadiansPerSecond();
        PID yawPID = new PID(1, 0.0, 0.0);
        
        CSD X = new CSD(pixhawk.getLocalPosition().x);
        CSD Y = new CSD(pixhawk.getLocalPosition().y);
        CSD Z = new CSD(pixhawk.getLocalPosition().z);
        CSD Yaw = new CSD(pixhawk.getAttitude().yaw);

        if(realtivePosition){
            Vector3D vector = new Vector3D((float)position.getX(), (float)position.getY(), (float)position.getZ());
            vector.rotate2D((float)-Yaw.current);
            
            X.desired += vector.y;
            Y.desired += vector.x;
            Z.desired += vector.z;
        }
        else{
            X.desired = position.getY();
            Y.desired = position.getX();
            Z.desired = position.getZ();
        }

         if(absoluteRotation){
            Yaw.desired = angle.normalize().toRadians();
            yawSpeed = 0;
         }else {
            if(!angle.isZero()){
                Yaw.desired = angle.toRadians();
                if(rotationDirection == RotationDirection.COUNTERCLOCKWISE){
                    Yaw.desired *= -1;
                    yawSpeed *= -1;
                    angle.multiply(-1);
                }
                Yaw.current = 0;
            }
            else absoluteRotation = true;
         }
        double oldYaw,newYaw = Yaw.start + Math.PI;      
        
        while(!cancle){
            
            //Main Loop
            X.current = pixhawk.getLocalPosition().x;
            Y.current = pixhawk.getLocalPosition().y;
            Z.current = pixhawk.getLocalPosition().z;
            
            if(absoluteRotation){
                Yaw.current = pixhawk.getAttitude().yaw;
            }
            else if(!angle.isZero()){
                oldYaw = newYaw;
                newYaw = pixhawk.getAttitude().yaw + Math.PI;
                dAngle = newYaw - oldYaw;
                if(dAngle > 5){
                    dAngle -= (Math.PI * 2.0);
                }
                if(dAngle < -5){
                    dAngle += (Math.PI * 2.0);
                }
                Yaw.current += dAngle;
                
                yawSpeedFaktor = yawPID.getRawCommand(-(Yaw.current - Yaw.desired)/rotationalSpeed.toAngularSpeed().toRadiansPerSecond());
                if(yawSpeedFaktor < -1) yawSpeedFaktor = -1;
                if(yawSpeedFaktor > 1) yawSpeedFaktor = 1;
                yawSpeed = rotationalSpeed.toAngularSpeed().toRadiansPerSecond() * yawSpeedFaktor;
                
            }

                        
            //Send Command
            pixhawk.setPositionTargetLocal(X.desired , Y.desired, -Z.desired,
                speed.mps(),speed.mps(),speed.mps(),
                Yaw.desired,yawSpeed,absoluteRotation);
            
            //Check Target == Current
            if(     inRange(X, 1.0) && 
                    inRange(Y, 1.0) &&
                    inRange(Z, 1.0)&&
                    inRange(Yaw, Math.PI / 180))
                    {                
                return CommandResult.COMPLETED;
            }
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(FlyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return CommandResult.CANCELLED;
    }

  

    @Override
    public void onCanle() {
        cancle = true;
    }

    class CSD{
        public double current;
        public double start;
        public double desired;

        public CSD() {
        }
        
        public CSD(double init){
            current = init;
            start = init;
            desired = init;
        }
        
        public CSD(CSD copy) {
            this.current = copy.current;
            this.start = copy.start;
            this.desired = copy.desired;
        }
    }
}
