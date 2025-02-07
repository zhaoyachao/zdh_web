package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "strategy_instance")
public class StrategyInstance {
    @Id
    private String id;

    /**
     * 策略说明
     */
    private String strategy_context;

    /**
     * 组id
     */
    private String group_id;

    /**
     * 组实例说明
     */
    private String group_context;

    private String group_type;

    /**
     * 组实例id
     */
    private String group_instance_id;

    /**
     * 实例类型
     */
    private String instance_type;

    /**
     * 开始时间
     */
    private Timestamp start_time;

    /**
     * 结束时间
     */
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
     * 是否禁用true:禁用,false:启用
     */
    private String is_disenable;

    /**
     * 判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
     */
    private String depend_level;

    /**
     * 推送类型,database,queue
     */
    private String touch_type;

    /**
     * 策略id
     */
    private String strategy_id;

    /**
     * 策略关系
     */
    private String jsmind_data;

    /**
     * 生成实例血源关系
     */
    private String run_jsmind_data;

    /**
     * 下游任务实例id
     */
    private String next_tasks;

    /**
     * 上游任务实例id
     */
    private String pre_tasks;

    private String data_node;

    /**
     * 是否已通知,false/true
     */
    private String is_notice;

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
     * 获取策略说明
     *
     * @return strategy_context - 策略说明
     */
    public String getStrategy_context() {
        return strategy_context;
    }

    /**
     * 设置策略说明
     *
     * @param strategy_context 策略说明
     */
    public void setStrategy_context(String strategy_context) {
        this.strategy_context = strategy_context;
    }

    /**
     * 获取组id
     *
     * @return group_id - 组id
     */
    public String getGroup_id() {
        return group_id;
    }

    /**
     * 设置组id
     *
     * @param group_id 组id
     */
    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    /**
     * 获取组实例说明
     *
     * @return group_context - 组实例说明
     */
    public String getGroup_context() {
        return group_context;
    }

    /**
     * 设置组实例说明
     *
     * @param group_context 组实例说明
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
     * 获取组实例id
     *
     * @return group_instance_id - 组实例id
     */
    public String getGroup_instance_id() {
        return group_instance_id;
    }

    /**
     * 设置组实例id
     *
     * @param group_instance_id 组实例id
     */
    public void setGroup_instance_id(String group_instance_id) {
        this.group_instance_id = group_instance_id;
    }

    /**
     * 获取实例类型
     *
     * @return instance_type - 实例类型
     */
    public String getInstance_type() {
        return instance_type;
    }

    /**
     * 设置实例类型
     *
     * @param instance_type 实例类型
     */
    public void setInstance_type(String instance_type) {
        this.instance_type = instance_type;
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
     * 获取是否禁用true:禁用,false:启用
     *
     * @return is_disenable - 是否禁用true:禁用,false:启用
     */
    public String getIs_disenable() {
        return is_disenable;
    }

    /**
     * 设置是否禁用true:禁用,false:启用
     *
     * @param is_disenable 是否禁用true:禁用,false:启用
     */
    public void setIs_disenable(String is_disenable) {
        this.is_disenable = is_disenable;
    }

    /**
     * 获取判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
     *
     * @return depend_level - 判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
     */
    public String getDepend_level() {
        return depend_level;
    }

    /**
     * 设置判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
     *
     * @param depend_level 判定级别0：成功时运行,1:杀死时运行,2:失败时运行,默认成功时运行
     */
    public void setDepend_level(String depend_level) {
        this.depend_level = depend_level;
    }

    /**
     * 获取推送类型,database,queue
     *
     * @return touch_type - 推送类型,database,queue
     */
    public String getTouch_type() {
        return touch_type;
    }

    /**
     * 设置推送类型,database,queue
     *
     * @param touch_type 推送类型,database,queue
     */
    public void setTouch_type(String touch_type) {
        this.touch_type = touch_type;
    }

    /**
     * 获取策略id
     *
     * @return strategy_id - 策略id
     */
    public String getStrategy_id() {
        return strategy_id;
    }

    /**
     * 设置策略id
     *
     * @param strategy_id 策略id
     */
    public void setStrategy_id(String strategy_id) {
        this.strategy_id = strategy_id;
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

    /**
     * 获取生成实例血源关系
     *
     * @return run_jsmind_data - 生成实例血源关系
     */
    public String getRun_jsmind_data() {
        return run_jsmind_data;
    }

    /**
     * 设置生成实例血源关系
     *
     * @param run_jsmind_data 生成实例血源关系
     */
    public void setRun_jsmind_data(String run_jsmind_data) {
        this.run_jsmind_data = run_jsmind_data;
    }

    /**
     * 获取下游任务实例id
     *
     * @return next_tasks - 下游任务实例id
     */
    public String getNext_tasks() {
        return next_tasks;
    }

    /**
     * 设置下游任务实例id
     *
     * @param next_tasks 下游任务实例id
     */
    public void setNext_tasks(String next_tasks) {
        this.next_tasks = next_tasks;
    }

    /**
     * 获取上游任务实例id
     *
     * @return pre_tasks - 上游任务实例id
     */
    public String getPre_tasks() {
        return pre_tasks;
    }

    /**
     * 设置上游任务实例id
     *
     * @param pre_tasks 上游任务实例id
     */
    public void setPre_tasks(String pre_tasks) {
        this.pre_tasks = pre_tasks;
    }

    public String getData_node() {
        return data_node;
    }

    public void setData_node(String data_node) {
        this.data_node = data_node;
    }

    public String getIs_notice() {
        return is_notice;
    }

    public void setIs_notice(String is_notice) {
        this.is_notice = is_notice;
    }
}