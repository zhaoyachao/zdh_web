package com.zyc.zspringboot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Table(name = "task_info")
public class TaskInfo extends PageBase implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8335156831210271689L;

	@Id
    @Column(name = "TASK_ID")
    private String taskId;

    @Column(name = "TASK_NAME")
    private String taskName;

    @Column(name = "TASK_GROUP")
    private String taskGroup;

    @Column(name = "TASK_TRIGGER")
    private String taskTrigger;

    @Column(name = "TASK_EXPRESSION")
    private String taskExpression;

    @Column(name = "TASK_STATUS")
    private String taskStatus;

    @Column(name = "TASK_TABLENAME")
    private String taskTablename;

    @Column(name = "TASK_BEANMAPPER")
    private String taskBeanmapper;

    @Column(name = "TASK_PARAM")
    private String taskParam;

    @Column(name = "TASK_DESC")
    private String taskDesc;

    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    @Column(name="TASK_PLAN_COUNT",nullable=true)
    private Integer taskPlanCount=null;
    
	@Column(name = "TASK_COUNT",nullable=true)
    private String taskCount;//重复执行次数如果是10那么会重复10次+开始执行时的1次

    @Column(name = "CREATE_TIME",insertable=false,updatable=false)
    private Date createTime;

    /**
     * @return TASK_ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * @return TASK_NAME
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * @return TASK_GROUP
     */
    public String getTaskGroup() {
        return taskGroup;
    }

    /**
     * @param taskGroup
     */
    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    /**
     * @return TASK_TRIGGER
     */
    public String getTaskTrigger() {
        return taskTrigger;
    }

    /**
     * @param taskTrigger
     */
    public void setTaskTrigger(String taskTrigger) {
        this.taskTrigger = taskTrigger;
    }

    /**
     * @return TASK_EXPRESSION
     */
    public String getTaskExpression() {
        return taskExpression;
    }

    /**
     * @param taskExpression
     */
    public void setTaskExpression(String taskExpression) {
        this.taskExpression = taskExpression;
    }

    /**
     * @return TASK_STATUS
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * @param taskStatus
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * @return TASK_TABLENAME
     */
    public String getTaskTablename() {
        return taskTablename;
    }

    /**
     * @param taskTablename
     */
    public void setTaskTablename(String taskTablename) {
        this.taskTablename = taskTablename;
    }

    /**
     * @return TASK_BEANMAPPER
     */
    public String getTaskBeanmapper() {
        return taskBeanmapper;
    }

    /**
     * @param taskBeanmapper
     */
    public void setTaskBeanmapper(String taskBeanmapper) {
        this.taskBeanmapper = taskBeanmapper;
    }

    /**
     * @return TASK_PARAM
     */
    public String getTaskParam() {
        return taskParam;
    }

    /**
     * @param taskParam
     */
    public void setTaskParam(String taskParam) {
        this.taskParam = taskParam;
    }

    /**
     * @return TASK_DESC
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * @param taskDesc
     */
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    /**
     * @return LAST_UPDATE_TIME
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return TASK_COUNT
     */
    public String getTaskCount() {
        return taskCount;
    }

    /**
     * @param taskCount
     */
    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Integer getTaskPlanCount() {
		return taskPlanCount;
	}

	public void setTaskPlanCount(Integer taskPlanCount) {
		this.taskPlanCount = taskPlanCount;
	}
}