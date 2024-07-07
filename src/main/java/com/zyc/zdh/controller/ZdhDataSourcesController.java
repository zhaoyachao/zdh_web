package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 数据源服务
 */
@Controller
public class ZdhDataSourcesController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataSourcesMapper dataSourcesMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    /**
     * 数据源列表首页
     * @return
     */
    @RequestMapping("/data_sources_index")
    public String data_sources_index() {

        return "etl/data_sources_index";
    }

    /**
     * 数据源列表
     * @return
     */
    @SentinelResource(value = "data_sources_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataSourcesInfo>> data_sources_list(String product_code, String dim_group) {
        //获取数据权限
        try{
            String[] tag_group_code = "".split(",");

//            PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
//            permissionUserInfo.setUser_account(getUser().getUserName());
//            Example example=new Example(PermissionUserInfo.class);
//            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("user_account",getUser().getUserName());
//            criteria.andEqualTo("product_code", ev.getProperty("zdp.product", "zdh"));
//            criteria.andEqualTo("enable",Const.TRUR);
//            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example);
//
//            if(permissionUserInfos!=null && permissionUserInfos.size()>=1){
//                tag_group_code = permissionUserInfos.get(0).getTag_group_code().split(",");
//            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
            dataSourcesInfo.setOwner(getOwner());
            dataSourcesInfo.setIs_delete(Const.NOT_DELETE);
            List<DataSourcesInfo> list = dataSourcesMapper.selectByParams(getOwner(), tag_group_code, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            if(list != null && list.size()>0){
                for (DataSourcesInfo dsi: list){
                    dsi.setPassword("");
                }
            }
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据源列表查询失败",e);
        }

    }

    /**
     * 数据源列表(带参数)
     * @param data_source_context 数据源说明
     * @param data_source_type 数据源类型
     * @param url 连接串
     * @return
     */
    @SentinelResource(value = "data_sources_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataSourcesInfo>> data_sources_list2(String data_source_context, String data_source_type, String url, String product_code, String dim_group) {

        try{
//            Example example = new Example(DataSourcesInfo.class);
//            Example.Criteria criteria = example.createCriteria();
//
//            criteria.andEqualTo("owner", getOwner());
//            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
//            //动态增加数据权限控制
//            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
//
//            if(!StringUtils.isEmpty(product_code)){
//                criteria.andEqualTo("product_code", product_code);
//            }
//            if(!StringUtils.isEmpty(dim_group)){
//                criteria.andEqualTo("dim_group", dim_group);
//            }
//
//            if(!StringUtils.isEmpty(data_source_context)){
//                criteria.andLike("data_source_context", getLikeCondition(data_source_context));
//            }
//            if(!StringUtils.isEmpty(data_source_type)){
//                criteria.andEqualTo("data_source_type", data_source_type);
//            }
//            if(!StringUtils.isEmpty(url)){
//                criteria.andLike("url", getLikeCondition(url));
//            }
//            String[] tag_group_code = "".split(",");

//            PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
//            permissionUserInfo.setUser_account(getUser().getUserName());
//            Example example2=new Example(PermissionUserInfo.class);
//            Example.Criteria criteria2 = example2.createCriteria();
//            criteria2.andEqualTo("user_account",getUser().getUserName());
//            criteria2.andEqualTo("product_code", ev.getProperty("zdp.product", "zdh"));
//            criteria2.andEqualTo("enable",Const.TRUR);
//            List<PermissionUserInfo> permissionUserInfos = permissionMapper.selectByExample(example2);

//            if(permissionUserInfos!=null && permissionUserInfos.size()>=1){
//                tag_group_code = permissionUserInfos.get(0).getTag_group_code().split(",");
//            }

            if(!StringUtils.isEmpty(data_source_context)){
                data_source_context = getLikeCondition(data_source_context);
            }
            if(!StringUtils.isEmpty(url)){
                url = getLikeCondition(url);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            List<DataSourcesInfo> list = dataSourcesMapper.selectByParams2(getOwner(), null, url, data_source_context, data_source_type, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            if(list != null && list.size()>0){
                for (DataSourcesInfo dsi: list){
                    dsi.setPassword("");
                }
            }
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据源列表2查询失败",e);
        }

    }

    /**
     * 根据数据源id(主键)获取数据源
     * @param id id
     * @return
     */
    @SentinelResource(value = "data_sources_info", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_info", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<DataSourcesInfo> data_sources_info(String id) {
        try{
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(id);
            if(dataSourcesInfo != null){
                dataSourcesInfo.setPassword("");
            }
            checkPermissionByProductAndDimGroup(zdhPermissionService, dataSourcesInfo.getProduct_code(), dataSourcesInfo.getDim_group());
            return ReturnInfo.buildSuccess(dataSourcesInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据源信息查询失败",e);
        }
    }

    /**
     * 批量删除数据源
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "data_sources_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> deleteIds(String[] ids) {
        try{
            checkPermissionByProductAndDimGroup(zdhPermissionService, dataSourcesMapper, dataSourcesMapper.getTable(), ids);
            dataSourcesMapper.deleteLogicByIds(dataSourcesMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 数据源新增页面
     * @return
     */
    @RequestMapping("/data_sources_add_index")
    public String data_sources_add_index() {

        return "etl/data_sources_add_index";
    }

    /**
     * 新增数据源
     * @param dataSourcesInfo
     * @return
     */
    @SentinelResource(value = "data_sources_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> data_sources_add(DataSourcesInfo dataSourcesInfo, String check_password) {
        try{
            dataSourcesInfo.setOwner(getOwner());
            dataSourcesInfo.setIs_delete("0");
            dataSourcesInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            if(StringUtils.isEmpty(check_password) || check_password.equalsIgnoreCase("off")){
                dataSourcesInfo.setPassword(null);
            }

            checkPermissionByProductAndDimGroup(zdhPermissionService, dataSourcesInfo.getProduct_code(), dataSourcesInfo.getDim_group());
            dataSourcesMapper.insertSelective(dataSourcesInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新数据源
     * @param dataSourcesInfo
     * @return
     */
    @SentinelResource(value = "data_sources_update", blockHandler = "handleReturn")
    @RequestMapping(value="/data_sources_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> data_sources_update(DataSourcesInfo dataSourcesInfo, String check_password) {
        try{
            DataSourcesInfo oldDataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfo.getId());

            checkPermissionByProductAndDimGroup(zdhPermissionService, dataSourcesInfo.getProduct_code(), dataSourcesInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldDataSourcesInfo.getProduct_code(), oldDataSourcesInfo.getDim_group());

            if(StringUtils.isEmpty(check_password) || check_password.equalsIgnoreCase("off")){
                dataSourcesInfo.setPassword(null);
            }
            dataSourcesInfo.setOwner(oldDataSourcesInfo.getOwner());
            dataSourcesInfo.setIs_delete("0");
            dataSourcesInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            dataSourcesMapper.updateByPrimaryKeySelective(dataSourcesInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }

    }

    /**
     * 获取所有的数据源类型
     * @return
     */
    @SentinelResource(value = "data_sources_type", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_sources_type", method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo<List<String>> data_sources_type() {
        try{
            List<String> result = dataSourcesMapper.selectDataSourcesType();
            return ReturnInfo.buildSuccess(result);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError(e);
        }
    }


    /**
     * 测试数据源联通性
     * @param dataSourcesInfo
     * @return
     */
    @SentinelResource(value = "test_connect", blockHandler = "handleReturn")
    @RequestMapping(value = "/test_connect", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> test_connect(DataSourcesInfo dataSourcesInfo) {
        try{
            if(!dataSourcesInfo.getData_source_type().equalsIgnoreCase("jdbc")){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"测试连接失败", "只支持JDBC类型连接测试");
            }
            new DBUtil().R3(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "");
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"测试连接成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"测试连接失败", e);
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
