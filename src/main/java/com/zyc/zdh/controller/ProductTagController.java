package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ProductTagMapper;
import com.zyc.zdh.entity.ProductTagInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
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
import java.util.List;

/**
 * 产品标识服务
 */
@Controller
public class ProductTagController extends BaseController {

    @Autowired
    private ProductTagMapper productTagMapper;

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
            //创建产品资源
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
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
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

}
