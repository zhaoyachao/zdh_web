package com.zyc.zspringboot.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.zyc.zspringboot.dao.TaskInfoMapper;
import com.zyc.zspringboot.entity.TaskInfo;
import com.zyc.zspringboot.job.MyJobBean;
import com.zyc.zspringboot.job.SnowflakeIdWorker;

@Service("quartzManager")
public class QuartzManager {

	private static Logger logger = LoggerFactory.getLogger(QuartzManager.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private TaskInfoMapper taskInfoDao;

	/**
	 * 根据任务名，对应的表名，表达式创建任务
	 * 
	 * @param taskName
	 * @param tableName
	 * @param cron
	 * @return
	 */
	public TaskInfo createTaskInfo(String taskName, String tableName,
			String cron, int plancount,String desc) {
		TaskInfo taskInfo = new TaskInfo();
		String taskId = String
				.valueOf(SnowflakeIdWorker.getInstance().nextId());
		taskInfo.setTaskId(taskId);
		String beanName = taskName;
		String mapperName = beanName.substring(0, 1).toLowerCase()
				+ beanName.substring(1, beanName.length()) + "Mapper";
		if (taskName == null || taskName.trim().equals("")) {
			taskInfo.setTaskName(beanName + "_" + taskId);
		}else{
			taskInfo.setTaskName(taskName+ "_" + taskId);
		}
		taskInfo.setTaskGroup(mapperName);
		taskInfo.setTaskTrigger("selectByExample");
		taskInfo.setTaskExpression(cron);
		taskInfo.setTaskTablename(tableName);
		taskInfo.setTaskBeanmapper(mapperName);
		taskInfo.setTaskDesc(desc);
		taskInfo.setTaskParam("update_time");
		taskInfo.setTaskPlanCount(plancount);
		taskInfo.setTaskStatus("create");
		return taskInfo;
	}

	/**
	 * 通过id查询dmTaskInfoTb
	 * @param taskId
	 * @return TaskInfoTb
	 */
	public TaskInfo selectTaskInfoTb(String taskId){
		return taskInfoDao.selectByPrimaryKey(taskId);
	}
	/**
	 * 将定时任务插入到任务信息表中
	 * @param taskInfo
	 */
	//@Transactional(value="platformTransactionManager")
	public void addTaskInfo(TaskInfo taskInfo){
		taskInfoDao.insert(taskInfo);
	}
	
	/**
	 * 执行定时任务
	 * 
	 * @param taskInfo
	 */
	public void addTaskToQuartz(TaskInfo taskInfo) {
		try {
			if(schedulerFactoryBean.getScheduler().getTrigger(new TriggerKey(taskInfo.getTaskName(), taskInfo.getTaskGroup()))!=null){
				logger.info("已经存在同名的triggerkey,请重新创建");
				throw new Exception("已经存在同名的triggerkey,请重新创建");
			}
			JobDetail jobDetail = JobBuilder
					.newJob(MyJobBean.class)
					.withDescription(taskInfo.getTaskDesc())
					.withIdentity(taskInfo.getTaskName(),
							taskInfo.getTaskGroup()).build();
			Trigger trigger = null;
			// CronScheduleBuilder.cronSchedule(taskInfo.getTaskExpression())
			String expression = taskInfo.getTaskExpression();
			if (expression.contains("s") || expression.contains("m")
					|| expression.contains("h")) {
				SimpleScheduleBuilder simpleScheduleBuilder = getSimpleScheduleBuilder(
						expression, taskInfo.getTaskPlanCount());
				trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(taskInfo.getTaskName(),
								taskInfo.getTaskGroup()).startNow()
						.withSchedule(simpleScheduleBuilder).build();
			} else {
				CronScheduleBuilder cronScheduleBuilder = getCronScheduleBuilder(expression);
				trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(taskInfo.getTaskName(),
								taskInfo.getTaskGroup()).startNow()
						.withSchedule(cronScheduleBuilder).build();
			}
			logger.debug("任务的trigger创建完成triggerkey is {}", trigger.getKey()
					.toString());
			JobDataMap jobDataMap = trigger.getJobDataMap();
			jobDataMap.put(MyJobBean.TASK_ID, taskInfo.getTaskId());
			taskInfo.setTaskStatus("start");
			taskInfoDao.updateByPrimaryKey(taskInfo);
			schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
			if (!schedulerFactoryBean.getScheduler().isStarted()) {
				schedulerFactoryBean.getScheduler().start();
			}
		} catch (SecurityException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 重启任务<br>
	 * 重启后的任务和重启前的任务的trigger保持一致
	 * @param taskInfo
	 * @return
	 */
	public TaskInfo reStartTask(TaskInfo taskInfo) {
		try {
			Trigger trigger = null;
			String expression = taskInfo.getTaskExpression();
			if (expression.contains("s") || expression.contains("m")
					|| expression.contains("h")) {
				trigger = (SimpleTrigger) schedulerFactoryBean.getScheduler().getTrigger(new TriggerKey(
						taskInfo.getTaskName(), taskInfo.getTaskGroup()));
				trigger = ((SimpleTrigger) trigger)
						.getTriggerBuilder()
						.withIdentity(
								new TriggerKey(taskInfo.getTaskName(),
										taskInfo.getTaskGroup()))
						.withSchedule(
								getSimpleScheduleBuilder(expression,
										taskInfo.getTaskPlanCount())).build();

				/*
				 * trigger=TriggerBuilder.newTrigger().startNow() .withIdentity(
				 * new TriggerKey(taskInfo.getTaskName(),
				 * taskInfo.getTaskGroup()))
				 * .withSchedule(getSimpleScheduleBuilder(expression)) .build();
				 */
				// trigger.getJobDataMap().put(MyJobBean.TASK_ID,
				// taskInfo.getTaskId());
				// 按新的trigger重新设置job执行
				schedulerFactoryBean.getScheduler().rescheduleJob(trigger.getKey(), trigger);
			} else {
				trigger = (CronTrigger) schedulerFactoryBean.getScheduler().getTrigger(new TriggerKey(
						taskInfo.getTaskName(), taskInfo.getTaskGroup()));
				trigger = ((CronTrigger) trigger)
						.getTriggerBuilder()
						.withIdentity(
								new TriggerKey(taskInfo.getTaskName(),
										taskInfo.getTaskGroup())).startNow()
						.withSchedule(getCronScheduleBuilder(expression))
						.build();
				trigger.getJobDataMap().put(MyJobBean.TASK_ID,
						taskInfo.getTaskId());
				// 按新的trigger重新设置job执行
				schedulerFactoryBean.getScheduler().rescheduleJob(trigger.getKey(), trigger);
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 下方可以做一些更新数据库中任务的操作
		taskInfo.setTaskStatus("runing");
		taskInfoDao.updateByPrimaryKey(taskInfo);
		return taskInfo;
	}
	/**
	 * 重启任务<br>
	 * 重启后的任务和重启前的任务的trigger不是同一种,比如之前是simpletrigger类型现在是crontrigger类型
	 * @param taskInfo
	 * @return
	 */
	public void reStartTask2(TaskInfo taskInfo){
		deleteTask(taskInfo);
		addTaskToQuartz(taskInfo);
	}
	
	/**
	 * 删除任务
	 * 
	 * @param taskInfo
	 * @return
	 */
	public TaskInfo deleteTask(TaskInfo taskInfo) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = new JobKey(taskInfo.getTaskName(),
					taskInfo.getTaskGroup());
			scheduler.pauseJob(jobKey);
			scheduler.deleteJob(jobKey);
			// 在自己定义的任务表中删除任务,状态删除
			taskInfo.setTaskStatus("remove");
			taskInfoDao.updateByPrimaryKey(taskInfo);
		} catch (SchedulerException e) {

			e.printStackTrace();
		}
		return taskInfo;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param task
	 */
	public void pauseTask(TaskInfo taskInfo) {
		try {
			// Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = new JobKey(taskInfo.getTaskName(),
					taskInfo.getTaskGroup());
			schedulerFactoryBean.getScheduler().pauseJob(jobKey);
			// 更新定时任务状态
			taskInfo.setTaskStatus("pause");
			taskInfoDao.updateByPrimaryKey(taskInfo);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param task
	 */
	public void resumeTask(TaskInfo taskInfo) {
		try {
			JobKey jobKey = new JobKey(taskInfo.getTaskName(),
					taskInfo.getTaskGroup());
			schedulerFactoryBean.getScheduler().resumeJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 关闭整个任务调度器
	 */
	public void shutDown() {
		try {
			schedulerFactoryBean.getScheduler().shutdown();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取SimpleScheduleBuilder
	 * 
	 * @param expression
	 * @return
	 */
	private SimpleScheduleBuilder getSimpleScheduleBuilder(String expression,
			int count) {
		String time = expression.substring(0, expression.length() - 1);
		int interval = Integer.parseInt(time);
		String timeType = expression.substring(expression.length() - 1,
				expression.length());
		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
				.simpleSchedule();

		switch (timeType) {
		case "s":
			simpleScheduleBuilder = simpleScheduleBuilder
					.withIntervalInSeconds(interval);
			break;
		case "m":
			simpleScheduleBuilder = simpleScheduleBuilder
					.withIntervalInMinutes(interval);
			break;
		case "h":
			simpleScheduleBuilder = simpleScheduleBuilder
					.withIntervalInHours(interval);
			break;
		default:

			break;
		}
		// repeatForever()代表不限次数
		// withRepeatCount(10) 次数为执行10次
		if (count <= 0) {
			simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
		} else if (count > 0) {
			simpleScheduleBuilder = simpleScheduleBuilder
					.withRepeatCount(count);
		}
		simpleScheduleBuilder = simpleScheduleBuilder
				.withMisfireHandlingInstructionNextWithRemainingCount();
		return simpleScheduleBuilder;
	}

	private CronScheduleBuilder getCronScheduleBuilder(String expression) {

		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule(expression)
				.withMisfireHandlingInstructionDoNothing();

		return cronScheduleBuilder;
	}
}