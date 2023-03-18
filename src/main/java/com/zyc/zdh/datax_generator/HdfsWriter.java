package com.zyc.zdh.datax_generator;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HdfsWriter implements DataxWriter{

    class HadoopConfig{
//           "dfs.nameservices": "testDfs",
//           "dfs.ha.namenodes.testDfs": "namenode1,namenode2",
//           "dfs.namenode.rpc-address.aliDfs.namenode1": "",
//           "dfs.namenode.rpc-address.aliDfs.namenode2": "",
//           "dfs.client.failover.proxy.provider.testDfs": "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
    }
    class Parameter{
        private Map<String,Object> hadoopConfig;
        private String path;
        private String defaultFS;
        private List<String> column;
        private String encoding;
        private String fileType;
        private String fieldDelimiter;
        private String nullFormat;
        private boolean haveKerberos=false;
        private String kerberosKeytabFilePath;
        private String kerberosPrincipal;
        private String compress;
        private Map<String,Object> csvReaderConfig;

        public Map<String, Object> getHadoopConfig() {
            return hadoopConfig;
        }

        public void setHadoopConfig(Map<String, Object> hadoopConfig) {
            this.hadoopConfig = hadoopConfig;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDefaultFS() {
            return defaultFS;
        }

        public void setDefaultFS(String defaultFS) {
            this.defaultFS = defaultFS;
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

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
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

        public boolean isHaveKerberos() {
            return haveKerberos;
        }

        public void setHaveKerberos(boolean haveKerberos) {
            this.haveKerberos = haveKerberos;
        }

        public String getKerberosKeytabFilePath() {
            return kerberosKeytabFilePath;
        }

        public void setKerberosKeytabFilePath(String kerberosKeytabFilePath) {
            this.kerberosKeytabFilePath = kerberosKeytabFilePath;
        }

        public String getKerberosPrincipal() {
            return kerberosPrincipal;
        }

        public void setKerberosPrincipal(String kerberosPrincipal) {
            this.kerberosPrincipal = kerberosPrincipal;
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
     *  "writer": {
     *                     "name": "hdfswriter",
     *                     "parameter": {
     *                         "defaultFS": "hdfs://xxx:port",
     *                         "fileType": "orc",
     *                         "path": "/user/hive/warehouse/writerorc.db/orcfull",
     *                         "fileName": "xxxx",
     *                         "column": [
     *                             {
     *                                 "name": "col1",
     *                                 "type": "TINYINT"
     *                             },
     *                             {
     *                                 "name": "col2",
     *                                 "type": "SMALLINT"
     *                             },
     *                             {
     *                                 "name": "col3",
     *                                 "type": "INT"
     *                             },
     *                             {
     *                                 "name": "col4",
     *                                 "type": "BIGINT"
     *                             },
     *                             {
     *                                 "name": "col5",
     *                                 "type": "FLOAT"
     *                             },
     *                             {
     *                                 "name": "col6",
     *                                 "type": "DOUBLE"
     *                             },
     *                             {
     *                                 "name": "col7",
     *                                 "type": "STRING"
     *                             },
     *                             {
     *                                 "name": "col8",
     *                                 "type": "VARCHAR"
     *                             },
     *                             {
     *                                 "name": "col9",
     *                                 "type": "CHAR"
     *                             },
     *                             {
     *                                 "name": "col10",
     *                                 "type": "BOOLEAN"
     *                             },
     *                             {
     *                                 "name": "col11",
     *                                 "type": "date"
     *                             },
     *                             {
     *                                 "name": "col12",
     *                                 "type": "TIMESTAMP"
     *                             }
     *                         ],
     *                         "writeMode": "append",
     *                         "fieldDelimiter": "\t",
     *                         "compress":"NONE"
     *                     }
     *                 }
     *             }
     */
    @Override
    public void writer(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        Map<String,Object> hadoopConfig=new HashMap<>();
        //hadoopConfig.put("hbase.zookeeper.quorum", config.get("url"));

        parameter.setDefaultFS(config.get("url").toString());
        parameter.setPath(config.get("path").toString());


        parameter.setFileType(config.get("fileType").toString());
        parameter.setEncoding(config.get("encoding").toString());
        parameter.setFieldDelimiter(config.get("fieldDelimiter").toString());
        parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));

        JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());

        parameter.setNullFormat(jsonObject.get("nullFormat").toString());
        parameter.setHaveKerberos(Boolean.valueOf(jsonObject.getOrDefault("haveKerberos","false").toString()));
        parameter.setKerberosKeytabFilePath(jsonObject.get("kerberosKeytabFilePath").toString());
        parameter.setKerberosPrincipal(jsonObject.get("kerberosPrincipal").toString());

        parameter.setCompress(jsonObject.get("compress").toString());

        this.name="hdfswriter";
        this.parameter=parameter;
    }
}