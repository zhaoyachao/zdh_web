package com.zyc.zdh.job;
import java.io.Serializable;
import java.util.Date;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.TaskLogs;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


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
	private TaskLogsMapper taskLogsMapper;

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
			Date currentTime = new Date();

			QuartzJobMapper quartzJobMapper2 = this.quartzJobMapper;
			QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
			quartzJobInfo = quartzJobMapper2.selectByPrimaryKey(taskId);

			if(quartzJobInfo==null){
				logger.info("调度任务发现空的任务,任务id"+taskId);
			}
			if(StringUtils.isEmpty(quartzJobInfo.getTask_log_id())){
				TaskLogs tls=taskLogsMapper.selectByPrimaryKey(quartzJobInfo.getTask_log_id());
				if(tls!=null && !tls.getServer_ack().equalsIgnoreCase("1")){
					//故障触发,关机触发,对tasklogs 增加日志打印
					logger.info("任务["+tls.getJob_context()+"],ETL_DATE:"+tls.getEtl_date()+",调度执行故障,将重新触发新的任务");
					JobCommon.insertLog(quartzJobInfo,"INFO","任务["+tls.getJob_context()+"],ETL_DATE:"+tls.getEtl_date()+",调度执行故障,将重新触发新的任务");
					//修改task log 任务状态为error
					tls.setStatus(InstanceStatus.ERROR.getValue());
					JobCommon.updateTaskLog(tls,taskLogsMapper);
				}
			}
			JobCommon.chooseJobBean(quartzJobInfo,false);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

}