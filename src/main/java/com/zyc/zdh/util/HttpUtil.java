package com.zyc.zdh.util;

import com.zyc.zdh.entity.EtlTaskInfo;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


public class HttpUtil {

  // 发送GET请求
  public static String getRequest(String path, List<NameValuePair> parametersBody) throws Exception, URISyntaxException {
    return getRequest(path, parametersBody, null);
  }
  public static String getRequest(String path, List<NameValuePair> parametersBody, Map<String,String> header, Map<String,String> cookie) throws Exception, URISyntaxException {
    return getRequest(path, parametersBody, header, cookie, null);
  }

  public static String getRequest(String path, List<NameValuePair> parametersBody, HttpHost proxy) throws Exception, URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(path);
    uriBuilder.setParameters(parametersBody);
    HttpGet get = new HttpGet(uriBuilder.build());
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    if(proxy != null){
      httpClientBuilder = httpClientBuilder.setProxy(proxy);
    }
    HttpClient client = httpClientBuilder.build();

    try {
      HttpResponse response = client.execute(get);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400) {
        throw new RuntimeException((new StringBuilder()).append("Could not access protected resource. Server returned http code: ").append(code).toString());
      }
      return EntityUtils.toString(response.getEntity());
    }
    catch (ClientProtocolException e) {
      throw new Exception("getRequest -- Client protocol exception!", e);
    }
    catch (IOException e) {
      throw new Exception("getRequest -- IO error!", e);
    }
    finally {
      get.releaseConnection();
    }
  }
  public static String getRequest(String path, List<NameValuePair> parametersBody, Map<String,String> header, Map<String,String> cookie, HttpHost proxy) throws Exception, URISyntaxException {
    URIBuilder uriBuilder = new URIBuilder(path);
    uriBuilder.setParameters(parametersBody);
    HttpGet get = new HttpGet(uriBuilder.build());
    if(header!=null){
      for(String key: header.keySet()){
        get.addHeader(key, header.get(key));
      }
    }
    CookieStore cookieStore = new BasicCookieStore();;
    if(cookie!=null){
      for(String key: header.keySet()){
        //添加cookie
        BasicClientCookie ck = new BasicClientCookie(key, header.get(key));
        //放入cookiestore
        cookieStore.addCookie(ck);
      }
    }

    // 设置超时时间
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(5000).build();

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    if(proxy != null){
      httpClientBuilder = httpClientBuilder.setProxy(proxy);
    }
    HttpClient client =httpClientBuilder.setDefaultRequestConfig(requestConfig).setDefaultCookieStore(cookieStore).build();
    try {
      HttpResponse response = client.execute(get);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400) {
        throw new RuntimeException((new StringBuilder()).append("Could not access protected resource. Server returned http code: ").append(code).toString());
      }
      return EntityUtils.toString(response.getEntity());
    }
    catch (ClientProtocolException e) {
      throw new Exception("getRequest -- Client protocol exception!", e);
    }
    catch (IOException e) {
      throw new Exception("getRequest -- IO error!", e);
    }
    finally {
      get.releaseConnection();
    }
  }


  // 发送POST请求（普通表单形式）
  public static String postForm(String path, List<NameValuePair> parametersBody) throws Exception {
    HttpEntity entity = new UrlEncodedFormEntity(parametersBody, Charsets.UTF_8);
    return postRequest(path, "application/x-www-form-urlencoded", entity, null);
  }

  // 发送POST请求（JSON形式）
  public static String postJSON(String path, String json) throws Exception {
    StringEntity entity = new StringEntity(json, Charsets.UTF_8);
    return postRequest(path, "application/json", entity, null);
  }

  public static String postJSON(String path, String json, Map<String,String> header, Map<String,String> cookie) throws Exception {
    StringEntity entity = new StringEntity(json, Charsets.UTF_8);
    return postRequest(path, entity, header, cookie, null);
  }

  // 发送POST请求（JSON形式）
  public static String postJSON(String path, String json, HttpHost proxy) throws Exception {
    StringEntity entity = new StringEntity(json, Charsets.UTF_8);
    return postRequest(path, "application/json", entity, proxy);
  }

  public static String postJSON(String path, String json, Map<String,String> header, Map<String,String> cookie, HttpHost proxy) throws Exception {
    StringEntity entity = new StringEntity(json, Charsets.UTF_8);
    return postRequest(path, entity, header, cookie, proxy);
  }

  // 发送POST请求
  public static String postRequest(String path, String mediaType, HttpEntity entity, HttpHost proxy) throws Exception {
      //LogUtil.debug(this.getClass(), "[postRequest] resourceUrl: {}", path);
    LogUtil.info(HttpUtil.class, "path: {}, param: {}, content-type: {}", path, EntityUtils.toString(entity), mediaType);
    HttpPost post = new HttpPost(path);
    post.addHeader("Content-Type", mediaType);
    post.addHeader("Accept", "application/json");
    post.setEntity(entity);
    try {
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      if(proxy != null){
        httpClientBuilder = httpClientBuilder.setProxy(proxy);
      }
      HttpClient client = httpClientBuilder.build();
      HttpResponse response = client.execute(post);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400) {
          throw new Exception(EntityUtils.toString(response.getEntity()));
      }
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

  public static String postRequest(String path, HttpEntity entity, Map<String,String> header, Map<String,String> cookie, HttpHost proxy) throws Exception {
      //LogUtil.debug(this.getClass(), "[postRequest] resourceUrl: {}", path);
    HttpPost post = new HttpPost(path);

    //默认
    //post.addHeader("Content-Type", "application/json");
    //post.addHeader("Accept", "application/json");

    if(header!=null){
      for(String key: header.keySet()){
        post.addHeader(key, header.get(key));
      }
    }
    CookieStore cookieStore = new BasicCookieStore();
    if(cookie!=null){
      for(String key: header.keySet()){
        //添加cookie
        BasicClientCookie ck = new BasicClientCookie(key, header.get(key));
        //放入cookiestore
        cookieStore.addCookie(ck);
      }
    }
    // 设置超时时间
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(5000).build();

    post.setEntity(entity);
    try {
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      if(proxy != null){
        httpClientBuilder = httpClientBuilder.setProxy(proxy);
      }

      HttpClient client = httpClientBuilder.setDefaultRequestConfig(requestConfig).setDefaultCookieStore(cookieStore).build();
      HttpResponse response = client.execute(post);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400) {
          throw new Exception(EntityUtils.toString(response.getEntity()));
      }
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


  public static String patchRequest(String path, List<NameValuePair> parametersBody) throws Exception {
      //LogUtil.debug(this.getClass(), "[postRequest] resourceUrl: {}", path);
    HttpPatch patch = new HttpPatch(path);
    patch.addHeader("Content-Type", "application/json");
    patch.addHeader("Accept", "application/json");
    HttpEntity entity = new UrlEncodedFormEntity(parametersBody, Charsets.UTF_8);
    patch.setEntity(entity);
    try {
      HttpClient client = HttpClientBuilder.create().build();
      HttpResponse response = client.execute(patch);
      int code = response.getStatusLine().getStatusCode();
      if (code >= 400) {
          throw new Exception(EntityUtils.toString(response.getEntity()));
      }
      return EntityUtils.toString(response.getEntity());
    }
    catch (ClientProtocolException e) {
      throw new Exception("patchRequest -- Client protocol exception!", e);
    }
    catch (IOException e) {
      throw new Exception("patchRequest -- IO error!", e);
    }
    finally {
      patch.releaseConnection();
    }
  }

  public static void main(String[] args) throws Exception{

    String url="http://127.0.0.1:8081/api/call_back_test";
    EtlTaskInfo etlTaskInfo=new EtlTaskInfo();
    etlTaskInfo.setId("1111111111");
    String json=JsonUtil.formatJsonString(etlTaskInfo);
    System.out.println(postJSON(url,json));

  }


}
