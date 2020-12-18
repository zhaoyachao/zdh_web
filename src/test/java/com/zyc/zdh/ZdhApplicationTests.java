package com.zyc.zdh;

import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.Role;
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

	@Test
	public void contextLoads() {

		CacheManager ec = CacheManager.getCacheManager("ec");
		ec.clearAll();

		Role r=roleService.getRole("1");
		System.out.println(r.getId()+"===="+r.getRoleName());
		Role r1=roleService.getRole("1");
		System.out.println(r1.getId()+"===="+r1.getRoleName());

		ec.clearAll();

	}

	@Test
	public void allTaskNum() {

		System.out.println(taskLogInstanceMapper.allTaskNum());

	}

	@Test
	public void checkDepJobTest(){

		CheckDepJob.run3();
	}




}
