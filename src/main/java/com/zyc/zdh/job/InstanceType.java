package com.zyc.zdh.job;

public enum InstanceType {

    LABEL("label","label","标签任务","true"),//为了异步数据一致性
    CROWD_RULE("crowd_rule","crowd_rule","人群规则任务","true"),
    CROWD_OPERATE("crowd_operate","crowd_operate","运算符","true"),
    CROWD_FILE("crowd_file","crowd_file","人群文件任务","true"),
    CUSTOM_LIST("custom_list","custom_list","自定义名单任务","true"),
    FILTER("filter","filter","过滤任务","true"),
    SHUNT("shunt","shunt","分流任务","true"),
    TOUCH("touch","touch","触达任务","true"),
    ID_MAPPING("id_mapping","id_mapping","ID_MAPPING","true"),
    PLUGIN("plugin","plugin","自定义插件","true"),
    MANUAL_CONFIRM("manual_confirm","manual_confirm","人工确认","true"),
    RIGHTS("rights","rights","权益任务","true"),
    CODE_BLOCK("code_block","code_block","代码块","true"),
    DATA_NODE("data_node","data_node","在线节点","true"),
    RISK("risk","risk","决策事件","true"),
    TN("tn","tn","t+n时间","true"),
    FUNCTION("function","function","函数","true");


    private String value;
    private String code;
    private String desc;
    private String async;//是否异步,true:异步 false:同步

    private InstanceType(String code, String value, String desc, String async) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.async=async;
    }
    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
