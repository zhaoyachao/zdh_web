package com.zyc.zdh.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpUtilTest {

    @Test
    public void getRequest() throws Exception {
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        NameValuePair nvp=new BasicNameValuePair("name", "张三");
        NameValuePair nvp2=new BasicNameValuePair("job", "IT");
        list.add(nvp);
        list.add(nvp2);
        System.out.println(HttpUtil.getRequest("http://127.0.0.1:9001/api/v1/test", list));
    }

    @Test
    public void postJSON() throws Exception {
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        Map<String,Object> nvp=new HashMap<>();
        nvp.put("name", "张三");

        System.out.println(HttpUtil.postJSON("http://127.0.0.1:9001/api/v1/test2", JSON.toJSONString(nvp)));
    }
}