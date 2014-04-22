package com.hachiyae.spock;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SampleUtilTest {

    @Test
    public void testGetDate() throws Exception {

    }

    @Test
    public void testIncrease() throws Exception {
        int increase = SampleUtil.increase(1, 2);
        assertThat("1+2="+increase, increase, is(3));
    }
}