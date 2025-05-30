package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.TouchConfigMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.TouchConfigInfo;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 智能营销-过滤规则服务
 */
@Controller
public class TouchController extends BaseController {

    @Autowired
    private TouchConfigMapper touchConfigMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 在线节点配置首页
     * @return
     */
    @RequestMapping(value = "/online_detail", method = RequestMethod.GET)
    public String online_detail() {

        return "digitalmarket/online_detail";
    }


    /**
     * 触达配置首页
     * @return
     */
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
    @SentinelResource(value = "touch_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/touch_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<TouchConfigInfo>>> touch_list(String touch_context, String product_code, String dim_group, int limit, int offset) {
        try {
            Example example=new Example(TouchConfigInfo.class);
            List<TouchConfigInfo> touchConfigInfos = new ArrayList<>();
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            //criteria.andEqualTo("owner",getOwner());
            if (!StringUtils.isEmpty(touch_context)) {
                Example.Criteria cri2 = example.and();
                cri2.andLike("touch_context", getLikeCondition(touch_context));
                cri2.orLike("touch_config", getLikeCondition(touch_context));
            }


            example.setOrderByClause("id asc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = touchConfigMapper.selectCountByExample(example);

            touchConfigInfos = touchConfigMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, touchConfigInfos);

            PageResult<List<TouchConfigInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(touchConfigInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 触达配置新增首页
     * @return
     */
    @RequestMapping(value = "/touch_add_index", method = RequestMethod.GET)
    public String touch_add_index() {

        return "digitalmarket/touch_add_index";
    }

    /**
     * 触达明细
     * @param id
     * @return
     */
    @SentinelResource(value = "touch_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/touch_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<TouchConfigInfo> touch_detail(String id) {
        try {
            TouchConfigInfo touchConfigInfo = touchConfigMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, touchConfigInfo.getProduct_code(), touchConfigInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", touchConfigInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 触达配置更新
     * @param touchConfigInfo
     * @return
     */
    @SentinelResource(value = "touch_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/touch_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo touch_update(TouchConfigInfo touchConfigInfo) {
        try {
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_context())){
                throw new Exception("触达说明参数不可为空");
            }
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_task())){
                throw new Exception("触达类型参数不可为空");
            }

            TouchConfigInfo oldTouchConfigInfo = touchConfigMapper.selectByPrimaryKey(touchConfigInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, touchConfigInfo.getProduct_code(), touchConfigInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldTouchConfigInfo.getProduct_code(), oldTouchConfigInfo.getDim_group(), getAttrEdit());

            touchConfigInfo.setOwner(oldTouchConfigInfo.getOwner());
            touchConfigInfo.setCreate_time(oldTouchConfigInfo.getCreate_time());
            touchConfigInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            touchConfigInfo.setIs_delete(Const.NOT_DELETE);
            touchConfigMapper.updateByPrimaryKeySelective(touchConfigInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", touchConfigInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 触达配置新增
     * @param touchConfigInfo
     * @return
     */
    @SentinelResource(value = "touch_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/touch_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo touch_add(TouchConfigInfo touchConfigInfo) {
        try {
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_context())){
                throw new Exception("触达说明参数不可为空");
            }
            if(StringUtils.isEmpty(touchConfigInfo.getTouch_task())){
                throw new Exception("触达类型参数不可为空");
            }

            touchConfigInfo.setOwner(getOwner());
            touchConfigInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            touchConfigInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            touchConfigInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, touchConfigInfo.getProduct_code(), touchConfigInfo.getDim_group(), getAttrAdd());

            touchConfigMapper.insertSelective(touchConfigInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 触达配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "touch_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/touch_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo touch_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, touchConfigMapper, touchConfigMapper.getTable(), ids, getAttrDel());
            touchConfigMapper.deleteLogicByIds(touchConfigMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }



    /**
     * 触达组件明细页
     * @return
     */
    @RequestMapping(value = "/touch_detail", method = RequestMethod.GET)
    public String touch_detail() {

        return "digitalmarket/touch_detail";
    }

    /**
     * 触达配置明细
     * @param touch_task 触达任务类型,email,sms
     * @return
     */
    @SentinelResource(value = "touch_list_by_task", blockHandler = "handleReturn")
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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

}
