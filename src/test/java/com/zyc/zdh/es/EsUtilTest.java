package com.zyc.zdh.es;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EsUtilTest {

    @Test
    public void put() throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name","zhaoyachao");
        jsonObject.put("project","zdh");
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.put("test_db", "_doc", "1",jsonObject.toJSONString(), new SingleIndexResponseListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void putAsync() throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("name","zhaoyachao");
        jsonObject.put("project","zdh");
        EsUtil esUtil=new EsUtil(new HttpHost[]{new HttpHost("192.168.110.10", 9200, "http")});
        esUtil.putAsync("test_db", "_doc", "1",jsonObject.toJSONString(), new SingleAsyncActionListener());
        while (true){
            Thread.sleep(10000);
        }
    }

    @Test
    public void putBulk() throws Exception {
        List<JSONObject> data=new ArrayList<>();
        for (int i=0;i<10;i++){
            JSONObject jsonObject=new JSONObject();
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
        List<JSONObject> data=new ArrayList<>();
        for (int i=0;i<10;i++){
            JSONObject jsonObject=new JSONObject();
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