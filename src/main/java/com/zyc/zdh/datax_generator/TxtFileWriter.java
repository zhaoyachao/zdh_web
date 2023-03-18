package com.zyc.zdh.datax_generator;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class TxtFileWriter implements DataxWriter{

    class Parameter{
        private String path;
        private String fileName;
        private List<Map<String,Object>> column;
        private List<String> header;
        private String fileFormat;

        private String writeMode;
        private String encoding;
        private String fieldDelimiter;
        private String nullFormat;
        private String compress;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public List<Map<String,Object>> getColumn() {
            return column;
        }

        public void setColumn(List<Map<String,Object>> column) {
            this.column = column;
        }

        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }

        public String getFileFormat() {
            return fileFormat;
        }

        public void setFileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
        }

        public String getWriteMode() {
            return writeMode;
        }

        public void setWriteMode(String writeMode) {
            this.writeMode = writeMode;
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

        public String getCompress() {
            return compress;
        }

        public void setCompress(String compress) {
            this.compress = compress;
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
     * "writer": {
     *                     "name": "txtfilewriter",
     *                     "parameter": {
     *                         "path": "/home/haiwei.luo/case00/result",
     *                         "fileName": "luohw",
     *                         "writeMode": "truncate",
     *                         "dateFormat": "yyyy-MM-dd"
     *                     }
     *                 }
     *             }
     */
    @Override
    public void writer(Map<String, Object> config) throws Exception {
        Parameter parameter=new Parameter();

        Map<String,Object> hadoopConfig=new HashMap<>();
        //hadoopConfig.put("hbase.zookeeper.quorum", config.get("url"));

        if(config.get("table").toString().contains("/")){
            String[] path = config.get("table").toString().split("/");
            int i = config.get("table").toString().lastIndexOf("/");
            parameter.setPath(config.get("table").toString().substring(0,i));
            parameter.setFileName(path[path.length-1]);
        }else {
            parameter.setFileName(config.get("table").toString());
        }
        JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());

        parameter.setEncoding(config.get("encoding").toString());
        parameter.setFieldDelimiter(config.get("fieldDelimiter").toString());

        String model = config.getOrDefault("model", "append").toString();
        if(model.equalsIgnoreCase("append")){

        }else if(model.equalsIgnoreCase("overwrite")){
            model="truncate";
        }else {
            model = "nonConflict";
        }
        parameter.setWriteMode(model);
        parameter.setColumn(Arrays.asList());
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
        parameter.setNullFormat(jsonObject.getOrDefault("nullFormat","\\N ").toString());

        parameter.setCompress(config.getOrDefault("compress","").toString());

        this.name="txtfilewriter";
        this.parameter=parameter;
    }
}