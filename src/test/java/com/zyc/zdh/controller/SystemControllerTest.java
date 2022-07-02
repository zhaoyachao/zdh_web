package com.zyc.zdh.controller;

import com.zyc.zdh.util.DateUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SystemControllerTest {

    @Test
    public void cronExpression2executeDates() throws ParseException {
        String crontab="1,2 * * * * ? *";
       // String crontab="100";
        System.out.println(crontab);
        List<String> resultList = new ArrayList<String>() ;
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(crontab);//这里写要准备猜测的cron表达式
        List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : dates) {
            //System.out.println(dateFormat.format(date));
            resultList.add(dateFormat.format(date));
        }

        dates=TriggerUtils.computeFireTimesBetween(cronTriggerImpl,null, DateUtil.pase("2020-12-18 01:03:00","yyyy-MM-dd HH:mm:ss"),DateUtil.pase("2020-12-18 02:00:00","yyyy-MM-dd HH:mm:ss"));
        for (Date date : dates) {
            System.out.println(dateFormat.format(date));
            resultList.add(dateFormat.format(date));
        }
    }

    @Test
    public void strRegex() throws ParseException {
        Pattern pattern = Pattern.compile("(;|\\*|expr \\s*|delete \\s*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("expr ");
        System.out.println(matcher.find());

    }

    @Test
    public void md5() throws Exception{
        Object obj = new SimpleHash("md5", new String("123456"), null, 2);
        Assert.assertTrue("加密结果不符合预期,实际值:"+obj.toString(), "4280d89a5a03f812751f504cc10ee8a5".equalsIgnoreCase(obj.toString()));

        System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
    }

}