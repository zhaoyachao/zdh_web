package com.zyc.zdh.util;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JsonUtilTest {

    @Test
    public void toJavaListMap() {


        Map<String, Object> map = new LinkedHashMap<>();
        map.put("a1", "a11");
        map.put("a2", "a12");
        map.put("a3", "a13");
        map.put("b1", "b1");
        map.put("b11", "b11");

        ArrayList<Map<String, Object>> maps = Lists.newArrayList(map);

        String json = JsonUtil.formatJsonString(maps);

        System.out.println(json);

        List<Map<String, Object>> maps1 = JsonUtil.toJavaListMap(json);

        System.out.println(maps1.size());

        List<Map<String, Object>> maps2 = JsonUtil.toJavaListMap("[]");

        System.out.println(maps2.size());

        JsonUtil.toJavaMap("");

        System.out.println("空map: "+JsonUtil.formatJsonString(new HashMap<>()));
        System.out.println("空List: "+JsonUtil.formatJsonString(new ArrayList<>()));
    }
}