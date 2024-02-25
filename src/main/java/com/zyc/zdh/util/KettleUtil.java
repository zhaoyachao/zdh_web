package com.zyc.zdh.util;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * kettle 执行工具类
 */
public class KettleUtil {

    public static Map<String, Job> jobMap = new ConcurrentHashMap<>();
    public static Map<String, Trans> transMap = new ConcurrentHashMap<>();

    public static class KettleResult{
        private int code;
        private String msg;
        private String log_text;
        private Object result;
        private Exception e;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getLog_text() {
            return log_text;
        }

        public void setLog_text(String log_text) {
            this.log_text = log_text;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public Exception getE() {
            return e;
        }

        public void setE(Exception e) {
            this.e = e;
        }
    }

    public static class DataSource {
        private String name;

        private String type;

        private String access;

        private String host;

        private String db;

        private String port;

        private String user;

        private String pass;

        public DataSource(String name, String type, String access, String host, String db, String port, String user, String pass) {
            this.name = name;
            this.type = type;
            this.access = access;
            this.host = host;
            this.db = db;
            this.port = port;
            this.user = user;
            this.pass = pass;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getDb() {
            return db;
        }

        public void setDb(String db) {
            this.db = db;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }

    /**
     * 通过存储库执行kettle job任务
     *
     * @param logId              当前执行记录id
     * @param jobName            kettle job名称
     * @param dbType             资源库类型,常用mysql,oracle
     * @param repositoryName     资源库名称
     * @param repositoryDict     资源库说明
     * @param dataSource         资源库连接信息
     * @param options            资源库连接其他信息,比如加密方式,是否安全连接等,兼容数据连接时参数
     * @param repositoryUser     资源库登录名, 默认admin
     * @param repositoryPassword 资源库登录密码, 默认admin
     * @param kettleParams       kettle任务执行时参数
     * @return true/false
     * @throws Exception 失败抛异常
     */
    public KettleResult runKettleJobByRepository(String logId, String jobName, String dbType, String repositoryName, String repositoryDict,
                                            KettleUtil.DataSource dataSource, Map<String, String> options, String repositoryUser,
                                            String repositoryPassword, Map<String, String> kettleParams) throws Exception {
        checkLogId(logId);
        String uuid = UUID.randomUUID().toString();
        KettleResult kettleResult = new KettleResult();
        kettleResult.setCode(0);
        try {
            /**
             * 本地调用kettle , 需在resources目录下新增kettle-password-encoder-plugins.xml
             */
            KettleEnvironment.init();
            DatabaseMeta databaseMeta = new DatabaseMeta(dataSource.name, dataSource.type, dataSource.access, dataSource.host,
                    dataSource.db, dataSource.port, dataSource.user, dataSource.pass);
            // 关闭mysql推荐SSL连接提示
            if (options != null) {
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    databaseMeta.addExtraOption(dbType, entry.getKey(), entry.getValue());
                }
            }
            KettleDatabaseRepositoryMeta repositoryMeta = new KettleDatabaseRepositoryMeta(uuid, repositoryName, repositoryName, databaseMeta);
            KettleDatabaseRepository repository = new KettleDatabaseRepository();
            repository.init(repositoryMeta);

            repository.connect(repositoryUser, repositoryPassword);
            RepositoryDirectoryInterface repositoryDirectoryInterface = repository.findDirectory(repositoryDict);

            ObjectId jobId = repository.getJobId(jobName, repositoryDirectoryInterface);
            if (jobId == null) {
                throw new Exception("not found jobName: " + jobName);
            }
            JobMeta jobMeta = repository.loadJob(jobId, null);
            Job job = new Job(repository, jobMeta);
            //初始化job参数，脚本中获取参数值：${variableName}
            if (kettleParams != null) {
                for (String variableName : kettleParams.keySet()) {
                    job.setVariable(variableName, kettleParams.get(variableName));
                }
            }
            jobMap.put(logId, job);
            job.start();
            job.waitUntilFinished();
            String logChannelId = job.getLogChannelId();
            String logText = KettleLogStore.getAppender().getBuffer(logChannelId, true).toString();
            kettleResult.setLog_text(logText);
            //此处记录日志,待实现
            if (job.getErrors() > 0) {
                throw new Exception("job execute error");
            }
            return kettleResult;
        } catch (Exception e) {
            kettleResult.setE(e);
            throw e;
        } finally {
            jobMap.remove(logId);
        }
    }

