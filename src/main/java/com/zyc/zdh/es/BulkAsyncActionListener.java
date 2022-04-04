package com.zyc.zdh.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * 批量异步写入,默认监控
 */
public class BulkAsyncActionListener implements ActionListener<BulkResponse> {

    @Override
    public void onResponse(BulkResponse bulkItemResponses) {
        if(!bulkItemResponses.hasFailures()){
            System.out.println("ES异步批量完成");
        }else{
            System.out.println("ES异步批量失败");
        }
    }

    @Override
    public void onFailure(Exception e) {
        System.out.println("ES异步批量失败");
        e.printStackTrace();
    }
}