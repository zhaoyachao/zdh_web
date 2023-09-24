package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.UserOperateLogInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志服务
 */
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
     * 操作日志列表
     *
     * bootstrap-table 分页
     *  设置
     *           sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
     *           pageNumber: 1,                       //初始化加载第一页，默认第一页
     *           pageSize: 10,                       //每页的记录行数（*）
     *          queryParams: function (params) {
     *               // 此处使用了LayUi组件 是为加载层
     *               loadIndex = layer.load(1);
     *               let resRepor = {
     *                   //服务端分页所需要的参数
     *                   limit: params.limit,
     *                   offset: params.offset
     *               };
     *               return resRepor;
     *           },
     *           responseHandler: res => {
     *               // 关闭加载层
     *               layer.close(loadIndex);
     *               return {                            //return bootstrap-table能处理的数据格式
     *                   "total":res.total,
     *                   "rows": res.rows
     *               }
     *           },
     *           data-content-type="application/x-www-form-urlencoded" data-query-params="queryParams"
     * @param log_context
     * @param id
     * @return
     */
    @SentinelResource(value = "user_operate_log_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/user_operate_log_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<UserOperateLogInfo>>> user_operate_log_list(String log_context, String id, String start_time, String end_time,int limit, int offset, ServletRequest request) {

        try{
            UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
            Example example = new Example(userOperateLogInfo.getClass());
            List<UserOperateLogInfo> userOperateLogInfos = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            cri.andBetween("create_time", start_time, end_time);
            if (!StringUtils.isEmpty(log_context)) {
                Example.Criteria cri2 = example.and();
                cri2.andLike("user_name", getLikeCondition(log_context));
                cri2.orLike("operate_input", getLikeCondition(log_context));
                cri2.orLike("operate_output", getLikeCondition(log_context));
                cri2.orLike("operate_context", getLikeCondition(log_context));
                cri2.orLike("operate_url", getLikeCondition(log_context));
            }
            example.setOrderByClause("update_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = userOperateLogMapper.selectCountByExample(example);

            userOperateLogInfos = userOperateLogMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<UserOperateLogInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(userOperateLogInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
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
