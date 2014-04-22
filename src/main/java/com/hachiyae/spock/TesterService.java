package com.hachiyae.spock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TesterService {
    @Autowired
    private CurrentDateFactory currentDateFactory;

    public String sayHello(String userName) {
        return "Hello " + userName;
    }

    public String sayCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return "current date is " + format.format(currentDateFactory.getCurrentDate());
    }

    public String sayCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = SampleUtil.getCurrentDate();
        return "current time is " + format.format(currentDate);
    }

    public String sayCurrentYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return "current year is " + format.format(date);
    }

    public String executeVoidMethod(Date date, int addDays) {
        currentDateFactory.add(date, addDays);
        return "foo -> " + date.getYear();
    }

    public String convert(String str){
        return "test_"+Dao.getFactory().execute(str);
    }
}
