package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.HelpDocumentMapper;
import com.zyc.zdh.entity.HelpDocumentInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
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
import java.util.List;

/**
 * 帮助文档服务
 */
@Controller
public class HelpDocumentController extends BaseController {

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
                example.and(criteria2);
            }

            List<HelpDocumentInfo> helpDocumentInfos = helpDocumentMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(helpDocumentInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
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
            helpDocumentInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            helpDocumentInfo.setIs_delete(Const.NOT_DELETE);
            helpDocumentInfo.setOwner(oldHelpDocumentInfo.getOwner());
            helpDocumentMapper.updateByPrimaryKeySelective(helpDocumentInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", helpDocumentInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
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
            helpDocumentInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            helpDocumentInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            helpDocumentMapper.insertSelective(helpDocumentInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", helpDocumentInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
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
            helpDocumentMapper.deleteLogicByIds("help_document_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
