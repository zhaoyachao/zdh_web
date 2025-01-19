package com.zyc.zdh.datax_generator;

import com.zyc.zdh.util.JsonUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HbaseWriter implements DataxWriter{

    class HbaseConfig{
//        private String hbase.rootdir;
//        private String hbase.cluster.distributed;
//        private String hbase.zookeeper.quorum;
    }
    class RowKey{
         private int index;
         private String type;
         private String value;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    class Parameter{
        private Map<String,Object> hbaseConfig;
        private String table;
        private List<String> column;
        private List<RowKey> rowkeyColumn;
        private String encoding;
        private String mode;
        private String maxVersion;
        private int scanCacheSize;
        private int scanBatchSize;
        private Map<String,Object> versionColumn;
        private String nullMode;
        private String walFlag;
        private String writeBufferSize;

        public Map<String, Object> getHbaseConfig() {
            return hbaseConfig;
        }

        public void setHbaseConfig(Map<String, Object> hbaseConfig) {
            this.hbaseConfig = hbaseConfig;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public List<String> getColumn() {
            return column;
        }

        public void setColumn(List<String> column) {
            this.column = column;
        }

        public List<RowKey> getRowkeyColumn() {
            return rowkeyColumn;
        }

        public void setRowkeyColumn(List<RowKey> rowkeyColumn) {
            this.rowkeyColumn = rowkeyColumn;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getMaxVersion() {
            return maxVersion;
        }

        public void setMaxVersion(String maxVersion) {
            this.maxVersion = maxVersion;
        }

        public int getScanCacheSize() {
            return scanCacheSize;
        }

        public void setScanCacheSize(int scanCacheSize) {
            this.scanCacheSize = scanCacheSize;
        }

        public int getScanBatchSize() {
            return scanBatchSize;
        }

        public void setScanBatchSize(int scanBatchSize) {
            this.scanBatchSize = scanBatchSize;
        }

        public Map<String, Object> getVersionColumn() {
            return versionColumn;
        }

        public void setVersionColumn(Map<String, Object> versionColumn) {
            this.versionColumn = versionColumn;
        }

        public String getNullMode() {
            return nullMode;
        }

        public void setNullMode(String nullMode) {
            this.nullMode = nullMode;
        }

        public String getWalFlag() {
            return walFlag;
        }

        public void setWalFlag(String walFlag) {
            this.walFlag = walFlag;
        }

        public String getWriteBufferSize() {
            return writeBufferSize;
        }

        public void setWriteBufferSize(String writeBufferSize) {
            this.writeBufferSize = writeBufferSize;
        }
    }

    
    private String name;

    private Parameter parameter;

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     *  "writer": {
     *           "name": "hbase11xwriter",
     *           "parameter": {
     *             "hbaseConfig": {
     *               "hbase.rootdir": "hdfs: //ip: 9000/hbase",
     *               "hbase.cluster.distributed": "true",
     *               "hbase.zookeeper.quorum": "***"
     *             },
     *             "table": "writer",
     *             "mode": "normal",
     *             "rowkeyColumn": [
     *                 {
     *                   "index":0,
     *                   "type":"string"
     *                 },
     *                 {
     *                   "index":-1,
     *                   "type":"string",
     *                   "value":"_"
     *                 }
     *             ],
     *             "column": [
     *               {
     *                 "index":1,
     *                 "name": "cf1:q1",
     *                 "type": "string"
     *               },
     *               {
     *                 "index":2,
     *                 "name": "cf1:q2",
     *                 "type": "string"
     *               },
     *               {
     *                 "index":3,
     *                 "name": "cf1:q3",
     *                 "type": "string"
     *               },
     *               {
     *                 "index":4,
     *                 "name": "cf2:q1",
     *                 "type": "string"
     *               },
     *               {
     *                 "index":5,
     *                 "name": "cf2:q2",
     *                 "type": "string"
     *               },
     *               {
     *                 "index":6,
     *                 "name": "cf2:q3",
     *                 "type": "string"
     *               }
     *             ],
     *             "versionColumn":{
     *               "index": -1,
     *               "value":"123456789"
     *             },
     *             "encoding": "utf-8"
     *           }
     *         }
     *       }
     */
    @Override
    public void writer(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        Map<String,Object> hbaseConfig=new HashMap<>();
        hbaseConfig.put("hbase.zookeeper.quorum", config.get("url"));

        parameter.setTable(config.get("table").toString());
        parameter.setEncoding(config.get("encoding").toString());
        parameter.setMode(config.get("mode").toString());
        parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));

        //JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());
        Map<String, Object> jsonObject=(Map<String, Object>)config.getOrDefault("param", JsonUtil.createEmptyMap());

        this.name="hbase11xwriter";
        if(jsonObject.containsKey("name")){
            this.name=jsonObject.getOrDefault("name", "").toString();
        }
        this.parameter=parameter;
    }
}