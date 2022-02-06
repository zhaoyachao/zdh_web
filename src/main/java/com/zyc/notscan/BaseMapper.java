package com.zyc.notscan;


import tk.mybatis.mapper.common.Mapper;

/**
 * ClassName: BaseMapper   
 * @author zyc-admin
 * @date 2017年12月25日  
 * @Description: mybatis 统一mapper 接口
 * <br>注意此接口存在泛型，不能被spring和mybatis 扫描到
 */
public interface BaseMapper<T> extends Mapper<T> {
}