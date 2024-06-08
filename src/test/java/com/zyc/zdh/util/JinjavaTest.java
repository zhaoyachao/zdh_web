package com.zyc.zdh.util;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JinjavaTest {


    @Test
    public void jinjavaString(){

        String t="hello {{name}},welcome to {{city}},date:{{zdh_date}}";

        Jinjava jj=new Jinjava();
        Map<String,Object> m1=new HashMap<>();
        m1.put("name","zyc");
        m1.put("city","china");
        m1.put("zdh_date","2020-12-01");
        String result=jj.render(t,m1);
        System.out.println(result);

    }

    @Test
    public void jinJavaFunction(){
       // DateUtil.getCurrentTime();
        Jinjava jinjava=new Jinjava();
        jinjava.getGlobalContext().registerFunction(new ELFunctionDefinition("", "currenttime",
                DateUtil.class, "getCurrentTime"));
//        jinjava.getGlobalContext().registerFunction(new ELFunctionDefinition("a", "addday",
//                DateUtil.class, "addDay",String.class,Integer.class));
        jinjava.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_day",
                DateUtil.class, "addDay", String.class, Integer.class));

        String t="hello {{name}},welcome to {{city}},date:{{currenttime()}}, dd: {{add_day(zdh_date, -1)}}";
        Map<String,Object> m1=new HashMap<>();
        m1.put("name","zyc");
        m1.put("city","china");
        m1.put("zdh_date","2020-12-01 00:00:00");
        String result=jinjava.render(t,m1);
        System.out.println(result);

    }

    @Test
    public void jinJavaClass(){
        Jinjava jinjava=new Jinjava();
        Map<String,Object> jinJavaParam=new HashMap<>();
        jinJavaParam.put("zdh_dt", new DateUtil());

        String result = jinjava.render("{{zdh_dt.addDay('2024-06-04 01:00:00', 1)}}", jinJavaParam);
        System.out.println(result);
        result = jinjava.render("{{zdh_dt.addDay('2024-06-04','yyyy-MM-dd', 1)}}", jinJavaParam);
        System.out.println(result);
    }
}