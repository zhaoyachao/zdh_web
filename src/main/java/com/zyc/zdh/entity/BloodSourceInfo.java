package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "blood_source_info")
public class BloodSourceInfo {
    @Id
    private Long id;

    /**
     * 任务说明
     */
    private String context;

    private String input_type;

    /**
     * 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    private String input_md5;

    /**
     * 数据库名称+表名/远程文件路径
     */
    private String input;

    /**
     * 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    private String output_md5;

    /**
     * 数据库名称+表名/远程文件路径
     */
    private String output;

    /**
     * version
     */
    private String version;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 输出数据源类型
     */
    private String output_type;

    /**
     * 输出源配置
     */
    private String output_json;

    /**
     * 输入源配置
     */
    private String input_json;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取任务说明
     *
     * @return context - 任务说明
     */
    public String getContext() {
        return context;
    }

    /**
     * 设置任务说明
     *
     * @param context 任务说明
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return input_type
     */
    public String getInput_type() {
        return input_type;
    }

    /**
     * @param input_type
     */
    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    /**
     * 获取输入源唯一标识，数据源类型+数据源url 组合生成的md5
     *
     * @return input_md5 - 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    public String getInput_md5() {
        return input_md5;
    }

    /**
     * 设置输入源唯一标识，数据源类型+数据源url 组合生成的md5
     *
     * @param input_md5 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    public void setInput_md5(String input_md5) {
        this.input_md5 = input_md5;
    }

    /**
     * 获取数据库名称+表名/远程文件路径
     *
     * @return input - 数据库名称+表名/远程文件路径
     */
    public String getInput() {
        return input;
    }

    /**
     * 设置数据库名称+表名/远程文件路径
     *
     * @param input 数据库名称+表名/远程文件路径
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * 获取输入源唯一标识，数据源类型+数据源url 组合生成的md5
     *
     * @return output_md5 - 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    public String getOutput_md5() {
        return output_md5;
    }

    /**
     * 设置输入源唯一标识，数据源类型+数据源url 组合生成的md5
     *
     * @param output_md5 输入源唯一标识，数据源类型+数据源url 组合生成的md5
     */
    public void setOutput_md5(String output_md5) {
        this.output_md5 = output_md5;
    }

    /**
     * 获取数据库名称+表名/远程文件路径
     *
     * @return output - 数据库名称+表名/远程文件路径
     */
    public String getOutput() {
        return output;
    }

    /**
     * 设置数据库名称+表名/远程文件路径
     *
     * @param output 数据库名称+表名/远程文件路径
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * 获取version
     *
     * @return version - version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置version
     *
     * @param version version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取拥有者
     *
     * @return owner - 拥有者
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置拥有者
     *
     * @param owner 拥有者
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreate_time() {
        return create_time;
    }

    /**
     * 设置创建时间
     *
     * @param create_time 创建时间
     */
    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取输出数据源类型
     *
     * @return output_type - 输出数据源类型
     */
    public String getOutput_type() {
        return output_type;
    }

    /**
     * 设置输出数据源类型
     *
     * @param output_type 输出数据源类型
     */
    public void setOutput_type(String output_type) {
        this.output_type = output_type;
    }

    /**
     * 获取输出源配置
     *
     * @return output_json - 输出源配置
     */
    public String getOutput_json() {
        return output_json;
    }

    /**
     * 设置输出源配置
     *
     * @param output_json 输出源配置
     */
    public void setOutput_json(String output_json) {
        this.output_json = output_json;
    }

    /**
     * 获取输入源配置
     *
     * @return input_json - 输入源配置
     */
    public String getInput_json() {
        return input_json;
    }

    /**
     * 设置输入源配置
     *
     * @param input_json 输入源配置
     */
    public void setInput_json(String input_json) {
        this.input_json = input_json;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}