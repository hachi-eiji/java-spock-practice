package com.hachiyae.spock;

import mockit.MockUp;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SampleUtilTest {

    @Test
    public void testGetCurrentDate() throws Exception {
        new MockUp<System>(){
            public long currentTimeMillis(){
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(2010, 0, 1);
                return cal.getTimeInMillis();
            }
        };
        Date currentDate = SampleUtil.getCurrentDate();
        assertThat(currentDate.getYear(), is(114));
    }
}