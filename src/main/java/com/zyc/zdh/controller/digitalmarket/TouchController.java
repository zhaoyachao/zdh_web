package com.zyc.zdh.controller.digitalmarket;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.FilterMapper;
import com.zyc.zdh.dao.TouchConfigMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 智能营销-过滤规则服务
 */
@Controller
public class TouchController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TouchConfigMapper touchConfigMapper;


    /**
     * 触达配置首页
     * @return
     */
    @White
    @RequestMapping(value = "/touch_index", method = RequestMethod.GET)
    public String touch_index() {

        return "digitalmarket/touch_index";
    }


    /**
     * 触达配置列表
     * @param touch_context
     * @param limit
     * @param offset
     * @return
     */
    @White
    @RequestMapping(value = "/touch_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<TouchConfigInfo>>> touch_list(String touch_context, int limit, int offset) {
        try {
            Example example=new Example(TouchConfigInfo.class);
            List<TouchConfigInfo> touchConfigInfos = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            cri.andEqualTo("is_delete", Const.NOT_DELETE);
            cri.andEqualTo("owner",getOwner());
            if (!StringUtils.isEmpty(touch_context)) {
                Example.Criteria cri2 = example.and();
                cri2.andLike("touch_context", getLikeCondition(touch_context));
                cri2.orLike("touch_config", getLikeCondition(touch_context));
            }


            example.setOrderByClause("id asc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = touchConfigMapper.selectCountByExample(example);

            touchConfigInfos = touchConfigMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<TouchConfigInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(touchConfigInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 触达配置新增首页
     * @return
     */
    @White
    @RequestMapping(value = "/touch_add_index", method = RequestMethod.GET)
    public String touch_add_index() {

        return "digitalmarket/touch_add_index";
    }

    /**
     * 触达明细
     * @param id
     * @return
     */
    @White
    @RequestMapping(value = "/touch_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<TouchConfigInfo> touch_detail(String id) {
        try {
            TouchConfigInfo touchConfigInfo = touchConfigMapper.selectByPrimaryKey(id);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", touchConfigInfo);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 触达配置更新
     * @param touchConfigInfo
     * @return
     */
    @RequestMapping(value = "/touch_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo touch_update(TouchConfigInfo touchConfigInfo) {
        try {
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_context())){
                throw new Exception("触达说明参数不可为空");
            }
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_task())){
                throw new Exception("触达类型参数不可为空");
            }

            TouchConfigInfo oldTouchConfigInfo = touchConfigMapper.selectByPrimaryKey(touchConfigInfo.getId());

            touchConfigInfo.setOwner(oldTouchConfigInfo.getOwner());
            touchConfigInfo.setCreate_time(oldTouchConfigInfo.getCreate_time());
            touchConfigInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            touchConfigInfo.setIs_delete(Const.NOT_DELETE);
            touchConfigMapper.updateByPrimaryKey(touchConfigInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", touchConfigInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 触达配置新增
     * @param touchConfigInfo
     * @return
     */
    @RequestMapping(value = "/touch_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo touch_add(TouchConfigInfo touchConfigInfo) {
        try {
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_context())){
                throw new Exception("触达说明参数不可为空");
            }
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_task())){
                throw new Exception("触达类型参数不可为空");
            }

            touchConfigInfo.setOwner(getOwner());
            touchConfigInfo.setCreate_time(new Timestamp(new Date().getTime()));
            touchConfigInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            touchConfigInfo.setIs_delete(Const.NOT_DELETE);

            touchConfigMapper.insert(touchConfigInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 触达配置删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/touch_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo touch_delete(String[] ids) {
        try {
            touchConfigMapper.deleteLogicByIds("touch_config_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }



    /**
     * 触达组件明细页
     * @return
     */
    @White
    @RequestMapping(value = "/touch_detail", method = RequestMethod.GET)
    public String touch_detail() {

        return "digitalmarket/touch_detail";
    }

    /**
     * 触达配置明细
     * @param touch_task 触达任务类型,email,sms
     * @return
     */
    @White
    @RequestMapping(value = "/touch_list_by_task", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<TouchConfigInfo>> touch_list_by_task(String touch_task) {
        try {
            Example example=new Example(TouchConfigInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("touch_task", touch_task);
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getOwner());

            List<TouchConfigInfo> touchConfigInfos = touchConfigMapper.selectByExample(example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", touchConfigInfos);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
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
