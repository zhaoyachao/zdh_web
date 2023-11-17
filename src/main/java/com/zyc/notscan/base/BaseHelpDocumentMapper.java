package com.zyc.notscan.base;

import com.zyc.notscan.BaseMapper;

public interface BaseHelpDocumentMapper<T> extends BaseMapper<T> {
    @Override
    default String getTable(){
        return "help_document_info";
    }
}