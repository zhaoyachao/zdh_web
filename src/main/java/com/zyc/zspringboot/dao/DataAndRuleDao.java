package com.zyc.zspringboot.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

//@Mapper
public interface DataAndRuleDao {

	/*@SelectProvider(type=DataAndRuleProvider.class,method="findDataByTableName")
	@Results({ @Result(property = "id", column = "id"),
		       @Result(property = "roleName",column = "role_name") })*/
	public <T> List<T> findDataByTableName(@Param("tableName") String tableName, @Param("sqlResult") String sqlResult, T t);
}
