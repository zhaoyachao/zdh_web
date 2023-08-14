package com.zyc.zdh.controller.digitalmarket;

import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.FilterMapper;
import com.zyc.zdh.entity.FilterInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 智能营销-过滤规则服务
 */
@Controller
public class FilterController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FilterMapper filterMapper;

    /**
     * 过滤列表首页
     * @return
     */
    @RequestMapping(value = "/filter_index", method = RequestMethod.GET)
    public String filter_index() {

        return "digitalmarket/filter_index";
    }

    /**
     * 过滤列表
     * @param filter_name 关键字
     * @return
     */
    @RequestMapping(value = "/filter_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  ReturnInfo<List<FilterInfo>> filter_list(String filter_name) {
        try{
            Example example=new Example(FilterInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(filter_name)){
                criteria2.orLike("filter_name", getLikeCondition(filter_name));
                criteria2.orLike("filter_code", getLikeCondition(filter_name));
            }
            example.and(criteria2);

            List<FilterInfo> filterInfos = filterMapper.selectByExample(example);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", filterInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 过滤新增首页
     * @return
     */
    @RequestMapping(value = "/filter_add_index", method = RequestMethod.GET)
    public String filter_add_index() {

        return "digitalmarket/filter_add_index";
    }


    /**
     * 过滤明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/filter_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<FilterInfo> filter_detail(String id) {
        try {
            FilterInfo filterInfo = filterMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", filterInfo);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 过滤更新
     * @param filterInfo
     * @return
     */
    @RequestMapping(value = "/filter_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<FilterInfo> filter_update(FilterInfo filterInfo) {
        try {

            FilterInfo oldFilterInfo = filterMapper.selectByPrimaryKey(filterInfo.getId());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            filterInfo.setOwner(oldFilterInfo.getOwner());
            filterInfo.setCreate_time(oldFilterInfo.getCreate_time());
            filterInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            filterInfo.setIs_delete(Const.NOT_DELETE);
            filterMapper.updateByPrimaryKey(filterInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", filterInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 过滤新增
     * @param filterInfo
     * @return
     */
    @RequestMapping(value = "/filter_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo filter_add(FilterInfo filterInfo) {
        try {

            filterInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            filterInfo.setOwner(getOwner());
            filterInfo.setIs_delete(Const.NOT_DELETE);
            filterInfo.setCreate_time(new Timestamp(new Date().getTime()));
            filterInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            filterMapper.insert(filterInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 过滤删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/filter_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo filter_delete(String[] ids) {
        try {
            filterMapper.deleteLogicByIds("filter_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
