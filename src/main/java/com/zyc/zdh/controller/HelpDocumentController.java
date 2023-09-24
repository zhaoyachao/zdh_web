package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.HelpDocumentInfo;
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
import com.zyc.zdh.dao.HelpDocumentMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 帮助文档服务
 */
@Controller
public class HelpDocumentController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HelpDocumentMapper helpDocumentMapper;

    /**
     * 帮助文档列表首页
     * @return
     */
    @RequestMapping(value = "/help_document_index", method = RequestMethod.GET)
    @White
    public String help_document_index() {

        return "service/help_document_index";
    }

    /**
     * 帮助文档列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "help_document_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/help_document_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<HelpDocumentInfo>> help_document_list(String context) {
        try{
            Example example=new Example(HelpDocumentInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            List<HelpDocumentInfo> helpDocumentInfos = helpDocumentMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(helpDocumentInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("帮助文档列表查询失败", e);
        }

    }

    /**
     * 帮助文档新增首页
     * @return
     */
    @RequestMapping(value = "/help_document_add_index", method = RequestMethod.GET)
    @White
    public String help_document_add_index() {

        return "service/help_document_add_index";
    }

    /**
     * 帮助文档展示首页
     * @return
     */
    @RequestMapping(value = "/help_document_show_index", method = RequestMethod.GET)
    @White
    public String help_document_show_index() {

        return "service/help_document_show_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "help_document_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/help_document_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<HelpDocumentInfo> help_document_detail(String id) {
        try {
            HelpDocumentInfo helpDocumentInfo = helpDocumentMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", helpDocumentInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 帮助文档更新
     * @param helpDocumentInfo
     * @return
     */
    @SentinelResource(value = "help_document_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/help_document_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<HelpDocumentInfo> help_document_update(HelpDocumentInfo helpDocumentInfo) {
        try {

            HelpDocumentInfo oldHelpDocumentInfo = helpDocumentMapper.selectByPrimaryKey(helpDocumentInfo.getId());


            helpDocumentInfo.setCreate_time(oldHelpDocumentInfo.getCreate_time());
            helpDocumentInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            helpDocumentInfo.setIs_delete(Const.NOT_DELETE);
            helpDocumentInfo.setOwner(oldHelpDocumentInfo.getOwner());
            helpDocumentMapper.updateByPrimaryKey(helpDocumentInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", helpDocumentInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 帮助文档新增
     * @param helpDocumentInfo
     * @return
     */
    @SentinelResource(value = "help_document_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/help_document_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<HelpDocumentInfo> help_document_add(HelpDocumentInfo helpDocumentInfo) {
        try {
            helpDocumentInfo.setOwner(getOwner());
            helpDocumentInfo.setIs_delete(Const.NOT_DELETE);
            helpDocumentInfo.setCreate_time(new Timestamp(new Date().getTime()));
            helpDocumentInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            helpDocumentMapper.insert(helpDocumentInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", helpDocumentInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 帮助文档删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "help_document_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/help_document_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo help_document_delete(String[] ids) {
        try {
            helpDocumentMapper.deleteLogicByIds("help_document_info",ids, new Timestamp(new Date().getTime()));
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
