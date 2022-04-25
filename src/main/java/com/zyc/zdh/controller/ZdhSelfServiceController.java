package com.zyc.zdh.controller;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.SelfHistoryMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自助服务
 */
@Controller
public class ZdhSelfServiceController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SelfHistoryMapper selfHistoryMapper;
    @Autowired
    DataSourcesMapper dataSourcesMapper;

    @RequestMapping(value = "/self_service_index", method = RequestMethod.GET)
    public String self_service_index() {

        return "service/self_service_index";
    }

    @RequestMapping(value = "/self_service_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String self_service_list(String history_context) {
        Example example=new Example(SelfHistory.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        criteria.andEqualTo("owner", getUser().getId());
        Example.Criteria criteria2=example.createCriteria();
        if(!org.apache.commons.lang3.StringUtils.isEmpty(history_context)){
            criteria2.orLike("history_context", getLikeCondition(history_context));
        }
        example.and(criteria2);

        List<SelfHistory> selfHistories = selfHistoryMapper.selectByExample(example);

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", selfHistories);
    }


    @RequestMapping(value = "/self_service_add_index", method = RequestMethod.GET)
    public String self_service_add_index() {

        return "service/self_service_add_index";
    }


    @RequestMapping(value = "/self_service_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String self_service_detail(String id) {
        try {
            SelfHistory selfHistory = selfHistoryMapper.selectByPrimaryKey(id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", selfHistory);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @RequestMapping(value = "/self_service_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String self_service_update(SelfHistory selfHistory) {
        try {
            SelfHistory oldSelfHistory = selfHistoryMapper.selectByPrimaryKey(selfHistory.getId());

            if(StringUtils.isEmpty(selfHistory.getData_sources_choose_input())){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", "数据源不可为空");
            }
            selfHistory.setOwner(oldSelfHistory.getOwner());
            selfHistory.setCreate_time(oldSelfHistory.getCreate_time());
            selfHistory.setUpdate_time(new Timestamp(new Date().getTime()));
            selfHistory.setIs_delete(Const.NOT_DELETE);
            selfHistoryMapper.updateByPrimaryKey(selfHistory);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    @RequestMapping(value = "/self_service_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String self_service_add(SelfHistory selfHistory) {
        try {
            if(StringUtils.isEmpty(selfHistory.getData_sources_choose_input())){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", "数据源不可为空");
            }
            selfHistory.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            selfHistory.setOwner(getUser().getId());
            selfHistory.setIs_delete(Const.NOT_DELETE);
            selfHistory.setCreate_time(new Timestamp(new Date().getTime()));
            selfHistory.setUpdate_time(new Timestamp(new Date().getTime()));
            selfHistoryMapper.insert(selfHistory);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/self_service_delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String self_service_delete(String[] ids) {
        try {
            selfHistoryMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/self_service_execute", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public String self_service_execute(String etl_sql, String data_sources_choose_input) {
        try {
            if(StringUtils.isEmpty(etl_sql)|| StringUtils.isEmpty(data_sources_choose_input)){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "执行失败", "数据源或者sql为空");
            }
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            List<Map<String,Object>> result = new DBUtil().R5(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    etl_sql);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "执行成功", result);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "执行失败", e.getMessage());
        }
    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            }
        }
    }


}
