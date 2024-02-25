package ${ControllerPackage};


import com.alibaba.fastjson.JSONObject;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.PageResult;
import ${EntityPackage}.${EntityName};
import ${EntityPackage}.RETURN_CODE;
import ${EntityPackage}.ReturnInfo;
import ${EntityPackage}.User;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.service.ZdhPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
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
import tk.mybatis.mapper.entity.Example;
import ${MapperPackage}.${MapperName};


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * ${tableDesc}服务
 */
@Controller
public class ${ControllerName} extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${MapperName} ${mapperName};
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * ${tableDesc}列表首页
     * @return
     */
    @RequestMapping(value = "/${controller}_index", method = RequestMethod.GET)
    @White
    public String ${controller}_index() {

        return "${controller}_index";
    }

    /**
     * ${tableDesc}列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "${controller}_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<${EntityName}>> ${controller}_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(${EntityName}.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            List<${EntityName}> ${entityName}s = ${mapperName}.selectByExample(example);

            return ReturnInfo.buildSuccess(${entityName}s);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("${tableDesc}列表查询失败", e);
        }

    }


    /**
    * ${tableDesc}列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "${controller}_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<${EntityName}>>> ${controller}_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(${EntityName}.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = ${mapperName}.selectCountByExample(example);

            List<${EntityName}> ${entityName}s = ${mapperName}.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<${EntityName}>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(${entityName}s);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("烽火台信息列表查询失败", e);
        }

    }

    /**
     * ${tableDesc}新增首页
     * @return
     */
    @RequestMapping(value = "/${controller}_add_index", method = RequestMethod.GET)
    @White
    public String ${controller}_add_index() {

        return "${controller}_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "${controller}_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<${EntityName}> ${controller}_detail(String id) {
        try {
            ${EntityName} ${entityName} = ${mapperName}.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", ${entityName});
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * ${tableDesc}更新
     * @param ${entityName}
     * @return
     */
    @SentinelResource(value = "${controller}_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<${EntityName}> ${controller}_update(${EntityName} ${entityName}) {
        try {

            ${EntityName} old${EntityName} = ${mapperName}.selectByPrimaryKey(${entityName}.getId());


            ${entityName}.setCreate_time(old${EntityName}.getCreate_time());
            ${entityName}.setUpdate_time(new Timestamp(new Date().getTime()));
            ${entityName}.setIs_delete(Const.NOT_DELETE);
            ${mapperName}.updateByPrimaryKeySelective(${entityName});

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", ${entityName});
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * ${tableDesc}新增
     * @param ${entityName}
     * @return
     */
    @SentinelResource(value = "${controller}_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<${EntityName}> ${controller}_add(${EntityName} ${entityName}) {
        try {
            ${entityName}.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            ${entityName}.setOwner(getOwner());
            ${entityName}.setIs_delete(Const.NOT_DELETE);
            ${entityName}.setCreate_time(new Timestamp(new Date().getTime()));
            ${entityName}.setUpdate_time(new Timestamp(new Date().getTime()));
            ${mapperName}.insertSelective(${entityName});
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", ${entityName});
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * ${tableDesc}删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "${controller}_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/${controller}_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo ${controller}_delete(String[] ids) {
        try {
            ${mapperName}.deleteLogicByIds("${tableName}",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
