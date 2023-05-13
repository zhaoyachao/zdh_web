package ${ControllerPackage};

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import ${EntityPackage}.${EntityName};
import ${EntityPackage}.RETURN_CODE;
import ${EntityPackage}.ReturnInfo;
import ${EntityPackage}.User;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
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
 * xxx服务1
 */
@Controller
public class ${ControllerName} extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${MapperName} ${mapperName};

    /**
     * xx列表首页
     * @return
     */
    @RequestMapping(value = "/${controller}_index", method = RequestMethod.GET)
    @White
    public String ${controller}_index() {

        return "${controller}_index";
    }

    /**
     * xx列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/${controller}_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public String ${controller}_list(String context) {
        Example example=new Example(${EntityName}.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        Example.Criteria criteria2=example.createCriteria();
        if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
        }
        example.and(criteria2);

        List<${EntityName}> ${entityName}s = ${mapperName}.selectByExample(example);

        return JSONObject.toJSONString(${entityName}s);
    }

    /**
     * xx新增首页
     * @return
     */
    @RequestMapping(value = "/${controller}_add_index", method = RequestMethod.GET)
    @White
    public String ${controller}_add_index() {

        return "${controller}_add_index";
    }

    /**
     * xx明细页面
     * @return
     */
    @RequestMapping(value = "/${controller}_detail", method = RequestMethod.GET)
    @White
    public String ${controller}_detail() {

        return "${controller}_detail";
    }
    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
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
     * xx更新
     * @param ${entityName}
     * @return
     */
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
            ${mapperName}.updateByPrimaryKey(${entityName});

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", ${entityName});
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * xx新增
     * @param ${entityName}
     * @return
     */
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
            ${mapperName}.insert(${entityName});
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", ${entityName});
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * xx删除
     * @param ids
     * @return
     */
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

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
