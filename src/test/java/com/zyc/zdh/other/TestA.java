package com.zyc.zdh.other;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class TestA {

    @Test
    public void run() throws Exception {

        JSONObject jo=new JSONObject();
        jo.put("a","1");
        jo.put("a","2");

        System.out.println(jo.toJSONString());


    }
}
