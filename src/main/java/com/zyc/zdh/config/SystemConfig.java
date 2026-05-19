package com.zyc.zdh.config;

import com.zyc.zdh.entity.SystemFilterParam;

/**
 * 系统配置
 */
public class SystemConfig {
    public static ThreadLocal<SystemFilterParam> urlThread = new ThreadLocal<>();
}
