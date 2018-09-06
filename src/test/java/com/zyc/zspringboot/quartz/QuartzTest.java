package com.zyc.zspringboot.quartz;

import javax.annotation.Resource;

import com.zyc.zspringboot.ZspringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.zyc.zspringboot.entity.TaskInfo;
import com.zyc.zspringboot.quartz.QuartzManager;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZspringbootApplication.class })
//@ActiveProfiles(resolver = ProfilesResolver.class)
@ActiveProfiles(value={"pro"})
public class QuartzTest {

	@Resource(name = "quartzManager")
	private QuartzManager quartzManager;

	@Test
	public void testQuartzCreate() {
		TaskInfo createTaskInfo = quartzManager.createTaskInfo("任务7", "tb7",
				"100s", 0, "测试任务");
		quartzManager.addTaskInfo(createTaskInfo);
		testQuartzRun(createTaskInfo);
		while (true) {

		}
	}

	public void testQuartzRun(TaskInfo taskInfo) {
		quartzManager.addTaskToQuartz(taskInfo);
	}

}
