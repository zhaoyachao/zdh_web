package com.zyc.zdh.service.impl;

import com.google.common.collect.Sets;
import com.zyc.zdh.controller.PermissionApiController;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.MapStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * zdh 数据权限控制
 */
@Service
public class ZdhPermissionServiceImpl implements ZdhPermissionService {

    @Autowired
    private Environment env;

    @Autowired
    private PermissionApiController permissionApiController;


    @Override
    public List<PermissionUserDimensionValueInfo> get_dim_value_by_user_account(String user_account, String dim_code) throws Exception {
        String product_code = env.getProperty("zdp.product");
        String ak = env.getProperty("zdp.ak");
        String sk = env.getProperty("zdp.sk");
        return get_dim_value_by_user_account(product_code, ak, sk, user_account, dim_code);
    }

    /**
     * 根据账号和维度 获取维度值包含的属性
     * @param user_account
     * @param dim_code
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Map<String,String>> get_dim_value_attr_by_user_account(String user_account, String dim_code) throws Exception {

        List<PermissionUserDimensionValueInfo> dim_value_by_user_account = get_dim_value_by_user_account(user_account, dim_code);

        Map<String, Map<String,String>> dim_value_attrs = new HashMap<>();
        for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: dim_value_by_user_account){
            if(permissionUserDimensionValueInfo.getId().startsWith("group_")){
                //组维度绑定信息
                dim_value_attrs.put(permissionUserDimensionValueInfo.getDim_value_code(), permissionUserDimensionValueInfo.getExtMap()==null?new HashMap<>():permissionUserDimensionValueInfo.getExtMap());
            }
        }
        for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: dim_value_by_user_account){
            if(!permissionUserDimensionValueInfo.getId().startsWith("group_")){
                //用户维度绑定信息
                if(dim_value_attrs.containsKey(permissionUserDimensionValueInfo.getDim_value_code())){
                    Map<String,String> atrts = dim_value_attrs.get(permissionUserDimensionValueInfo.getDim_value_code());
                    if(permissionUserDimensionValueInfo.getExtMap()!=null){
                        for(Map.Entry<String, String> entry: permissionUserDimensionValueInfo.getExtMap().entrySet()){
                            atrts.put(entry.getKey(), entry.getValue());
                        }
                    }
                }else{
                    if(permissionUserDimensionValueInfo.getExtMap()!=null){
                        dim_value_attrs.put(permissionUserDimensionValueInfo.getDim_value_code(), permissionUserDimensionValueInfo.getExtMap()==null?new HashMap<>():permissionUserDimensionValueInfo.getExtMap());
                    }
                }
            }
        }

        return dim_value_attrs;
    }

    /**
     * 根据用户账号,获取账号及账号用户组绑定的维度信息
     * @param user_account
     * @param dim_code
     */
    private List<PermissionUserDimensionValueInfo> get_dim_value_by_user_account(String product_code, String ak, String sk, String user_account, String dim_code) throws Exception {
        //1 根据账号获取绑定的用户组

        ReturnInfo<PermissionUserInfo> user = permissionApiController.get_user_by_product(product_code, ak, sk, user_account);
        if(!user.getCode().equalsIgnoreCase(RETURN_CODE.SUCCESS.getCode())){
            throw user.getE();
        }
        String user_group = ((PermissionUserInfo)user.getResult()).getUser_group();
        //2 获取账号邦迪的维度值
        ReturnInfo<List<PermissionUserDimensionValueInfo>> udim = permissionApiController.get_user_dimension_value_list_by_product(user_account, product_code, ak, sk);
        List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = new ArrayList<>();

        if(udim.getCode().equalsIgnoreCase(RETURN_CODE.SUCCESS.getCode())){
            if(udim.getResult() != null){
                for(PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: ((List<PermissionUserDimensionValueInfo>)udim.getResult())){
                    if(permissionUserDimensionValueInfo.getDim_code().equalsIgnoreCase(dim_code)){
                        permissionUserDimensionValueInfos.add(permissionUserDimensionValueInfo);
                    }
                }
            }
        }else{
            throw udim.getE();
        }

        //3 获取用户组绑定的维度值
        ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> gdim=permissionApiController.get_usergroup_dimension_value_list_by_product(user_group, product_code, ak, sk);
        if(gdim.getCode().equalsIgnoreCase(RETURN_CODE.SUCCESS.getCode())){
            if(gdim.getResult() != null){
                for( PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo: ((List<PermissionUserGroupDimensionValueInfo>)gdim.getResult())){
                    if(permissionUserGroupDimensionValueInfo.getDim_code().equalsIgnoreCase(dim_code)){
                        PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = new PermissionUserDimensionValueInfo();
                        //BeanUtils.copyProperties(permissionUserDimensionValueInfo, permissionUserGroupDimensionValueInfo);
                        permissionUserDimensionValueInfo = MapStructMapper.INSTANCE.permissionUserGroupDimensionValueInfoToPermissionUserDimensionValueInfo(permissionUserGroupDimensionValueInfo);
                        permissionUserDimensionValueInfo.setUser_account(user_account);
                        permissionUserDimensionValueInfo.setId("group_"+permissionUserDimensionValueInfo.getId());
                        permissionUserDimensionValueInfos.add(permissionUserDimensionValueInfo);
                    }

                }
            }
        }else{
            throw gdim.getE();
        }

        return permissionUserDimensionValueInfos;
    }


