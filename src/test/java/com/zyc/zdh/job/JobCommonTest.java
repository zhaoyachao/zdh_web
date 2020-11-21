package com.zyc.zdh.job;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class JobCommonTest {

    @Test
    public void resolveQuartzExpr() throws ParseException {

       for(Date d: JobCommon.resolveQuartzExpr("100s")){
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           System.out.println(dateFormat.format(d));
       }


    }
}