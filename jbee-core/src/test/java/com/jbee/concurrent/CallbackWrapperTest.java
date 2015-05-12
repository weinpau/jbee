package com.jbee.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class CallbackWrapperTest {

    @Test
    public void testCall() throws Exception {

        CallbackWrapper instance = new CallbackWrapper<String>() {

            @Override
            public void handle() {
                submit("test");
            }
        };

        Object result = instance.call();
        assertEquals("test", result);
    }

    @Test
    public void testCall_Async() throws Exception {

        CallbackWrapper instance = new CallbackWrapper<String>() {

            @Override
            public void handle() {
                submit("test");
            }
        };
        Future<String> future = Executors.newSingleThreadExecutor().submit(instance);
        String result = future.get(1, TimeUnit.MILLISECONDS);
        assertEquals("test", result);
    }

    @Test(expected = TimeoutException.class)
    public void testCall_Interrupt() throws Exception {

        CallbackWrapper instance = new CallbackWrapper<String>() {

            @Override
            public void handle() {
            }
        };
        Future future = Executors.newSingleThreadExecutor().submit(instance);
        future.get(1, TimeUnit.MILLISECONDS);
    }

}