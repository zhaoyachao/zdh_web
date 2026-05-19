package com.zyc.zdh.util;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class KettleUtilTest {

    @Test
    public void getJobNamesByRepository() throws Exception {

        String dbType="mysql";
        String repositoryName="";
        Map<String, String> options=null;
        String repositoryUser="admin";
        String repositoryPassword="admin";
        KettleUtil.DataSource dataSource = new KettleUtil.DataSource("mydb", dbType, "Native","127.0.0.1", "mydb","3306", "zyc", "123456");
        KettleUtil kettleUtil = new KettleUtil();
        List<String> jobNamesByRepository = kettleUtil.getJobNamesByRepository(dbType, repositoryName, dataSource, options, repositoryUser, repositoryPassword);
        System.out.println(JsonUtil.formatJsonString(jobNamesByRepository));
    }

    @Test
    public void getTransNamesByRepository() {
    }

    @Test
    public void url() throws URISyntaxException {
        URI uri =  URI.create("mysql://127.0.0.1:3306/zdh?serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true");

        // 提取 URI 组件
        System.out.println("Scheme: " + uri.getScheme());           // https
        System.out.println("Authority: " + uri.getAuthority());     // www.example.com:8080
        System.out.println("Host: " + uri.getHost());               // www.example.com
        System.out.println("Port: " + uri.getPort());               // 8080
        System.out.println("Path: " + uri.getPath());               // /path/to/resource
        System.out.println("Query: " + uri.getQuery());             // param1=value1&param2=value2
        System.out.println("Fragment: " + uri.getFragment());
    }
}