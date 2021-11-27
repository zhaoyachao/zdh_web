package com.zyc.zdh.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zyc.zdh.ProfilesResolver;
import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.entity.RoleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import com.zyc.zdh.service.RoleService;
import com.zyc.zdh.shiro.RedisOtherDb;
import com.zyc.zdh.shiro.RedisUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZdhApplication.class })
@ActiveProfiles(resolver=ProfilesResolver.class)
public class RedisTest {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RedisOtherDb redisOtherDb;

	//@TestA
	public void redisDb() {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 100; i++) {
			set.add(i + "");
		}
		redisOtherDb.set("set1", set,1);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			list.add(i + "");
		}
		redisOtherDb.set("list1", list,1);

		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < 100; i++) {
			map.put(i + "", "result" + i);
		}
		redisOtherDb.set("map1", map,1);
		Set<String> s = (Set<String>) redisOtherDb.get("set1",1);
		for(String s0:s){
			System.out.println(s0);
		}
	    System.out.println("set=======end===== ");
	    for(String s1:((List<String>) redisOtherDb.get("list1",1))){
	    	System.out.println(s1);
	    }
	    System.out.println("list=========end======");
	    for(Map.Entry<String, Object> m:((Map<String,Object>) redisOtherDb.get("map1",1)).entrySet()){
	    	System.out.println(m.getKey()+"==="+m.getValue());
	    }
	    System.out.println("map==========end====");
		/*System.out.println(((List<String>) redisOtherDb.get("list1",1)).size());
		System.out.println(((Map) redisOtherDb.get("map1",1)).size());*/
	}

	// @TestA
	public void redisSet() {
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < 100; i++) {
			set.add(i + "");
		}
		redisUtil.set("set1", set);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			list.add(i + "");
		}
		redisUtil.set("list1", list);

		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < 100; i++) {
			map.put(i + "", "result" + i);
		}
		redisUtil.set("map1", map);
		Set<String> s = (Set<String>) redisUtil.get("set1");
		System.out.println(s.size());
		System.out.println(((List<String>) redisUtil.get("list1")).size());
		System.out.println(((Map) redisUtil.get("map1")).size());
	}

	@Test
	public void redisCacheTest() {
		roleService.getRole("1");
		RoleInfo role = (RoleInfo) redisUtil.get("role:id:1");

		while (true) {

		}
	}

	// @TestA
	public void redisTest() {
		redisUtil.set("abc", "zyc");
		redisUtil.set("twodb", "twodb", 1);
		System.out.println(redisUtil.get("abc"));
		System.out.println(redisUtil.get("www.boco.com", 1));
		System.out.println(redisUtil.get("abc"));
		System.out.println(Arrays.toString(redisUtil.keys().toArray()));
		System.out.println(redisUtil.type("myRedis~keys"));
		for (String s : redisUtil.getZset("myRedis~keys")) {
			System.out.println(s);
		}
	}

}
