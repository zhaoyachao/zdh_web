package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskDataxMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.EtlTaskDataxInfo;
import com.zyc.zdh.entity.EtlTaskUpdateLogs;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL-datax服务
 */
@Controller
public class ZdhDataxController extends BaseController{

    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private EtlTaskDataxMapper etlTaskDataxMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * datax 任务首页
     * @return
     */
    @RequestMapping("/etl_task_datax_index")
    public String etl_task_datax_index() {

        return "etl/etl_task_datax_index";
    }

    /**
     * datax任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_datax_add_index")
    public String etl_task_datax_add_index() {

        return "etl/etl_task_datax_add_index";
    }


    /**
     * datax任务列表
     * @param datax_context 关键字
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_datax_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskDataxInfo>> etl_task_datax_list(String datax_context, String id) {
        try{
            EtlTaskDataxInfo etlTaskDataxInfo=new EtlTaskDataxInfo();
            Example etlTaskDataxInfoExample= new Example(etlTaskDataxInfo.getClass());
            List<EtlTaskDataxInfo> etlTaskDataxInfos = new ArrayList<>();
            Example.Criteria cri=etlTaskDataxInfoExample.createCriteria();
            if(!StringUtils.isEmpty(datax_context)){
                cri.andLike("datax_context", getLikeCondition(datax_context));
            }
            if(!StringUtils.isEmpty(id)){
                cri.andEqualTo("id", id);
            }
            //cri.andEqualTo("owner", getOwner());
            cri.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, cri);

            etlTaskDataxInfos = etlTaskDataxMapper.selectByExample(etlTaskDataxInfoExample);
            dynamicAuth(zdhPermissionService, etlTaskDataxInfos);

            return ReturnInfo.buildSuccess(etlTaskDataxInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("查询datax任务列表",e);
        }

    }

    /**
     * 删除datax任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_datax_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> etl_task_datax_delete(String[] ids) {

        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxMapper, etlTaskDataxMapper.getTable(), ids, getAttrDel());
            etlTaskDataxMapper.deleteLogicByIds("etl_task_datax_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增datax任务
     * @param etlTaskDataxInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_datax_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_add(EtlTaskDataxInfo etlTaskDataxInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskDataxInfo.setOwner(owner);
            String id=SnowflakeIdWorker.getInstance().nextId()+"";
            etlTaskDataxInfo.setId(id);
            etlTaskDataxInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxInfo.getProduct_code(), etlTaskDataxInfo.getDim_group(), getAttrAdd());

            etlTaskDataxMapper.insertSelective(etlTaskDataxInfo);

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId().toString());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新datax任务
     * @param etlTaskDataxInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_update(EtlTaskDataxInfo etlTaskDataxInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskDataxInfo.setOwner(owner);
            String id=etlTaskDataxInfo.getId();
            etlTaskDataxInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);

            etlTaskDataxMapper.updateByPrimaryKeySelective(etlTaskDataxInfo);

            EtlTaskDataxInfo oldEtlTaskDataxInfo = etlTaskDataxMapper.selectByPrimaryKey(etlTaskDataxInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxInfo.getProduct_code(), etlTaskDataxInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskDataxInfo.getProduct_code(), oldEtlTaskDataxInfo.getDim_group(), getAttrEdit());

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")
                    && !etlTaskDataxInfo.getUpdate_context().equals(oldEtlTaskDataxInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

}
