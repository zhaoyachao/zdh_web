package com.zyc.zdh.entity;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一权限实体
 */
public class UserAndGroupPermissionDimensionValueInfo {
    private String user_account;
    private String user_group;
    private String product_code;
    private List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos;
    private List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos;
    private List<String> dim_groups;
    private List<String> dim_products;


    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public List<PermissionUserDimensionValueInfo> getPermissionUserDimensionValueInfos() {
        return permissionUserDimensionValueInfos;
    }

    public void setPermissionUserDimensionValueInfos(List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos) {
        this.permissionUserDimensionValueInfos = permissionUserDimensionValueInfos;
    }

    public List<PermissionUserGroupDimensionValueInfo> getPermissionUserGroupDimensionValueInfos() {
        return permissionUserGroupDimensionValueInfos;
    }

    public void setPermissionUserGroupDimensionValueInfos(List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos) {
        this.permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueInfos;
    }

    public List<String> getDimValueCodeByDimCode(String dim_code){
        List<String> dim_values = Lists.newArrayList();
        if(permissionUserGroupDimensionValueInfos!=null && permissionUserGroupDimensionValueInfos.size() > 0){
            dim_values.addAll(permissionUserGroupDimensionValueInfos.stream().filter(s->s.getDim_code().equalsIgnoreCase(dim_code)).map(s->s.getDim_value_code()).collect(Collectors.toList()));
        }
        if(permissionUserDimensionValueInfos!=null && permissionUserDimensionValueInfos.size() > 0){
            dim_values.addAll(permissionUserDimensionValueInfos.stream().filter(s->s.getDim_code().equalsIgnoreCase(dim_code)).map(s->s.getDim_value_code()).collect(Collectors.toList()));
        }
        return dim_values;
    }

    public List<String> getDim_groups() {
        return getDimValueCodeByDimCode("dim_group");
    }

    public void setDim_groups(List<String> dim_groups) {
        this.dim_groups = dim_groups;
    }

    public List<String> getDim_products() {
        return getDimValueCodeByDimCode("dim_product");
    }

    public void setDim_products(List<String> dim_products) {
        this.dim_products = dim_products;
    }

    public List<PermissionUserGroupDimensionValueInfo> getPermissionUserGroupDimensionValueInfosByDimCode(String dim_code){
        return permissionUserGroupDimensionValueInfos.stream().filter(s->s.getDim_code().equalsIgnoreCase(dim_code)).collect(Collectors.toList());
    }

    public List<PermissionUserDimensionValueInfo> getPermissionUserDimensionValueInfosByDimCode(String dim_code){
        return permissionUserDimensionValueInfos.stream().filter(s->s.getDim_code().equalsIgnoreCase(dim_code)).collect(Collectors.toList());
    }

    public static UserAndGroupPermissionDimensionValueInfo build(String product_code, String user_account, String user_group,
                                                                 List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos,
                                                                 List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos){
        UserAndGroupPermissionDimensionValueInfo userAndGroupPermissionDimensionValueInfo = new UserAndGroupPermissionDimensionValueInfo();

        userAndGroupPermissionDimensionValueInfo.setProduct_code(product_code);
        userAndGroupPermissionDimensionValueInfo.setUser_account(user_account);
        userAndGroupPermissionDimensionValueInfo.setUser_group(user_group);
        userAndGroupPermissionDimensionValueInfo.setPermissionUserDimensionValueInfos(permissionUserDimensionValueInfos);
        userAndGroupPermissionDimensionValueInfo.setPermissionUserGroupDimensionValueInfos(permissionUserGroupDimensionValueInfos);

        return userAndGroupPermissionDimensionValueInfo;

    }
}
