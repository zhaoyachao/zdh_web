package com.zyc.zdh.quartz;

import javax.annotation.Resource;

import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.job.JobModel;
import com.zyc.zdh.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZdhApplication.class })
//@ActiveProfiles(resolver = ProfilesResolver.class)
@ActiveProfiles(value={"dev"})
public class QuartzTest {


	@Resource(name = "quartzManager2")
	private QuartzManager2 quartzManager2;

	@Autowired
	private QuartzJobMapper quartzJobMapper;


//	@TestA
//	public void testQuartzCreate() {
//		TaskInfo createTaskInfo = quartzManager.createTaskInfo("任务7", "tb7",
//				"100s", 0, "测试任务");
//		quartzManager.addTaskInfo(createTaskInfo);
//		testQuartzRun(createTaskInfo);
//		while (true) {
//
//		}
//	}

	@Test
	public void testQuartz2Create() {

		QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("SHELL",JobModel.TIME_SEQ.getValue(),
				new Date(),new Date(),"第一个调度任务","10s","5","command","1");
				quartzManager2.addQuartzJobInfo(quartzJobInfo);

		quartzManager2.addTaskToQuartz(quartzJobInfo);
		while (true) {

		}
	}

	@Test
	public void testQuartz2Del() {
		QuartzJobInfo quartzJobInfo=new QuartzJobInfo();
		quartzJobInfo.setJob_id("674733379031138304");
		quartzJobInfo=quartzJobMapper.selectByPrimaryKey(quartzJobInfo);

		quartzManager2.deleteTask(quartzJobInfo,"remove");
		while (true) {

		}
	}

	@Test
	public void testQuartz2Pause() throws Exception{
		QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("SHELL",JobModel.TIME_SEQ.getValue(),
				DateUtil.pase("2019-01-01"),new Date(),"第一个调度任务","10s","5","command","1");
		quartzManager2.addQuartzJobInfo(quartzJobInfo);

		quartzJobInfo=quartzJobMapper.selectByPrimaryKey(quartzJobInfo);

		quartzManager2.addTaskToQuartz(quartzJobInfo);
		//quartzManager2.deleteTask(quartzJobInfo);

		try{
			Thread.sleep(1000*20);
			System.out.println("暂停任务");
			quartzManager2.pauseTask(quartzJobInfo);
			Thread.sleep(1000*20);
			System.out.println("恢复任务");
			quartzManager2.resumeTask(quartzJobInfo);
		}catch (Exception e){

		}

		while (true) {

		}
	}
}
