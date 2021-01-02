package com.zyc.zdh.job;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.shiro.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class MyJobBean extends QuartzJobBean implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(MyJobBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -8509585011462529939L;

	public static final String TASK_ID = "task_id";
	
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

	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			String taskId = jobDataMap.getString(TASK_ID);

			if (taskId == null || taskId.trim().equals("")) {
				throw new Exception("任务id为空");
			}
			// 记录当前时间更新任务最后执行时间
			Date currentTime =context.getFireTime();

			QuartzJobMapper quartzJobMapper2 = this.quartzJobMapper;
			QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
			quartzJobInfo = quartzJobMapper2.selectByPrimaryKey(taskId);
			if(quartzJobInfo==null){
				logger.info("调度任务发现空的任务,任务id"+taskId);
				return ;
			}
			quartzJobInfo.setQuartz_time(new Timestamp(currentTime.getTime()));


			JobCommon2.chooseJobBean(quartzJobInfo,0,null,null);

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
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}