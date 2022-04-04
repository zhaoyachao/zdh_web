package com.zyc.zdh.es;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;

/**
 * 单条同步写入默认监控
 */
public class SingleIndexResponseListener implements ResponseListener<IndexResponse>{


    @Override
    public void success(IndexResponse indexResponse) {
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("ES新增完成");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("ES更新完成");
        }
    }

    @Override
    public void fail(IndexResponse indexResponse) {
        System.out.println("失败");
    }

    @Override
    public void fail(Exception e) {
        System.out.println("失败");
        e.printStackTrace();
    }
}