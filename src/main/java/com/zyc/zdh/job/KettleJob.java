package com.zyc.zdh.job;

import com.alibaba.druid.util.JdbcUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.entity.ZdhKettleAutoInfo;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.KettleUtil;

import java.net.URI;
import java.util.Map;

public class KettleJob extends JobCommon2 {

    public static String jobType = "KETTLE";


    /**
     * 当前Kettle实现只支持同步类型
     * @param tli
     * @return
     */
    public static Boolean kettleCommand(TaskLogInstance tli, ZdhKettleAutoInfo zdhKettleAutoInfo) {
        Boolean exe_status = true;
        //执行命令
        try {
            logger.info("kettle任务当前只支持同步kettle,异步kettle暂不支持");
            insertLog(tli,"info","kettle任务当前只支持同步kettle,异步kettle暂不支持");

            //调用kettle 工具类
            KettleUtil kettleUtil = new KettleUtil();
            KettleUtil.KettleResult kettleResult = null;
            String repositoryType = zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository_type();
            String repositoryPath = zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository_path();
            String user = zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository_user();
            String password = zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository_password();
            String kettleType = zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_type();
            logger.info("[" + jobType + "] JOB ,repository_type: "+repositoryType+", kettle_type:" + kettleType+", repository_path: "+repositoryPath);
            insertLog(tli, "info", "[" + jobType + "] JOB ,kettle_type:" + kettleType+", repository_path: "+repositoryPath);

            String params = zdhKettleAutoInfo.getEtlTaskKettleInfo().getData_sources_params_input();
            Map<String,String> options = JsonUtil.toJavaObj(params, new TypeReference<Map<String, String>>() {});
            if(repositoryType.equalsIgnoreCase("db")){
                String dbType = JdbcUtils.getDbType(zdhKettleAutoInfo.getDsi_Input().getUrl(), zdhKettleAutoInfo.getDsi_Input().getDriver());
                KettleUtil.DataSource dataSource = buildKettleDataSource(zdhKettleAutoInfo);
                if(kettleType.equalsIgnoreCase("trans")){
                    kettleResult = kettleUtil.runKettleTransferByRepository(tli.getId(),tli.getEtl_context(), dbType, tli.getJob_context(), repositoryPath, dataSource,options, user, password, options);
                }else if(kettleType.equalsIgnoreCase("job")){
                    kettleResult = kettleUtil.runKettleJobByRepository(tli.getId(),tli.getEtl_context(), dbType, tli.getJob_context(), repositoryPath, dataSource,options, user, password, options);
                }
            }else if(repositoryType.equalsIgnoreCase("file")){
                if(kettleType.equalsIgnoreCase("trans")){
                    kettleResult = kettleUtil.runKettleTransferByFile(tli.getId(), options,  repositoryPath);
                }else if(kettleType.equalsIgnoreCase("job")){
                    kettleResult = kettleUtil.runKettleJobByFile(tli.getId(), options,  repositoryPath);
                }
            }else{
                throw new Exception("kettle任务不支持"+repositoryType+"存储库类型");
            }

            if(kettleResult == null){
                throw new Exception("kettle任务异常, 未正常运行");
            }

            logger.info("[" + jobType + "] JOB ,kettle_log: "+kettleResult.getLog_text());
            insertLog(tli, "info", "[" + jobType + "] JOB ,kettle_log: "+kettleResult.getLog_text());

            logger.info("[" + jobType + "] JOB ,执行结果:" + kettleResult.getLog_text());
//                logger.info("[" + jobType + "] JOB ,正常输出:" + result.get("out").toString().trim());
//                logger.info("[" + jobType + "] JOB ,错误输出:" + result.get("error").toString().trim());
            insertLog(tli, "info", "[" + jobType + "] JOB ,执行结果:" + kettleResult.getLog_text());

            if(kettleResult.getCode() != 0){
                throw new Exception("kettle任务异常");
            }
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            logger.error(e.getMessage());
            insertLog(tli, "error","[" + jobType + "] JOB ,"+ e.getMessage());
            jobFail(jobType,tli);
            exe_status = false;
        }
        return exe_status;
    }

    public static KettleUtil.DataSource buildKettleDataSource(ZdhKettleAutoInfo zdhKettleAutoInfo){
        String dbType = JdbcUtils.getDbType(zdhKettleAutoInfo.getDsi_Input().getUrl(), zdhKettleAutoInfo.getDsi_Input().getDriver());
        URI uri = URI.create(zdhKettleAutoInfo.getDsi_Input().getUrl());
        System.out.println(uri.getScheme());
        System.out.println(uri.getHost());
        System.out.println(uri.getPort());
        System.out.println(uri.getPath());
        KettleUtil.DataSource dataSource = new KettleUtil.DataSource(zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository(), dbType, "Native",uri.getHost(), zdhKettleAutoInfo.getEtlTaskKettleInfo().getKettle_repository(),String.valueOf(uri.getPort()), zdhKettleAutoInfo.getDsi_Input().getUser(), zdhKettleAutoInfo.getDsi_Input().getPassword());
        return dataSource;
    }


}
