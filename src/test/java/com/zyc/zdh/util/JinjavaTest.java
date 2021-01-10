package com.zyc.zdh.util;

import com.hubspot.jinjava.Jinjava;
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
}