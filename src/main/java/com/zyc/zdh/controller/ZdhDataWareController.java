package com.zyc.zdh.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.ExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新数据仓库服务
 */
@Controller
public class ZdhDataWareController extends BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    IssueDataMapper issueDataMapper;
    @Autowired
    EnumMapper enumMapper;
    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ApplyMapper applyMapper;

    @RequestMapping("/data_ware_house_index_plus")
    public String data_ware_house_index_plus() {

        return "etl/data_ware_house_index_plus";
    }


    @RequestMapping(value = "/data_ware_house_list6", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list6(String issue_context,String current_page,String label_params,Integer  page_size) {
        if(page_size==null || page_size==0){
            page_size = 10;
        }
        List<IssueDataInfo> list = new ArrayList<>();
        String[] labels = new String[]{};
        if(!StringUtils.isEmpty(label_params)){
            labels = label_params.split(",");
        }

        if(!StringUtils.isEmpty(issue_context)){
            issue_context = getLikeCondition(issue_context);
        }
        list = issueDataMapper.selectByParams(issue_context, labels);

        int end_index = Integer.parseInt(current_page)*page_size;
        if(list.size()< end_index){
            end_index = list.size();
        }

        int total_page = list.size()/page_size;
        if(list.size()%page_size != 0)
            total_page=total_page+1;

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("total_page",total_page);
        jsonObject.put("total_size", list.size());
        jsonObject.put("current_page", current_page);

        List<String> left_page = new ArrayList<>();
        List<String> right_page = new ArrayList<>();
        for(int i=Integer.parseInt(current_page)-3; i<Integer.parseInt(current_page);i++ ){
            if(i>=1){
                left_page.add(String.valueOf(i));
            }
        }
        //当前页后的2页展示
        for(int j=Integer.parseInt(current_page); j<Integer.parseInt(current_page)+3;j++ ){
            if(j<= total_page){
                right_page.add(String.valueOf(j));
            }
        }
        if(Integer.parseInt(current_page)+4<total_page){
            right_page.add("...");
            right_page.add(String.valueOf(total_page-1));
            right_page.add(String.valueOf(total_page));
        }else if(Integer.parseInt(current_page)+3==total_page){
            //right_page.add(String.valueOf(total_page-1));
            right_page.add(String.valueOf(total_page));
        }else if(Integer.parseInt(current_page)+3<total_page){
            right_page.add(String.valueOf(total_page-1));
            right_page.add(String.valueOf(total_page));
        }

        list =list.subList((Integer.parseInt(current_page)-1)*page_size, end_index);
        jsonObject.put("left_page", left_page);
        jsonObject.put("right_page", right_page);
        jsonObject.put("list", list);

        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/data_ware_house_label", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_label() {

        Example example=new Example(EnumInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        criteria.andLike("enum_type", "label_category");
        //criteria.andLike("enum_code", "data_ware_house_label_%");
        List<EnumInfo> enumInfos = enumMapper.selectByExample(example);

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"查询成功", enumInfos);
    }



    @RequestMapping(value = "/data_ware_house_sample", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_sample(String context, String id) {

        IssueDataInfo idi = issueDataMapper.selectById(id);


        //验证权限,验证当前数据是否同一个组下的人申请
        String user_group = getUser().getUser_group();

        Example example=new Example(AccountInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("user_group", user_group);
        List<AccountInfo> accountInfos=accountMapper.selectByExample(example);
        List<String> owners = new ArrayList<>();
        for (AccountInfo accountInfo: accountInfos){
            owners.add(accountInfo.getId().toString());
        }

        Example example2=new Example(ApplyInfo.class);
        Example.Criteria criteria2=example2.createCriteria();
        criteria2.andEqualTo("issue_id", idi.getId());
        criteria2.andEqualTo("status", Const.APPLY_STATUS_SUCCESS);
        criteria2.andIn("owner", owners);
        int count = applyMapper.selectCountByExample(example2);
        if(count<=0 && !idi.getOwner().equalsIgnoreCase(getUser().getId())){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"查询失败", "当前表无操作权限");
        }

        if(!idi.getData_source_type_input().equalsIgnoreCase("jdbc")){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"查询失败", "只支持JDBC类型数据查询");
        }
        String sources_id = idi.getData_sources_choose_input();
        String table = idi.getData_sources_table_name_input();
        List<column_data> columns = idi.getColumn_data_list();
        DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(sources_id);

        String sql = "select ";
        List<String> columnList = new ArrayList<>();
        for(column_data column:columns){
            columnList.add(column.getColumn_name());
        }
        sql=sql+StringUtils.join(columnList,",")+" from "+table +" limit 1000";
        try {
            List<Map<String,Object>> result = new DBUtil().R5(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    sql);
            return JSON.toJSONString(result);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

        return JSON.toJSONString("");
    }


    @RequestMapping(value = "/data_ware_house_export", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_export(String id, HttpServletResponse response) {

        IssueDataInfo idi = issueDataMapper.selectById(id);
        //验证权限,验证当前数据是否同一个组下的人申请
        String user_group = getUser().getUser_group();

        Example example=new Example(AccountInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("user_group", user_group);
        List<AccountInfo> accountInfos=accountMapper.selectByExample(example);
        List<String> owners = new ArrayList<>();
        for (AccountInfo accountInfo: accountInfos){
            owners.add(accountInfo.getId().toString());
        }

        Example example2=new Example(ApplyInfo.class);
        Example.Criteria criteria2=example2.createCriteria();
        criteria2.andEqualTo("issue_id", idi.getId());
        criteria2.andEqualTo("status", Const.APPLY_STATUS_SUCCESS);
        criteria2.andIn("owner", owners);
        int count = applyMapper.selectCountByExample(example2);
        if(count<=0 && !idi.getOwner().equalsIgnoreCase(getUser().getId())){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"导出失败", "当前表无操作权限");
        }

        if(!idi.getData_source_type_input().equalsIgnoreCase("jdbc")){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"导出失败", "只支持JDBC类型数据查询");
        }
        String sources_id = idi.getData_sources_choose_input();
        String table = idi.getData_sources_table_name_input();
        List<column_data> columns = idi.getColumn_data_list();
        DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(sources_id);

        String sql = "select ";
        List<String> columnList = new ArrayList<>();
        for(column_data column:columns){
            columnList.add(column.getColumn_name());
        }
        sql=sql+StringUtils.join(columnList,",")+" from "+table +" limit 1000000";
        BufferedWriter csvWtriter = null;
        try {
            List<Map<String,Object>> result = new DBUtil().R5(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    sql);

            ExportUtil.responseSetProperties(idi.getIssue_context(),response);
            ExportUtil.doExport(result, StringUtils.join(columnList,","), StringUtils.join(columnList,","),response.getOutputStream());
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"查询失败", e);
        }
        return null;
    }

    /**
     * 获取当前数据的申请人列表
     * @param issue_id
     * @return
     */
    @RequestMapping(value = "/data_ware_house_apply", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_apply(String issue_id){

        List<ApplyAlarmInfo> applyAlarmInfos = applyMapper.selectByIssueId(issue_id);

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", applyAlarmInfos);


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
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}");
            }
        }
    }

}
