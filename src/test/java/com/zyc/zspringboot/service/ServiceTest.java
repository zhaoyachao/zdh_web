package com.zyc.zspringboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyc.zspringboot.dao.TaskInfoMapper;
import com.zyc.zspringboot.entity.TaskInfo;
import com.zyc.zspringboot.entity.User;
import com.zyc.zspringboot.util.SpringContext;
import com.zyc.zspringboot.ZspringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * ClassName: ServiceTest   
 * @author zyc-admin
 * @date 2018年2月6日  
 * @Description: TODO  
 */
@SpringBootTest(classes=ZspringbootApplication.class)
@RunWith(SpringRunner.class)
public class ServiceTest {

	@Autowired
	private MsgService msgService;
	
	@Autowired
	private TaskInfoMapper taskInfoMapper;
	
	@Autowired
	private AccountService accountService;
	
	//@Test
	public void testTransaction(){
		PlatformTransactionManager a=(PlatformTransactionManager) SpringContext.getBean("txManager");
		PlatformTransactionManager b=(PlatformTransactionManager) SpringContext.getBean("txManager2");
		System.out.println(a.getClass());
		System.out.println(b.getClass());
		msgService.delete(396380350361632768L);
	}
	
	@Test
	public void testPage(){
		User user=new User();
		user.setPageNum(2);
		user.setPageSize(2);
		user.setPassword("e10adc3949ba59abbe56e057f20f883e");
		List<User> findList = accountService.findList(user);
		System.out.println(findList.size());
		TaskInfo taskInfo=new TaskInfo();
		//taskInfo.setTaskPlanCount(10);
		taskInfo.setPageNum(1);
		Page startPage = PageHelper.startPage(2, 2);
		List<TaskInfo> select = taskInfoMapper.select(taskInfo);
		System.out.println("startPage.getTotal()==="+startPage.getTotal());
		System.out.println(select.size());
	}
}
