package com.zyc.zdh.datax_generator;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JdbcReader implements DataxReader{

    class Connection{
        private List<String> table;
        private List<String> jdbcUrl;
        private List<String> querySql;

        public List<String> getTable() {
            return table;
        }

        public void setTable(List<String> table) {
            this.table = table;
        }

        public List<String> getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(List<String> jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public List<String> getQuerySql() {
            return querySql;
        }

        public void setQuerySql(List<String> querySql) {
            this.querySql = querySql;
        }
    }

    class Parameter{
        private String username;
        private String password;
        private List<String> column;
        private String splitPk;
        private List<Connection> connection;
        private String where;

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

        public List<String> getColumn() {
            return column;
        }

        public void setColumn(List<String> column) {
            this.column = column;
        }

        public String getSplitPk() {
            return splitPk;
        }

        public void setSplitPk(String splitPk) {
            this.splitPk = splitPk;
        }

        public List<Connection> getConnection() {
            return connection;
        }

        public void setConnection(List<Connection> connection) {
            this.connection = connection;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
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
     * "reader": {
     *                     "name": "mysqlreader",
     *                     "parameter": {
     *                         "username": "root",
     *                         "password": "root",
     *                         "column": [
     *                             "id",
     *                             "name"
     *                         ],
     *                         "splitPk": "db_id",
     *                         "connection": [
     *                             {
     *                                 "table": [
     *                                     "table"
     *                                 ],
     *                                 "jdbcUrl": [
     *      "jdbc:mysql://127.0.0.1:3306/database"
     *                                 ]
     *                             }
     *                         ]
     *                     }
     *                 },
     *
     *  "reader": {
     *                     "name": "mysqlreader",
     *                     "parameter": {
     *                         "username": "root",
     *                         "password": "root",
     *                         "connection": [
     *                             {
     *                                 "querySql": [
     *                                     "select db_id,on_line_flag from db_info where db_id < 10;"
     *                                 ],
     *                                 "jdbcUrl": [
     *                                     "jdbc:mysql://bad_ip:3306/database",
     *                                     "jdbc:mysql://127.0.0.1:bad_port/database",
     *                                     "jdbc:mysql://127.0.0.1:3306/database"
     *                                 ]
     *                             }
     *                         ]
     *                     }
     *                 },
     */
    @Override
    public void reader(Map<String, Object> config) {
        try{
            Parameter parameter=new Parameter();

            parameter.setUsername(config.get("username").toString());
            parameter.setPassword(config.get("password").toString());
            if(!StringUtils.isEmpty(config.getOrDefault("splitPk","").toString())){
                parameter.setSplitPk(config.getOrDefault("splitPk","").toString());
            }

            parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));

            parameter.setWhere(config.getOrDefault("where","").toString());


            Connection connection=new Connection();
            connection.setJdbcUrl(Arrays.asList(config.get("url").toString().split(",")));

            if(!StringUtils.isEmpty(config.getOrDefault("table","").toString())){
                connection.setTable(Arrays.asList(config.get("table").toString().split(",")));
            }

            if(!StringUtils.isEmpty(config.getOrDefault("querySql","").toString())){
                connection.setQuerySql(Arrays.asList(config.getOrDefault("querySql","").toString().split(",")));
            }


            parameter.setConnection(Lists.newArrayList(connection));
            JSONObject jsonObject=(JSONObject)config.getOrDefault("param", new JSONObject());
            this.name=config.getOrDefault("name", "xxx").toString()+"reader";
            if(jsonObject.containsKey("name")){
                this.name=jsonObject.getString("name");
            }
            this.parameter=parameter;
        }catch (Exception e){
            throw e;
        }

    }
}