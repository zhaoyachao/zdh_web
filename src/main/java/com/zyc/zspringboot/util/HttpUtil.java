package com.zyc.zspringboot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zspringboot.entity.EtlTaskInfo;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class HttpUtil {

  // 发送GET请求
  public static String getRequest(String path, List<NameValuePair> parametersBody) throws Exception, URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(path);
    uriBuilder.setParameters(parametersBody);
    HttpGet get = new HttpGet(uriBuilder.build());
    HttpClient client = HttpClientBuilder.create().build();
    try {
      HttpResponse response = client.execute(get);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400)
        throw new RuntimeException((new StringBuilder()).append("Could not access protected resource. Server returned http code: ").append(code).toString());
      return EntityUtils.toString(response.getEntity());
    }
    catch (ClientProtocolException e) {
      throw new Exception("postRequest -- Client protocol exception!", e);
    }
    catch (IOException e) {
      throw new Exception("postRequest -- IO error!", e);
    }
    finally {
      get.releaseConnection();
    }
  }

  // 发送POST请求（普通表单形式）
  public static String postForm(String path, List<NameValuePair> parametersBody) throws Exception {
    HttpEntity entity = new UrlEncodedFormEntity(parametersBody, Charsets.UTF_8);
    return postRequest(path, "application/x-www-form-urlencoded", entity);
  }

  // 发送POST请求（JSON形式）
  public static String postJSON(String path, String json) throws Exception {
    StringEntity entity = new StringEntity(json, Charsets.UTF_8);
    return postRequest(path, "application/json", entity);
  }

  // 发送POST请求
  public static String postRequest(String path, String mediaType, HttpEntity entity) throws Exception {
    //logger.debug("[postRequest] resourceUrl: {}", path);
    HttpPost post = new HttpPost(path);
    post.addHeader("Content-Type", mediaType);
    post.addHeader("Accept", "application/json");
    post.setEntity(entity);
    try {
      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(post);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400)
        throw new Exception(EntityUtils.toString(response.getEntity()));
      return EntityUtils.toString(response.getEntity());
    }
    catch (ClientProtocolException e) {
      throw new Exception("postRequest -- Client protocol exception!", e);
    }
    catch (IOException e) {
      throw new Exception("postRequest -- IO error!", e);
    }
    finally {
      post.releaseConnection();
    }
  }

  public static void main(String[] args) throws Exception{

    String url="http://127.0.0.1:60001/api/v1/zdh";
    EtlTaskInfo etlTaskInfo=new EtlTaskInfo();
    etlTaskInfo.setId("1111111111");
    String json=JSON.toJSONString(etlTaskInfo);
    postJSON(url,json);
    while (true){

    }

  }


}
