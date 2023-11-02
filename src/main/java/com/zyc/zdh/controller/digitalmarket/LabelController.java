package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.LabelMapper;
import com.zyc.zdh.entity.LabelInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 智能营销-标签服务
 */
@Controller
public class LabelController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 标签列表首页
     * @return
     */
    @RequestMapping(value = "/label_index", method = RequestMethod.GET)
    public String label_index() {

        return "digitalmarket/label_index";
    }

    /**
     * 标签列表
     * @param label_context 关键字
     * @return
     */
    @SentinelResource(value = "label_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<LabelInfo>> label_list(String label_context, String product_code) {
        try{
            Example example=new Example(LabelInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //动态增加数据权限控制
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!org.apache.commons.lang3.StringUtils.isEmpty(label_context)){
                criteria2.orLike("label_code", getLikeCondition(label_context));
                criteria2.orLike("label_context", getLikeCondition(label_context));
                criteria2.orLike("param_json", getLikeCondition(label_context));
            }
            example.and(criteria2);

            List<LabelInfo> labelInfos = labelMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(labelInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("标签列表查询失败", e);
        }

    }

    /**
     * 标签新增首页
     * @return
     */
    @RequestMapping(value = "/label_add_index", method = RequestMethod.GET)
    public String label_add_index() {

        return "digitalmarket/label_add_index";
    }


    /**
     * 标签明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "label_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo label_detail(String id) {
        try {
            LabelInfo labelInfo = labelMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", labelInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 根据code查询标签明细
     * @param label_code
     * @return
     */
    @SentinelResource(value = "label_detail_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo label_detail_by_code(String label_code, String label_use_type) {
        try {

            LabelInfo labelInfo = new LabelInfo();
            labelInfo.setLabel_code(label_code);
            List<LabelInfo> labelInfos = labelMapper.select(labelInfo);
            if(labelInfos != null ){
                labelInfo = labelInfos.get(0);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", labelInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 标签更新
     * @param labelInfo
     * @param param_code 参数code
     * @param param_context 参数说明
     * @param param_operate 参数操作符
     * @param param_value 参数可选值
     * @return
     */
    @SentinelResource(value = "label_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_update(LabelInfo labelInfo,String[] param_code, String[] param_context, String[] param_type,String[] param_operate, String[] param_value, String[] param_enum_key) {
        try {
            if(param_code==null || param_code.length<1){
                throw new Exception("参数不可为空");
            }
            if(StringUtils.isEmpty(labelInfo.getData_sources_choose_input())){
                throw new Exception("数据源参数不可为空");
            }

            checkParam(labelInfo.getLabel_code(),"标签名");
            if(!labelInfo.getLabel_code().startsWith("tag_")){
                throw new Exception("标签code 必须以tag_开头");
            }

            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
                jsonObject.put("param_type", param_type[i]);
                jsonObject.put("param_enum_key", param_enum_key[i]);
                if(i>=param_value.length){
                    jsonObject.put("param_value", "");
                }else{
                    jsonObject.put("param_value", param_value[i]);
                }
                jsonArray.add(jsonObject);
            }

            LabelInfo oldLabelInfo = labelMapper.selectByPrimaryKey(labelInfo.getId());

            labelInfo.setParam_json(jsonArray.toJSONString());
            labelInfo.setOwner(oldLabelInfo.getOwner());
            labelInfo.setCreate_time(oldLabelInfo.getCreate_time());
            labelInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            labelInfo.setIs_delete(Const.NOT_DELETE);
            labelInfo.setStatus(oldLabelInfo.getStatus());
            labelMapper.updateByPrimaryKeySelective(labelInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", labelInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 标签新增
     * @param labelInfo
     * @param param_code 参数code
     * @param param_context 参数说明
     * @param param_operate 参数操作符
     * @param param_value 参数可选值
     * @return
     */
    @SentinelResource(value = "label_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_add(LabelInfo labelInfo,String[] param_code, String[] param_context, String[] param_type, String[] param_operate, String[] param_value, String[] param_enum_key) {
        try {
            if(param_code==null || param_code.length<1){
               throw new Exception("参数不可为空");
            }

            checkParam(labelInfo.getLabel_code(),"标签名");
            if(!labelInfo.getLabel_code().startsWith("tag_")){
                throw new Exception("标签code 必须以tag_开头");
            }

            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
                jsonObject.put("param_type", param_type[i]);
                jsonObject.put("param_enum_key", param_enum_key[i]);
                if(i>=param_value.length){
                    jsonObject.put("param_value", "");
                }else{
                    jsonObject.put("param_value", param_value[i]);
                }

                jsonArray.add(jsonObject);
            }
            labelInfo.setParam_json(jsonArray.toJSONString());
            labelInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            labelInfo.setOwner(getOwner());
            labelInfo.setIs_delete(Const.NOT_DELETE);
            labelInfo.setCreate_time(new Timestamp(new Date().getTime()));
            labelInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            labelInfo.setStatus(Const.STATUS_NOT_PUB);
            labelMapper.insertSelective(labelInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 标签删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "label_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_delete(String[] ids) {
        try {
            labelMapper.deleteLogicByIds("label_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 标签启用/禁用
     * @param id
     * @return
     */
    @SentinelResource(value = "label_enable", blockHandler = "handleReturn")
    @RequestMapping(value = "/label_enable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_enable(String id) {
        try {
            LabelInfo labelInfo = labelMapper.selectByPrimaryKey(id);
            labelInfo.setStatus(labelInfo.getStatus().equalsIgnoreCase("2")?"1":"2");
            labelInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            labelMapper.updateByPrimaryKeySelective(labelInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e.getMessage());
        }
    }

}
