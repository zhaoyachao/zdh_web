package com.zyc.zdh.es;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Collection;
import java.util.List;

public class EsUtil {

    private RestHighLevelClient client;

    public EsUtil(HttpHost[] httpHosts){
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        httpHosts
                ));
    }

    public RestHighLevelClient getClient() throws Exception {
        if(this.client != null ){
            return this.client;
        }
        throw new Exception("当前EsUtil未正常初始化");
    }

    /**
     * 单条同步写入ES
     * @param index
     * @param type
     * @param id
     * @param json
     * @param myResponseListener
     * @throws Exception
     */
    public void put(String index, String type, String id, String json, ResponseListener<IndexResponse> myResponseListener) throws Exception {
        if(StringUtils.isEmpty(id)){
            throw new Exception("写入ES必须指定主键字段");
        }
        RestHighLevelClient client = getClient();

        try {
            // 1、创建索引请求
            IndexRequest request = new IndexRequest(
                    index,   //索引
                    type,    // mapping type
                    id
                    );

            // 2、准备文档数据
            // 方式一：直接给JSON串
            request.source(json, XContentType.JSON);

            //3、其他的一些可选设置
            /*
            request.routing("routing");  //设置routing值
            request.timeout(TimeValue.timeValueSeconds(1));  //设置主分片等待时长
            request.setRefreshPolicy("wait_for");  //设置重刷新策略
            request.version(2);  //设置版本号
            request.opType(DocWriteRequest.OpType.CREATE);  //操作类别
            */
            //4、发送请求
            IndexResponse indexResponse = null;
            try {
                // 同步方式
                indexResponse = client.index(request, RequestOptions.DEFAULT);
            } catch(ElasticsearchException e) {
                // 捕获，并处理异常
                myResponseListener.fail(e);
                throw e;
            }

            //5、处理响应
            if(indexResponse != null) {
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    myResponseListener.success(indexResponse);
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    myResponseListener.success(indexResponse);
                }else{
                    myResponseListener.fail(indexResponse);
                }
                // 分片处理信息
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                }
                // 如果有分片副本失败，可以获得失败原因信息
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        //System.out.println("副本失败原因：" + reason);
                    }
                }
            }
        } catch (Exception e) {
            myResponseListener.fail(e);
            throw e;
        }
    }

    /**
     * 单条异步写入ES
     * @param index
     * @param type
     * @param id
     * @param json
     * @param listener
     * @throws Exception
     */
    public void putAsync(String index, String type, String id, String json, ActionListener<IndexResponse> listener ) throws Exception {
        if(StringUtils.isEmpty(id)){
            throw new Exception("写入ES必须指定主键字段");
        }
        RestHighLevelClient client = getClient();

        try {
            // 1、创建索引请求
            IndexRequest request = new IndexRequest(
                    index,   //索引
                    type,    // mapping type
                    id
            );

            // 2、准备文档数据
            // 方式一：直接给JSON串
            request.source(json, XContentType.JSON);

            //3、其他的一些可选设置
            /*
            request.routing("routing");  //设置routing值
            request.timeout(TimeValue.timeValueSeconds(1));  //设置主分片等待时长
            request.setRefreshPolicy("wait_for");  //设置重刷新策略
            request.version(2);  //设置版本号
            request.opType(DocWriteRequest.OpType.CREATE);  //操作类别
            */

            //异步方式发送索引请求
            client.indexAsync(request, RequestOptions.DEFAULT,listener);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 多条同步写入ES
     * @param index
     * @param type
     * @param idColName
     * @param jsons
     * @param myResponseListener
     * @throws Exception
     */
    public void putBulk(String index, String type, String idColName, List<JSONObject> jsons, ResponseListener<BulkItemResponse> myResponseListener) throws Exception {
        if(StringUtils.isEmpty(idColName)){
            throw new Exception("批量写入ES必须指定主键字段");
        }
        RestHighLevelClient client = getClient();
        try{
            BulkRequest request = new BulkRequest();

            for (JSONObject json:jsons){
                if(StringUtils.isEmpty(json.getString(idColName))){
                    throw new Exception("批量写入ES必须指定主键字段");
                }
                request.add(new IndexRequest(index, type,json.getString(idColName))
                        .source(json.toJSONString(),XContentType.JSON));
            }

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

            //4、处理响应
            if(bulkResponse != null) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if(bulkItemResponse.isFailed()){
                        myResponseListener.fail(bulkItemResponse);
                    }else{
                        myResponseListener.success(bulkItemResponse);
                    }
                }
            }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 多条异步写入ES
     * @param index
     * @param type
     * @param idColName
     * @param jsons
     * @param listener
     * @throws Exception
     */
    public void putBulkAsync(String index, String type, String idColName, List<JSONObject> jsons, ActionListener<BulkResponse> listener) throws Exception {
        if(StringUtils.isEmpty(idColName)){
            throw new Exception("批量写入ES必须指定主键字段");
        }
        RestHighLevelClient client = getClient();
        try{
            BulkRequest request = new BulkRequest();

            for (JSONObject json:jsons){
                if(StringUtils.isEmpty(json.getString(idColName))){
                    throw new Exception("批量写入ES必须指定主键字段");
                }
                request.add(new IndexRequest(index, type,json.getString(idColName))
                        .source(json.toJSONString(),XContentType.JSON));
            }
            client.bulkAsync(request, RequestOptions.DEFAULT,listener);
        }catch (Exception e){

        }
    }

    public void delete(String index, String type, String id) throws Exception {
        RestHighLevelClient client = getClient();
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        DeleteResponse deleteResponse = client.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    public void deleteAsync(String index, String type, String id, ActionListener<DeleteResponse> listener) throws Exception {
        RestHighLevelClient client = getClient();
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        client.deleteAsync(deleteRequest, RequestOptions.DEFAULT, listener);
    }

    public void deleteBulk(String index, String type,Collection<String> ids, ResponseListener<BulkItemResponse> myResponseListener ) throws Exception {
        RestHighLevelClient client = getClient();
        BulkRequest request = new BulkRequest();

        for (String id:ids){
            if(StringUtils.isEmpty(id)){
                throw new Exception("批量删除ES必须指定主键字段");
            }
            request.add(new DeleteRequest(index, type,id));
        }
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        //4、处理响应
        if(bulkResponse != null) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if(bulkItemResponse.isFailed()){
                    myResponseListener.fail(bulkItemResponse);
                }else{
                    myResponseListener.success(bulkItemResponse);
                }
            }
        }
    }

    public void deleteBulkAsync(String index, String type, Collection<String> ids, ActionListener<BulkResponse> listener) throws Exception {
        RestHighLevelClient client = getClient();
        BulkRequest request = new BulkRequest();

        for (String id:ids){
            if(StringUtils.isEmpty(id)){
                throw new Exception("批量删除ES必须指定主键字段");
            }
            request.add(new DeleteRequest(index, type,id));
        }
        client.bulkAsync(request, RequestOptions.DEFAULT, listener);

    }
}








