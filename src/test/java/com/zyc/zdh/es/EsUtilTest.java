package com.zyc.zdh.es;

import com.zyc.zdh.util.JsonUtil;
import org.apache.http.HttpHost;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsUtilTest {

    @Test
    public void put() throws Exception {
        Map<String, Object> jsonObject=JsonUtil.createEmptyMap();
        jsonObject.put("name","zhaoyachao");
        jsonObject.put("project","zdh");
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.put("test_db", "_doc", "1",JsonUtil.formatJsonString(jsonObject), new SingleIndexResponseListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void putAsync() throws Exception {
        Map<String, Object> jsonObject=JsonUtil.createEmptyMap();
        jsonObject.put("name","zhaoyachao");
        jsonObject.put("project","zdh");
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.putAsync("test_db", "_doc", "1",JsonUtil.formatJsonString(jsonObject), new SingleAsyncActionListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void putBulk() throws Exception {
        List<Map<String, Object>> data=new ArrayList<>();
        for (int i=0;i<10;i++){
            Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
            jsonObject.put("name","zhaoyachao"+i);
            jsonObject.put("project","zdh"+i);
            jsonObject.put("id",""+i);
            data.add(jsonObject);
        }
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.putBulk("test_db", "_doc", "id",data, new BulkItemResponseListener());
        while (true){
            Thread.sleep(10000);
        }
    }
    @Test
    public void putBulkAsync() throws Exception {
        List<Map<String, Object>> data=new ArrayList<>();
        for (int i=0;i<10;i++){
            Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
            jsonObject.put("name","zhaoyachao"+i);
            jsonObject.put("project","zdh"+i);
            jsonObject.put("id",""+i);
            data.add(jsonObject);
        }
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.putBulkAsync("test_db", "_doc", "id",data, new BulkAsyncActionListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void deleteBulkAsync() throws Exception {
        List<String> data=new ArrayList<>();

        for (int i=0;i<10;i++){

            data.add(""+i);
        }
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.deleteBulkAsync("test_db", "_doc", data, new BulkAsyncActionListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void deleteBulk() throws Exception {
        List<String> data=new ArrayList<>();

        for (int i=0;i<10;i++){

            data.add(""+i);
        }
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.deleteBulk("test_db", "_doc", data, new BulkItemResponseListener());
        while (true){
            Thread.sleep(10000);
        }
    }

}