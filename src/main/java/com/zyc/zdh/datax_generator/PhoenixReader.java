package com.zyc.zdh.datax_generator;

import java.util.List;
import java.util.Map;

public class PhoenixReader implements DataxReader{

    class HbaseConfig{
//        private String hbase.zookeeper.quorum;
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
     *  废弃
     */
    @Override
    public void reader(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        this.name=config.getOrDefault("name", "xxx").toString()+"reader";
        this.parameter=parameter;
    }
}