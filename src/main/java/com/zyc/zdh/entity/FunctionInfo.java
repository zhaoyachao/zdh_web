package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Table(name = "function_info")
public class FunctionInfo {
    @Id
    private String id;

    /**
     * 函数说明
     */
    private String function_context;

    /**
     * 函数名
     */
    private String function_name;

    /**
     * 函数全类名
     */
    private String function_class;

    /**
     * 函数加载路径
     */
    private String function_load_path;

    /**
     * 作用域,global:全局,local:局部
     */
    private String function_scope;

    /**
     * 函数参数
     */
    private String param_json;

    /**
     * 函数逻辑
     */
    private String function_script;

    /**
     * 函数返回类型,int,long,string,array,map,object
     */
    private String return_type;

    /**
     * 状态,offline:下线,online:上线
     */
    private String status;

    /**
     * 产品code
     */
    private String product_code;

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

    @Transient
    private List<Map<String, Object>> param_json_object;

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
     * 获取函数说明
     *
     * @return function_context - 函数说明
     */
    public String getFunction_context() {
        return function_context;
    }

    /**
     * 设置函数说明
     *
     * @param function_context 函数说明
     */
    public void setFunction_context(String function_context) {
        this.function_context = function_context;
    }

    /**
     * 获取函数名
     *
     * @return function_name - 函数名
     */
    public String getFunction_name() {
        return function_name;
    }

    /**
     * 设置函数名
     *
     * @param function_name 函数名
     */
    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    /**
     * 获取函数全类名
     *
     * @return function_class - 函数全类名
     */
    public String getFunction_class() {
        return function_class;
    }

    /**
     * 设置函数全类名
     *
     * @param function_class 函数全类名
     */
    public void setFunction_class(String function_class) {
        this.function_class = function_class;
    }

    /**
     * 获取函数加载路径
     *
     * @return function_load_path - 函数加载路径
     */
    public String getFunction_load_path() {
        return function_load_path;
    }

    /**
     * 设置函数加载路径
     *
     * @param function_load_path 函数加载路径
     */
    public void setFunction_load_path(String function_load_path) {
        this.function_load_path = function_load_path;
    }

    /**
     * 获取作用域,global:全局,local:局部
     *
     * @return function_scope - 作用域,global:全局,local:局部
     */
    public String getFunction_scope() {
        return function_scope;
    }

    /**
     * 设置作用域,global:全局,local:局部
     *
     * @param function_scope 作用域,global:全局,local:局部
     */
    public void setFunction_scope(String function_scope) {
        this.function_scope = function_scope;
    }

    /**
     * 获取状态,offline:下线,online:上线
     *
     * @return status - 状态,offline:下线,online:上线
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,offline:下线,online:上线
     *
     * @param status 状态,offline:下线,online:上线
     */
    public void setStatus(String status) {
        this.status = status;
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

    public String getParam_json() {
        return param_json;
    }

    public void setParam_json(String param_json) {
        this.param_json = param_json;
    }

    public String getFunction_script() {
        return function_script;
    }

    public void setFunction_script(String function_script) {
        this.function_script = function_script;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public List<Map<String, Object>> getParam_json_object() {
        if(!StringUtils.isEmpty(param_json)){
            return JsonUtil.toJavaListMap(param_json);
        }
        return JsonUtil.createEmptyListMap();
    }
}