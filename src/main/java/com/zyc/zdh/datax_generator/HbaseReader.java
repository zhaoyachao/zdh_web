package com.zyc.zdh.datax_generator;

import com.zyc.zdh.util.JsonUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HbaseReader implements DataxReader{

    class HbaseConfig{
//        private String hbase.rootdir;
//        private String hbase.cluster.distributed;
//        private String hbase.zookeeper.quorum;
    }
    class Range{
         private String startRowkey;
         private String endRowkey;
         private boolean isBinaryRowkey;

        public String getStartRowkey() {
            return startRowkey;
        }

        public void setStartRowkey(String startRowkey) {
            this.startRowkey = startRowkey;
        }

        public String getEndRowkey() {
            return endRowkey;
        }

        public void setEndRowkey(String endRowkey) {
            this.endRowkey = endRowkey;
        }

        public boolean isBinaryRowkey() {
            return isBinaryRowkey;
        }

        public void setBinaryRowkey(boolean binaryRowkey) {
            isBinaryRowkey = binaryRowkey;
        }
    }
    class Parameter{
        private Map<String,Object> hbaseConfig;
        private String table;
        private List<String> column;
        private String encoding;
        private String mode;
        private String maxVersion;
        private int scanCacheSize;
        private int scanBatchSize;
        private Range range;

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

        public Range getRange() {
            return range;
        }

        public void setRange(Range range) {
            this.range = range;
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
     *  "reader": {
     *                     "name": "hbase11xreader",
     *                     "parameter": {
     *                         "hbaseConfig": {
     *                             "hbase.rootdir": "hdfs: //xxx: 9000/hbase",
     *                             "hbase.cluster.distributed": "true",
     *                             "hbase.zookeeper.quorum": "xxx"
     *                         },
     *                         "table": "users",
     *                         "encoding": "utf-8",
     *                         "mode": "normal",
     *                         "column": [
     *                             {
     *                                 "name": "rowkey",
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "name": "info: age",
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "name": "info: birthday",
     *                                 "type": "date",
     *                                 "format":"yyyy-MM-dd"
     *                             },
     *                             {
     *                                 "name": "info: company",
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "name": "address: contry",
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "name": "address: province",
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "name": "address: city",
     *                                 "type": "string"
     *                             }
     *                         ],
     *                         "range": {
     *                             "startRowkey": "",
     *                             "endRowkey": "",
     *                             "isBinaryRowkey": true
     *                         }
     *                     }
     *                 },
     *
     *  "reader": {
     *           "name": "hbase11xreader",
     *           "parameter": {
     *             "hbaseConfig": {
     *               "hbase.rootdir": "hdfs: //xxx: 9000/hbase",
     *               "hbase.cluster.distributed": "true",
     *               "hbase.zookeeper.quorum": "xxx"
     *             },
     *             "table": "users",
     *             "encoding": "utf-8",
     *             "mode": "multiVersionFixedColumn",
     *             "maxVersion": "-1",
     *             "column": [
     *                 {
     *                     "name": "rowkey",
     *                     "type": "string"
     *                 },
     *                 {
     *                     "name": "info: age",
     *                     "type": "string"
     *                 },
     *                 {
     *                     "name": "info: birthday",
     *                     "type": "date",
     *                     "format":"yyyy-MM-dd"
     *                 },
     *                 {
     *                     "name": "info: company",
     *                     "type": "string"
     *                 },
     *                 {
     *                     "name": "address: contry",
     *                     "type": "string"
     *                 },
     *                 {
     *                     "name": "address: province",
     *                     "type": "string"
     *                 },
     *                 {
     *                     "name": "address: city",
     *                     "type": "string"
     *                 }
     *             ],
     *             "range": {
     *               "startRowkey": "",
     *               "endRowkey": ""
     *             }
     *           }
     *         },
     */
    @Override
    public void reader(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        Map<String,Object> hbaseConfig=new HashMap<>();
        hbaseConfig.put("hbase.zookeeper.quorum", config.get("url"));

        parameter.setTable(config.get("table").toString());
        parameter.setEncoding(config.get("encoding").toString());

        parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));

        Map<String, Object> jsonObject=(Map<String, Object>)config.getOrDefault("param", JsonUtil.createEmptyMap());
        parameter.setMode(jsonObject.getOrDefault("mode","normal").toString());
        parameter.setScanBatchSize(Integer.parseInt(jsonObject.getOrDefault("scanBatchSize", "100").toString()));
        parameter.setScanCacheSize(Integer.parseInt(jsonObject.getOrDefault("scanCacheSize", "256").toString()));

        parameter.setMaxVersion(jsonObject.getOrDefault("maxVersion","-1").toString());

        Range range=new Range();
        range.setStartRowkey(config.get("where").toString().split(",")[0]);
        range.setEndRowkey(config.get("where").toString().split(",")[1]);
        range.setBinaryRowkey(Boolean.valueOf(jsonObject.getOrDefault("binaryRowKey", "true").toString()));

        parameter.setRange(range);

        this.name="hbase11xreader";
        if(jsonObject.containsKey("name")){
            this.name=jsonObject.get("name").toString();
        }
        this.parameter=parameter;
    }
}