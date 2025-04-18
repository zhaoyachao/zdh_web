package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.SelfHistoryMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.SelfHistory;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.ExportUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 自助服务
 */
@Controller
public class ZdhSelfServiceController extends BaseController {

    @Autowired
    private SelfHistoryMapper selfHistoryMapper;
    @Autowired
    private DataSourcesMapper dataSourcesMapper;

    /**
     * 自助服务首页
     * @return
     */
    @RequestMapping(value = "/self_service_index", method = RequestMethod.GET)
    public String self_service_index() {

        return "service/self_service_index";
    }

    /**
     * 自助服务列表
     * @param history_context
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "self_service_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SelfHistory>> self_service_list(String history_context) throws Exception {
        Example example=new Example(SelfHistory.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        criteria.andEqualTo("owner", getOwner());
        Example.Criteria criteria2=example.createCriteria();
        if(!org.apache.commons.lang3.StringUtils.isEmpty(history_context)){
            criteria2.orLike("history_context", getLikeCondition(history_context));
            example.and(criteria2);
        }

        List<SelfHistory> selfHistories = selfHistoryMapper.selectByExample(example);

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", selfHistories);
    }


    /**
     * 自助服务新增首页
     * @return
     */
    @RequestMapping(value = "/self_service_add_index", method = RequestMethod.GET)
    public String self_service_add_index() {

        return "service/self_service_add_index";
    }


    /**
     * 自助服务明细
     * @param id id
     * @return
     */
    @SentinelResource(value = "self_service_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<SelfHistory> self_service_detail(String id) {
        try {
            SelfHistory selfHistory = selfHistoryMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", selfHistory);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 自助服务更新
     * @param selfHistory
     * @return
     */
    @SentinelResource(value = "self_service_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo self_service_update(SelfHistory selfHistory) {
        try {
            SelfHistory oldSelfHistory = selfHistoryMapper.selectByPrimaryKey(selfHistory.getId());

            if(StringUtils.isEmpty(selfHistory.getData_sources_choose_input())){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", "数据源不可为空");
            }
            selfHistory.setOwner(oldSelfHistory.getOwner());
            selfHistory.setCreate_time(oldSelfHistory.getCreate_time());
            selfHistory.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            selfHistory.setIs_delete(Const.NOT_DELETE);
            selfHistoryMapper.updateByPrimaryKeySelective(selfHistory);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 自助服务新增
     * @param selfHistory
     * @return
     */
    @SentinelResource(value = "self_service_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo self_service_add(SelfHistory selfHistory) {
        try {
            if(StringUtils.isEmpty(selfHistory.getData_sources_choose_input())){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", "数据源不可为空");
            }
            selfHistory.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            selfHistory.setOwner(getOwner());
            selfHistory.setIs_delete(Const.NOT_DELETE);
            selfHistory.setCreate_time(new Timestamp(System.currentTimeMillis()));
            selfHistory.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            selfHistoryMapper.insertSelective(selfHistory);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 自助服务删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "self_service_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo self_service_delete(String[] ids) {
        try {
            selfHistoryMapper.deleteBatchById(ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 自助服务执行
     * @param etl_sql
     * @param data_sources_choose_input
     * @return
     */
    @SentinelResource(value = "self_service_execute", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo self_service_execute(String etl_sql, String data_sources_choose_input) {
        try {
            if(StringUtils.isEmpty(etl_sql)|| StringUtils.isEmpty(data_sources_choose_input)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "执行失败", "数据源或者sql为空");
            }
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            List<Map<String,Object>> result = new DBUtil().R5(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    etl_sql);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "执行成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "执行失败", e.getMessage());
        }
    }

    /**
     * 自助服务导出
     * @param etl_sql
     * @param data_sources_choose_input
     * @param response
     * @return
     */
    @SentinelResource(value = "self_service_export", blockHandler = "handleReturn")
    @RequestMapping(value = "/self_service_export", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ReturnInfo data_ware_house_export(String etl_sql, String data_sources_choose_input,HttpServletResponse response) {

        try {
            if(StringUtils.isEmpty(etl_sql)|| StringUtils.isEmpty(data_sources_choose_input)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "执行失败", "数据源或者sql为空");
            }
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            List<Map<String,Object>> result = new DBUtil().R5(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    etl_sql);
            String column = "";
            if(result!=null && result.size()>=1){
                column = StringUtils.join(result.get(0).keySet(),",");
            }
            ExportUtil.responseSetProperties(UUID.randomUUID().toString(),response);
            ExportUtil.doExport(result, column, column,response.getOutputStream());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "执行成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "执行失败", e.getMessage());
        }
    }

}
