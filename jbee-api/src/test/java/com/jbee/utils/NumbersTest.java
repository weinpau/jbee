package com.jbee.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author weinpau
 */
public class NumbersTest {

    @Test
    public void testFormat() {
        assertEquals("0.000", Numbers.format(0, 3));
        assertEquals("3.14", Numbers.format(Math.PI, 2));
        assertEquals("3", Numbers.format(Math.PI, 0));
        assertEquals("38309238.000", Numbers.format(38309238, 3));
        assertEquals("38309238.232", Numbers.format(38309238.232323, 3));

    }

}
