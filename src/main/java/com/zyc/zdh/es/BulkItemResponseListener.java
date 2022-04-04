package com.zyc.zdh.es;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;

/**
 * 批量同步写入默认监控
 */
public class BulkItemResponseListener implements ResponseListener<BulkItemResponse>{

    @Override
    public void success(BulkItemResponse bulkItemResponse) {
        if(bulkItemResponse.getResponse().getResult() == DocWriteResponse.Result.CREATED) System.out.println("ES同步批量创建完成");
        if(bulkItemResponse.getResponse().getResult() == DocWriteResponse.Result.UPDATED) System.out.println("ES同步批量更新完成");
        if(bulkItemResponse.getResponse().getResult() == DocWriteResponse.Result.DELETED) System.out.println("ES同步批量删除完成");
        if(bulkItemResponse.getResponse().getResult() == DocWriteResponse.Result.NOT_FOUND) System.out.println("ES同步批量未找到数据");
    }

    @Override
    public void fail(BulkItemResponse bulkItemResponse) {
        System.out.println("ES同步批量失败");
    }

    @Override
    public void fail(Exception e) {

    }
}
