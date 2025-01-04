package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.ProductTagMapper;
import com.zyc.zdh.entity.ProductTagInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.entity.UserAndGroupPermissionDimensionValueInfo;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import tk.mybatis.mapper.entity.Example;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String ZDH_PERMISSION_PRODUCT_DIM_GROUP_SELECT="zdh_permission_product_dim_group_select";

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });

    }


    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    public String getOwner() throws Exception {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user == null){
            throw new Exception("获取用户失败,请检查用户是否登录");
        }
        return user.getUserName();
    }

    public String getProductCode() throws Exception {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user == null){
            throw new Exception("获取产品失败,请尝试重新登录");
        }
        return user.getProduct_code();
    }

    public String getUserGroup() throws Exception {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user == null){
            throw new Exception("获取用户组失败,请尝试重新登录");
        }
        return user.getUser_group();
    }

    public String[] getAttrSelect(){
        return new String[]{"select"};
    }

    public String[] getAttrEdit(){
        return new String[]{"edit"};
    }

    public String[] getAttrAdd(){
        return new String[]{"add"};
    }

    public String[] getAttrDel(){
        return new String[]{"del"};
    }

    public String[] getAttrApprove(){
        return new String[]{"approve"};
    }

    public void setCache(String key, String data){
        RedisUtil redisUtil= (RedisUtil) SpringContext.getBean("redisUtil");
        redisUtil.set(key, data, 300L, TimeUnit.SECONDS);
    }

    public String getCache(String key){
        RedisUtil redisUtil= (RedisUtil) SpringContext.getBean("redisUtil");
        return redisUtil.get(key,"").toString();
    }

    public String dynamicPermissionByProductAndGroupKey(String owner){
        return  ZDH_PERMISSION_PRODUCT_DIM_GROUP_SELECT+"-"+owner;
    }

    /**
     *
     * 获取当前用户绑定的产品信息
     * 获取当前用户绑定的【归属组】信息
     * 获取当前用户所属组绑定的【归属组】信息
     * todo 用户所在组和归属组是2个概念, 所在组一般和组织架构对应, 归属组则属于权限控制
     * 支持缓存查询
     * @param zdhPermissionService
     * @throws Exception
     */
    public Map<String,List<String>> dynamicPermissionByProductAndGroup(ZdhPermissionService zdhPermissionService) throws Exception {
        Map<String,List<String>> dimMap = new HashMap<>();

        String cacheKey = dynamicPermissionByProductAndGroupKey(getOwner());
        //检查缓存
        String cache = getCache(cacheKey);
        if(!StringUtils.isEmpty(cache)){
            return JSON.parseObject(cache, new TypeReference<Map<String,List<String>>>(){});
        }

        //查询用户是否有所在组
        UserAndGroupPermissionDimensionValueInfo dim_permission = zdhPermissionService.get_dim_permission(getProductCode(), getOwner(), getUserGroup());

        List<String> dim_groups = dim_permission.getDim_groups();

        if(dim_groups != null && dim_groups.size() == 0){
            dim_groups.add("-1");
        }

        List<String> dim_products = dim_permission.getDim_products();

        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }

        dimMap.put("dim_groups", dim_groups);
        dimMap.put("product_codes",dim_products);

        setCache(cacheKey, JsonUtil.formatJsonString(dimMap));
        return dimMap;
    }

    /**
     * 通过归属产品+归属组双重控制数据权限
     *
     * 对于查询权限,支持外部归属组控制,需要单独配置dim_group_select维度信息
     *
     * @param zdhPermissionService
     * @param criteria
     * @throws Exception
     */
    public void dynamicPermissionByProductAndGroup(ZdhPermissionService zdhPermissionService,  Example.Criteria criteria) throws Exception {
        Map<String,List<String>> dimMap = new HashMap<>();
        String cacheKey = dynamicPermissionByProductAndGroupKey(getOwner());
        //检查缓存
        String cache = getCache(cacheKey);
        if(!StringUtils.isEmpty(cache)){
            dimMap = JSON.parseObject(cache, new TypeReference<Map<String,List<String>>>(){});
            criteria.andIn("dim_group", dimMap.getOrDefault("dim_groups", Lists.newArrayList("-1")));
            criteria.andIn("product_code", dimMap.getOrDefault("product_codes", Lists.newArrayList("-1")));
            return;
        }

        //查询用户是否有所在组
        UserAndGroupPermissionDimensionValueInfo dim_permission = zdhPermissionService.get_dim_permission(getProductCode(), getOwner(), getUserGroup());

        List<String> dim_groups = dim_permission.getDim_groups();

        if(dim_groups != null && dim_groups.size() == 0){
            dim_groups.add("-1");
        }

        List<String> dim_products = dim_permission.getDim_products();

        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }


        dimMap.put("dim_groups", dim_groups);
        dimMap.put("product_codes", dim_products);

        setCache(cacheKey, JsonUtil.formatJsonString(dimMap));
        criteria.andIn("dim_group", dim_groups);
        criteria.andIn("product_code", dim_products);
    }

    /**
     * 通过归属产品控制数据权限
     * @param zdhPermissionService
     * @param criteria
     * @throws Exception
     */
    public void dynamicPermissionByProduct(ZdhPermissionService zdhPermissionService,  Example.Criteria criteria) throws Exception {

        //根据账号,查询归属产品
        UserAndGroupPermissionDimensionValueInfo dim_permission = zdhPermissionService.get_dim_permission(getProductCode(), getOwner(), getUserGroup());

        List<String> dim_products = dim_permission.getDim_products();
        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }
        criteria.andIn("product_code", dim_products);
    }

    /**
     * 查询用户及用户组绑定的产品code集合
     * @param zdhPermissionService
     * @return
     * @throws Exception
     */
    public List<String> getPermissionByProduct(ZdhPermissionService zdhPermissionService) throws Exception {

        //根据账号,查询归属产品
        UserAndGroupPermissionDimensionValueInfo dim_permission = zdhPermissionService.get_dim_permission(getProductCode(), getOwner(), getUserGroup());

        List<String> dim_products = dim_permission.getDim_products();
        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }
        return dim_products;
    }

    /**
     * 通过归属产品控制数据权限
     * @param zdhPermissionService
     * @param criteria
     * @throws Exception
     */
    public void dynamicPermissionByProductAndFilterProduct(ZdhPermissionService zdhPermissionService,  Example.Criteria criteria, String product_code) throws Exception {

        //根据账号,查询归属产品
        UserAndGroupPermissionDimensionValueInfo dim_permission = zdhPermissionService.get_dim_permission(getProductCode(), getOwner(), getUserGroup());

        List<String> dim_products = dim_permission.getDim_products();
        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }

        criteria.andIn("product_code", dim_products);

        if(!StringUtils.isEmpty(product_code)){
            criteria.andEqualTo("product_code", product_code);
        }
    }

    /**
     * 通过主键查询信息,并验证是否有产品和用户组权限
     * 当前数据中不包含产品,用户组字段时,不校验权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @param actions 权限行为,select, edit, add, del, approve
     * @throws Exception
     */
    public void checkAttrPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids, String[] actions) throws Exception {
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        List<Map<String,Object>> list = baseMapper.selectListMapByIds(table, ids);
        if(list == null || list.size() == 0){
            throw new Exception("权限验证,未找到相关数据信息");
        }
        for(Map<String, Object> objectMap : list){
            if(objectMap.containsKey("product_code")){
                String product_code = objectMap.getOrDefault("product_code", "").toString();
                if(!dims.get("product_codes").contains(product_code)){
                    throw new Exception("无产品权限,产品code: "+product_code);
                }
            }
            if(objectMap.containsKey("dim_group")){
                String dim_group = objectMap.getOrDefault("dim_group", "").toString();
                if(!dims.get("dim_groups").contains(dim_group)){
                    throw new Exception("无归属组权限,归属组code: "+dim_group);
                }

                //检查是否有维度对应自定义权限(增,删,改,审批等)
                Map<String, Map<String, String>> dim_group_attrs = zdhPermissionService.get_dim_value_attr_by_user_account(getProductCode(),getOwner(),getUserGroup(), "dim_group");
                for (String action: actions){
                    if(action.equalsIgnoreCase("select")){
                        continue;
                    }
                    if(!dim_group_attrs.containsKey(dim_group)){
                        throw new Exception("无归属组权限,归属组code: "+dim_group);
                    }
                    if(!dim_group_attrs.get(dim_group).containsKey(action) || !dim_group_attrs.get(dim_group).get(action).equalsIgnoreCase("true")){
                        throw new Exception("归属组code: "+dim_group+", 无"+action+"权限");
                    }
                }

            }
        }
    }


    /**
     * 通过主键查询信息,并验证是否有产品和用户组编辑权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @throws Exception
     */
    public void checkEditPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids) throws Exception {
        checkAttrPermissionByProductAndDimGroup(zdhPermissionService, baseMapper, table, ids, new String[]{"eidt"});
    }

    /**
     * 通过主键查询信息,并验证是否有产品和用户组新增权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @throws Exception
     */
    public void checkAddPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids) throws Exception {
        checkAttrPermissionByProductAndDimGroup(zdhPermissionService, baseMapper, table, ids, new String[]{"add"});
    }

    /**
     * 通过主键查询信息,并验证是否有产品和用户组删除权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @throws Exception
     */
    public void checkDelPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids) throws Exception {
        checkAttrPermissionByProductAndDimGroup(zdhPermissionService, baseMapper, table, ids, new String[]{"del"});
    }

    /**
     * 通过主键查询信息,并验证是否有产品和用户组审批权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @throws Exception
     */
    public void checkApprovePermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids) throws Exception {
        checkAttrPermissionByProductAndDimGroup(zdhPermissionService, baseMapper, table, ids, new String[]{"approve"});
    }

    /**
     * 通过主键查询信息,并验证是否有产品和用户组权限
     * @param zdhPermissionService
     * @param baseMapper
     * @param table
     * @param ids
     * @throws Exception
     */
    public void checkPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, BaseMapper baseMapper, String table, String[] ids) throws Exception {
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        List<Map<String,Object>> list = baseMapper.selectListMapByIds(table, ids);
        if(list == null || list.size() == 0){
            throw new Exception("权限验证,未找到相关数据信息");
        }
        for(Map<String, Object> objectMap : list){
            if(objectMap.containsKey("product_code")){
                String product_code = objectMap.getOrDefault("product_code", "").toString();
                if(!dims.get("product_codes").contains(product_code)){
                    throw new Exception("无产品权限,产品code: "+product_code);
                }
            }
            if(objectMap.containsKey("dim_group")){
                String dim_group = objectMap.getOrDefault("dim_group", "").toString();
                if(!dims.get("dim_groups").contains(dim_group)){
                    throw new Exception("无归属组权限,归属组code: "+dim_group);
                }
            }
        }
    }

    /**
     * 通过指定产品和用户组校验是否有对应权限
     * @param zdhPermissionService
     * @param product_code
     * @param dim_group
     * @throws Exception
     */
    public void checkPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, String product_code, String dim_group) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品code为空");
        }
        if(StringUtils.isEmpty(dim_group)){
            throw new Exception("维度-归属组为空");
        }
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        if(!dims.get("product_codes").contains(product_code)){
            throw new Exception("无产品权限,产品code: "+product_code);
        }
        if(!dims.get("dim_groups").contains(dim_group)){
            throw new Exception("无归属组权限,归属组code: "+dim_group);
        }
    }

    /**
     * 通过指定产品和用户组校验是否有对应(增,删,改,查)权限
     * @param zdhPermissionService
     * @param product_code
     * @param dim_group
     * @param actions
     * @throws Exception
     */
    public void checkAttrPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, String product_code, String dim_group, String[] actions) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品code为空");
        }
        if(StringUtils.isEmpty(dim_group)){
            throw new Exception("维度-归属组为空");
        }
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        if(!dims.get("product_codes").contains(product_code)){
            throw new Exception("无产品权限,产品code: "+product_code);
        }
        if(!dims.get("dim_groups").contains(dim_group)){
            throw new Exception("无归属组权限,归属组code: "+dim_group);
        }

        //检查是否有维度对应自定义权限(增,删,改,审批等)
        Map<String, Map<String, String>> dim_group_attrs = zdhPermissionService.get_dim_value_attr_by_user_account(getProductCode(),getOwner(),getUserGroup(), "dim_group");
        for (String action: actions){
            if(action.equalsIgnoreCase("select")){
                continue;
            }
            if(!dim_group_attrs.containsKey(dim_group)){
                throw new Exception("无归属组权限,归属组code: "+dim_group);
            }
            if(!dim_group_attrs.get(dim_group).containsKey(action) || !dim_group_attrs.get(dim_group).get(action).equalsIgnoreCase("true")){
                throw new Exception("归属组code: "+dim_group+", 无"+action+"权限");
            }
        }

    }


    /**
     * 通过指定产品校验是否有对应(增,删,改,查)权限
     * @param zdhPermissionService
     * @param product_code
     * @param actions
     * @throws Exception
     */
    public void checkAttrPermissionByProduct(ZdhPermissionService zdhPermissionService, String product_code, String[] actions) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品code为空");
        }
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        if(!dims.get("product_codes").contains(product_code)){
            throw new Exception("无产品权限,产品code: "+product_code);
        }


        //检查是否有维度对应自定义权限(增,删,改,审批等)
        Map<String, Map<String, String>> dim_product_attrs = zdhPermissionService.get_dim_value_attr_by_user_account(getProductCode(),getOwner(),getUserGroup(), "dim_product");
        for (String action: actions){
            if(action.equalsIgnoreCase("select")){
                continue;
            }
            if(!dim_product_attrs.containsKey(product_code)){
                throw new Exception("无归属产品权限,归属产品code: "+product_code);
            }
            if(!dim_product_attrs.get(product_code).containsKey(action) || !dim_product_attrs.get(product_code).get(action).equalsIgnoreCase("true")){
                throw new Exception("归属产品code: "+product_code+", 无"+action+"权限");
            }
        }

    }

    /**
     * 通过产品校验权限
     * @param zdhPermissionService
     * @param product_code
     * @throws Exception
     */
    public void checkPermissionByProduct(ZdhPermissionService zdhPermissionService, String product_code) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw new Exception("产品code为空");
        }
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        if(!dims.get("product_codes").contains(product_code)){
            throw new Exception("无产品权限,产品code: "+product_code);
        }
    }

    /**
     * 返回 %conditon% 格式
     * @param condition
     * @return
     */
    public String getLikeCondition(String condition){
        return "%"+condition+"%";
    }

    /**
     * 检查产品code是否为空,为空直接抛出异常
     * @param product_code
     * @return
     * @throws Exception
     */
    public boolean checkProductCode(String product_code) throws Exception {
        if(StringUtils.isEmpty(product_code)){
            throw  new Exception("产品code参数不可为空");
        }
        return true;
    }

    /**
     * 检查参数是否为空,为空直接抛出异常
     * @param param
     * @param name
     * @return
     * @throws Exception
     */
    public boolean checkParam(String param, String name) throws Exception {
        if(StringUtils.isEmpty(param)){
            throw  new Exception(name+"参数不可为空");
        }
        return true;
    }

    /**
     * 当前登录用户操作产品权限(仅产品管理员)
     * 权限模块-都是用此控制
     * @param product_code
     * @return
     */
    public boolean checkPermissionByOwner(String product_code) throws Exception {
        boolean isPermission = checkPermission(product_code, getOwner());
        if(!isPermission){
            throw new Exception("产品code: "+product_code+" ,无权限");
        }
        return isPermission;
    }
    /**
     * 用户操作产品权限(仅产品管理员)
     * 权限模块-都是用此控制
     * @param product_code
     * @param owner
     * @return
     */
    public boolean checkPermission(String product_code, String owner){
        try{

            if(StringUtils.isEmpty(product_code) || StringUtils.isEmpty(owner)){
                throw new Exception("产品code或者用户账号为空");
            }

            Example example=new Example(ProductTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andCondition("find_in_set('"+owner+"', product_admin)");
            criteria.andEqualTo("product_code", product_code);
            ProductTagMapper productTagMapper = (ProductTagMapper) SpringContext.getBean("productTagMapper");
            int count = productTagMapper.selectCountByExample(example);

            if(count != 1){
                return false;
            }
            return true;
        }catch (Exception e){
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
        }

        return false;
    }


    public Exception getBaseException(){

        return getBaseException(null);
    }

    public Exception getBaseException(Exception e){
        return e;
    }



}
