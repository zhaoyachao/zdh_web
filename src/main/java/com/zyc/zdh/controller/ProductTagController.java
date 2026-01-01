package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品标识服务
 */
@Controller
public class ProductTagController extends BaseController {

    @Autowired
    private ProductTagMapper productTagMapper;

    @Autowired
    private ResourceTreeMapper resourceTreeMapper;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private PermissionDimensionMapper permissionDimensionMapper;

    @Autowired
    private PermissionDimensionValueMapper permissionDimensionValueMapper;

    /**
     * 产品列表首页
     * @return
     */
    @RequestMapping(value = "/product_tag_index", method = RequestMethod.GET)
    public String product_tag_index() {

        return "admin/product_tag_index";
    }


    /**
     * 产品列表
     * 所有平台可用,查询当前所有产品列表-一般用在申请产品权限
     * @param tag_context 关键字
     * @return
     */
    @SentinelResource(value = "product_tag_all", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProductTagInfo>> product_tag_all(String tag_context) {
        try{
            Example example=new Example(ProductTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            Example.Criteria criteria2=example.createCriteria();
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tag_context)){
                criteria2.orLike("product_code", getLikeCondition(tag_context));
                criteria2.orLike("product_name", getLikeCondition(tag_context));
                example.and(criteria2);
            }

            List<ProductTagInfo> productTagInfos = productTagMapper.selectByExample(example);

            for (ProductTagInfo pti: productTagInfos){
                pti.setSk("");
                pti.setAk("");
            }
            return ReturnInfo.buildSuccess(productTagInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 产品列表(带权限控制)
     * 当前仅支持zdh平台权限是使用,原因如下：zdh和权限平台在同一个项目中,无法使用zdh权限控制zdh
     * @param tag_context 关键字
     * @return
     */
    @SentinelResource(value = "product_tag_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProductTagInfo>> product_tag_list(String tag_context) {
        try{
            Example example=new Example(ProductTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andCondition("find_in_set('"+getOwner()+"', product_admin)");
            Example.Criteria criteria2=example.createCriteria();
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tag_context)){
                criteria2.orLike("product_code", getLikeCondition(tag_context));
                criteria2.orLike("product_name", getLikeCondition(tag_context));
                example.and(criteria2);
            }

            List<ProductTagInfo> productTagInfos = productTagMapper.selectByExample(example);

            for (ProductTagInfo pti: productTagInfos){
                pti.setSk("");
                pti.setAk("");
            }
            return ReturnInfo.buildSuccess(productTagInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 产品新增首页
     * @return
     */
    @RequestMapping(value = "/product_tag_add_index", method = RequestMethod.GET)
    public String product_tag_add_index() {

        return "admin/product_tag_add_index";
    }


    /**
     * 产品明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "product_tag_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ProductTagInfo> product_tag_detail(String id) {
        try {
            ProductTagInfo productTagInfo = productTagMapper.selectByPrimaryKey(id);
            checkPermissionByOwner(productTagInfo.getProduct_code());
            productTagInfo.setAk("");
            productTagInfo.setSk("");
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", productTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 产品更新
     * @param productTagInfo
     * @return
     */
    @SentinelResource(value = "product_tag_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> product_tag_update(ProductTagInfo productTagInfo) {
        try {
            ProductTagInfo oldProductTagInfo = productTagMapper.selectByPrimaryKey(productTagInfo.getId());
            checkPermissionByOwner(oldProductTagInfo.getProduct_code());

            productTagInfo.setOwner(oldProductTagInfo.getOwner());
            productTagInfo.setCreate_time(oldProductTagInfo.getCreate_time());
            productTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            productTagInfo.setIs_delete(Const.NOT_DELETE);
            productTagInfo.setAk(oldProductTagInfo.getAk());
            productTagInfo.setSk(oldProductTagInfo.getSk());
            productTagInfo.setStatus(oldProductTagInfo.getStatus());
            productTagMapper.updateByPrimaryKeySelective(productTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 产品新增
     * @param productTagInfo
     * @return
     */
    @SentinelResource(value = "product_tag_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> product_tag_add(ProductTagInfo productTagInfo) {
        try {
            //检查code是否存在
            ProductTagInfo pti=new ProductTagInfo();
            pti.setProduct_code(productTagInfo.getProduct_code());
            List<ProductTagInfo> productTagInfos = productTagMapper.select(pti);
            if(productTagInfos.size()>0){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", productTagInfo.getProduct_code()+",产品code已存在");
            }

            productTagInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            productTagInfo.setStatus(Const.PRODUCT_ENABLE);
            productTagInfo.setAk(DigestUtils.md5DigestAsHex((SnowflakeIdWorker.getInstance().nextId()+"").getBytes()));
            productTagInfo.setSk(DigestUtils.md5DigestAsHex((SnowflakeIdWorker.getInstance().nextId()+"").getBytes()));
            productTagInfo.setOwner(getOwner());
            productTagInfo.setIs_delete(Const.NOT_DELETE);
            if(StringUtils.isEmpty(productTagInfo.getProduct_admin())){
                productTagInfo.setProduct_admin(getOwner());
            }
            productTagInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            productTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            productTagMapper.insertSelective(productTagInfo);

            //同步默认产品zdh产品下的所有资源
            Example example=new Example(ResourceTreeInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("product_code", ConfigUtil.getProductCode());
            List<ResourceTreeInfo> resourceTreeInfos = resourceTreeMapper.selectByExample(example);
            Map<String, String> idMap = new HashMap<>();
            for(ResourceTreeInfo resourceTreeInfo: resourceTreeInfos){
                idMap.put(resourceTreeInfo.getId(), String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
                resourceTreeInfo.setProduct_code(productTagInfo.getProduct_code());
            }

            for(ResourceTreeInfo resourceTreeInfo: resourceTreeInfos){
                resourceTreeInfo.setId(idMap.get(resourceTreeInfo.getId()));
                if(!resourceTreeInfo.getLevel().equalsIgnoreCase("1")){
                    //根节点,不修改父节点
                    resourceTreeInfo.setParent(idMap.get(resourceTreeInfo.getParent()));
                }else{
                    resourceTreeInfo.setText(productTagInfo.getProduct_code());
                }
                resourceTreeInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                resourceTreeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                resourceTreeMapper.insertSelective(resourceTreeInfo);
            }

            //同步默认产品zdh产品下的所有的角色
            Example exampleRole=new Example(RoleInfo.class);
            Example.Criteria criteriaRole=exampleRole.createCriteria();
            criteriaRole.andEqualTo("product_code", ConfigUtil.getProductCode());
            List<RoleInfo> roleInfos = roleDao.selectByExample(exampleRole);
            for(RoleInfo roleInfo: roleInfos){
                idMap.put(roleInfo.getId(),  String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
                roleInfo.setProduct_code(productTagInfo.getProduct_code());
                roleInfo.setId(idMap.get(roleInfo.getId()));
                roleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                roleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                roleDao.insert(roleInfo);
            }

            //同步角色和资源
            Example exampleRoleResource=new Example(RoleResourceInfo.class);
            Example.Criteria criteriaRoleResource=exampleRoleResource.createCriteria();
            criteriaRoleResource.andEqualTo("product_code", ConfigUtil.getProductCode());
            List<RoleResourceInfo> roleResourceInfos = roleResourceMapper.selectByExample(exampleRoleResource);

            for(RoleResourceInfo roleResourceInfo: roleResourceInfos){
                idMap.put(roleResourceInfo.getId(), String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
                roleResourceInfo.setProduct_code(productTagInfo.getProduct_code());
                roleResourceInfo.setId(null);
                roleResourceInfo.setResource_id(idMap.get(roleResourceInfo.getResource_id()));
                roleResourceInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                roleResourceInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                roleResourceMapper.insert(roleResourceInfo);
            }

            //同步维度信息
            Example examplePermissionDimension=new Example(PermissionDimensionInfo.class);
            Example.Criteria criteriaPermissionDimension=examplePermissionDimension.createCriteria();
            criteriaPermissionDimension.andEqualTo("product_code", ConfigUtil.getProductCode());
            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectByExample(examplePermissionDimension);
            for(PermissionDimensionInfo permissionDimensionInfo: permissionDimensionInfos){
                //idMap.put(permissionDimensionInfo.getId(), String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
                //permissionDimensionInfo.setId(idMap.get(permissionDimensionInfo.getId()));
                permissionDimensionInfo.setId(null);
                permissionDimensionInfo.setProduct_code(productTagInfo.getProduct_code());
                permissionDimensionInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                permissionDimensionInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionDimensionMapper.insert(permissionDimensionInfo);
            }

            //新增基础维度值
            PermissionDimensionValueInfo permissionDimensionValueInfoRoot = new PermissionDimensionValueInfo();
            permissionDimensionValueInfoRoot.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfoRoot.setDim_code(Const.PERMISSION_DIM_PRODUCT_CODE);
            permissionDimensionValueInfoRoot.setProduct_code(productTagInfo.getProduct_code());
            permissionDimensionValueInfoRoot.setParent_dim_value_code("#");
            permissionDimensionValueInfoRoot.setOwner(getOwner());
            permissionDimensionValueInfoRoot.setDim_value_code(Const.PERMISSION_DIM_PRODUCT_CODE);
            permissionDimensionValueInfoRoot.setDim_value_name(Const.PERMISSION_DIM_PRODUCT_CODE);
            permissionDimensionValueInfoRoot.setCreate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueInfoRoot.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            PermissionDimensionValueInfo permissionDimensionValueInfo = new PermissionDimensionValueInfo();
            permissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfo.setDim_code(Const.PERMISSION_DIM_PRODUCT_CODE);
            permissionDimensionValueInfo.setProduct_code(productTagInfo.getProduct_code());
            permissionDimensionValueInfo.setParent_dim_value_code(Const.PERMISSION_DIM_PRODUCT_CODE);
            permissionDimensionValueInfo.setOwner(getOwner());
            permissionDimensionValueInfo.setDim_value_code(productTagInfo.getProduct_code());
            permissionDimensionValueInfo.setDim_value_name(productTagInfo.getProduct_name());
            permissionDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            PermissionDimensionValueInfo permissionDimensionValueInfoDimGroupRoot = new PermissionDimensionValueInfo();
            permissionDimensionValueInfoDimGroupRoot.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfoDimGroupRoot.setDim_code(Const.PERMISSION_DIM_GROUP_CODE);
            permissionDimensionValueInfoDimGroupRoot.setProduct_code(productTagInfo.getProduct_code());
            permissionDimensionValueInfoDimGroupRoot.setParent_dim_value_code("#");
            permissionDimensionValueInfoDimGroupRoot.setOwner(getOwner());
            permissionDimensionValueInfoDimGroupRoot.setDim_value_code(Const.PERMISSION_DIM_GROUP_CODE);
            permissionDimensionValueInfoDimGroupRoot.setDim_value_name(Const.PERMISSION_DIM_GROUP_CODE);
            permissionDimensionValueInfoDimGroupRoot.setCreate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueInfoDimGroupRoot.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            PermissionDimensionValueInfo permissionDimensionValueInfoDimGroup = new PermissionDimensionValueInfo();
            permissionDimensionValueInfoDimGroup.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfoDimGroup.setDim_code(Const.PERMISSION_DIM_GROUP_CODE);
            permissionDimensionValueInfoDimGroup.setProduct_code(productTagInfo.getProduct_code());
            permissionDimensionValueInfoDimGroup.setParent_dim_value_code(Const.PERMISSION_DIM_GROUP_CODE);
            permissionDimensionValueInfoDimGroup.setOwner(getOwner());
            permissionDimensionValueInfoDimGroup.setDim_value_code("group_test");
            permissionDimensionValueInfoDimGroup.setDim_value_name("测试组");
            permissionDimensionValueInfoDimGroup.setCreate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueInfoDimGroup.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            permissionDimensionValueMapper.insertSelective(permissionDimensionValueInfoRoot);
            permissionDimensionValueMapper.insertSelective(permissionDimensionValueInfo);
            permissionDimensionValueMapper.insertSelective(permissionDimensionValueInfoDimGroupRoot);
            permissionDimensionValueMapper.insertSelective(permissionDimensionValueInfoDimGroup);

            //创建产品资源
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 产品删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "product_tag_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> product_tag_delete(String[] ids) {
        try {
            List<ProductTagInfo> productTagInfos = productTagMapper.selectObjectByIds("product_info", ids);
            for (ProductTagInfo productTagInfo: productTagInfos){
                checkPermissionByOwner(productTagInfo.getProduct_code());
            }
            productTagMapper.deleteLogicByIds("product_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }
    /**
     * 同步产品菜单及维度信息
     * @param id 产品id
     * @return
     */
    @SentinelResource(value = "product_tag_async", blockHandler = "handleReturn")
    @RequestMapping(value = "/product_tag_async", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> product_tag_async(String id) {
        try {

            // 检查目标产品是否存在
            ProductTagInfo targetProduct = productTagMapper.selectByPrimaryKey(id);
            if (targetProduct == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "目标产品不存在", null);
            }

            // 检查源产品是否存在
            ProductTagInfo productTagInfo = new ProductTagInfo();
            productTagInfo.setProduct_code(ConfigUtil.getProductCode());
            productTagInfo.setIs_delete(Const.NOT_DELETE);

            ProductTagInfo sourceProduct = productTagMapper.selectOne(productTagInfo);
            if (sourceProduct == null) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "源产品不存在", null);
            }

            // 检查权限
            checkPermissionByOwner(targetProduct.getProduct_code());

            // 同步资源树信息
            syncResourceTreeInfo(targetProduct.getProduct_code(), sourceProduct.getProduct_code());

            // 同步角色信息
            syncRoleInfo(targetProduct.getProduct_code(), sourceProduct.getProduct_code());

            // 同步权限维度信息
            syncPermissionDimensionInfo(targetProduct.getProduct_code(), sourceProduct.getProduct_code());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "同步成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e);
        }
    }

    /**
     * 同步资源树信息
     */
    private void syncResourceTreeInfo(String product_code, String base_product_code) throws Exception {

        // 查询源产品下的所有资源树信息
        Example exampleSource=new Example(ResourceTreeInfo.class);
        Example.Criteria criteriaSource=exampleSource.createCriteria();
        criteriaSource.andEqualTo("product_code", base_product_code);
        List<ResourceTreeInfo> baseResourceTreeInfos = resourceTreeMapper.selectByExample(exampleSource);

        Example example=new Example(ResourceTreeInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("product_code", product_code);
        List<ResourceTreeInfo> resourceTreeInfos = resourceTreeMapper.selectByExample(example);

        //根据资源url 或者 text 生成菜单等信息
        Map<String, ResourceTreeInfo> baseResourceTreeInfoMapById = new HashMap<>();
        Map<String, ResourceTreeInfo> baseResourceTreeInfoMap = new HashMap<>();
        for(ResourceTreeInfo resourceTreeInfo: baseResourceTreeInfos){
            baseResourceTreeInfoMapById.put(resourceTreeInfo.getId(), resourceTreeInfo);
            if(!StringUtils.isEmpty(resourceTreeInfo.getUrl())){
//                if(baseResourceTreeInfoMap.containsKey(resourceTreeInfo.getUrl())){
//                    throw new Exception("产品:"+resourceTreeInfo.getProduct_code()+", 存在重复的资源: "+resourceTreeInfo.getUrl());
//                }
                baseResourceTreeInfoMap.put(resourceTreeInfo.getUrl(), resourceTreeInfo);
            }else if(!StringUtils.isEmpty(resourceTreeInfo.getText())){
//                if(baseResourceTreeInfoMap.containsKey(resourceTreeInfo.getText())){
//                    throw new Exception("产品:"+resourceTreeInfo.getProduct_code()+", 存在重复的资源: "+resourceTreeInfo.getText());
//                }
                baseResourceTreeInfoMap.put(resourceTreeInfo.getText(), resourceTreeInfo);
            }
        }

        Map<String, ResourceTreeInfo> resourceTreeInfoMapById = new HashMap<>();
        Map<String, ResourceTreeInfo> resourceTreeInfoMap = new HashMap<>();
        for(ResourceTreeInfo resourceTreeInfo: resourceTreeInfos){
            resourceTreeInfoMapById.put(resourceTreeInfo.getId(), resourceTreeInfo);
            if(!StringUtils.isEmpty(resourceTreeInfo.getUrl())){
//                if(resourceTreeInfoMap.containsKey(resourceTreeInfo.getUrl())){
//                    throw new Exception("产品:"+resourceTreeInfo.getProduct_code()+", 存在重复的资源: "+resourceTreeInfo.getUrl());
//                }
                resourceTreeInfoMap.put(resourceTreeInfo.getUrl(), resourceTreeInfo);
            }else if(!StringUtils.isEmpty(resourceTreeInfo.getText())){
//                if(resourceTreeInfoMap.containsKey(resourceTreeInfo.getText())){
//                    throw new Exception("产品:"+resourceTreeInfo.getProduct_code()+", 存在重复的资源: "+resourceTreeInfo.getText());
//                }
                resourceTreeInfoMap.put(resourceTreeInfo.getText(), resourceTreeInfo);
            }
        }

        List<ResourceTreeInfo> newResourceTreeInfos = new ArrayList<>();
        Map<String, ResourceTreeInfo> newResourceTreeInfoMap = new HashMap<>();
        //对比新增的资源树信息
        for(ResourceTreeInfo resourceTreeInfo: baseResourceTreeInfos){
            if(!StringUtils.isEmpty(resourceTreeInfo.getUrl())){
                if(!resourceTreeInfoMap.containsKey(resourceTreeInfo.getUrl())){
                    newResourceTreeInfos.add(resourceTreeInfo);
                    newResourceTreeInfoMap.put(resourceTreeInfo.getUrl(), resourceTreeInfo);
                }
            }else if(!StringUtils.isEmpty(resourceTreeInfo.getText())){
                //新增的资源树信息
                if(!resourceTreeInfoMap.containsKey(resourceTreeInfo.getText())){
                    newResourceTreeInfos.add(resourceTreeInfo);
                    newResourceTreeInfoMap.put(resourceTreeInfo.getText(), resourceTreeInfo);
                }
            }
        }

        //根据新增的资源生成新的映射关系
        Map<String, String> idMap = new HashMap<>();

        for(ResourceTreeInfo resourceTreeInfo: newResourceTreeInfos) {
            String rid = resourceTreeInfo.getId();
            //生成新的rid
            String newRid = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
            idMap.put(rid, newRid);
        }

        //开始替换id和parent
        for(ResourceTreeInfo resourceTreeInfo: newResourceTreeInfos) {
            String rid = resourceTreeInfo.getId();
            String parent = resourceTreeInfo.getParent();
            if(parent.equals("#")){
                continue;
            }
            String newParentId = "";
            //根据parent 查找对应的资源信息
            ResourceTreeInfo baseResourceTreeInfo = baseResourceTreeInfoMapById.get(parent);

            //在产品中查找是否已经存在节点
            String urlOrText = StringUtils.isEmpty(baseResourceTreeInfo.getUrl())?baseResourceTreeInfo.getText():baseResourceTreeInfo.getUrl();
            if(resourceTreeInfoMap.containsKey(urlOrText)){
                //当前新增的资源,使用历史的父节点信息
                newParentId =  resourceTreeInfoMap.get(urlOrText).getId();
            }else{
                //当前新增资源,使用新增的资源作为父节点
                newParentId = idMap.get(newResourceTreeInfoMap.get(urlOrText).getId());
            }
            String s = JsonUtil.formatJsonString(resourceTreeInfo);
            ResourceTreeInfo newResourceTreeInfo = JsonUtil.toJavaBean(s, ResourceTreeInfo.class);
            newResourceTreeInfo.setId(idMap.get(rid));
            if(!newResourceTreeInfo.getParent().equals("#")){
                newResourceTreeInfo.setParent(newParentId);
            }
            newResourceTreeInfo.setProduct_code(product_code);
            if(!StringUtils.isEmpty(newResourceTreeInfo.getOrder())){
                newResourceTreeInfo.setOrder(newResourceTreeInfo.getOrder());
            }
            newResourceTreeInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            newResourceTreeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            LogUtil.info(this.getClass(),"新增资源树信息:{}", JsonUtil.formatJsonString(newResourceTreeInfo));

            resourceTreeMapper.insertSelective(newResourceTreeInfo);
        }

    }

    /**
     * 同步角色信息
     */
    private void syncRoleInfo(String product_code, String base_product_code) {
        // 删除目标产品下已有的角色信息
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setProduct_code(base_product_code);
        List<RoleInfo> baseRoleInfos = roleDao.select(roleInfo);
        roleInfo.setProduct_code(product_code);
        List<RoleInfo> roleInfos = roleDao.select(roleInfo);

        for(RoleInfo baseRoleInfo: baseRoleInfos){
            String code = baseRoleInfo.getCode();
            //遍历在新产品中是否存在角色
            boolean flag = false;
            for(RoleInfo roleInfo1: roleInfos){
                if(roleInfo1.getCode().equals(code)){
                    flag = true;
                }
            }
            if(!flag){
                String s = JsonUtil.formatJsonString(baseRoleInfo);
                RoleInfo roleInfo1 = JsonUtil.toJavaBean(s, RoleInfo.class);
                roleInfo1.setId(SnowflakeIdWorker.getInstance().nextId()+"");
                roleInfo1.setProduct_code(product_code);
                roleInfo1.setCreate_time(new Timestamp(System.currentTimeMillis()));
                roleInfo1.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                LogUtil.info(this.getClass(),"新增角色信息:{}", JsonUtil.formatJsonString(roleInfo1));
                roleDao.insertSelective(roleInfo1);
            }
        }
    }

    /**
     * 同步权限维度信息
     */
    private void syncPermissionDimensionInfo(String product_code, String base_product_code) throws Exception {
        Example examplePermissionDimension=new Example(PermissionDimensionInfo.class);
        Example.Criteria criteriaPermissionDimension=examplePermissionDimension.createCriteria();
        criteriaPermissionDimension.andEqualTo("product_code", product_code);
        criteriaPermissionDimension.andEqualTo("is_delete", Const.NOT_DELETE);
        List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectByExample(examplePermissionDimension);

        // 查询源产品下的所有权限维度信息
        Example exampleSourcePermissionDimension=new Example(PermissionDimensionInfo.class);
        Example.Criteria criteriaSourcePermissionDimension=exampleSourcePermissionDimension.createCriteria();
        criteriaSourcePermissionDimension.andEqualTo("product_code", base_product_code);
        criteriaSourcePermissionDimension.andEqualTo("is_delete", Const.NOT_DELETE);
        List<PermissionDimensionInfo> basePermissionDimensionInfos = permissionDimensionMapper.selectByExample(exampleSourcePermissionDimension);

        // 插入新的权限维度信息
        for(PermissionDimensionInfo basePermissionDimensionInfo: basePermissionDimensionInfos){

            String dim_code = basePermissionDimensionInfo.getDim_code();
            //遍历在新产品中是否存在角色
            boolean flag = false;
            for(PermissionDimensionInfo permissionDimensionInfo1: permissionDimensionInfos){
                if(permissionDimensionInfo1.getDim_code().equals(dim_code)){
                    flag = true;
                }
            }
            if(!flag){
                String s = JsonUtil.formatJsonString(basePermissionDimensionInfo);
                PermissionDimensionInfo permissionDimensionInfo = JsonUtil.toJavaBean(s, PermissionDimensionInfo.class);
                permissionDimensionInfo.setId(null);
                permissionDimensionInfo.setProduct_code(product_code);
                permissionDimensionInfo.setOwner(getOwner());
                permissionDimensionInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                permissionDimensionInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                LogUtil.info(this.getClass(),"新增权限维度信息:{}", JsonUtil.formatJsonString(permissionDimensionInfo));
                permissionDimensionMapper.insertSelective(permissionDimensionInfo);
            }
        }
    }
}
