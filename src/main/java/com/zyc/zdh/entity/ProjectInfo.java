package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Table(name = "project_info")
public class ProjectInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 项目code
     */
    private String project_code;

    /**
     * 项目名称
     */
    private String project_name;

    /**
     * 项目类型
     */
    private String project_type;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 用户组
     */
    private String dim_group;

    /**
     * 项目子参数
     */
    private String project_json;

    @Transient
    private List<Map<String, Object>> project_json_object;

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
     * 获取项目code
     *
     * @return project_code - 项目code
     */
    public String getProject_code() {
        return project_code;
    }

    /**
     * 设置项目code
     *
     * @param project_code 项目code
     */
    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    /**
     * 获取项目名称
     *
     * @return project_name - 项目名称
     */
    public String getProject_name() {
        return project_name;
    }

    /**
     * 设置项目名称
     *
     * @param project_name 项目名称
     */
    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    /**
     * 获取项目类型
     *
     * @return project_type - 项目类型
     */
    public String getProject_type() {
        return project_type;
    }

    /**
     * 设置项目类型
     *
     * @param project_type 项目类型
     */
    public void setProject_type(String project_type) {
        this.project_type = project_type;
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
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取用户组
     *
     * @return dim_group - 用户组
     */
    public String getDim_group() {
        return dim_group;
    }

    /**
     * 设置用户组
     *
     * @param dim_group 用户组
     */
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    /**
     * 获取项目子参数
     *
     * @return project_json - 项目子参数
     */
    public String getProject_json() {
        return project_json;
    }

    /**
     * 设置项目子参数
     *
     * @param project_json 项目子参数
     */
    public void setProject_json(String project_json) {
        this.project_json = project_json;
    }

    public List<Map<String, Object>> getProject_json_object() {
        if(!StringUtils.isEmpty(project_json)){
            return JsonUtil.toJavaListMap(project_json);
        }
        return new ArrayList<>();
    }
}