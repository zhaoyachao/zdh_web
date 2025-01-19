package com.zyc.zdh.controller;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.es.BulkAsyncActionListener;
import com.zyc.zdh.es.EsUtil;
import com.zyc.zdh.hadoop.Dsi_Info;
import com.zyc.zdh.hadoop.HadoopUtil;
import com.zyc.zdh.hadoop.SqlTemplate;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.ResoveExcel;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试非结构化上传
 */
@Controller
public class ZdhTestController extends BaseController{
    public Logger logger= LoggerFactory.getLogger(this.getClass());


    /**
     * 非结构化任务首页
     * @return
     */
    @RequestMapping("/test_excel_index")
    @White
    public String test_excel() {

        return "etl/test_excel_index";
    }


    /**
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "/test_excel_upload", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String test_excel_upload(MultipartFile[] files) {
        try{

            Map<String, String> colm=new HashMap<>();
            colm.put("一", "one");
            colm.put("二", "two");
            colm.put("三", "three");
            colm.put("四", "four");
            for (MultipartFile jar_file : files) {
                List<Map<String, Object>> data = ResoveExcel.importExcel(jar_file, colm, true);
                for (Map<String, Object> d:data){
                    System.out.println(JsonUtil.formatJsonString(d));
                }
                EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
                esUtil.putBulkAsync("test_db", "_doc", "one", data, new BulkAsyncActionListener());
                byte[] b = IOUtils.toByteArray(jar_file.getInputStream());
                Dsi_Info dsi_info=new Dsi_Info();
                dsi_info.setUrl("hdfs://192.168.110.10:9001");
                dsi_info.setUser("zyc");
                HadoopUtil.writeHdfs(dsi_info,"/home/zyc/"+jar_file.getName(),b);

                Map<String, String> map = new HashMap<>();
                map.put("TABLE_NAME", "file_info");
                map.put("COLUMNS","file_name, file_path");
                map.put("VALUES","'"+jar_file.getName()+"', '/home/zyc/'");
                Dsi_Info dsi_info2=new Dsi_Info();
                dsi_info2.setDriver("com.mysql.cj.jdbc.Driver");
                dsi_info2.setUser("zyc");
                dsi_info2.setPassword("123456");
                dsi_info2.setUrl("jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false");
                SqlTemplate.build(new String[]{SqlTemplate.TEMPLATE_INSERT_MYSQL}, map, dsi_info2);
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            e.printStackTrace();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
