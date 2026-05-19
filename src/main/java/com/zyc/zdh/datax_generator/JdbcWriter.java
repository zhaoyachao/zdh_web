package com.zyc.zdh.datax_generator;

import com.zyc.zdh.util.JsonUtil;
import org.assertj.core.util.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JdbcWriter implements DataxWriter{

    class Connection{
        private List<String> table;
        private List<String> jdbcUrl;

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

    }

    class Parameter{
        private String username;
        private String password;
        private String driverClassName;
        private List<String> column;
        private List<String> preSql;
        private List<String> postSql;
        private int batchSize;
        private List<String> session;
        private List<Connection> connection;
        private String writeMode;

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

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public List<String> getColumn() {
            return column;
        }

        public void setColumn(List<String> column) {
            this.column = column;
        }

        public List<String> getPreSql() {
            return preSql;
        }

        public void setPreSql(List<String> preSql) {
            this.preSql = preSql;
        }

        public List<String> getPostSql() {
            return postSql;
        }

        public void setPostSql(List<String> postSql) {
            this.postSql = postSql;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public List<String> getSession() {
            return session;
        }

        public void setSession(List<String> session) {
            this.session = session;
        }

        public List<Connection> getConnection() {
            return connection;
        }

        public void setConnection(List<Connection> connection) {
            this.connection = connection;
        }

        public String getWriteMode() {
            return writeMode;
        }

        public void setWriteMode(String writeMode) {
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
     * "writer": {
     *                     "name": "oraclewriter",
     *                     "parameter": {
     *                         "username": "root",
     *                         "password": "root",
     *                         "column": [
     *                             "id",
     *                             "name"
     *                         ],
     *                         "preSql": [
     *                             "delete from test"
     *                         ],
     *                         "connection": [
     *                             {
     *                                 "jdbcUrl": "jdbc:oracle:thin:@[HOST_NAME]:PORT:[DATABASE_NAME]",
     *                                 "table": [
     *                                     "test"
     *                                 ]
     *                             }
     *                         ]
     *                     }
     *                 }
     *             }
     */
    @Override
    public void writer(Map<String, Object> config) {
        Parameter parameter=new Parameter();

        parameter.setUsername(config.get("username").toString());
        parameter.setPassword(config.get("password").toString());
        parameter.setDriverClassName(config.get("driverClassName").toString());

        parameter.setPreSql(Arrays.asList(config.get("clear").toString()));

        parameter.setColumn(Arrays.asList(config.get("column").toString().split(",")));
        Map<String, Object> jsonObject=(Map<String, Object>)config.getOrDefault("param", JsonUtil.createEmptyMap());

        parameter.setBatchSize(Integer.valueOf(jsonObject.getOrDefault("batchSize", "1024").toString()));
        parameter.setSession(Arrays.asList(config.get("param").toString()));

        Connection connection=new Connection();
        connection.setJdbcUrl(Arrays.asList(config.get("url").toString().split(",")));
        connection.setTable(Arrays.asList(config.get("table").toString().split(",")));

        parameter.setConnection(Lists.newArrayList(connection));
        this.name=config.getOrDefault("name", "xxx").toString()+"writer";
        if(jsonObject.containsKey("name")){
            this.name=jsonObject.get("name").toString();
        }
        this.parameter=parameter;
    }
}