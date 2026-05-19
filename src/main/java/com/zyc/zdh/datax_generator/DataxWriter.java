package com.zyc.zdh.datax_generator;

import java.util.Map;

public interface DataxWriter {

    public String getName();

    public void writer(Map<String, Object> config) throws Exception;
}
