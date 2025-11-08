package com.zyc.zdh.job;

public class ProcessEnum {

    public final static String INIT = "1";
    public final static String DISPATCHING = "5";
    public final static String INIT_DISPATCH_TIME = "6";
    public final static String CHECK_DEP = "7";
    public final static String CHECK_COUNT = "8";
    public final static String EXEC_SCRIPT = "9";
    public final static String CREATE_ETL_TASKINFO = "10";
    public final static String DISPATCH_JOB_FAIL = "10.5";
    public final static String INIT_ETL_ENGINE15 = "15";
    public final static String INIT_ETL_ENGINE17 = "17";
    public final static String FINISH = "100";

    public static String getProcessName(String process){
        switch (process){
            case INIT:
                return "未开始";
            case DISPATCHING:
                return "调度中";
            case INIT_DISPATCH_TIME:
                return "生成调度时间";
            case CHECK_DEP:
                return "检查依赖";
            case CHECK_COUNT:
                return "检查调度次数";
            case EXEC_SCRIPT:
                return "执行脚本";
            case CREATE_ETL_TASKINFO:
                return "生成ETL任务";
            case INIT_ETL_ENGINE15:
            case INIT_ETL_ENGINE17:
                return "调用ETL引擎";
            case FINISH:
                return "任务完成";
            default:
                return "执行中";
        }
    }


}