    /**
     * 通过存储库执行kettle transfer 任务
     *
     * @param logId              当前执行记录id
     * @param transName          kettle trans名称
     * @param dbType             资源库类型,常用mysql,oracle
     * @param repositoryName     资源库名称
     * @param repositoryDict     资源库说明
     * @param dataSource         资源库连接信息
     * @param options            资源库连接其他信息,比如加密方式,是否安全连接等,兼容数据连接时参数
     * @param repositoryUser     资源库登录名, 默认admin
     * @param repositoryPassword 资源库登录密码, 默认admin
     * @param kettleParams       kettle任务执行时参数
     * @throws Exception
     */
    public KettleResult runKettleTransferByRepository(String logId, String transName, String dbType, String repositoryName, String repositoryDict,
                                                 KettleUtil.DataSource dataSource, Map<String, String> options, String repositoryUser,
                                                 String repositoryPassword, Map<String, String> kettleParams) throws Exception {
        checkLogId(logId);
        String uuid = UUID.randomUUID().toString();
        KettleResult kettleResult = new KettleResult();
        kettleResult.setCode(0);
        try {
            /**
             * 本地调用kettle , 需在resources目录下新增kettle-password-encoder-plugins.xml
             */
            KettleEnvironment.init();

            DatabaseMeta databaseMeta = new DatabaseMeta(dataSource.name, dataSource.type, dataSource.access, dataSource.host,
                    dataSource.db, dataSource.port, dataSource.user, dataSource.pass);
            // 关闭mysql推荐SSL连接提示
            if (options != null) {
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    databaseMeta.addExtraOption(dbType, entry.getKey(), entry.getValue());
                }
            }
            KettleDatabaseRepositoryMeta repositoryMeta = new KettleDatabaseRepositoryMeta(uuid, repositoryName, repositoryName, databaseMeta);
            KettleDatabaseRepository repository = new KettleDatabaseRepository();
            repository.init(repositoryMeta);

            repository.connect(repositoryUser, repositoryPassword);
            RepositoryDirectoryInterface repositoryDirectoryInterface = repository.findDirectory(repositoryDict);

            ObjectId transformationID = repository.getTransformationID(transName, repositoryDirectoryInterface);
            TransMeta transMeta = repository.loadTransformation(transformationID, null);
            Trans trans = new Trans(transMeta);

            //初始化trans参数，脚本中获取参数值：${variableName}
            if (kettleParams != null) {
                for (String variableName : kettleParams.keySet()) {
                    trans.setVariable(variableName, kettleParams.get(variableName));
                }
            }

            transMap.put(logId, trans);
            trans.execute(null);
            trans.waitUntilFinished();
            String logChannelId = trans.getLogChannelId();
            String logText = KettleLogStore.getAppender().getBuffer(logChannelId, true).toString();
            kettleResult.setLog_text(logText);
            //此处记录日志,待实现
            if (trans.getErrors() > 0) {
                throw new Exception("job execute error");
            }
            return kettleResult;
        } catch (Exception e) {
            throw e;
        } finally {
            transMap.remove(logId);
        }
    }


    /**
     * @param logId        执行任务唯一id
     * @param kettleParams job参数
     * @param kjbFilePath  job路径
     * @return
     * @功能描述: 执行job
     */
    public KettleResult runKettleJobByFile(String logId, Map<String, String> kettleParams, String kjbFilePath) throws Exception {
        checkLogId(logId);
        String uuid = UUID.randomUUID().toString();
        KettleResult kettleResult = new KettleResult();
        kettleResult.setCode(0);
        try {
            KettleEnvironment.init();
            //初始化job路径 
            JobMeta jobMeta = new JobMeta(kjbFilePath, null);
            Job job = new Job(null, jobMeta);
            //初始化job参数，脚本中获取参数值：${variableName}
            if (kettleParams != null) {
                for (String variableName : kettleParams.keySet()) {
                    job.setVariable(variableName, kettleParams.get(variableName));
                }
            }
            jobMap.put(logId, job);
            job.start();
            job.waitUntilFinished();
            String logChannelId = job.getLogChannelId();
            String logText = KettleLogStore.getAppender().getBuffer(logChannelId, true).toString();
            kettleResult.setLog_text(logText);
            //此处记录日志,待实现
            if (job.getErrors() > 0) {
                kettleResult.setCode(job.getErrors());
                return kettleResult;
            }
            return kettleResult;
        } catch (Exception e) {
            kettleResult.setE(e);
            return kettleResult;
        } finally {
            jobMap.remove(logId);
        }
    }

    /**
     * @param logId        执行任务唯一id
     * @param kettleParams Transfer参数
     * @param ktrFilePath  Transfer路径
     * @return
     * @功能描述: 执行Transfer
     */
    public KettleResult runKettleTransferByFile(String logId, Map<String, String> kettleParams, String ktrFilePath) throws Exception {
        checkLogId(logId);
        Trans trans = null;
        String uuid = UUID.randomUUID().toString();
        KettleResult kettleResult = new KettleResult();
        kettleResult.setCode(0);
        try {
            //初始化 
            KettleEnvironment.init();
            EnvUtil.environmentInit();
            TransMeta transMeta = new TransMeta(ktrFilePath);
            //转换 
            trans = new Trans(transMeta);
            //初始化trans参数，脚本中获取参数值：${variableName}
            if (kettleParams != null) {
                for (String variableName : kettleParams.keySet()) {
                    trans.setVariable(variableName, kettleParams.get(variableName));
                }
            }
            transMap.put(logId, trans);
            //执行转换 
            trans.execute(null);
            //等待转换执行结束 
            trans.waitUntilFinished();
            String logChannelId = trans.getLogChannelId();
            String logText = KettleLogStore.getAppender().getBuffer(logChannelId, true).toString();
            kettleResult.setLog_text(logText);
            //此处记录日志,待实现
            if (trans.getErrors() > 0) {
                kettleResult.setCode(trans.getErrors());
                return kettleResult;
            }
            return kettleResult;
        } catch (Exception e) {
            kettleResult.setE(e);
            return kettleResult;
        } finally {
            transMap.remove(logId);
        }
    }

    private void checkLogId(String logId) throws Exception {
        if(transMap.containsKey(logId) || jobMap.containsKey(logId)){
            throw new Exception("logId 重复, "+logId);
        }
    }

}