package com.zyc.zdh.hadoop;

import com.hubspot.jinjava.Jinjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于上传,删除hdfs后,保存非结构化数据信息
 */
public class SqlTemplate {

    public static String TEMPLATE_INSERT_MYSQL = "INSERT INTO {{TABLE_NAME}}({{COLUMNS}}) VALUES ({{VALUES}})";
    public static String TEMPLATE_UPDATE_MYSQL = "UPDATE {{TABLE_NAME}} SET {{VALUES}} WHERE {{CONDITIONS}}";

    /**
     * 构建描述非结构化数据的关系
     * @param templates 模板,目前支持TEMPLATE_INSERT_MYSQL,TEMPLATE_UPDATE_MYSQL 2种模板
     * @param params  map结构,存储模板种需要转换的参数
     * @param dsi_info 数据库连接配置
     * @return
     */
    public static ReturnInfo build(String[] templates, Map<String, String> params, Dsi_Info dsi_info){
        if(dsi_info ==  null){
            dsi_info = getDsi_info();
        }
        List<String> etl_sqls=new ArrayList<>();
        Jinjava jj = new Jinjava();
        for (String template: templates){
            String etl_sql = jj.render(template, params);
            etl_sqls.add(etl_sql);
        }
        String[] result = new DBUtil().CUD(dsi_info.getDriver(), dsi_info.getUrl(), dsi_info.getUser(), dsi_info.getPassword(),
                etl_sqls.toArray(new String[]{}));

        if(result[0].equalsIgnoreCase("true")){
            return new ReturnInfo(true, "成功", null);
        }
        return new ReturnInfo(false, result[1], null);
    }

    /**
     * 生成默认的数据源,一般都
     * @return
     */
    public static Dsi_Info getDsi_info(){
        Dsi_Info dsi_info=new Dsi_Info();
        dsi_info.setDriver("");
        dsi_info.setUrl("");
        dsi_info.setUser("");
        dsi_info.setPassword("");
        return dsi_info;
    }

}
