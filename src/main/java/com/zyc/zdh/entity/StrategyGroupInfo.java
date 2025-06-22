package com.zyc.zdh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "strategy_group_info")
public class StrategyGroupInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 策略组说明
     */
    private String group_context;

    /**
     *类型,offline,online
     */
    private String group_type;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp start_time;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp end_time;

    /**
     * 账号
     */
    private String owner;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * cron表达式/自定义表达式
     */
    private String expr;

    /**
     * 恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行
     */
    private String misfire;

    /**
     * 任务优先级
     */
    private String priority;

    /**
     * 调度任务状态,create,running,pause,finish,remove,error
     */
    private String status;

    /**
     * 实例触发时间
     */
    private Timestamp quartz_time;

    /**
     * 是否使用quartz 调度时间
     */
    private String use_quartz_time;

    /**
     * 后退时间差
     */
    private String time_diff;

    /**
     * 调度来源,1:例行,2:手动
     */
    private String schedule_source;

    /**
     * 实例逻辑调度时间
     */
    private Timestamp cur_time;

    /**
     * 实例开始执行时间
     */
    private Timestamp run_time;

    /**
     * 策略关系
     */
    private String jsmind_data;

    /**
     * 在线监控数据节点,实时策略使用
     */
    private String data_node;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;

    /**
     * 默认小流量配置example: 1,100
     */
    private String small_flow_rate;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取策略组说明
     *
     * @return group_context - 策略组说明
     */
    public String getGroup_context() {
        return group_context;
    }

    /**
     * 设置策略组说明
     *
     * @param group_context 策略组说明
     */
    public void setGroup_context(String group_context) {
        this.group_context = group_context;
    }

    public String getGroup_type() {
        return group_type;
    }

    public void setGroup_type(String group_type) {
        this.group_type = group_type;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Timestamp getStart_time() {
        return start_time;
    }

    /**
     * 设置开始时间
     *
     * @param start_time 开始时间
     */
    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Timestamp getEnd_time() {
        return end_time;
    }

    /**
     * 设置结束时间
     *
     * @param end_time 结束时间
     */
    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    /**
     * 获取账号
     *
     * @return owner - 账号
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置账号
     *
     * @param owner 账号
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取是否删除,0:未删除,1:删除
     *
     * @return is_delete - 是否删除,0:未删除,1:删除
     */
    public String getIs_delete() {
        return is_delete;
    }

    /**
     * 设置是否删除,0:未删除,1:删除
     *
     * @param is_delete 是否删除,0:未删除,1:删除
     */
    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Timestamp getCreate_time() {
        return create_time;
    }

    /**
     * 设置创建时间
     *
     * @param create_time 创建时间
     */
    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Timestamp getUpdate_time() {
        return update_time;
    }

    /**
     * 设置更新时间
     *
     * @param update_time 更新时间
     */
    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    /**
     * 获取cron表达式/自定义表达式
     *
     * @return expr - cron表达式/自定义表达式
     */
    public String getExpr() {
        return expr;
    }

    /**
     * 设置cron表达式/自定义表达式
     *
     * @param expr cron表达式/自定义表达式
     */
    public void setExpr(String expr) {
        this.expr = expr;
    }

    /**
     * 获取恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行
     *
     * @return misfire - 恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行
     */
    public String getMisfire() {
        return misfire;
    }

    /**
     * 设置恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行
     *
     * @param misfire 恢复策略，0:无操作,1:所有历史重新执行,2:最近一次历史重新执行
     */
    public void setMisfire(String misfire) {
        this.misfire = misfire;
    }

    /**
     * 获取任务优先级
     *
     * @return priority - 任务优先级
     */
    public String getPriority() {
        return priority;
    }

    /**
     * 设置任务优先级
     *
     * @param priority 任务优先级
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 获取调度任务状态,create,running,pause,finish,remove,error
     *
     * @return status - 调度任务状态,create,running,pause,finish,remove,error
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置调度任务状态,create,running,pause,finish,remove,error
     *
     * @param status 调度任务状态,create,running,pause,finish,remove,error
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取实例触发时间
     *
     * @return quartz_time - 实例触发时间
     */
    public Timestamp getQuartz_time() {
        return quartz_time;
    }

    /**
     * 设置实例触发时间
     *
     * @param quartz_time 实例触发时间
     */
    public void setQuartz_time(Timestamp quartz_time) {
        this.quartz_time = quartz_time;
    }

    /**
     * 获取是否使用quartz 调度时间
     *
     * @return use_quartz_time - 是否使用quartz 调度时间
     */
    public String getUse_quartz_time() {
        return use_quartz_time;
    }

    /**
     * 设置是否使用quartz 调度时间
     *
     * @param use_quartz_time 是否使用quartz 调度时间
     */
    public void setUse_quartz_time(String use_quartz_time) {
        this.use_quartz_time = use_quartz_time;
    }

    /**
     * 获取后退时间差
     *
     * @return time_diff - 后退时间差
     */
    public String getTime_diff() {
        return time_diff;
    }

    /**
     * 设置后退时间差
     *
     * @param time_diff 后退时间差
     */
    public void setTime_diff(String time_diff) {
        this.time_diff = time_diff;
    }

    /**
     * 获取调度来源,1:例行,2:手动
     *
     * @return schedule_source - 调度来源,1:例行,2:手动
     */
    public String getSchedule_source() {
        return schedule_source;
    }

    /**
     * 设置调度来源,1:例行,2:手动
     *
     * @param schedule_source 调度来源,1:例行,2:手动
     */
    public void setSchedule_source(String schedule_source) {
        this.schedule_source = schedule_source;
    }

    /**
     * 获取实例逻辑调度时间
     *
     * @return cur_time - 实例逻辑调度时间
     */
    public Timestamp getCur_time() {
        return cur_time;
    }

    /**
     * 设置实例逻辑调度时间
     *
     * @param cur_time 实例逻辑调度时间
     */
    public void setCur_time(Timestamp cur_time) {
        this.cur_time = cur_time;
    }

    /**
     * 获取实例开始执行时间
     *
     * @return run_time - 实例开始执行时间
     */
    public Timestamp getRun_time() {
        return run_time;
    }

    /**
     * 设置实例开始执行时间
     *
     * @param run_time 实例开始执行时间
     */
    public void setRun_time(Timestamp run_time) {
        this.run_time = run_time;
    }

    /**
     * 获取策略关系
     *
     * @return jsmind_data - 策略关系
     */
    public String getJsmind_data() {
        return jsmind_data;
    }

    /**
     * 设置策略关系
     *
     * @param jsmind_data 策略关系
     */
    public void setJsmind_data(String jsmind_data) {
        this.jsmind_data = jsmind_data;
    }

    public String getData_node() {
        return data_node;
    }

    public void setData_node(String data_node) {
        this.data_node = data_node;
    }

    @Override
    public String getDim_group() {
        return dim_group;
    }

    @Override
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSmall_flow_rate() {
        return small_flow_rate;
    }

    public void setSmall_flow_rate(String small_flow_rate) {
        this.small_flow_rate = small_flow_rate;
    }
}