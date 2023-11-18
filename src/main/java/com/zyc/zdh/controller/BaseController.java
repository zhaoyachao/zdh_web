package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.ProductTagMapper;
import com.zyc.zdh.entity.PermissionDimensionValueInfo;
import com.zyc.zdh.entity.PermissionUserDimensionValueInfo;
import com.zyc.zdh.entity.ProductTagInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

    public String getProductCode(){
        Environment environment= (Environment) SpringContext.getBean("environment");
        return environment.getProperty("zdp.product","");
    }


    public void setCache(String key, String data){
        RedisUtil redisUtil= (RedisUtil) SpringContext.getBean("redisUtil");
        redisUtil.set(key, data, 300L, TimeUnit.SECONDS);
    }

    public String getCache(String key){
        RedisUtil redisUtil= (RedisUtil) SpringContext.getBean("redisUtil");
        return redisUtil.get(key,"").toString();
    }

    /**
     * 通过归属产品+归属组双重控制数据权限
     *
     * 对于查询权限,支持外部归属组控制,需要单独配置dim_group_select维度信息
     *
     * 支持缓存查询
     * @param zdhPermissionService
     * @throws Exception
     */
    public Map<String,List<String>> dynamicPermissionByProductAndGroup(ZdhPermissionService zdhPermissionService) throws Exception {
        Map<String,List<String>> dimMap = new HashMap<>();

        String cacheKey = "zdh_permission_product_dim_group_select_"+getOwner();
        //检查缓存
        String cache = getCache(cacheKey);
        if(!StringUtils.isEmpty(cache)){
            return JSON.parseObject(cache, new TypeReference<Map<String,List<String>>>(){});
        }

        //根据账号,查询归属组
        List<PermissionDimensionValueInfo> dim_group_pdvi = zdhPermissionService.get_dim_group(getOwner());
        List<String> dim_groups = zdhPermissionService.dim_value2code(dim_group_pdvi);

        if(dim_groups != null && dim_groups.size() == 0){
            dim_groups.add("-1");
        }

        //根据账号-查询组外查询权限
        List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = zdhPermissionService.get_dim_value_by_user_account(getOwner(), "dim_group_select");
        if(permissionUserDimensionValueInfos !=  null && permissionUserDimensionValueInfos.size() >= 1){
            for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: permissionUserDimensionValueInfos){
                dim_groups.add(permissionUserDimensionValueInfo.getDim_value_code());
            }
        }

        //根据账号,查询归属产品
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);

        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }

        dimMap.put("dim_groups", dim_groups);
        dimMap.put("product_codes", dim_products);

        setCache(cacheKey, JSON.toJSONString(dimMap));
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
        String cacheKey = "zdh_permission_product_dim_group_select_"+getOwner();
        //检查缓存
        String cache = getCache(cacheKey);
        if(!StringUtils.isEmpty(cache)){
            dimMap = JSON.parseObject(cache, new TypeReference<Map<String,List<String>>>(){});
            criteria.andIn("dim_group", dimMap.getOrDefault("dim_groups", Lists.newArrayList("-1")));
            criteria.andIn("product_code", dimMap.getOrDefault("product_codes", Lists.newArrayList("-1")));
            return;
        }

        //根据账号,查询归属组
        List<PermissionDimensionValueInfo> dim_group_pdvi = zdhPermissionService.get_dim_group(getOwner());
        List<String> dim_groups = zdhPermissionService.dim_value2code(dim_group_pdvi);
        if(dim_groups != null && dim_groups.size() == 0){
            dim_groups.add("-1");
        }

        //根据账号-查询组外查询权限
        List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = zdhPermissionService.get_dim_value_by_user_account(getOwner(), "dim_group_select");
        if(permissionUserDimensionValueInfos !=  null && permissionUserDimensionValueInfos.size() >= 1){
            for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: permissionUserDimensionValueInfos){
                dim_groups.add(permissionUserDimensionValueInfo.getDim_value_code());
            }
        }

        //根据账号,查询归属产品
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);

        if(dim_products != null && dim_products.size() == 0){
            dim_products.add("-1");
        }
        dimMap.put("dim_groups", dim_groups);
        dimMap.put("product_codes", dim_products);

        setCache(cacheKey, JSON.toJSONString(dimMap));
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
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);
        if(dim_products != null && dim_products.size()==0){
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
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);
        if(dim_products != null && dim_products.size()==0){
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
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);

        if(dim_products != null && dim_products.size()==0){
            dim_products.add("-1");
        }
        criteria.andIn("product_code", dim_products);

        if(!StringUtils.isEmpty(product_code)){
            criteria.andEqualTo("product_code", product_code);
        }
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
     * 通过产品和用户组校验权限
     * @param zdhPermissionService
     * @param product_code
     * @param dim_group
     * @throws Exception
     */
    public void checkPermissionByProductAndDimGroup(ZdhPermissionService zdhPermissionService, String product_code, String dim_group) throws Exception {
        Map<String, List<String>> dims = dynamicPermissionByProductAndGroup(zdhPermissionService);
        if(!dims.get("product_codes").contains(product_code)){
            throw new Exception("无产品权限,产品code: "+product_code);
        }
        if(!dims.get("dim_groups").contains(dim_group)){
            throw new Exception("无归属组权限,归属组code: "+dim_group);
        }
    }


    /**
     * 通过产品校验权限
     * @param zdhPermissionService
     * @param product_code
     * @throws Exception
     */
    public void checkPermissionByProduct(ZdhPermissionService zdhPermissionService, String product_code) throws Exception {
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

    public boolean checkPermissionByOwner(String product_code) throws Exception {
        boolean isPermission = checkPermission(product_code, getOwner());
        if(!isPermission){
            throw new Exception("产品code: "+product_code+" ,无权限");
        }
        return isPermission;
    }
    /**
     * 检查用户是否拥有操作产品的权限
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
