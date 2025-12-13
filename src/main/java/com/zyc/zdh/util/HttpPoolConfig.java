package com.zyc.zdh.util;

/**
 * HTTP连接池配置类
 */
public class HttpPoolConfig {

    private int maxTotal = 200;              // 最大连接数
    private int maxPerRoute = 50;             // 每个路由的最大连接数
    private int connectionTimeout = 5000;     // 连接超时时间(ms)
    private int socketTimeout = 5000;         // 读取超时时间(ms)
    private int retryCount = 3;               // 重试次数
    private long evictIdleTime = 30000;       // 空闲连接清理时间(ms)
    private long retryInterval = 1;        // 重试间隔时间(ms)

    // 单例实例
    private static final HttpPoolConfig INSTANCE = new HttpPoolConfig();

    private HttpPoolConfig() {
        // 私有构造函数
    }

    public static HttpPoolConfig getInstance() {
        return INSTANCE;
    }

    // Getter和Setter方法
    public int getMaxTotal() {
        return maxTotal;
    }

    public HttpPoolConfig setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public HttpPoolConfig setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public HttpPoolConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public HttpPoolConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public HttpPoolConfig setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public long getEvictIdleTime() {
        return evictIdleTime;
    }

    public HttpPoolConfig setEvictIdleTime(long evictIdleTime) {
        this.evictIdleTime = evictIdleTime;
        return this;
    }

    public long getRetryInterval() {
        return retryInterval;
    }

    public HttpPoolConfig setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }

    /**
     * 从配置文件加载配置
     */
    public void loadFromProperties() {
        try {
            // 可以从配置文件中读取配置，这里使用默认值
            String maxTotalStr = ConfigUtil.getValue("http.pool.maxTotal", String.valueOf(maxTotal));
            String maxPerRouteStr = ConfigUtil.getValue("http.pool.maxPerRoute", String.valueOf(maxPerRoute));
            String connTimeoutStr = ConfigUtil.getValue("http.pool.connectionTimeout", String.valueOf(connectionTimeout));
            String socketTimeoutStr = ConfigUtil.getValue("http.pool.socketTimeout", String.valueOf(socketTimeout));
            String retryCountStr = ConfigUtil.getValue("http.pool.retryCount", String.valueOf(retryCount));
            String evictIdleTimeStr = ConfigUtil.getValue("http.pool.evictIdleTime", String.valueOf(evictIdleTime));
            String retryIntervalStr = ConfigUtil.getValue("http.pool.retryInterval", String.valueOf(retryInterval));

            this.maxTotal = Integer.parseInt(maxTotalStr);
            this.maxPerRoute = Integer.parseInt(maxPerRouteStr);
            this.connectionTimeout = Integer.parseInt(connTimeoutStr);
            this.socketTimeout = Integer.parseInt(socketTimeoutStr);
            this.retryCount = Integer.parseInt(retryCountStr);
            this.evictIdleTime = Long.parseLong(evictIdleTimeStr);
            this.retryInterval = Long.parseLong(retryIntervalStr);

            LogUtil.info(HttpPoolConfig.class, "HTTP连接池配置已加载: {}", this);
        } catch (Exception e) {
            LogUtil.warn(HttpPoolConfig.class, "加载HTTP连接池配置失败，使用默认配置", e);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "HttpPoolConfig{maxTotal=%d, maxPerRoute=%d, connectionTimeout=%d, " +
                "socketTimeout=%d, retryCount=%d, evictIdleTime=%d, retryInterval=%d}",
                maxTotal, maxPerRoute, connectionTimeout, socketTimeout,
                retryCount, evictIdleTime, retryInterval
        );
    }

    /**
     * 重新加载配置
     */
    public static void reload() {
        INSTANCE.loadFromProperties();
    }

    /**
     * 初始化配置
     */
    public static void initialize() {
        INSTANCE.loadFromProperties();
    }
}