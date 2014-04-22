package com.hachiyae.spock;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CurrentDateFactory {
    public Date getCurrentDate() {
        return new DateTime().toDate();
    }

    public void add(Date date, int addDays) {
        long time = date.getTime() + (864000 * addDays);
        date = new Date(time);
    }
}
