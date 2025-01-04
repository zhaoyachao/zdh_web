package com.zyc.zdh.util;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.job.JobBeaconFire;
import org.junit.Test;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GroovyFactoryTest {

    @Test
    public void execExpress() throws ScriptException {
        List<String> rs = new ArrayList<>();
        rs.add("1");
        rs.add("2");
        Map<String, Object> params = new HashMap<>();
        params.put("rs", rs);
        params.put("out", new JobBeaconFire.Out());
        String groovy = "if(rs != null){\n" +
                "    if(rs.size() > 1){\n" +
                "        out.code = '201'\n" +
                "        return out\n" +
                "    }\n" +
                "}\n" +
                "return out";
        JobBeaconFire.Out out = (JobBeaconFire.Out)GroovyFactory.execExpress(groovy, params);

        System.out.println(JsonUtil.formatJsonString(out));
    }
}