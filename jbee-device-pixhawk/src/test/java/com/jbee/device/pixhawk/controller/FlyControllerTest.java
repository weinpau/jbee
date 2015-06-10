/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import com.MAVLink.common.msg_local_position_ned;
import com.jbee.RotationDirection;
import com.jbee.commands.Commands;
import com.jbee.commands.FlyCommand;
import com.jbee.device.pixhawk.controller.helper.Vector3D;
import com.jbee.units.Angle;
import com.jbee.units.Distance;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Trader
 */
public class FlyControllerTest extends ControllerTestHelper{
    
    public FlyControllerTest() {
        super("FlyControllerTest");
    }

    @Ignore
    @Test
    public void testMovement(){
        float yawStart = pixhawk.getAttitude().yaw;
        
        //Simple Movement
        testMovement("Forward", Commands.forward(Distance.ofMeters(2)).build(),yawStart);
        testMovement("Backward", Commands.backward(Distance.ofMeters(2)).build(),yawStart);

        testMovement("Right", Commands.right(Distance.ofMeters(2)).build(),yawStart);
        testMovement("Left", Commands.left(Distance.ofMeters(2)).build(),yawStart);

        testMovement("Up", Commands.up(Distance.ofMeters(2)).build(),yawStart);
        testMovement("Down", Commands.down(Distance.ofMeters(2)).build(),yawStart);
        
        //simple Rotation
        testMovement("Turn to Zero",Commands.rotateTo(Angle.ZERO, RotationDirection.CLOCKWISE).build(),0);
        testMovement("Turn",Commands.rotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).build(),(float)(Math.PI/ 2.0));
        testMovement("Turn",Commands.rotate(Angle.ofDegrees(180), RotationDirection.COUNTERCLOCKWISE).build(),(float)(-Math.PI / 2.0));
        testMovement("Turn",Commands.rotate(Angle.ofDegrees(360), RotationDirection.CLOCKWISE).build(),(float)(-Math.PI / 2.0));
        testMovement("Turn",Commands.rotate(Angle.ofDegrees(2 * 360), RotationDirection.COUNTERCLOCKWISE).build(),(float)(-Math.PI / 2.0));
        testMovement("Turn",Commands.rotate(Angle.ofDegrees(270), RotationDirection.COUNTERCLOCKWISE).build(),0);
        
        yawStart = pixhawk.getAttitude().yaw;
        
        //Complex Movement
        testMovement("Side", Commands.forward(Distance.ofMeters(3)).right(Distance.ofMeters(2)).down(Distance.ofMeters(1)).build(),yawStart);
        testMovement("Side", Commands.backward(Distance.ofMeters(3)).left(Distance.ofMeters(2)).up(Distance.ofMeters(1)).build(),yawStart);
        
        //Complex Movement + Rotation
        testMovement("Turn to Zero",Commands.rotateTo(Angle.ZERO, RotationDirection.CLOCKWISE).build(),0);
        testMovement("Side", Commands.forward(Distance.ofMeters(3)).andRotate(Angle.ofDegrees(90), RotationDirection.CLOCKWISE).build(),(float)(Math.PI/ 2.0));
        testMovement("Side", Commands.right(Distance.ofMeters(3)).andRotate(Angle.ofDegrees(90), RotationDirection.COUNTERCLOCKWISE).build(),0);
    }
    
    public void testMovement(String name,FlyCommand command,float expectedYaw) {
        
        FlyController controller = new FlyController(pixhawk);
        
        float yawStart = -pixhawk.getAttitude().yaw;
        
        final msg_local_position_ned startPosition = pixhawk.getLocalPosition();   
        controller.execute(command);        
        final msg_local_position_ned endPosition = pixhawk.getLocalPosition();
        
        Vector3D start = new Vector3D((float)startPosition.x, (float)startPosition.y, 0);
        start.rotate2D(yawStart);
        start.z = (float)-startPosition.z;
        Vector3D end = new Vector3D((float)endPosition.x, (float)endPosition.y,0);
        end.rotate2D(yawStart);
        end.z = (float)-endPosition.z;
        
        assertEquals(name + ": X Coordinate",command.getPosition().getY(),end.x - start.x,0.2);
        assertEquals(name + ": Y Coordinate",command.getPosition().getX(),end.y - start.y,0.2);
        assertEquals(name + ": Z Coordinate",command.getPosition().getZ(),end.z - start.z ,0.2);
        assertEquals(name + ": Yaw Coordinate",expectedYaw,pixhawk.getAttitude().yaw ,(Math.PI / 18));
    }

}
