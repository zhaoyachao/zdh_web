package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "etl_task_log_info")
public class EtlTaskLogInfo {
    @Id
    private String id;

    /**
     * 日志唯一标识code
     */
    private String log_code;

    /**
     * 日志说明
     */
    private String log_context;

    /**
     * flume启动命令
     */
    private String flume_command;

    /**
     * flume地址
     */
    private String flume_path;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 主机端口
     */
    private String port;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 密码
     */
    private String password;

    /**
     * 日志解析类型,1:正则解析
     */
    private String log_resovle_type;

    /**
     * 日志解析表达式
     */
    private String log_resovle_expr;

    /**
     * 日志输出类型,string,json
     */
    private String log_output_type;

    /**
     * 输出数据源id
     */
    private String data_sources_choose_output;

    /**
     * 输出数据源类型
     */
    private String data_source_type_output;

    /**
     * 输出数据源表名,文件名,topic
     */
    private String data_sources_output;

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
     * 任务配置
     */
    private String job_config;

    /**
     * 日志样例
     */
    private String log_example;

    /**
     * 日志输出配置
     */
    private String log_output_config;

    /**
     * 输出数据字段
     */
    private String data_sources_output_columns;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;

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
     * 获取日志唯一标识code
     *
     * @return log_code - 日志唯一标识code
     */
    public String getLog_code() {
        return log_code;
    }

    /**
     * 设置日志唯一标识code
     *
     * @param log_code 日志唯一标识code
     */
    public void setLog_code(String log_code) {
        this.log_code = log_code;
    }

    /**
     * 获取日志说明
     *
     * @return log_context - 日志说明
     */
    public String getLog_context() {
        return log_context;
    }

    /**
     * 设置日志说明
     *
     * @param log_context 日志说明
     */
    public void setLog_context(String log_context) {
        this.log_context = log_context;
    }

    public String getFlume_command() {
        return flume_command;
    }

    public void setFlume_command(String flume_command) {
        this.flume_command = flume_command;
    }

    /**
     * 获取flume地址
     *
     * @return flume_path - flume地址
     */
    public String getFlume_path() {
        return flume_path;
    }

    /**
     * 设置flume地址
     *
     * @param flume_path flume地址
     */
    public void setFlume_path(String flume_path) {
        this.flume_path = flume_path;
    }

    /**
     * 获取主机地址
     *
     * @return host - 主机地址
     */
    public String getHost() {
        return host;
    }

    /**
     * 设置主机地址
     *
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 获取主机端口
     *
     * @return port - 主机端口
     */
    public String getPort() {
        return port;
    }

    /**
     * 设置主机端口
     *
     * @param port 主机端口
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * 设置用户名
     *
     * @param user_name 用户名
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取日志解析类型,1:正则解析
     *
     * @return log_resovle_type - 日志解析类型,1:正则解析
     */
    public String getLog_resovle_type() {
        return log_resovle_type;
    }

    /**
     * 设置日志解析类型,1:正则解析
     *
     * @param log_resovle_type 日志解析类型,1:正则解析
     */
    public void setLog_resovle_type(String log_resovle_type) {
        this.log_resovle_type = log_resovle_type;
    }

    /**
     * 获取日志解析表达式
     *
     * @return log_resovle_expr - 日志解析表达式
     */
    public String getLog_resovle_expr() {
        return log_resovle_expr;
    }

    /**
     * 设置日志解析表达式
     *
     * @param log_resovle_expr 日志解析表达式
     */
    public void setLog_resovle_expr(String log_resovle_expr) {
        this.log_resovle_expr = log_resovle_expr;
    }

    /**
     * 获取日志输出类型,string,json
     *
     * @return log_output_type - 日志输出类型,string,json
     */
    public String getLog_output_type() {
        return log_output_type;
    }

    /**
     * 设置日志输出类型,string,json
     *
     * @param log_output_type 日志输出类型,string,json
     */
    public void setLog_output_type(String log_output_type) {
        this.log_output_type = log_output_type;
    }

    /**
     * 获取输出数据源id
     *
     * @return data_sources_choose_output - 输出数据源id
     */
    public String getData_sources_choose_output() {
        return data_sources_choose_output;
    }

    /**
     * 设置输出数据源id
     *
     * @param data_sources_choose_output 输出数据源id
     */
    public void setData_sources_choose_output(String data_sources_choose_output) {
        this.data_sources_choose_output = data_sources_choose_output;
    }

    /**
     * 获取输出数据源类型
     *
     * @return data_source_type_output - 输出数据源类型
     */
    public String getData_source_type_output() {
        return data_source_type_output;
    }

    /**
     * 设置输出数据源类型
     *
     * @param data_source_type_output 输出数据源类型
     */
    public void setData_source_type_output(String data_source_type_output) {
        this.data_source_type_output = data_source_type_output;
    }

    /**
     * 获取输出数据源表名,文件名,topic
     *
     * @return data_sources_output - 输出数据源表名,文件名,topic
     */
    public String getData_sources_output() {
        return data_sources_output;
    }

    /**
     * 设置输出数据源表名,文件名,topic
     *
     * @param data_sources_output 输出数据源表名,文件名,topic
     */
    public void setData_sources_output(String data_sources_output) {
        this.data_sources_output = data_sources_output;
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
     * 获取任务配置
     *
     * @return job_config - 任务配置
     */
    public String getJob_config() {
        return job_config;
    }

    /**
     * 设置任务配置
     *
     * @param job_config 任务配置
     */
    public void setJob_config(String job_config) {
        this.job_config = job_config;
    }

    /**
     * 获取日志样例
     *
     * @return log_example - 日志样例
     */
    public String getLog_example() {
        return log_example;
    }

    /**
     * 设置日志样例
     *
     * @param log_example 日志样例
     */
    public void setLog_example(String log_example) {
        this.log_example = log_example;
    }

    /**
     * 获取日志输出配置
     *
     * @return log_output_config - 日志输出配置
     */
    public String getLog_output_config() {
        return log_output_config;
    }

    /**
     * 设置日志输出配置
     *
     * @param log_output_config 日志输出配置
     */
    public void setLog_output_config(String log_output_config) {
        this.log_output_config = log_output_config;
    }

    /**
     * 获取输出数据字段
     *
     * @return data_sources_output_columns - 输出数据字段
     */
    public String getData_sources_output_columns() {
        return data_sources_output_columns;
    }

    /**
     * 设置输出数据字段
     *
     * @param data_sources_output_columns 输出数据字段
     */
    public void setData_sources_output_columns(String data_sources_output_columns) {
        this.data_sources_output_columns = data_sources_output_columns;
    }

    public String getDim_group() {
        return dim_group;
    }

    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}