package com.zyc.zdh.datax_generator;

import java.util.Map;

public interface DataxReader {

    public String getName();

    public void reader(Map<String,Object> config) throws Exception;
}
