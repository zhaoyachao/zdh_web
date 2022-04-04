package com.zyc.zdh.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;

/**
 * 单条异步写入监控
 */
public class SingleAsyncActionListener implements ActionListener<IndexResponse> {


    @Override
    public void onResponse(IndexResponse indexResponse) {

        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("ES异步新增完成");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("ES异步更新完成");
        }

    }

    @Override
    public void onFailure(Exception e) {
        System.out.println("异步失败");
        e.printStackTrace();
    }
}