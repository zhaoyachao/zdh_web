package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseZdhDownloadMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "zdh_download_info";
    }
}