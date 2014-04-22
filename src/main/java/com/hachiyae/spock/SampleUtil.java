package com.hachiyae.spock;

import org.joda.time.DateTime;

import java.util.Date;

public class SampleUtil {

    public static Date getCurrentDate() {
        return new DateTime().toDate();
    }

    public static int increase(int i, int j) {
        return i + j;
    }
}
