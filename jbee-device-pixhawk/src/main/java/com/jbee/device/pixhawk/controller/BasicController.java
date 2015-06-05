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
    
    protected static boolean cancle = false;
    private static List<BasicController> instances = new ArrayList();
    private Thread thread;

    public BasicController() {
        cancle = false;
        instances.add(this);
        thread = Thread.currentThread();
    }
    
    public synchronized static boolean isCancle() {
        return cancle;
    }

    public synchronized static void setCancle(boolean cancle) {
        BasicController.cancle = cancle;
        instances.forEach(c -> {c.onCanle(c.thread);});
    }
    
    
    public void onCanle(Thread t){
        cancle = true;
        t.interrupt();
    }
    
    @Override
    public void finalize(){
           instances.remove(this);
    }
}
