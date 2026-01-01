package com.zyc.zdh.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 优化的HTTP工具类，支持连接池和自动重试机制,日志等
 */
public class HttpUtil {

    // 使用配置类管理参数
    private static final HttpPoolConfig config = HttpPoolConfig.getInstance();

    private static PoolingHttpClientConnectionManager connectionManager;
    private static CloseableHttpClient httpClient;
    private CloseableHttpClient httpClientCustom;
    private Map<String, String> cookieCustom = new HashMap<>();
    private Map<String, String> headersCustom = new HashMap<>();

    public HttpUtil() {

    }

    public HttpUtil(CloseableHttpClient httpClientCustom, Map<String, String> headersCustom, Map<String, String> cookieCustom) {
        this.httpClientCustom = httpClientCustom;
        this.headersCustom = headersCustom;
        this.cookieCustom = cookieCustom;
    }

    static {
        initHttpClient();
    }

    /**
     * 初始化连接池和HttpClient
     */
    private static synchronized void initHttpClient() {
        if (connectionManager == null) {
            connectionManager = new PoolingHttpClientConnectionManager();
            // 设置整个连接池最大连接数
            connectionManager.setMaxTotal(config.getMaxTotal());
            // 设置每个路由的最大连接数
            connectionManager.setDefaultMaxPerRoute(config.getMaxPerRoute());

            // 配置连接池定时清理空闲和过期连接
            connectionManager.closeExpiredConnections();
            connectionManager.closeIdleConnections(config.getEvictIdleTime(), TimeUnit.MILLISECONDS);
        }

        if (httpClient == null) {
            // 配置重试策略
            HttpRequestRetryStrategy retryStrategy = new HttpRequestRetryStrategy(
                config.getRetryCount(), config.getRetryInterval());

            httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setRetryHandler(retryStrategy)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(config.getConnectionTimeout())
                            .setSocketTimeout(config.getSocketTimeout())
                            .setConnectionRequestTimeout(1000) // 从连接池获取连接的超时时间
                            .build())
                    .build();
        }
    }

    /**
     * 获取连接池统计信息
     */
    public static String getPoolStats() {
        if (connectionManager == null) {
            return "Connection pool not initialized";
        }
        return String.format("Total: %d, Available: %d, Leased: %d, Pending: %d, Max: %d",
                connectionManager.getTotalStats().getAvailable(),
                connectionManager.getTotalStats().getAvailable(),
                connectionManager.getTotalStats().getLeased(),
                connectionManager.getTotalStats().getPending(),
                connectionManager.getMaxTotal());
    }

    /**
     * 发送GET请求（支持重试）
     * 默认不重试
     */
    public String getRequest(String path, List<NameValuePair> parametersBody) throws Exception {
        return getRequest(path, parametersBody, null, null);
    }

    /**
     * 发送POST请求（表单形式）
     * 默认不重试
     */
    public String postForm(String path, List<NameValuePair> parametersBody) throws Exception {
        HttpEntity entity = new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8);
        return postRequest(path, "application/x-www-form-urlencoded", entity);
    }
    /**
     * 发送POST请求（JSON形式）
     * 默认不重试
     */
    public String postJSON(String path, String json) throws Exception {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, "application/json", entity);
    }

    public String postJSON(String path, HttpEntity entity) throws Exception {
        return postRequest(path, "application/json", entity);
    }

    /**
     * 默认不重试
     * @param path
     * @param json
     * @param header
     * @param cookie
     * @return
     * @throws Exception
     */
    public String postJSON(String path, String json, Map<String, String> header,
                                 Map<String, String> cookie) throws Exception {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, entity, header, cookie);
    }

    /**
     * 默认不重试
     * @param path
     * @param parametersBody
     * @param header
     * @param cookie
     * @return
     * @throws Exception
     */
    public String getRequest(String path, List<NameValuePair> parametersBody, Map<String, String> header,
                             Map<String, String> cookie) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(path);
        uriBuilder.setParameters(parametersBody);
        String fullUrl = uriBuilder.build().toString();

        long startTime = System.currentTimeMillis();
        try {
            // 构建请求日志信息
            StringBuilder requestLog = new StringBuilder("HTTP请求 - ")
                    .append("Method: GET, ")
                    .append("URL: ").append(fullUrl);

            if (parametersBody != null && !parametersBody.isEmpty()) {
                requestLog.append(", Parameters: ").append(parametersBody);
            }
            if (header != null && !header.isEmpty()) {
                requestLog.append(", Headers: ").append(header);
            }
            if (cookie != null && !cookie.isEmpty()) {
                requestLog.append(", Cookies: ").append(cookie);
            }

            LogUtil.info(HttpUtil.class, requestLog.toString());

            HttpGet get = new HttpGet(fullUrl);
            HttpClientContext context = HttpClientContext.create();
            setCookie(context, cookie);
            setHeader(get, header);

            CloseableHttpClient client = httpClient;

            if(httpClientCustom != null){
                client = httpClientCustom;
            }

            try (CloseableHttpResponse response = client.execute(get)) {
                int code = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                // 构建响应日志信息
                String responseLog = String.format("HTTP响应 - URL: %s, Status: %d, Response: %s",
                        fullUrl, code, responseBody);

                if (code >= 400) {
                    String errorLog = String.format("HTTP请求失败 - URL: %s, Status: %d, Error: %s",
                            fullUrl, code, responseBody);
                    LogUtil.error(HttpUtil.class, errorLog);
                    throw new RuntimeException("HTTP请求失败，状态码: " + code + "，响应: " + responseBody);
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                String timedResponseLog = String.format("%s, Time: %dms", responseLog, duration);
                LogUtil.info(HttpUtil.class, timedResponseLog);
                return responseBody;
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogUtil.error(HttpUtil.class, "HTTP请求异常 - URL: " + fullUrl + ", Time: " + duration + "ms", e);
            throw e;
        }
    }


    public String postRequest(String path, String mediaType, HttpEntity entity) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", mediaType);
        return postRequest(path, entity, header, null);
    }

    public String postRequest(String path, HttpEntity entity, Map<String, String> header,
                                   Map<String, String> cookie) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            // 构建请求日志信息
            StringBuilder requestLog = new StringBuilder("HTTP请求 - ")
                .append("Method: POST, ")
                .append("URL: ").append(path);

            if (header != null && !header.isEmpty()) {
                requestLog.append(", Headers: ").append(header);
            }
            if (cookie != null && !cookie.isEmpty()) {
                requestLog.append(", Cookies: ").append(cookie);
            }

            // 记录请求体内容
            if (entity instanceof StringEntity) {
                try {
                    String requestBody = EntityUtils.toString(entity);
                    requestLog.append(", Body: ").append(requestBody);
                    // 重新创建entity，因为toString消耗了流
                    entity = new StringEntity(requestBody, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    LogUtil.warn(HttpUtil.class, "无法记录POST请求体内容", e);
                }
            }

            LogUtil.info(HttpUtil.class, requestLog.toString());

            HttpPost post = new HttpPost(path);

            HttpClientContext context = HttpClientContext.create();
            setCookie(context, cookie);
            setHeader(post, header);

            post.setEntity(entity);

            CloseableHttpClient client = httpClient;

            if(httpClientCustom != null){
                client = httpClientCustom;
            }

            try (CloseableHttpResponse response = client.execute(post, context)) {
                int code = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                // 构建响应日志信息
                String responseLog = String.format("HTTP响应 - URL: %s, Status: %d, Response: %s",
                    path, code, responseBody);

                if (code >= 400) {
                    String errorLog = String.format("HTTP请求失败 - URL: %s, Status: %d, Error: %s",
                        path, code, responseBody);
                    LogUtil.error(HttpUtil.class, errorLog);
                    throw new Exception(responseBody);
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                String timedResponseLog = String.format("%s, Time: %dms", responseLog, duration);
                LogUtil.info(HttpUtil.class, timedResponseLog);
                return responseBody;
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogUtil.error(HttpUtil.class, "HTTP请求异常 - URL: " + path + ", Time: " + duration + "ms", e);
            throw e;
        }
    }

    /**
     * PATCH请求
     */
    public String patchRequest(String path, List<NameValuePair> parametersBody) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            // 构建请求日志信息
            StringBuilder requestLog = new StringBuilder("HTTP请求 - ")
                .append("Method: PATCH, ")
                .append("URL: ").append(path);

            if (parametersBody != null && !parametersBody.isEmpty()) {
                requestLog.append(", Parameters: ").append(parametersBody);
            }

            LogUtil.info(HttpUtil.class, requestLog.toString());

            HttpPatch patch = new HttpPatch(path);
            HttpClientContext context = HttpClientContext.create();
            setCookie(context, null);
            setHeader(patch, null);

            patch.addHeader("Content-Type", "application/json");
            patch.addHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8);
            patch.setEntity(entity);

            CloseableHttpClient client = httpClient;

            if(httpClientCustom != null){
                client = httpClientCustom;
            }

            try (CloseableHttpResponse response = client.execute(patch, context)) {
                int code = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                // 构建响应日志信息
                String responseLog = String.format("HTTP响应 - URL: %s, Status: %d, Response: %s",
                    path, code, responseBody);

                if (code >= 400) {
                    String errorLog = String.format("HTTP请求失败 - URL: %s, Status: %d, Error: %s",
                        path, code, responseBody);
                    LogUtil.error(HttpUtil.class, errorLog);
                    throw new Exception(responseBody);
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                String timedResponseLog = String.format("%s, Time: %dms", responseLog, duration);
                LogUtil.info(HttpUtil.class, timedResponseLog);
                return responseBody;
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogUtil.error(HttpUtil.class, "HTTP请求异常 - URL: " + path + ", Time: " + duration + "ms", e);
            throw e;
        }
    }

    private void setHeader(HttpRequestBase request, Map<String, String> headers) {
        if(headersCustom != null && !headersCustom.isEmpty()){
            for (String key : headersCustom.keySet()) {
                request.addHeader(key, headersCustom.get(key));
            }
        }

        if(headers != null && !headers.isEmpty()){
            for (String key : headers.keySet()) {
                request.addHeader(key, headers.get(key));
            }
        }
    }

    private void setCookie(HttpClientContext context, Map<String, String> cookies) {
        CookieStore cookieStore = new BasicCookieStore();
        if(cookieCustom != null && !cookieCustom.isEmpty()){
            for (String key : cookieCustom.keySet()) {
                BasicClientCookie ck = new BasicClientCookie(key, cookieCustom.get(key));
                cookieStore.addCookie(ck);
            }
        }

        if(cookies != null && !cookies.isEmpty()){
            for (String key : cookies.keySet()) {
                BasicClientCookie ck = new BasicClientCookie(key, cookies.get(key));
                cookieStore.addCookie(ck);
            }
        }
        context.setCookieStore(cookieStore);
    }


    /**
     * 关闭连接池（应用关闭时调用）
     */
    public static void shutdown() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LogUtil.error(HttpUtil.class,"Failed to close HTTP client", e);
            }
        }
        if (connectionManager != null) {
            connectionManager.shutdown();
        }
    }

    /**
     * 将Map转换为List<NameValuePair>（重载方法，支持Object类型的值）
     * @param params 参数Map，key为参数名，value为参数值（会自动转换为String）
     * @return NameValuePair列表
     */
    public static List<NameValuePair> mapToNameValuePairs(Map<String, Object> params) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String value = entry.getValue() != null ? entry.getValue().toString() : null;
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }
        return nameValuePairs;
    }

    /**
     * 统一的HTTP请求入口，支持通过函数参数指定重试次数
     */
    public static class Builder {
        private int retryCount = config.getRetryCount();
        private long retryInterval = config.getRetryInterval();
        private int connectionTimeout = config.getConnectionTimeout();
        private int socketTimeout = config.getSocketTimeout();
        private int connectionRequestTimeout = config.getConnectionRequestTimeout();
        private HttpHost proxy = null;
        /**
         * header 和 cookie 不做缓存, 当前header 和 cookie 只做基础配置,减少工作量
         */
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> cookie = new HashMap<>();
        private static Cache<String, HttpUtil> cache = CacheBuilder.newBuilder()
                .maximumSize(10000) // 最大缓存容量（超过则按 LRU 回收）
                .expireAfterWrite(6, TimeUnit.HOURS) // 写入后 5 秒过期
                .expireAfterAccess(6, TimeUnit.HOURS) // 访问后 3 秒过期（优先级高于写入过期）
                .concurrencyLevel(8) // 并发级别（同时写缓存的线程数）
                .build();

        private Builder() {}

        /**
         * 创建Builder实例
         */
        public static Builder create() {
            return new Builder();
        }


        public Builder header(String name, String value) {
            return header(name, value, true);
        }

        public Builder header(String name, String value, boolean isOverride) {
            if (null != name && null != value) {
                if (!isOverride && !headers.containsKey(name.trim())) {

                } else {
                    headers.put(name.trim(), value);
                }
            }
            return this;
        }

        public Builder cookie(String name, String value) {
            this.cookie.put(name, value);
            return this;
        }

        public Builder cookie(Map<String, String> cookie) {
            if(cookie != null){
                this.cookie.putAll(cookie);
            }
            return this;
        }

        /**
         * 设置重试次数
         */
        public Builder retryCount(int retryCount) {
            this.retryCount = Math.max(0, retryCount);
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }
        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public Builder connectionRequestTimeout(int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        /**
         * 设置代理
         */
        public Builder proxy(HttpHost proxy) {
            this.proxy = proxy;
            return this;
        }

        public HttpUtil getHttpUtil(){

            HttpUtil httpUtil;
            HttpRequestRetryStrategy retryStrategy = new HttpRequestRetryStrategy(
                    retryCount, retryInterval);

            CloseableHttpClient httpclientCustom = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setRetryHandler(retryStrategy)
                    .setProxy(proxy)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(connectionTimeout)
                            .setSocketTimeout(socketTimeout)
                            .setConnectionRequestTimeout(connectionRequestTimeout) // 从连接池获取连接的超时时间
                            .build())
                    .build();
            httpUtil = new HttpUtil(httpclientCustom, headers, cookie);
            return httpUtil;
        }

        /**
         * 执行GET请求
         */
        public String getRequest(String path, List<NameValuePair> parameters) throws Exception {
            //根据retryCount和proxy 确定是否有缓存
            return getHttpUtil().getRequest(path, parameters, null, null);
        }

        public String getRequest(String path, List<NameValuePair> parameters,
                                 Map<String, String> headers, Map<String, String> cookies) throws Exception {

            return getHttpUtil().getRequest(path, parameters, headers, cookies);
        }

        /**
         * 执行POST表单请求
         */
        public String postForm(String path, List<NameValuePair> parameters) throws Exception {
            return getHttpUtil().postForm(path, parameters);
        }

        /**
         * 执行POST JSON请求
         */
        public String postJSON(String path, String json) throws Exception {
            return getHttpUtil().postJSON(path, json);
        }

        public String postJSON(String path, HttpEntity entity) throws Exception {
            return getHttpUtil().postJSON(path, entity);
        }

        public String postJSON(String path, String json,
                               Map<String, String> headers, Map<String, String> cookies) throws Exception {
            return getHttpUtil().postJSON(path, json, headers, cookies);
        }

        public String postJSON(String path, HttpEntity entity,
                               Map<String, String> headers, Map<String, String> cookies) throws Exception {
            return getHttpUtil().postRequest(path, entity, headers, cookies);
        }

        /**
         * 执行PATCH请求
         */
        public String patch(String path, List<NameValuePair> parameters) throws Exception {
            return getHttpUtil().patchRequest(path, parameters);
        }
    }

    /**
     * 获取Builder实例，用于链式调用
     */
    public static Builder builder() {
        return Builder.create();
    }
}