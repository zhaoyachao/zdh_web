package com.zyc.zdh.datax_generator;

import com.zyc.zdh.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FtpWriter implements DataxWriter{

    class Parameter{
        private String protocol;
        private String host;
        private String path;
        private String username;
        private String password;
        private int port;
        private String encoding;
        private String fieldDelimiter;
        private int timeout=60000;
        private String connectPattern;
        private String compress;
        private String writeMode;
        private String dateFormat;
        private String fileFormat;
        private String suffix;
        private List<String> header;
        private String nullFormat;
        private String fileName;

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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
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

        public String getWriteMode() {
            return writeMode;
        }

        public void setWriteMode(String writeMode) {
            this.writeMode = writeMode;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getFileFormat() {
            return fileFormat;
        }

        public void setFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }

        public String getNullFormat() {
            return nullFormat;
        }

        public void setNullFormat(String nullFormat) {
            this.nullFormat = nullFormat;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
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
    public void writer(Map<String, Object> config) throws Exception {
        Parameter parameter=new Parameter();

        parameter.setUsername(config.get("username").toString());
        parameter.setPassword(config.get("password").toString());

        //parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));

        parameter.setProtocol("sftp");

        parameter.setHost(config.get("url").toString());
        parameter.setPort(22);
        if(config.get("url").toString().contains(":")){
            parameter.setPort(Integer.valueOf(config.get("url").toString().split(":")[1]));
        }

        if(config.get("table").toString().contains("/")){
            String[] path = config.get("table").toString().split("/");
            int i = config.get("table").toString().lastIndexOf("/");
            parameter.setPath(config.get("table").toString().substring(0,i));
            parameter.setFileName(path[path.length-1]);
        }else {
            parameter.setFileName(config.get("table").toString());
        }

        List<String> hader=new ArrayList<>();
        for (String column:config.get("column").toString().split(",")){
            if(!column.contains(":")){
                throw new Exception("列格式必须是column_name:colum_type格式");
            }
            if(column.contains(":")){
                hader.add(column.split(":")[0]);
            }else{
                hader.add(column);
            }
        }
        parameter.setHeader(hader);

        String model = config.getOrDefault("model", "append").toString();
        if(model.equalsIgnoreCase("append")){

        }else if(model.equalsIgnoreCase("overwrite")){
            model="truncate";
        }else {
            model = "nonConflict";
        }
        parameter.setWriteMode(model);

        parameter.setEncoding(config.get("encoding").toString());
        parameter.setFieldDelimiter(config.get("fieldDelimiter").toString());

        //JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());
        Map<String, Object> jsonObject=(Map<String, Object>)config.getOrDefault("param", JsonUtil.createEmptyMap());
        parameter.setNullFormat(jsonObject.getOrDefault("nullFormat","\\N ").toString());

        parameter.setCompress(config.getOrDefault("compress","").toString());
        
        this.name="ftpwriter";
        this.parameter=parameter;
    }
}