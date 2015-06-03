/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.jbee.device.pixhawk.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Trader
 */
public abstract class BasicController {
    
    private static boolean cancle = false;
    private static List<BasicController> instances = new ArrayList();

    public BasicController() {
        instances.add(this);
    }
    
    public synchronized static boolean isCancle() {
        return cancle;
    }

    public synchronized static void setCancle(boolean cancle) {
        BasicController.cancle = cancle;
        instances.forEach(c -> {c.onCanle();});
    }
    
    
    public abstract void onCanle();
    
    @Override
    public void finalize(){
           instances.remove(this);
    }
    
    
    
    
}
