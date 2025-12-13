package com.zyc.zdh.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public static String getRequest(String path, List<NameValuePair> parametersBody) throws Exception {
        return getRequest(path, parametersBody, null, null, null, 0);
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
    public static String getRequest(String path, List<NameValuePair> parametersBody, Map<String, String> header,
                                  Map<String, String> cookie) throws Exception {
        return getRequest(path, parametersBody, header, cookie, null, 0);
    }

    /**
     * 默认不重试
     * @param path
     * @param parametersBody
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String getRequest(String path, List<NameValuePair> parametersBody, HttpHost proxy) throws Exception {
        return getRequest(path, parametersBody, null, null, proxy, 0);
    }

    /**
     * 默认不重试
     * @param path
     * @param parametersBody
     * @param header
     * @param cookie
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String getRequest(String path, List<NameValuePair> parametersBody, Map<String, String> header,
                                  Map<String, String> cookie, HttpHost proxy) throws Exception {
        return getRequest(path, parametersBody, header, cookie, proxy, 0);
    }

    /**
     * 默认不重试
     * @param path
     * @param parametersBody
     * @param header
     * @param cookie
     * @param proxy
     * @param retryCount
     * @return
     * @throws Exception
     */
    public static String getRequest(String path, List<NameValuePair> parametersBody, Map<String, String> header,
                                  Map<String, String> cookie, HttpHost proxy, int retryCount) throws Exception {
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
            if (header != null) {
                for (String key : header.keySet()) {
                    get.addHeader(key, header.get(key));
                }
            }

            CookieStore cookieStore = new BasicCookieStore();
            if (cookie != null) {
                for (String key : cookie.keySet()) {
                    BasicClientCookie ck = new BasicClientCookie(key, cookie.get(key));
                    cookieStore.addCookie(ck);
                }
            }

            HttpClientBuilder builder = HttpClientBuilder.create()
                    .setDefaultCookieStore(cookieStore)
                    .setRetryHandler(new HttpRequestRetryStrategy(retryCount));

            if (proxy != null) {
                builder.setProxy(proxy);
            }

            try (CloseableHttpClient client = builder.build()) {
                HttpResponse response = client.execute(get);
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

    /**
     * 发送POST请求（表单形式）
     * 默认不重试
     */
    public static String postForm(String path, List<NameValuePair> parametersBody) throws Exception {
        return postForm(path, parametersBody, 0);
    }

    public static String postForm(String path, List<NameValuePair> parametersBody, int retryCount) throws Exception {
        HttpEntity entity = new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8);
        return postRequest(path, "application/x-www-form-urlencoded", entity, null, retryCount);
    }
    /**
     * 发送POST请求（JSON形式）
     * 默认不重试
     */
    public static String postJSON(String path, String json) throws Exception {
        return postJSON(path, json, 0);
    }

    public static String postJSON(String path, String json, int retryCount) throws Exception {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, "application/json", entity, null, retryCount);
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
    public static String postJSON(String path, String json, Map<String, String> header, Map<String, String> cookie) throws Exception {
        return postJSON(path, json, header, cookie, null, 0);
    }

    /**
     * 默认不重试
     * @param path
     * @param json
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String postJSON(String path, String json, HttpHost proxy) throws Exception {
        return postJSON(path, json, proxy, 0);
    }

    /**
     * 默认不重试
     * @param path
     * @param json
     * @param header
     * @param cookie
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String postJSON(String path, String json, Map<String, String> header,
                                 Map<String, String> cookie, HttpHost proxy) throws Exception {
        return postJSON(path, json, header, cookie, proxy, config.getRetryCount());
    }

    public static String postJSON(String path, String json, HttpHost proxy, int retryCount) throws Exception {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, "application/json", entity, proxy, retryCount);
    }

    public static String postJSON(String path, String json, Map<String, String> header,
                                 Map<String, String> cookie, HttpHost proxy, int retryCount) throws Exception {
        StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
        return postRequest(path, entity, header, cookie, proxy, retryCount);
    }

    /**
     * 发送POST请求（通用方法）
     * 默认不重试
     */
    public static String postRequest(String path, String mediaType, HttpEntity entity, HttpHost proxy) throws Exception {
        return postRequest(path, mediaType, entity, proxy, 0);
    }

    public static String postRequest(String path, String mediaType, HttpEntity entity, HttpHost proxy, int retryCount) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            // 构建请求日志信息
            StringBuilder requestLog = new StringBuilder("HTTP请求 - ")
                .append("Method: POST, ")
                .append("URL: ").append(path)
                .append(", Content-Type: ").append(mediaType);

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
            post.addHeader("Content-Type", mediaType);
            post.addHeader("Accept", "application/json");
            post.setEntity(entity);

            HttpClientBuilder builder = HttpClientBuilder.create()
                    .setRetryHandler(new HttpRequestRetryStrategy(retryCount));

            if (proxy != null) {
                builder.setProxy(proxy);
            }

            try (CloseableHttpClient client = builder.build()) {
                HttpResponse response = client.execute(post);
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

    public static String postRequest(String path, HttpEntity entity, Map<String, String> header,
                                   Map<String, String> cookie, HttpHost proxy) throws Exception {
        return postRequest(path, entity, header, cookie, proxy, config.getRetryCount());
    }
    public static String postRequest(String path, HttpEntity entity, Map<String, String> header,
                                   Map<String, String> cookie, HttpHost proxy, int retryCount) throws Exception {
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

            // 设置请求头
            if (header != null) {
                for (String key : header.keySet()) {
                    post.addHeader(key, header.get(key));
                }
            }

            // 设置Cookie
            CookieStore cookieStore = new BasicCookieStore();
            if (cookie != null) {
                for (String key : cookie.keySet()) {
                    BasicClientCookie ck = new BasicClientCookie(key, cookie.get(key));
                    cookieStore.addCookie(ck);
                }
            }

            // 配置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getConnectionTimeout())
                    .setSocketTimeout(config.getSocketTimeout())
                    .setConnectionRequestTimeout(1000)
                    .build();

            post.setEntity(entity);

            HttpClientBuilder builder = HttpClientBuilder.create()
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultCookieStore(cookieStore)
                    .setRetryHandler(new HttpRequestRetryStrategy(retryCount));

            if (proxy != null) {
                builder.setProxy(proxy);
            }

            try (CloseableHttpClient client = builder.build()) {
                HttpResponse response = client.execute(post);
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
    public static String patchRequest(String path, List<NameValuePair> parametersBody) throws Exception {
        return patchRequest(path, parametersBody, config.getRetryCount());
    }
    public static String patchRequest(String path, List<NameValuePair> parametersBody, int retryCount) throws Exception {
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
            patch.addHeader("Content-Type", "application/json");
            patch.addHeader("Accept", "application/json");
            HttpEntity entity = new UrlEncodedFormEntity(parametersBody, StandardCharsets.UTF_8);
            patch.setEntity(entity);

            HttpClientBuilder builder = HttpClientBuilder.create()
                    .setRetryHandler(new HttpRequestRetryStrategy(retryCount));

            try (CloseableHttpClient client = builder.build()) {
                HttpResponse response = client.execute(patch);
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
     * 统一的HTTP请求入口，支持通过函数参数指定重试次数
     */
    public static class Builder {
        private int retryCount = config.getRetryCount();
        private HttpHost proxy = null;

        private Builder() {}

        /**
         * 创建Builder实例
         */
        public static Builder create() {
            return new Builder();
        }

        /**
         * 设置重试次数
         */
        public Builder retryCount(int retryCount) {
            this.retryCount = Math.max(0, retryCount);
            return this;
        }

        /**
         * 设置代理
         */
        public Builder proxy(HttpHost proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * 执行GET请求
         */
        public String get(String path, List<NameValuePair> parameters) throws Exception {
            return get(path, parameters, null, null);
        }

        public String get(String path, List<NameValuePair> parameters,
                        Map<String, String> headers, Map<String, String> cookies) throws Exception {
            return HttpUtil.getRequest(path, parameters, headers, cookies, proxy, retryCount);
        }

        /**
         * 执行POST表单请求
         */
        public String postForm(String path, List<NameValuePair> parameters) throws Exception {
            return HttpUtil.postForm(path, parameters, retryCount);
        }

        /**
         * 执行POST JSON请求
         */
        public String postJSON(String path, String json) throws Exception {
            return HttpUtil.postJSON(path, json, retryCount);
        }

        public String postJSON(String path, String json,
                              Map<String, String> headers, Map<String, String> cookies) throws Exception {
            return HttpUtil.postJSON(path, json, headers, cookies, proxy, retryCount);
        }

        /**
         * 执行PATCH请求
         */
        public String patch(String path, List<NameValuePair> parameters) throws Exception {
            return HttpUtil.patchRequest(path, parameters, retryCount);
        }
    }

    /**
     * 获取Builder实例，用于链式调用
     */
    public static Builder builder() {
        return Builder.create();
    }

    /**
     * 关闭连接池（应用关闭时调用）
     */
    public static void shutdown() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LogUtil.error(HttpUtil.class, "Failed to close HTTP client", e);
            }
        }
        if (connectionManager != null) {
            connectionManager.shutdown();
        }
    }
}