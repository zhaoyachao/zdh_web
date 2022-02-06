package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.EtlTaskDataxMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ZdhOperateLogController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserOperateLogMapper userOperateLogMapper;

    /**
     * 操作日志首页
     *
     * @return
     */
    @RequestMapping("/user_operate_log_index")
    public String etl_task_datax_index() {

        return "admin/user_operate_log_index";
    }


    /**
     * datax任务明细
     *
     * @param log_context
     * @param id
     * @return
     */
    @RequestMapping(value = "/user_operate_log_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String user_operate_log_list(String log_context, String id) {
        UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
        Example example = new Example(userOperateLogInfo.getClass());
        List<UserOperateLogInfo> userOperateLogInfos = new ArrayList<>();
        Example.Criteria cri = example.createCriteria();
        cri.andEqualTo("owner", getUser().getId());
        if (!StringUtils.isEmpty(log_context)) {
            cri.andLike("user_name", "%" + log_context + "%");
            cri.orLike("operate_input", "%" + log_context + "%");
            cri.orLike("operate_output", "%" + log_context + "%");
            cri.orLike("operate_context", "%" + log_context + "%");
            cri.orLike("operate_url", "%" + log_context + "%");
        }
        example.setOrderByClause("update_time desc");
        userOperateLogInfos = userOperateLogMapper.selectByExample(example);

        return JSON.toJSONString(userOperateLogInfos);
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常:" + e.getMessage());
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常:" + e.getMessage());
            }
        }
    }

}
