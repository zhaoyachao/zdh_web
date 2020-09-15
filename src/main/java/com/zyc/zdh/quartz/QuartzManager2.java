package com.zyc.zdh.quartz;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.job.MyJobBean;
import com.zyc.zdh.job.SnowflakeIdWorker;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("quartzManager2")
public class QuartzManager2 {

	private static Logger logger = LoggerFactory.getLogger(QuartzManager2.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Autowired
	private QuartzJobMapper quartzJobMapper;

	/**
	 * 根据任务名，对应的表名，表达式创建任务
	 * @return
	 */
	public QuartzJobInfo createQuartzJobInfo(String job_type, String job_model,Date start_date,Date end_date,String job_context,
			String expr, String plancount,String command,String etl_task_id) {
		QuartzJobInfo quartzJobInfo = new QuartzJobInfo();

		quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId()+"");
        quartzJobInfo.setJob_type(job_type);
        quartzJobInfo.setJob_model(job_model);
        quartzJobInfo.setStart_time(new Timestamp(start_date.getTime()));
        quartzJobInfo.setEnd_time(new Timestamp(end_date.getTime()));
        quartzJobInfo.setJob_context(job_context);
        quartzJobInfo.setCommand(command);
        quartzJobInfo.setExpr(expr);
        quartzJobInfo.setPlan_count(plancount);
		quartzJobInfo.setStatus("create");
        quartzJobInfo.setEtl_task_id(etl_task_id);

		return quartzJobInfo;
	}

	/**
	 * 通过id查询dmTaskInfoTb
	 * @param taskId
	 * @return TaskInfoTb
	 */
	public QuartzJobInfo selectQuartzJobInfo(String taskId){
		return quartzJobMapper.selectByPrimaryKey(taskId);
	}
	/**
	 * 将定时任务插入到任务信息表中
	 * @param quartzJobInfo
	 */
	//@Transactional(value="platformTransactionManager")
	public void addQuartzJobInfo(QuartzJobInfo quartzJobInfo){
		quartzJobMapper.insert(quartzJobInfo);
	}
	
