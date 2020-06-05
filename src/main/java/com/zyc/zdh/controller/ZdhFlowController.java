package com.zyc.zdh.controller;

import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ZdhFlowController {


    @Autowired
    DataSourcesService dataSourcesServiceImpl;
    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    DispatchTaskService dispatchTaskService;
    @Autowired
    ZdhLogsService zdhLogsService;
    @Autowired
    QuartzManager2 quartzManager2;

    @RequestMapping("/zdh_flow_index")
    public String data_sources_index() {

        return "zdh_flow/index";
    }

//    @RequestMapping(value = "/data_sources_list", produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String data_sources_list(String[] ids) {
//
//        DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
//       // dataSourcesInfo.setOwner(getUser().getId());
//        List<DataSourcesInfo> list = dataSourcesServiceImpl.select(dataSourcesInfo);
//
//        return JSON.toJSONString(list);
//    }


}