    @Override
    public List<PermissionDimensionValueInfo> get_dim_value_list(String dim_code) throws Exception {
        String product_code = env.getProperty("zdp.product");
        String ak = env.getProperty("zdp.ak");
        String sk = env.getProperty("zdp.sk");
        ReturnInfo<List<PermissionDimensionValueInfo>> returnInfo =  permissionApiController.get_dimension_value_list_by_product(product_code, ak, sk);
        returnInfo.checkExeception();
        List<PermissionDimensionValueInfo> permissionDimensionValueInfoList = new ArrayList<>();
        for(PermissionDimensionValueInfo permissionDimensionValueInfo: (List<PermissionDimensionValueInfo>)returnInfo.getResult()){
            if(permissionDimensionValueInfo.getDim_code().equalsIgnoreCase(dim_code)){
                permissionDimensionValueInfoList.add(permissionDimensionValueInfo);
            }
        }
        return permissionDimensionValueInfoList;
    }

    /**
     * 查询维度通用
     * 目前会查询当前账号及对应的组绑定的维度信息
     * tips: 如果维度以树形结构构造,建议只选择最后一级节点(目前不返回树形结构-后续待优化)
     * @param user_account
     * @param dim_code
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionDimensionValueInfo> get_dim_value_list_by_user(String user_account, String dim_code) throws Exception {

        Set<String> dim_value_codes = Sets.newHashSet();
        List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = get_dim_value_by_user_account(user_account,dim_code);
        for(PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: permissionUserDimensionValueInfos){
            dim_value_codes.add(permissionUserDimensionValueInfo.getDim_value_code());
        }

        //根据维度code查询维度值
        List<PermissionDimensionValueInfo> permissionDimensionValueInfoList = get_dim_value_list(dim_code);

        Map<String,PermissionDimensionValueInfo> dimValMap = new HashMap<>();
        for (PermissionDimensionValueInfo permissionDimensionValueInfo: permissionDimensionValueInfoList){
            dimValMap.put(permissionDimensionValueInfo.getDim_value_code(), permissionDimensionValueInfo);
        }

        List<PermissionDimensionValueInfo> result = new ArrayList<>();
        for (String dim_value_code: dim_value_codes){
            result.add(dimValMap.get(dim_value_code));
        }

        return result;
    }


    /**
     * 查询归属组维度
     * 目前会查询当前账号及对应的组绑定的维度信息
     *
     * @param user_account
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionDimensionValueInfo> get_dim_group(String user_account) throws Exception {
        return get_dim_value_list_by_user(user_account, "dim_group");
    }

    /**
     * 查询归属产品维度
     * 目前会查询当前账号及对应的组绑定的维度信息
     *
     * @param user_account
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionDimensionValueInfo> get_dim_product(String user_account) throws Exception {
        return get_dim_value_list_by_user(user_account, "dim_product");
    }

    @Override
    public List<String> dim_value2code(List<PermissionDimensionValueInfo> permissionDimensionValueInfos) throws Exception {
        List<String> dim_value_codes = new ArrayList<>();
        if(permissionDimensionValueInfos != null){
            for (PermissionDimensionValueInfo permissionDimensionValueInfo: permissionDimensionValueInfos){
                dim_value_codes.add(permissionDimensionValueInfo.getDim_value_code());
            }
        }
        return dim_value_codes;
    }


}