	/**
	 * 执行定时任务
	 * 
	 * @param quartzJobInfo
	 */
	public void addTaskToQuartz(QuartzJobInfo quartzJobInfo) throws Exception {
		try {
			//根据调度id 和etl任务id 确定唯一的triggerkey
			if(schedulerFactoryBean.getScheduler().getTrigger(new TriggerKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()))!=null){
				logger.info("已经存在同名的triggerkey,请重新创建");
				throw new Exception("已经存在同名的triggerkey,请重新创建");
			}
			JobDetail jobDetail = JobBuilder
					.newJob(MyJobBean.class)
                    .requestRecovery(true)
					.withDescription(quartzJobInfo.getJob_context())
					.withIdentity(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()).build();
			Trigger trigger = null;
			// CronScheduleBuilder.cronSchedule(taskInfo.getTaskExpression())
			String expression = quartzJobInfo.getExpr();
			if (expression.contains("s") || expression.contains("m")
					|| expression.contains("h")) {
				SimpleScheduleBuilder simpleScheduleBuilder = getSimpleScheduleBuilder(
						expression, -1);
				trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()).startNow()
						.withSchedule(simpleScheduleBuilder).build();
			} else {
				CronScheduleBuilder cronScheduleBuilder = getCronScheduleBuilder(expression);
				trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()).startNow()
						.withSchedule(cronScheduleBuilder).build();
			}
			logger.debug("任务的trigger创建完成triggerkey is {}", trigger.getKey()
					.toString());
			JobDataMap jobDataMap = trigger.getJobDataMap();
			jobDataMap.put(MyJobBean.TASK_ID, quartzJobInfo.getJob_id());
			quartzJobInfo.setStatus("running");
			schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
			if (!schedulerFactoryBean.getScheduler().isStarted()) {
				schedulerFactoryBean.getScheduler().start();
			}
			quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
		} catch (SecurityException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 重启任务<br>
	 * 重启后的任务和重启前的任务的trigger保持一致
	 * @param quartzJobInfo
	 * @return
	 */
	public QuartzJobInfo reStartTask(QuartzJobInfo quartzJobInfo) {
		try {
			Trigger trigger = null;
			String expression = quartzJobInfo.getExpr();
			if (expression.contains("s") || expression.contains("m")
					|| expression.contains("h")) {
				trigger = (SimpleTrigger) schedulerFactoryBean.getScheduler().getTrigger(new TriggerKey(
						quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()));
				trigger = ((SimpleTrigger) trigger)
						.getTriggerBuilder()
						.withIdentity(
								new TriggerKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()))
						.withSchedule(
								getSimpleScheduleBuilder(expression,
										-1)).build();

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
						quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id()));
				trigger = ((CronTrigger) trigger)
						.getTriggerBuilder()
						.withIdentity(
								new TriggerKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id())).startNow()
						.withSchedule(getCronScheduleBuilder(expression))
						.build();
				trigger.getJobDataMap().put(MyJobBean.TASK_ID,
						quartzJobInfo.getJob_id());
				// 按新的trigger重新设置job执行
				schedulerFactoryBean.getScheduler().rescheduleJob(trigger.getKey(), trigger);
			}

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 下方可以做一些更新数据库中任务的操作
		quartzJobInfo.setStatus("runing");
		quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
		return quartzJobInfo;
	}
	/**
	 * 重启任务<br>
	 * 重启后的任务和重启前的任务的trigger不是同一种,比如之前是simpletrigger类型现在是crontrigger类型
	 * @param quartzJobInfo
	 * @return
	 */
	public void reStartTask2(QuartzJobInfo quartzJobInfo) throws Exception {
		deleteTask(quartzJobInfo,"finish");
		addTaskToQuartz(quartzJobInfo);
	}
	
	/**
	 * 删除任务
	 * 
	 * @param quartzJobInfo
	 * @return
	 */
	public QuartzJobInfo deleteTask(QuartzJobInfo quartzJobInfo,String status) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = new JobKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id());
			scheduler.pauseJob(jobKey);
			scheduler.deleteJob(jobKey);
			// 在自己定义的任务表中删除任务,状态删除
			if(status.equals("")) status="finish";
			quartzJobInfo.setStatus(status);
			quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
		} catch (SchedulerException e) {

			e.printStackTrace();
		}
		return quartzJobInfo;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param quartzJobInfo
	 */
	public void pauseTask(QuartzJobInfo quartzJobInfo) {
		try {
			// Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = new JobKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id());
			schedulerFactoryBean.getScheduler().pauseJob(jobKey);
			// 更新定时任务状态
			quartzJobInfo.setStatus("pause");
			quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param quartzJobInfo
	 */
	public void resumeTask(QuartzJobInfo quartzJobInfo) {
		try {
			JobKey jobKey = new JobKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id());
			schedulerFactoryBean.getScheduler().resumeJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<QuartzJobInfo> getScheduleTask(String owner) throws SchedulerException {
		try {
			Set<JobKey> jobKeySet=schedulerFactoryBean.getScheduler().getJobKeys(GroupMatcher.anyGroup());
			List<String> job_ids=new ArrayList<String>();
			//调度任务id
			for(JobKey jobKey:jobKeySet){
				job_ids.add(jobKey.getName());
			}
			return quartzJobMapper.selectRunJobByOwner(owner,job_ids);

		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
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
				.withMisfireHandlingInstructionFireNow();
				//.withMisfireHandlingInstructionNextWithRemainingCount();
		return simpleScheduleBuilder;
	}

	private CronScheduleBuilder getCronScheduleBuilder(String expression) {

		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule(expression)
				.withMisfireHandlingInstructionFireAndProceed();
				//.withMisfireHandlingInstructionDoNothing();

		return cronScheduleBuilder;
	}
}