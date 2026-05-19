package com.zyc.zdh.datax_generator;

import java.util.*;

public class FtpReader implements DataxReader{

    class Parameter{
        private String protocol;
        private String host;
        private List<Map<String,Object>> column;
        private List<String> path;
        private String username;
        private String password;
        private int port;
        private String encoding;
        private String fieldDelimiter;
        private int timeout=60000;
        private String connectPattern;
        private String compress;
        private Map<String,Object> csvReaderConfig;
        private String nullFormat;
        private int maxTraversalLevel;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public List<Map<String,Object>> getColumn() {
            return column;
        }

        public void setColumn(List<Map<String,Object>> column) {
            this.column = column;
        }

        public List<String> getPath() {
            return path;
        }

        public void setPath(List<String> path) {
            this.path = path;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
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

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public String getConnectPattern() {
            return connectPattern;
        }

        public void setConnectPattern(String connectPattern) {
            this.connectPattern = connectPattern;
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

        public String getNullFormat() {
            return nullFormat;
        }

        public void setNullFormat(String nullFormat) {
            this.nullFormat = nullFormat;
        }

        public int getMaxTraversalLevel() {
            return maxTraversalLevel;
        }

        public void setMaxTraversalLevel(int maxTraversalLevel) {
            this.maxTraversalLevel = maxTraversalLevel;
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
     *                     "name": "ftpreader",
     *                     "parameter": {
     *                         "protocol": "sftp",
     *                         "host": "127.0.0.1",
     *                         "port": 22,
     *                         "username": "xx",
     *                         "password": "xxx",
     *                         "path": [
     *                             "/home/hanfa.shf/ftpReaderTest/data"
     *                         ],
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
     *                         "encoding": "UTF-8",
     *                         "fieldDelimiter": ","
     *                     }
     *                 },
     */
    @Override
    public void reader(Map<String, Object> config) throws Exception {
        Parameter parameter=new Parameter();

        parameter.setUsername(config.get("username").toString());
        parameter.setPassword(config.get("password").toString());

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

        parameter.setProtocol("sftp");

        parameter.setHost(config.get("url").toString());
        parameter.setPort(22);
        if(config.get("url").toString().contains(":")){
            parameter.setPort(Integer.valueOf(config.get("url").toString().split(":")[1]));
        }

        parameter.setPath(Arrays.asList(config.get("table").toString().split(",")));

        parameter.setEncoding(config.get("encoding").toString());
        parameter.setFieldDelimiter(config.get("fieldDelimiter").toString());
        parameter.setNullFormat(config.get("nullFormat").toString());
        parameter.setCompress(config.get("compress").toString());
        
        this.name="ftpreader";
        this.parameter=parameter;
    }
}