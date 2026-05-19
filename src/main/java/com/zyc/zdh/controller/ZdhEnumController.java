package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EnumMapper;
import com.zyc.zdh.entity.EnumInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举服务
 */
@Controller
public class ZdhEnumController extends BaseController{

    @Autowired
    private EnumMapper enumMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;


    /**
     * 枚举首页
     * @return
     */
    @RequestMapping("enum_index")
    public String enum_index() {

        return "service/enum_index";
    }

    /**
     * 获取枚举详情
     * @param id 主键
     * @return
     */
    @SentinelResource(value = "enum_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/enum_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EnumInfo> enum_detail(String id) {
        try{
            EnumInfo enumInfo=enumMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService, enumInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", enumInfo);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 获取枚举列表
     * @param enum_context 关键字
     * @return
     */
    @SentinelResource(value = "enum_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/enum_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EnumInfo>> enum_list(String enum_context) {
        try{


            List<EnumInfo> list = new ArrayList<>();
            Example example=new Example(EnumInfo.class);
            Example.Criteria criteria= example.createCriteria();

            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(enum_context)){
                criteria2.orLike("enum_context", getLikeCondition(enum_context));
                criteria2.orLike("enum_json", getLikeCondition(enum_context));
                criteria2.orLike("enum_code", getLikeCondition(enum_context));
                example.and(criteria2);
            }

            list = enumMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, list);

            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("枚举列表查询失败", e);
        }
    }

    /**
     * 批量删除枚举信息
     * @param ids 主键数组
     * @return
     */
    @SentinelResource(value = "enum_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/enum_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enum_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, enumMapper, enumMapper.getTable(), ids, getAttrDel());
            enumMapper.deleteBatchByIds(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 枚举新增首页
     * @return
     */
    @RequestMapping("/enum_add_index")
    public String enum_add() {

        return "service/enum_add_index";
    }


    /**
     * 新增枚举
     * @param enumInfo
     * @param enum_value 枚举code
     * @param enum_value_context 枚举code说明
     * @return
     */
    @SentinelResource(value = "enum_add", blockHandler = "handleReturn")
    @RequestMapping(value="/enum_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enum_add(EnumInfo enumInfo,String[] enum_value, String[] enum_value_context) {

        try{
            checkAttrPermissionByProduct(zdhPermissionService, enumInfo.getProduct_code(), getAttrAdd());

            String owner = getOwner();
            enumInfo.setOwner(owner);

            //校验枚举code是否唯一
            EnumInfo ei = new EnumInfo();
            ei.setEnum_code(enumInfo.getEnum_code());
            ei.setProduct_code(enumInfo.getProduct_code());
            ei.setIs_delete(Const.NOT_DELETE);

            EnumInfo enumInfo1 = enumMapper.selectOne(ei);
            if(enumInfo1!=null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"枚举code不唯一", enumInfo);
            }

            List<Map<String, Object>> jsonArray=new ArrayList<>();

            for(int i=0;i<enum_value.length;i++){
                Map<String, Object> jsonObject=new LinkedHashMap<>();
                jsonObject.put("enum_value", enum_value[i]);
                jsonObject.put("enum_value_context", enum_value_context[i]);
                jsonArray.add(jsonObject);
            }

            enumInfo.setEnum_json(JsonUtil.formatJsonString(jsonArray));
            enumInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            enumInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            enumInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            enumInfo.setIs_delete(Const.NOT_DELETE);
            enumMapper.insertSelective(enumInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }




    /**
     * 枚举更新
     * @param enumInfo
     * @param enum_value 枚举code
     * @param enum_value_context 枚举code说明
     * @return
     */
    @SentinelResource(value = "enum_update", blockHandler = "handleReturn")
    @RequestMapping(value="/enum_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo enum_update(EnumInfo enumInfo,String[] enum_value, String[] enum_value_context) {
        try{
            checkAttrPermissionByProduct(zdhPermissionService, enumInfo.getProduct_code(), getAttrEdit());

            String owner = getOwner();
            enumInfo.setOwner(owner);
            enumInfo.setIs_delete(Const.NOT_DELETE);
            enumInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            List<Map<String, Object>> jsonArray=new ArrayList<>();

            for(int i=0;i<enum_value.length;i++){
                Map<String, Object> jsonObject= new LinkedHashMap<>();
                jsonObject.put("enum_value", enum_value[i]);
                jsonObject.put("enum_value_context", enum_value_context[i]);
                jsonArray.add(jsonObject);
            }

            enumInfo.setEnum_json(JsonUtil.formatJsonString(jsonArray));

            enumMapper.updateByPrimaryKeySelective(enumInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 查询枚举
     * @param enum_code 枚举code
     * @return
     */
    @SentinelResource(value = "enum_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/enum_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EnumInfo> enum_by_code(String enum_code) {
        try{
            String product_code = getProductCode();

            if(StringUtils.isEmpty(enum_code)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"枚举code不可为空", null);
            }

            List<EnumInfo> list = new ArrayList<>();
            Example example=new Example(EnumInfo.class);
            Example.Criteria criteria= example.createCriteria();
            criteria.andEqualTo("enum_code", enum_code);
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code",product_code);

            list = enumMapper.selectByExample(example);

            if(list!=null && list.size()==1){
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"查询成功", list.get(0));
            }
            throw new Exception("未找到对应枚举值");
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"查询失败", e);
        }

    }

}
