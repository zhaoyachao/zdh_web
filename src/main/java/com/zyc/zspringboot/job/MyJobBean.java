package com.zyc.zspringboot.job;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;





import com.zyc.zspringboot.dao.TaskInfoMapper;
import com.zyc.zspringboot.entity.TaskInfo;
import com.zyc.zspringboot.util.SpringContext;


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
	private TaskInfoMapper taskInfoMapper;

	public TaskInfoMapper getTaskInfoMapper() {
		return taskInfoMapper;
	}

	public void setTaskInfoMapper(TaskInfoMapper taskInfoMapper) {
		this.taskInfoMapper = taskInfoMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
			System.out.println(jobDataMap.get(TASK_ID)
					+ "----执行-----"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			String taskId = jobDataMap.getString(TASK_ID);
			if (taskId == null || taskId.trim().equals("")) {
				throw new Exception("任务id为空");
			}
			// 记录当前时间更新任务最后执行时间
			Date currentTime = new Date();
			// 根据taskId获取TaskInfoTb
//			TaskInfoMapper dmTaskInfoTbMapper = (TaskInfoMapper) SpringContext
//					.getBean("taskInfoMapper");
			TaskInfoMapper dmTaskInfoTbMapper = taskInfoMapper;
			TaskInfo dmTaskInfoTb = new TaskInfo();
			dmTaskInfoTb = dmTaskInfoTbMapper.selectByPrimaryKey(taskId);
			Date lastTime = dmTaskInfoTb.getLastUpdateTime();
			dmTaskInfoTb.setLastUpdateTime(currentTime);
			// 任务执行次数计算
			String cou = dmTaskInfoTb.getTaskCount();
			Long count = 0L;
			if (cou == null || cou.equals("")
					|| Long.valueOf(cou) == Long.MAX_VALUE) {
				count = 1L;
			} else {
				count = Long.valueOf(cou) + 1;
			}
			dmTaskInfoTb.setTaskCount(count.toString());
			// 更新任务到数据库
			dmTaskInfoTb.setTaskStatus("runing");
			int result = dmTaskInfoTbMapper.updateByPrimaryKey(dmTaskInfoTb);
			//Thread.sleep(1000);
			logger.info("{}", result);
			// 获取规则服务
			// DimCheckedTablesInfoTbService ruleInfoService =
			// (DimCheckedTablesInfoTbService) SpringContextService
			// .getBean("dimCheckedTablesInfoTbService");
			//
			// @SuppressWarnings("rawtypes")
			// IDataNormProcessor dataNormProcessor = new ProcessorFactory()
			// .getgetProcessor(taskInfoTb.getTaskTablename());
			// dataNormProcessor.getDataNormContext().setTableName(
			// taskInfoTb.getTaskTablename());// tablename
			// dataNormProcessor.getDataNormContext().setDimNormDataList(// 规则
			// ruleInfoService.getNormInfoByTableName(taskInfoTb
			// .getTaskTablename()));
			// dataNormProcessor.getDataNormContext().setDataList(// 数据
			// new ScanData().getScanData(taskInfoTb.getTaskTablename(),
			// lastTime, currentTime));
			// dataNormProcessor.alarmDetection();// 告警
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}