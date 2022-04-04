package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.entity.DataSourcesInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据源服务
 */
@Controller
public class ZdhDataSourcesController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    DispatchTaskService dispatchTaskService;
    @Autowired
    ZdhLogsService zdhLogsService;

    /**
     * 返回数据源列表首页
     * @return
     */
    @RequestMapping("/data_sources_index")
    public String data_sources_index() {

        return "etl/data_sources_index";
    }

    /**
     * 获取当前用户下的所有数据源
     * @return
     */
    @RequestMapping(value = "/data_sources_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_list() {
        //获取数据权限
        String[] tag_group_code = getUser().getTag_group_code().split(",");

        DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
        dataSourcesInfo.setOwner(getUser().getId());
        dataSourcesInfo.setIs_delete(Const.NOT_DELETE);
        List<DataSourcesInfo> list = dataSourcesMapper.selectByParams(getUser().getId(), tag_group_code);

        return JSON.toJSONString(list);
    }

    /**
     * 根据选择条件模糊查询数据源
     * @param data_source_context 数据源说明
     * @param data_source_type 数据源类型
     * @param url 连接串
     * @return
     */
    @RequestMapping(value = "/data_sources_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_list2(String data_source_context, String data_source_type, String url) {

        Example example = new Example(DataSourcesInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("owner", getUser().getId());
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        if(!StringUtils.isEmpty(data_source_context)){
            criteria.andLike("data_source_context", getLikeCondition(data_source_context));
        }
        if(!StringUtils.isEmpty(data_source_type)){
            criteria.andEqualTo("data_source_type", data_source_type);
        }
        if(!StringUtils.isEmpty(url)){
            criteria.andLike("url", getLikeCondition(url));
        }
        String[] tag_group_code = getUser().getTag_group_code().split(",");
        List<DataSourcesInfo> list = dataSourcesMapper.selectByParams2(getUser().getId(), tag_group_code, url, data_source_context, data_source_type);

        return JSON.toJSONString(list);
    }

    /**
     * 根据数据源id(主键)获取数据源
     * @param id id
     * @return
     */
    @RequestMapping(value = "/data_sources_info", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_info(String id) {

        DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(id);

        return JSON.toJSONString(dataSourcesInfo);
    }

    /**
     * 批量删除数据源
     * @param ids
     * @return
     */
    @RequestMapping(value = "/data_sources_delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String deleteIds(String[] ids) {
        try{
            dataSourcesMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 增加数据源页面
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping("/data_sources_add_index")
    public String data_sources_add_index(HttpServletRequest request, HttpServletResponse response, Long id) {

        return "etl/data_sources_add_index";
    }

    /**
     * 新增数据源
     * @param dataSourcesInfo
     * @return
     */
    @RequestMapping(value = "/data_sources_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_add(DataSourcesInfo dataSourcesInfo) {
        try{
            dataSourcesInfo.setOwner(getUser().getId());
            dataSourcesInfo.setIs_delete("0");
            dataSourcesInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            dataSourcesMapper.insert(dataSourcesInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新数据源
     * @param dataSourcesInfo
     * @return
     */
    @RequestMapping(value="/data_sources_update", method = RequestMethod.POST)
    @ResponseBody
    public String data_sources_update(DataSourcesInfo dataSourcesInfo) {
        try{
            DataSourcesInfo oldDataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(dataSourcesInfo.getId());
            dataSourcesInfo.setOwner(oldDataSourcesInfo.getOwner());
            dataSourcesInfo.setIs_delete("0");
            dataSourcesInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            dataSourcesMapper.updateByPrimaryKey(dataSourcesInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }

    }

    /**
     * 获取所有的数据源类型
     * @return
     */
    @RequestMapping(value = "/data_sources_type", method = RequestMethod.GET)
    @ResponseBody
    public List<String> data_sources_type() {
        List<String> result = dataSourcesMapper.selectDataSourcesType();

        return result;
    }


    @RequestMapping(value = "/test_connect", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String test_connect(DataSourcesInfo dataSourcesInfo) {
        try{
            if(!dataSourcesInfo.getData_source_type().equalsIgnoreCase("jdbc")){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"测试连接失败", "只支持JDBC类型连接测试");
            }
            new DBUtil().R3(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "");
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"测试连接成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"测试连接失败", e.getMessage());
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
