package com.zyc.zdh.controller;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.impl.SecondaryDataSourceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 第二数据源测试 Controller
 */
@Controller
public class SecondaryDataSourceController {

    @Autowired
    private SecondaryDataSourceServiceImpl secondaryDataSourceServiceImpl;

    /**
     * 测试第二数据源连接
     */
    @RequestMapping(value = "/secondary_db_info", method = RequestMethod.GET)
    @ResponseBody
    @White
    public ReturnInfo<Map<String, Object>> testSecondaryDataSource() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String version = secondaryDataSourceServiceImpl.getSecondaryDatabaseVersion();
            result.put("databaseVersion", version);
            result.put("status", "success");
            result.put("message", "第二数据源连接成功");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "第二数据源连接失败: " + e.getMessage());
        }
        
        return ReturnInfo.buildSuccess(result);
    }

}
