package com.zyc.zdh.util;

import com.hubspot.jinjava.Jinjava;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JinjavaTest {


    @Test
    public void jinjavaString(){

        String t="hello {{name}},welcome to {{city}}";

        Jinjava jj=new Jinjava();
        Map<String,Object> m1=new HashMap<>();
        m1.put("name","zyc");
        m1.put("city","china");
        String result=jj.render(t,m1);
        System.out.println(result);

    }
}