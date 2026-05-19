package com.zyc.zdh.util;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * HTTP请求重试策略
 */
public class HttpRequestRetryStrategy implements HttpRequestRetryHandler {

    private static final Set<Class<? extends IOException>> RETRIABLE_IO_EXCEPTIONS = new HashSet<>(
            Arrays.asList(
                    UnknownHostException.class,        // 域名解析失败
                    InterruptedIOException.class,      // 连接超时、读取超时等
                    ConnectException.class             // Connection refused等连接异常
            ));

    private static final Set<Integer> RETRIABLE_STATUS_CODES = new HashSet<>(
            Arrays.asList(
                    429, // Too Many Requests
                    500, // Internal Server Error
                    502, // Bad Gateway
                    503, // Service Unavailable
                    504  // Gateway Timeout
            ));

    private final int maxRetries;
    private final long retryIntervalMs;

    public HttpRequestRetryStrategy(int maxRetries) {
        this(maxRetries, 10); // 默认1秒重试间隔
    }

    public HttpRequestRetryStrategy(int maxRetries, long retryIntervalMs) {
        this.maxRetries = Math.max(0, maxRetries);
        this.retryIntervalMs = Math.max(0, retryIntervalMs);
    }

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount > maxRetries) {
            LogUtil.warn(HttpRequestRetryStrategy.class,
                    "Max retries ({}) exceeded for request", maxRetries);
            return false;
        }

        // 检查是否是连接超时或读取超时等可重试的异常
        if (isRetriableException(exception)) {
            LogUtil.warn(HttpRequestRetryStrategy.class,
                    "Retry {} for {}: {}", executionCount, exception.getClass().getSimpleName(), exception.getMessage());
            waitBeforeRetry(executionCount);
            return true;
        }

        // 检查是否是服务器错误状态码
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpResponse response = clientContext.getResponse();
        if (response != null && RETRIABLE_STATUS_CODES.contains(response.getStatusLine().getStatusCode())) {
            LogUtil.warn(HttpRequestRetryStrategy.class,
                    "Retry {} for status code: {}", executionCount, response.getStatusLine().getStatusCode());
            waitBeforeRetry(executionCount);
            return true;
        }

        LogUtil.error(HttpRequestRetryStrategy.class,
                "Non-retriable exception: {}", exception.getMessage());
        return false;
    }

    /**
     * 判断异常是否可重试
     */
    private boolean isRetriableException(IOException exception) {
        for (Class<? extends IOException> retriableClass : RETRIABLE_IO_EXCEPTIONS) {
            if (retriableClass.isInstance(exception)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重试前等待（指数退避策略）
     */
    private void waitBeforeRetry(int executionCount) {
        try {
            long waitTime = retryIntervalMs * (long) Math.pow(2, executionCount - 1);
            waitTime = Math.min(waitTime, TimeUnit.SECONDS.toMillis(30)); // 最大等待30秒

            LogUtil.info(HttpRequestRetryStrategy.class,
                    "Waiting {}ms before retry attempt {}", waitTime, executionCount);

            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogUtil.warn(HttpRequestRetryStrategy.class, "Retry wait interrupted");
        }
    }

    /**
     * 判断请求是否是幂等的（非幂等请求不重试）
     */
    private boolean isIdempotent(HttpRequest request) {
        if (request instanceof HttpUriRequest) {
            String method = ((HttpUriRequest) request).getMethod();
            // GET、HEAD、OPTIONS、TRACE 是幂等的
            return "GET".equalsIgnoreCase(method) ||
                   "HEAD".equalsIgnoreCase(method) ||
                   "OPTIONS".equalsIgnoreCase(method) ||
                   "TRACE".equalsIgnoreCase(method);
        }
        return false;
    }

    /**
     * 获取最大重试次数
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * 获取重试间隔时间
     */
    public long getRetryIntervalMs() {
        return retryIntervalMs;
    }
}