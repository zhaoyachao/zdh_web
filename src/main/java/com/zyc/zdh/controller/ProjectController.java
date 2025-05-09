package com.zyc.zdh.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ProjectMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.ProjectInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import java.util.List;
import java.util.Map;

/**
 * 项目信息服务
 */
@Controller
public class ProjectController extends BaseController {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 项目信息列表首页
     * @return
     */
    @RequestMapping(value = "/project_index", method = RequestMethod.GET)
    public String project_index() {

        return "service/project_index";
    }

    /**
     * 项目信息列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "project_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProjectInfo>> project_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(ProjectInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<ProjectInfo> projectInfos = projectMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, projectInfos);

            return ReturnInfo.buildSuccess(projectInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("项目信息列表查询失败", e);
        }

    }


    /**
    * 项目信息列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "project_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<ProjectInfo>>> project_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(ProjectInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = projectMapper.selectCountByExample(example);

            List<ProjectInfo> projectInfos = projectMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, projectInfos);

            PageResult<List<ProjectInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(projectInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("烽火台信息列表查询失败", e);
        }

    }

    /**
     * 项目信息新增首页
     * @return
     */
    @RequestMapping(value = "/project_add_index", method = RequestMethod.GET)
    public String project_add_index() {

        return "service/project_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "project_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ProjectInfo> project_detail(String id) {
        try {
            ProjectInfo projectInfo = projectMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, projectInfo.getProduct_code(), projectInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", projectInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 项目信息更新
     * @param projectInfo
     * @param enum_value
     * @param enum_value_context
     * @return
     */
    @SentinelResource(value = "project_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<ProjectInfo> project_update(ProjectInfo projectInfo,String[] enum_value, String[] enum_value_context) {
        try {

            ProjectInfo oldProjectInfo = projectMapper.selectByPrimaryKey(projectInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, projectInfo.getProduct_code(), projectInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldProjectInfo.getProduct_code(), oldProjectInfo.getDim_group(), getAttrEdit());


            List<Map<String, Object>> jsonArray= JsonUtil.createEmptyListMap();

            for(int i=0;i<enum_value.length;i++){
                Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
                jsonObject.put("enum_value", enum_value[i]);
                jsonObject.put("enum_value_context", enum_value_context[i]);
                jsonArray.add(jsonObject);
            }

            projectInfo.setProject_json(JsonUtil.formatJsonString(jsonArray));

            projectInfo.setCreate_time(oldProjectInfo.getCreate_time());
            projectInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            projectInfo.setIs_delete(Const.NOT_DELETE);
            projectMapper.updateByPrimaryKeySelective(projectInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", projectInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 项目信息新增
     * @param projectInfo
     * @param enum_value
     * @param enum_value_context
     * @return
     */
    @SentinelResource(value = "project_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<ProjectInfo> project_add(ProjectInfo projectInfo,String[] enum_value, String[] enum_value_context) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, projectInfo.getProduct_code(), projectInfo.getDim_group(), getAttrAdd());

            List<Map<String, Object>> jsonArray= JsonUtil.createEmptyListMap();

            for(int i=0;i<enum_value.length;i++){
                Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
                jsonObject.put("enum_value", enum_value[i]);
                jsonObject.put("enum_value_context", enum_value_context[i]);
                jsonArray.add(jsonObject);
            }

            projectInfo.setProject_json(JsonUtil.formatJsonString(jsonArray));

            projectInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            projectInfo.setOwner(getOwner());
            projectInfo.setIs_delete(Const.NOT_DELETE);
            projectInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            projectInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            projectMapper.insertSelective(projectInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", projectInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 项目信息删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "project_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/project_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo project_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, projectMapper, projectMapper.getTable(), ids, getAttrDel());
            projectMapper.deleteLogicByIds("project_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
