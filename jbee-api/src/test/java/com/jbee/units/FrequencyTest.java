package com.jbee.units;

import java.time.Duration;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.jbee.units.Frequency.*;

/**
 *
 * @author weinpau
 */
public class FrequencyTest {

    @Test
    public void testAdd() {
        assertEquals(ofHz(50), ofHz(20).add(ofHz(30)));
        assertEquals(ofHz(0), ofHz(-20).add(ofHz(20)));
        assertEquals(ofHz(0), ofHz(0).add(ofHz(0)));

    }

    @Test(expected = ArithmeticException.class)
    public void testAdd_Overflow() {
        ofMilliHz(Long.MAX_VALUE).add(ofHz(30));
    }

    @Test
    public void testSub() {

        assertEquals(ofHz(-10), ofHz(20).sub(ofHz(30)));
        assertEquals(ofHz(100), ofHz(200).sub(ofHz(100)));
        assertEquals(ofHz(0), ofHz(0).sub(ofHz(0)));
        assertEquals(ofHz(0), ofHz(Integer.MAX_VALUE).sub(ofHz(Integer.MAX_VALUE)));
    }

    @Test
    public void testMultiply() {
        assertEquals(ofHz(40), ofHz(20).multiply(2));
        assertEquals(ofHz(-20), ofHz(20).multiply(-1));
        assertEquals(ofHz(20), ofHz(40).multiply(.5));
    }

    @Test
    public void testToCycleDuration() {
        assertEquals(Duration.ofSeconds(1), ofHz(1).toCycleDuration());
        assertEquals(Duration.ofSeconds(-1), ofHz(-1).toCycleDuration());

        assertEquals(Duration.ofMillis(1), ofHz(1000).toCycleDuration());
        assertEquals(1000000, ofHz(1000).toCycleDuration().getNano());

        assertEquals(Duration.ofNanos(1000), ofHz(1000_000).toCycleDuration());
        assertEquals(Duration.ofSeconds(1000), ofMilliHz(1).toCycleDuration());

        assertEquals(Duration.ofNanos(1), ofHz(1000_000_000).toCycleDuration());
        assertEquals(Duration.ZERO, ofHz(1000_000_001).toCycleDuration());

        assertEquals(Duration.ZERO, ofHz(Integer.MAX_VALUE).toCycleDuration());
        assertEquals(Duration.ZERO, ofHz(Integer.MIN_VALUE).toCycleDuration());
        assertEquals(Duration.ZERO, ofMilliHz(Long.MAX_VALUE).toCycleDuration());
        assertEquals(Duration.ZERO, ofMilliHz(Long.MIN_VALUE).toCycleDuration());

    }

    @Test(expected = ArithmeticException.class)
    public void testToCycleDuration_DivisionByNull() {
        ofHz(0).toCycleDuration();
    }

}
