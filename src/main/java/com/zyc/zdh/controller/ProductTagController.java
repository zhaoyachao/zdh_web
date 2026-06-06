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
     *
     * 核心逻辑：
     * 1. 根节点（parent="#"）通过 createRootNode 单独创建，不通过 url/text 判断是否为新增
     * 2. 但根节点必须保留在基础信息中，因为新增的资源可能挂在根节点下（parent="#"）
     * 3. 非根节点通过 url 或 text 字段进行匹配，判断是否为新增资源
     * 4. 新增资源会生成新的ID，并正确处理 parent 层级关系
     *
     * @param product_code 目标产品编码
     * @param base_product_code 源产品编码
     * @throws Exception
     */
    private void syncResourceTreeInfo(String product_code, String base_product_code) throws Exception {
        LogUtil.info(this.getClass(), "开始同步资源树信息, 源产品: {}, 目标产品: {}", base_product_code, product_code);

        // 1. 查询源产品和目标产品的所有资源树信息
        List<ResourceTreeInfo> baseResourceList = getResourceTreeByProductCode(base_product_code);
        List<ResourceTreeInfo> targetResourceList = getResourceTreeByProductCode(product_code);

        // 2. 如果目标产品没有资源，创建根节点（根节点不参与url/text匹配判断新增）
        if (targetResourceList.isEmpty()) {
            LogUtil.info(this.getClass(), "目标产品无资源, 创建根节点");
            createRootNode(product_code);
            // 重新查询（包含刚创建的根节点）
            targetResourceList = getResourceTreeByProductCode(product_code);
        }

        // 3. 构建映射表（用于快速查找）
        // 3.1 源产品的映射表
        Map<String, ResourceTreeInfo> baseMapById = buildMapById(baseResourceList);
        // 注意：url/text映射排除根节点，避免根节点被误判为"新增资源"
        Map<String, ResourceTreeInfo> baseMapByUrlOrText = buildMapByUrlOrText(baseResourceList);

        // 3.2 目标产品的映射表
        Map<String, ResourceTreeInfo> targetMapById = buildMapById(targetResourceList);
        Map<String, ResourceTreeInfo> targetMapByUrlOrText = buildMapByUrlOrText(targetResourceList);

        // 3.3 获取根节点信息（保留根节点用于处理parent关系）
        ResourceTreeInfo baseRootNode = getRootNode(baseResourceList);
        ResourceTreeInfo targetRootNode = getRootNode(targetResourceList);

        if (targetRootNode == null) {
            throw new Exception("目标产品未找到根节点");
        }

        LogUtil.info(this.getClass(), "源产品根节点: ID={}, Text={}, 目标产品根节点: ID={}, Text={}",
                    baseRootNode != null ? baseRootNode.getId() : "null",
                    baseRootNode != null ? baseRootNode.getText() : "null",
                    targetRootNode.getId(),
                    targetRootNode.getText());

        // 4. 找出需要新增的资源（通过url或text匹配，排除根节点判断）
        List<ResourceTreeInfo> resourcesToAdd = findResourcesToAdd(baseResourceList, targetMapByUrlOrText);

        if (resourcesToAdd.isEmpty()) {
            LogUtil.info(this.getClass(), "没有需要新增的资源");
            return;
        }

        LogUtil.info(this.getClass(), "发现 {} 个需要新增的资源", resourcesToAdd.size());

        // 5. 为新增资源生成新的ID映射关系
        Map<String, String> oldIdToNewIdMap = generateNewIdMapping(resourcesToAdd);

        // 6. 插入新增资源到目标产品（传入根节点信息用于处理parent关系）
        insertNewResources(resourcesToAdd, baseMapById, baseMapByUrlOrText,
                          targetMapById, targetMapByUrlOrText, oldIdToNewIdMap,
                          product_code, baseRootNode, targetRootNode);

        LogUtil.info(this.getClass(), "资源树信息同步完成");
    }

    /**
     * 根据产品编码查询所有资源树信息
     */
    private List<ResourceTreeInfo> getResourceTreeByProductCode(String product_code) {
        Example example = new Example(ResourceTreeInfo.class);
        example.createCriteria().andEqualTo("product_code", product_code);
        return resourceTreeMapper.selectByExample(example);
    }

    /**
     * 创建根节点
     */
    private void createRootNode(String product_code) throws Exception {
        LogUtil.info(this.getClass(), "创建目标产品的根节点, 产品: {}", product_code);
        ResourceTreeInfo rootNode = new ResourceTreeInfo();
        rootNode.setId(String.valueOf(SnowflakeIdWorker.getInstance().nextId()));
        rootNode.setParent("#");
        rootNode.setLevel("1");
        rootNode.setText(product_code);
        rootNode.setProduct_code(product_code);
        rootNode.setIcon("fa fa-folder");
        rootNode.setIs_enable("1");
        rootNode.setOwner(getOwner());
        rootNode.setOrder("1");
        rootNode.setResource_type("2");
        rootNode.setCreate_time(new Timestamp(System.currentTimeMillis()));
        rootNode.setUpdate_time(new Timestamp(System.currentTimeMillis()));

        resourceTreeMapper.insertSelective(rootNode);
        LogUtil.info(this.getClass(), "根节点创建成功, ID: {}", rootNode.getId());
    }

    /**
     * 获取产品的根节点（parent="#"的节点）
     */
    private ResourceTreeInfo getRootNode(List<ResourceTreeInfo> resourceList) {
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }
        for (ResourceTreeInfo resource : resourceList) {
            if (isRootNode(resource)) {
                return resource;
            }
        }
        return null;
    }

    /**
     * 构建ID到资源的映射
     */
    private Map<String, ResourceTreeInfo> buildMapById(List<ResourceTreeInfo> list) {
        Map<String, ResourceTreeInfo> map = new HashMap<>();
        for (ResourceTreeInfo item : list) {
            map.put(item.getId(), item);
        }
        return map;
    }

    /**
     * 构建URL或Text到资源的映射（优先使用URL）
     * 注意：排除根节点（parent="#"），避免根节点通过text匹配导致重复
     */
    private Map<String, ResourceTreeInfo> buildMapByUrlOrText(List<ResourceTreeInfo> list) {
        Map<String, ResourceTreeInfo> map = new HashMap<>();
        for (ResourceTreeInfo item : list) {
            // 排除根节点，根节点不能通过text/url匹配
            if (isRootNode(item)) {
                LogUtil.debug(this.getClass(), "排除根节点: ID={}, Text={}", item.getId(), item.getText());
                continue;
            }
            String key = getUrlOrTextKey(item);
            if (!StringUtils.isEmpty(key)) {
                map.put(key, item);
            }
        }
        return map;
    }

    /**
     * 判断是否为根节点
     */
    private boolean isRootNode(ResourceTreeInfo resource) {
        return resource != null && "#".equalsIgnoreCase(resource.getParent());
    }

    /**
     * 获取用于匹配的key（优先使用url，其次使用text）
     */
    private String getUrlOrTextKey(ResourceTreeInfo resource) {
        if (!StringUtils.isEmpty(resource.getUrl())) {
            return resource.getUrl();
        } else if (!StringUtils.isEmpty(resource.getText())) {
            return resource.getText();
        }
        return null;
    }

    /**
     * 找出需要新增的资源（在源产品中存在但目标产品中不存在）
     * 注意：排除根节点，根节点通过createRootNode单独创建
     */
    private List<ResourceTreeInfo> findResourcesToAdd(List<ResourceTreeInfo> baseResources,
                                                        Map<String, ResourceTreeInfo> targetMapByUrlOrText) {
        List<ResourceTreeInfo> result = new ArrayList<>();
        for (ResourceTreeInfo baseResource : baseResources) {
            // 排除根节点，不参与匹配
            if (isRootNode(baseResource)) {
                LogUtil.debug(this.getClass(), "跳过根节点: Text={}", baseResource.getText());
                continue;
            }

            String key = getUrlOrTextKey(baseResource);
            if (key != null && !targetMapByUrlOrText.containsKey(key)) {
                result.add(baseResource);
                LogUtil.debug(this.getClass(), "发现新增资源: {} ({})", baseResource.getText(), key);
            }
        }
        return result;
    }

    /**
     * 为新增资源生成新的ID映射（旧ID -> 新ID）
     */
    private Map<String, String> generateNewIdMapping(List<ResourceTreeInfo> resourcesToAdd) {
        Map<String, String> idMap = new HashMap<>();
        for (ResourceTreeInfo resource : resourcesToAdd) {
            String newId = String.valueOf(SnowflakeIdWorker.getInstance().nextId());
            idMap.put(resource.getId(), newId);
            LogUtil.debug(this.getClass(), "ID映射: {} -> {}", resource.getId(), newId);
        }
        return idMap;
    }

    /**
     * 插入新增资源到目标产品
     *
     * @param resourcesToAdd 需要新增的资源列表
     * @param baseMapById 源产品的ID映射
     * @param baseMapByUrlOrText 源产品的URL/Text映射（排除根节点）
     * @param targetMapById 目标产品的ID映射（包含根节点）
     * @param targetMapByUrlOrText 目标产品的URL/Text映射（排除根节点）
     * @param oldIdToNewIdMap 旧ID到新ID的映射
     * @param product_code 目标产品编码
     * @param baseRootNode 源产品的根节点信息
     * @param targetRootNode 目标产品的根节点信息
     * @throws Exception
     */
    private void insertNewResources(List<ResourceTreeInfo> resourcesToAdd,
                                    Map<String, ResourceTreeInfo> baseMapById,
                                    Map<String, ResourceTreeInfo> baseMapByUrlOrText,
                                    Map<String, ResourceTreeInfo> targetMapById,
                                    Map<String, ResourceTreeInfo> targetMapByUrlOrText,
                                    Map<String, String> oldIdToNewIdMap,
                                    String product_code,
                                    ResourceTreeInfo baseRootNode,
                                    ResourceTreeInfo targetRootNode) throws Exception {

        for (ResourceTreeInfo baseResource : resourcesToAdd) {
            // 双重检查：确保不插入根节点（根节点通过createRootNode单独创建）
            if (isRootNode(baseResource)) {
                LogUtil.warn(this.getClass(), "跳过根节点的插入: Text={}", baseResource.getText());
                continue;
            }

            String oldId = baseResource.getId();
            String newId = oldIdToNewIdMap.get(oldId);

            // 复制资源属性
            String jsonStr = JsonUtil.formatJsonString(baseResource);
            ResourceTreeInfo newResource = JsonUtil.toJavaBean(jsonStr, ResourceTreeInfo.class);

            // 设置新的ID
            newResource.setId(newId);

            // 处理parent关系（传入根节点信息，用于处理挂在根节点下的资源）
            resolveParentRelation(baseResource, newResource, baseMapById,
                                 baseMapByUrlOrText, targetMapById,
                                 targetMapByUrlOrText, oldIdToNewIdMap,
                                 baseRootNode, targetRootNode);

            // 更新产品编码和时间戳
            newResource.setProduct_code(product_code);
            newResource.setCreate_time(new Timestamp(System.currentTimeMillis()));
            newResource.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            LogUtil.info(this.getClass(), "插入新资源: ID={}, Text={}, Parent={}",
                        newResource.getId(), newResource.getText(), newResource.getParent());

            resourceTreeMapper.insertSelective(newResource);
        }
    }

    /**
     * 解析并设置资源的parent关系
     *
     * 核心逻辑：
     * - 如果是根节点（parent="#"），使用目标产品的根节点ID
     * - 如果父节点已在目标产品中存在，使用目标产品中的父节点ID
     * - 如果父节点也在新增列表中，使用父节点的新ID
     * - 否则向上递归查找已存在的祖先节点（最终会找到目标产品的根节点）
     *
     * @param baseResource 源资源信息
     * @param newResource 新资源信息（需要设置parent）
     * @param baseMapById 源产品的ID映射
     * @param baseMapByUrlOrText 源产品的URL/Text映射（排除根节点）
     * @param targetMapById 目标产品的ID映射（包含根节点）
     * @param targetMapByUrlOrText 目标产品的URL/Text映射（排除根节点）
     * @param oldIdToNewIdMap 旧ID到新ID的映射
     * @param baseRootNode 源产品的根节点信息
     * @param targetRootNode 目标产品的根节点信息
     */
    private void resolveParentRelation(ResourceTreeInfo baseResource,
                                        ResourceTreeInfo newResource,
                                        Map<String, ResourceTreeInfo> baseMapById,
                                        Map<String, ResourceTreeInfo> baseMapByUrlOrText,
                                        Map<String, ResourceTreeInfo> targetMapById,
                                        Map<String, ResourceTreeInfo> targetMapByUrlOrText,
                                        Map<String, String> oldIdToNewIdMap,
                                        ResourceTreeInfo baseRootNode,
                                        ResourceTreeInfo targetRootNode) {

        String parent = baseResource.getParent();

        // 特殊情况：parent="#" 表示该资源挂在根节点下
        if ("#".equalsIgnoreCase(parent)) {
            // 使用目标产品的根节点ID作为parent
            newResource.setParent(targetRootNode.getId());
            LogUtil.debug(this.getClass(), "资源挂在根节点下, 使用目标根节点ID: {}", targetRootNode.getId());
            return;
        }

        // 获取父节点的源数据
        ResourceTreeInfo baseParent = baseMapById.get(parent);
        if (baseParent == null) {
            LogUtil.warn(this.getClass(), "未找到父节点信息, 父节点ID: {}, 使用目标根节点作为父节点", parent);
            newResource.setParent(targetRootNode.getId());
            return;
        }

        // 获取父节点的匹配key（url或text）
        String parentKey = getUrlOrTextKey(baseParent);
        if (StringUtils.isEmpty(parentKey)) {
            LogUtil.warn(this.getClass(), "父节点无有效的url或text, 父节点ID: {}, 使用目标根节点作为父节点", parent);
            newResource.setParent(targetRootNode.getId());
            return;
        }

        // 情况1：父节点已在目标产品中存在 → 直接使用目标产品中的父节点ID
        if (targetMapByUrlOrText.containsKey(parentKey)) {
            String existingParentId = targetMapByUrlOrText.get(parentKey).getId();
            newResource.setParent(existingParentId);
            LogUtil.debug(this.getClass(), "父节点已存在, 使用已有ID: {}", existingParentId);
            return;
        }

        // 情况2：父节点也在新增列表中 → 使用父节点的新ID
        if (baseMapByUrlOrText.containsKey(parentKey)) {
            ResourceTreeInfo parentInBase = baseMapByUrlOrText.get(parentKey);
            String parentOldId = parentInBase.getId();
            if (oldIdToNewIdMap.containsKey(parentOldId)) {
                String newParentId = oldIdToNewIdMap.get(parentOldId);
                newResource.setParent(newParentId);
                LogUtil.debug(this.getClass(), "父节点也是新增的, 使用新ID: {}", newParentId);
                return;
            }
        }

        // 情况3：向上递归查找已存在的祖先节点（传入targetMapById用于查找根节点）
        String resolvedParentId = findExistingAncestor(baseParent, baseMapById,
                                                       baseMapByUrlOrText, targetMapById,
                                                       targetMapByUrlOrText, oldIdToNewIdMap,
                                                       targetRootNode);
        newResource.setParent(resolvedParentId);
        LogUtil.debug(this.getClass(), "递归查找后确定的父节点ID: {}", resolvedParentId);
    }

    /**
     * 递归查找已存在的祖先节点ID
     *
     * @param currentNode 当前节点
     * @param baseMapById 源产品的ID映射
     * @param baseMapByUrlOrText 源产品的URL/Text映射（排除根节点）
     * @param targetMapById 目标产品的ID映射（包含根节点）
     * @param targetMapByUrlOrText 目标产品的URL/Text映射（排除根节点）
     * @param oldIdToNewIdMap 旧ID到新ID的映射
     * @param targetRootNode 目标产品的根节点信息
     * @return 找到的祖先节点ID，如果找不到则返回目标产品的根节点ID
     */
    private String findExistingAncestor(ResourceTreeInfo currentNode,
                                         Map<String, ResourceTreeInfo> baseMapById,
                                         Map<String, ResourceTreeInfo> baseMapByUrlOrText,
                                         Map<String, ResourceTreeInfo> targetMapById,
                                         Map<String, ResourceTreeInfo> targetMapByUrlOrText,
                                         Map<String, String> oldIdToNewIdMap,
                                         ResourceTreeInfo targetRootNode) {

        String currentParent = currentNode.getParent();

        // 到达根节点：返回目标产品的根节点ID
        if ("#".equalsIgnoreCase(currentParent)) {
            LogUtil.debug(this.getClass(), "递归到达根节点, 返回目标根节点ID: {}", targetRootNode.getId());
            return targetRootNode.getId();
        }

        // 获取当前节点的父节点
        ResourceTreeInfo parentNode = baseMapById.get(currentParent);
        if (parentNode == null) {
            LogUtil.warn(this.getClass(), "递归查找时未找到节点, ID: {}, 返回目标根节点", currentParent);
            return targetRootNode.getId();
        }

        String parentKey = getUrlOrTextKey(parentNode);
        if (StringUtils.isEmpty(parentKey)) {
            // 继续向上查找
            return findExistingAncestor(parentNode, baseMapById, baseMapByUrlOrText,
                                       targetMapById, targetMapByUrlOrText, oldIdToNewIdMap,
                                       targetRootNode);
        }

        // 检查父节点是否在目标产品中已存在
        if (targetMapByUrlOrText.containsKey(parentKey)) {
            return targetMapByUrlOrText.get(parentKey).getId();
        }

        // 检查父节点是否在新增列表中
        if (oldIdToNewIdMap.containsKey(parentNode.getId())) {
            return oldIdToNewIdMap.get(parentNode.getId());
        }

        // 继续向上递归
        return findExistingAncestor(parentNode, baseMapById, baseMapByUrlOrText,
                                   targetMapById, targetMapByUrlOrText, oldIdToNewIdMap,
                                   targetRootNode);
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
