package com.zyc.zdh.util;

import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void year() {

        System.out.println(DateUtil.year(new Date()));
    }

    @Test
    public void month() {
        System.out.println(DateUtil.month(new Date()));
    }

    @Test
    public void day() throws Exception {
        System.out.println(DateUtil.day(new Timestamp(DateUtil.pase("2024-05-05").getTime())));
    }

    @Test
    public void hour() throws Exception {
        System.out.println(DateUtil.hour(new Timestamp(DateUtil.pase("2024-05-05").getTime())));
        System.out.println(DateUtil.hour(new Timestamp(DateUtil.pase("2024-05-05 22:20:00", "yyyy-MM-dd HH:mm:ss").getTime())));
    }

    @Test
    public void minute() throws Exception {
        System.out.println(DateUtil.minute(new Timestamp(DateUtil.pase("2024-05-05").getTime())));
        System.out.println(DateUtil.minute(new Timestamp(DateUtil.pase("2024-05-05 22:20:00", "yyyy-MM-dd HH:mm:ss").getTime())));
    }

    @Test
    public void second() {
        System.out.println(DateUtil.second(new Timestamp(new Date().getTime())));
    }
}