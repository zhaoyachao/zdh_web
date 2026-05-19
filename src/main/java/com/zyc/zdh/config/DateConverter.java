package com.zyc.zdh.config;

import com.zyc.zdh.util.LogUtil;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

public class DateConverter implements Converter<String, Timestamp> {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";


    @Override
    public Timestamp convert(String value) {
        LogUtil.info(this.getClass(), "转换日期：" + value);

        if (value == null || value.trim().equals("") || value.equalsIgnoreCase("null")) {
            return null;
        }

        value = value.trim();

        try {
            return Timestamp.valueOf(value);

        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to Date fail", value));
        }
    }
}