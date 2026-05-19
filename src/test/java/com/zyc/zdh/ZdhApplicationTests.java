package com.zyc.zdh;

import com.google.common.collect.Lists;
import com.zyc.notscan.BaseMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.RoleInfo;
import com.zyc.zdh.job.CheckBloodSourceJob;
import com.zyc.zdh.job.CheckDepJob;
import com.zyc.zdh.service.RoleService;
import net.sf.ehcache.CacheManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZdhApplication.class})
@ActiveProfiles(profiles = {"dev"})
public class ZdhApplicationTests {

	@Autowired
	RedisTemplate redisTemplate;
	@Autowired
	RoleService roleService;
	@Autowired
	EhCacheCacheManager ehCacheCacheManager;
	@Autowired
	TaskLogInstanceMapper taskLogInstanceMapper;

	@Resource
	Map<String , BaseMapper> mapperMap;

	@Test
	public void contextLoads() {

		CacheManager ec = CacheManager.getCacheManager("ec");
		ec.clearAll();

//		Role r=roleService.getRole("1");
//		System.out.println(r.getId()+"===="+r.getRoleName());
//		Role r1=roleService.getRole("1");
//		System.out.println(r1.getId()+"===="+r1.getRoleName());

		ec.clearAll();

	}

	@Test
	public void allTaskNum() {

		System.out.println(taskLogInstanceMapper.allTaskNum());

	}

	@Test
	public void checkDepJobTest(){

		CheckDepJob.create_group_final_status();
	}

	@Test
	public void check_sql_blood_source(){
		CheckBloodSourceJob.Check("zdh");
	}


	@Test
	public void testMapper(){
		Set<Map.Entry<String, BaseMapper>> entries = mapperMap.entrySet();
		for (Map.Entry<String, BaseMapper> entry : entries){
			System.out.println("beanMame: " +entry.getKey());
			System.out.println("bean: " +entry.getValue());
			System.out.println("getTable: "+entry.getValue().getTable());
			if(Lists.newArrayList("task_info").contains(entry.getValue().getTable())){
				continue;
			}
			Map<String, Object> param = new HashMap<>();
			param.put("id", "1");
			entry.getValue().selectTest(entry.getValue().getTable(), param);
			System.out.println("******");
		}
	}

}
