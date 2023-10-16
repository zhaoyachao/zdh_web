package com.zyc.zdh.controller;

import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.ProductTagMapper;
import com.zyc.zdh.entity.PermissionDimensionValueInfo;
import com.zyc.zdh.entity.ProductTagInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.service.ZdhPermissionService;
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
import java.util.List;

public class BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());
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

    /**
     * 通过归属产品+归属组双重控制数据权限
     * @param zdhPermissionService
     * @param criteria
     * @throws Exception
     */
    public void dynamicPermissionByProductAndGroup(ZdhPermissionService zdhPermissionService,  Example.Criteria criteria) throws Exception {
        //根据账号,查询归属组
        List<PermissionDimensionValueInfo> dim_group_pdvi = zdhPermissionService.get_dim_group(getOwner());
        List<String> dim_groups = zdhPermissionService.dim_value2code(dim_group_pdvi);

        //根据账号,查询归属产品
        List<PermissionDimensionValueInfo> dim_product_pdvi = zdhPermissionService.get_dim_product(getOwner());
        List<String> dim_products = zdhPermissionService.dim_value2code(dim_product_pdvi);

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

        criteria.andIn("product_code", dim_products);
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
