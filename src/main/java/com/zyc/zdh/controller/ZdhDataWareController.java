package com.zyc.zdh.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.EmailJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 新数据仓库服务
 */
@Controller
public class ZdhDataWareController extends BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    IssueDataMapper issueDataMapper;
    @Autowired
    EnumMapper enumMapper;

    @RequestMapping("/data_ware_house_index_plus")
    public String data_ware_house_index_plus() {

        return "etl/data_ware_house_index_plus";
    }

    @White
    @RequestMapping(value = "/data_ware_house_list6", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list6(String issue_context,String current_page,String label_params) {
        int page_size = 3;
        List<IssueDataInfo> list = new ArrayList<>();
        String[] labels = new String[]{};
        if(!StringUtils.isEmpty(label_params)){
            labels = label_params.split(",");
        }
        list = issueDataMapper.selectByParams(issue_context, labels);

        int end_index = Integer.parseInt(current_page)*page_size;
        if(list.size()< end_index){
            end_index = list.size();
        }

        int total_page = list.size()/page_size;
        if(list.size()%page_size != 0)
            total_page=total_page+1;

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("total_page",total_page);
        jsonObject.put("total_size", list.size());
        jsonObject.put("current_page", current_page);

        List<String> left_page = new ArrayList<>();
        List<String> right_page = new ArrayList<>();
        for(int i=Integer.parseInt(current_page)-3; i<Integer.parseInt(current_page);i++ ){
            if(i>=1){
                left_page.add(String.valueOf(i));
            }
        }
        for(int i=Integer.parseInt(current_page); i<Integer.parseInt(current_page)+3;i++ ){
            if(i<= total_page){
                right_page.add(String.valueOf(i));
            }
        }
        if(Integer.parseInt(current_page)+4<total_page){
            right_page.add("...");
            right_page.add(String.valueOf(total_page-1));
            right_page.add(String.valueOf(total_page));
        }else if(Integer.parseInt(current_page)+3<total_page){
            right_page.add(String.valueOf(total_page-1));
            right_page.add(String.valueOf(total_page));
        }

        list =list.subList((Integer.parseInt(current_page)-1)*page_size, end_index);
        jsonObject.put("left_page", left_page);
        jsonObject.put("right_page", right_page);
        jsonObject.put("list", list);

        return jsonObject.toJSONString();
    }

    @White
    @RequestMapping(value = "/data_ware_house_label", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_label() {

        Example example=new Example(EnumInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        criteria.andLike("enum_code", "data_ware_house_label_%");
        List<EnumInfo> enumInfos = enumMapper.selectByExample(example);

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"查询成功", enumInfos);
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常:" + e.getMessage() + ", 异常详情:{}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常:" + e.getMessage() + ", 异常详情:{}");
            }
        }
    }

}
