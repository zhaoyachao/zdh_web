package com.zyc.zdh.service;

import java.util.List;
import java.util.Map;

public interface DataAndRuleService {

	public <T> List<T> findDataByTableName(Map<String, Object> map, T t)throws IllegalArgumentException, IllegalAccessException ;
}
