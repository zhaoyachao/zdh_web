package com.zyc.zdh.datax_generator;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class TxtFileReader implements DataxReader{

    class Parameter{
        private List<String> path;
        private List<Map<String,Object>> column;
        private String encoding;
        private String fieldDelimiter;
        private String nullFormat;
        private String skipHeader;
        private String compress;
        private Map<String,Object> csvReaderConfig;

        public List<String> getPath() {
            return path;
        }

        public void setPath(List<String> path) {
            this.path = path;
        }

        public List<Map<String,Object>> getColumn() {
            return column;
        }

        public void setColumn(List<Map<String,Object>> column) {
            this.column = column;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public String getFieldDelimiter() {
            return fieldDelimiter;
        }

        public void setFieldDelimiter(String fieldDelimiter) {
            this.fieldDelimiter = fieldDelimiter;
        }

        public String getNullFormat() {
            return nullFormat;
        }

        public void setNullFormat(String nullFormat) {
            this.nullFormat = nullFormat;
        }

        public String getSkipHeader() {
            return skipHeader;
        }

        public void setSkipHeader(String skipHeader) {
            this.skipHeader = skipHeader;
        }

        public String getCompress() {
            return compress;
        }

        public void setCompress(String compress) {
            this.compress = compress;
        }

        public Map<String, Object> getCsvReaderConfig() {
            return csvReaderConfig;
        }

        public void setCsvReaderConfig(Map<String, Object> csvReaderConfig) {
            this.csvReaderConfig = csvReaderConfig;
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
     *                     "name": "txtfilereader",
     *                     "parameter": {
     *                         "path": ["/home/haiwei.luo/case00/data"],
     *                         "encoding": "UTF-8",
     *                         "column": [
     *                             {
     *                                 "index": 0,
     *                                 "type": "long"
     *                             },
     *                             {
     *                                 "index": 1,
     *                                 "type": "boolean"
     *                             },
     *                             {
     *                                 "index": 2,
     *                                 "type": "double"
     *                             },
     *                             {
     *                                 "index": 3,
     *                                 "type": "string"
     *                             },
     *                             {
     *                                 "index": 4,
     *                                 "type": "date",
     *                                 "format": "yyyy.MM.dd"
     *                             }
     *                         ],
     *                         "fieldDelimiter": ","
     *                     }
     *                 },
     */
    @Override
    public void reader(Map<String, Object> config) throws Exception {
        Parameter parameter=new Parameter();

        Map<String,Object> hadoopConfig=new HashMap<>();
        //hadoopConfig.put("hbase.zookeeper.quorum", config.get("url"));

        parameter.setPath(Arrays.asList(config.get("table").toString().split(",")));

        JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());
        parameter.setEncoding(config.get("encoding").toString());
        parameter.setFieldDelimiter(config.get("fieldDelimiter").toString());
        List<Map<String,Object>> columns=new ArrayList<>();
        for(int i=0;i<config.get("column").toString().split(",").length;i++){
            String column = config.get("column").toString().split(",")[i];
            if(column.contains(":")){
                Map<String,Object> map=new HashMap<>();
                String name = column.split(":")[0];
                String type = column.split(":")[1];
                map.put("type", type);
                if(column.split(":").length==3){
                    String value = column.split(":")[2];
                    map.put("value", value);
                }else{
                    map.put("index", i);
                }
                columns.add(map);
            }else{
                throw new Exception("列格式必须是column_name:colum_type格式");
            }
        }

        parameter.setColumn(columns);
        parameter.setSkipHeader(config.get("skipHeader").toString());
        parameter.setNullFormat(jsonObject.getOrDefault("nullFormat","").toString());

        parameter.setCompress(jsonObject.getOrDefault("compress","").toString());

        this.name="txtfilereader";
        this.parameter=parameter;
    }
}