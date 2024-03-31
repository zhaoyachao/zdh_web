package com.zyc.zdh.job;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.StrategyGroupMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.BeaconFireTask;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.StrategyGroupInfo;
import com.zyc.zdh.shiro.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MyJobBean extends QuartzJobBean implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(MyJobBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -8509585011462529939L;

	public static final String TASK_ID = "task_id";
	public static final String TASK_TYPE = "task_type";
	
	@Autowired
	private QuartzJobMapper quartzJobMapper;

	public QuartzJobMapper getQuartzJobMapper() {
		return quartzJobMapper;
	}

	public void setQuartzJobMapper(QuartzJobMapper quartzJobMapper) {
		this.quartzJobMapper = quartzJobMapper;
	}

	@Autowired
	private TaskLogInstanceMapper taskLogInstanceMapper;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private StrategyGroupMapper sgm;


	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			String taskId = jobDataMap.getString(TASK_ID);
			String taskType = jobDataMap.getString(TASK_TYPE);

			if (taskId == null || taskId.trim().equals("")) {
				throw new Exception("任务id为空");
			}
			// 记录当前时间更新任务最后执行时间
			Date currentTime =context.getScheduledFireTime();
			List<String> taskTypes=Arrays.asList(new String[]{"email","check","retry","etl", "blood"});
			if(StringUtils.isEmpty(taskType) || taskTypes.contains(taskType.toLowerCase())){
				QuartzJobMapper quartzJobMapper2 = this.quartzJobMapper;
				QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
				quartzJobInfo = quartzJobMapper2.selectByPrimaryKey(taskId);
				if(quartzJobInfo==null){
					logger.info("调度任务发现空的任务,任务id"+taskId);
					return ;
				}
				quartzJobInfo.setQuartz_time(new Timestamp(currentTime.getTime()));
				if(context.getTrigger().getNextFireTime()==null){
					//最后一次触发,设置任务状态为已完成
					quartzJobInfo.setStatus(JobStatus.FINISH.getValue());
				}
				JobCommon2.chooseJobBean(quartzJobInfo,0,null,null);
			}else if(!StringUtils.isEmpty(taskType) && taskType.equalsIgnoreCase("DIGITALMARKET")){
				//智能营销调度任务
				StrategyGroupInfo strategyGroupInfo=new StrategyGroupInfo();
				strategyGroupInfo = sgm.selectByPrimaryKey(taskId);
				strategyGroupInfo.setQuartz_time(new Timestamp(currentTime.getTime()));
				if(context.getTrigger().getNextFireTime()==null){
					strategyGroupInfo.setStatus(JobStatus.FINISH.getValue());
				}
				JobDigitalMarket.chooseJobBean(strategyGroupInfo, 0, null, null);
			}else if(!StringUtils.isEmpty(taskType) && taskType.equalsIgnoreCase("beaconfire")){
				//烽火台告警模块调度任务,告警模块计划采用纯内存存储,可以理解,这部分数据可以不做一致性(原因:告警是需要时效性的,错过去的告警已经失去了告警的意义)
				BeaconFireTask beaconFireTask = new BeaconFireTask();
				beaconFireTask.init(taskId, new Timestamp(currentTime.getTime()));
				JobBeaconFire.linkedBlockingQueue.add(beaconFireTask);
			}


//下方故障转移删除
//			if(!StringUtils.isEmpty(quartzJobInfo.getTask_log_id())){
//				TaskLogInstance tls=taskLogInstanceMapper.selectByPrimaryKey(quartzJobInfo.getTask_log_id());
//				if(tls!=null && tls.getServer_id()!=null && tls.getStatus().equalsIgnoreCase("dispatch")){
//					String key=tls.getServer_id().split(":")[0];
//					if(!redisUtil.exists(key) || !redisUtil.get(key).equals(tls.getServer_id().split(":")[1])){
//							//故障触发
//							logger.info("任务["+tls.getJob_context()+"],ETL_DATE:"+tls.getEtl_date()+",调度执行故障,将重新触发新的任务");
//							JobCommon2.insertLog(tls,"INFO","任务["+tls.getJob_context()+"],ETL_DATE:"+tls.getEtl_date()+",调度执行故障,将重新触发新的任务");
//							//修改task log 任务状态为error
//							tls.setStatus(InstanceStatus.ERROR.getValue());
//							JobCommon2.updateTaskLog(tls,taskLogInstanceMapper);
//						    JobCommon2.chooseJobBean(quartzJobInfo,2,tls);
//					}
//				}else{
//					//正常运行情况
//					JobCommon2.chooseJobBean(quartzJobInfo,0,null);
//				}
//			}else{
//				//正常运行情况
//				JobCommon2.chooseJobBean(quartzJobInfo,0,null);
//			}

		} catch (Exception e) {
			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
		}

	}

}