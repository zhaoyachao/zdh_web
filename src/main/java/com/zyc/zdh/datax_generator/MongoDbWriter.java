package com.zyc.zdh.datax_generator;

import com.zyc.zdh.util.JsonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MongoDbWriter implements DataxWriter{

    class Parameter{
        private String userName;
        private String userPassword;
        private List<String> column;
        private List<String> address;
        private String dbName;
        private String collectionName;
        private Map<String,Object> writeMode;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public List<String> getColumn() {
            return column;
        }

        public void setColumn(List<String> column) {
            this.column = column;
        }

        public List<String> getAddress() {
            return address;
        }

        public void setAddress(List<String> address) {
            this.address = address;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getCollectionName() {
            return collectionName;
        }

        public void setCollectionName(String collectionName) {
            this.collectionName = collectionName;
        }

        public Map<String, Object> getWriteMode() {
            return writeMode;
        }

        public void setWriteMode(Map<String, Object> writeMode) {
            this.writeMode = writeMode;
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
     *           "writer": {
     *               "name": "mongodbwriter",
     *               "parameter": {
     *                   "address": [
     *                       "127.0.0.1:27017"
     *                   ],
     *                   "userName": "",
     *                   "userPassword": "",
     *                   "dbName": "tag_per_data",
     *                   "collectionName": "tag_data",
     *                   "column": [
     *                       {
     *                           "name": "unique_id",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "sid",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "user_id",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "auction_id",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "content_type",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "pool_type",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "frontcat_id",
     *                           "type": "Array",
     *                           "splitter": " "
     *                       },
     *                       {
     *                           "name": "categoryid",
     *                           "type": "Array",
     *                           "splitter": " "
     *                       },
     *                       {
     *                           "name": "gmt_create",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "taglist",
     *                           "type": "Array",
     *                           "splitter": " "
     *                       },
     *                       {
     *                           "name": "property",
     *                           "type": "string"
     *                       },
     *                       {
     *                           "name": "scorea",
     *                           "type": "int"
     *                       },
     *                       {
     *                           "name": "scoreb",
     *                           "type": "int"
     *                       },
     *                       {
     *                           "name": "scorec",
     *                           "type": "int"
     *                       }
     *                   ],
     *                 "writeMode": {
     *                   "isReplace": "true",
     *                   "replaceKey": "unique_id"
     *                 }
     *               }
     *           }
     *       }
     */
    @Override
    public void writer(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        parameter.setUserName(config.get("username").toString());
        parameter.setUserPassword(config.get("password").toString());

        parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));


        parameter.setAddress(Arrays.asList(config.get("url").toString().split(",")));
        parameter.setDbName(config.get("table").toString().split("\\.")[0]);
        parameter.setCollectionName(config.get("table").toString().split("\\.")[1]);

        Map<String, Object> jsonObject=(Map<String, Object>)config.getOrDefault("param", JsonUtil.createEmptyMap());

        parameter.setWriteMode(jsonObject);

        this.name="mongodbreader";
        this.parameter=parameter;
    }
}