package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.LabelMapper;
import com.zyc.zdh.entity.LabelInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
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
    @RequestMapping(value = "/label_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String label_list(String label_context) {
        Example example=new Example(LabelInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        Example.Criteria criteria2=example.createCriteria();
        if(!org.apache.commons.lang3.StringUtils.isEmpty(label_context)){
            criteria2.orLike("label_code", getLikeCondition(label_context));
            criteria2.orLike("label_context", getLikeCondition(label_context));
            criteria2.orLike("param_json", getLikeCondition(label_context));
        }
        example.and(criteria2);

        List<LabelInfo> labelInfos = labelMapper.selectByExample(example);

        return JSONObject.toJSONString(labelInfos);
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
    @RequestMapping(value = "/label_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo label_detail_by_code(String label_code) {
        try {

            LabelInfo labelInfo = new LabelInfo();
            labelInfo.setLabel_code(label_code);
            labelInfo = labelMapper.selectOne(labelInfo);
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
    @RequestMapping(value = "/label_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_update(LabelInfo labelInfo,String[] param_code, String[] param_context, String[] param_operate, String[] param_value) {
        try {
            if(param_code==null || param_code.length<1){
                throw new Exception("参数不可为空");
            }
            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
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
            labelMapper.updateByPrimaryKey(labelInfo);

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
    @RequestMapping(value = "/label_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo label_add(LabelInfo labelInfo,String[] param_code, String[] param_context, String[] param_operate, String[] param_value) {
        try {
            if(param_code==null || param_code.length<1){
               throw new Exception("参数不可为空");
            }
            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
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
            labelMapper.insert(labelInfo);
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

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
