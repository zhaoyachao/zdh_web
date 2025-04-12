package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.UserOperateLogMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.UserOperateLogInfo;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志服务
 */
@Controller
public class ZdhOperateLogController extends BaseController {

    @Autowired
    private UserOperateLogMapper userOperateLogMapper;

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
     * @param owner
     * @param operate_url
     * @param start_time
     * @param end_time
     * @param limit
     * @param offset
     * @return
     */
    @SentinelResource(value = "user_operate_log_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/user_operate_log_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<UserOperateLogInfo>>> user_operate_log_list(String log_context, String owner, String operate_url, String start_time, String end_time,int limit, int offset) {

        try{
            UserOperateLogInfo userOperateLogInfo = new UserOperateLogInfo();
            Example example = new Example(userOperateLogInfo.getClass());
            List<UserOperateLogInfo> userOperateLogInfos = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            cri.andBetween("create_time", start_time, end_time);

            if (!StringUtils.isEmpty(owner)) {
                cri.andLike("user_name", getLikeCondition(owner));
            }
            if (!StringUtils.isEmpty(operate_url)) {
                cri.andLike("operate_url", getLikeCondition(operate_url));
            }

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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

}
