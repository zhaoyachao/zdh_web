package com.zyc.zdh.util;

import com.jcraft.jsch.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.SetUpJob;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Vector;

public class SSHUtil {
    //private transient Logger log = LoggerFactory.getLogger(this.getClass());
    public Logger logger= LoggerFactory.getLogger(this.getClass());
    private ChannelExec exec;

    private Session session;
    /** FTP 登录用户名*/
    private String username;
    /** FTP 登录密码*/
    private String password;
    /** 私钥 */
    private String privateKey;
    /** FTP 服务器地址IP地址*/
    private String host;
    /** FTP 端口*/
    private int port;

    private String cmd;

    public String[] createUri(){
        String connectUri=String.format("ssh://%s:%s@%s",username,password,host);
        if(privateKey!=null){
            connectUri=String.format("ssh://%s@%s",username,host);
        }
        return new String[]{connectUri,cmd};
    }
    /**
     * 构造基于密码认证的sftp对象
     * @param username
     * @param password
     * @param host
     * @param port
     */
    public SSHUtil(String username, String password, String host, int port) {
        this.username = username;
        if(password.startsWith("privateKey:")){
            this.privateKey=password.substring(10);
        }else{
            this.password = password;
        }
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     * @param username
     * @param host
     * @param port
     * @param privateKey
     */
    public SSHUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SSHUtil(){}
       
    /** 
     * 连接sftp服务器
     * @throws Exception  
     */   
    public void login(){   
        try {   
            JSch jsch = new JSch();
            if (privateKey != null) {   
                jsch.addIdentity(privateKey);// 设置私钥   
                logger.info("ssh connect,path of private key file：{}" , privateKey);
            }
            logger.info("ssh connect by host:{} username:{}",host,username);
       
            session = jsch.getSession(username, host, port);
            logger.info("Session is build");
            if (password != null) {   
                session.setPassword(password);     
            }   
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");   
                   
            session.setConfig(config);   
            session.connect();
            logger.info("Session is connected");
                 
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setPty(true);
            exec =channel;
            logger.info(String.format("ssh server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            logger.error("Cannot connect to specified ssh server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }   
    }     
       
    /** 
     * 关闭连接 server  
     */   
    public void logout(){   
        if (exec != null) {
            if (exec.isConnected()) {
                exec.disconnect();
                logger.info("ssh is closed already");
            }   
        }   
        if (session != null) {   
            if (session.isConnected()) {   
                session.disconnect();
                logger.info("sshSession is closed already");
            }   
        }   
    }

    public String[] exec(String cmd,String task_logs_id,String job_id) throws IOException, JSchException {
        this.cmd=cmd;
        exec.setCommand(cmd);
        String line = Const.LINE_SEPARATOR;
        try {
            InputStream in=exec.getInputStream();
            InputStream error_in=exec.getErrStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(error_in, StandardCharsets.UTF_8));
            exec.setPty(true);
            exec.connect();
            String buf;
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            while ((buf = reader.readLine()) != null) {
                System.out.println(buf);
                //sb.append(buf);
                //sb.append(line);
                JobCommon2.insertLog(job_id,task_logs_id,"INFO",buf);
            }
            String errbuf;

            while ((errbuf = errorReader.readLine()) != null) {
                System.out.println(errbuf);
                sb2.append(errbuf);
                sb2.append(line);
                JobCommon2.insertLog(job_id,task_logs_id,"ERROR",errbuf);
            }
            return new String[]{sb2.toString(),sb.toString()};
        } catch (JSchException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
           throw e;
        } catch (IOException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            throw e;
        }finally {
         logout();
        }
    }

    public String[] exec2(String cmd,String task_logs_id,String job_id) throws IOException, JSchException {
        this.cmd=cmd;
        exec.setCommand(cmd);
        String line = Const.LINE_SEPARATOR;
        try {
            InputStream in=exec.getInputStream();
            InputStream error_in=exec.getErrStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(error_in, StandardCharsets.UTF_8));
            exec.connect();
            String buf;
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            while ((buf = reader.readLine()) != null) {
                System.out.println(buf);
                sb.append(buf);
                sb.append(line);
                JobCommon2.insertLog(job_id,task_logs_id,"INFO",buf);
            }
            SetUpJob.setStatus(task_logs_id,"1");
            String errbuf;

            while ((errbuf = errorReader.readLine()) != null) {
                System.out.println(errbuf);
                sb2.append(errbuf);
                sb2.append(line);
                JobCommon2.insertLog(job_id,task_logs_id,"ERROR",errbuf);
                SetUpJob.setStatus(task_logs_id,"2");
            }
            return new String[]{sb2.toString(),sb.toString()};
        } catch (JSchException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            throw e;
        } catch (IOException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            throw e;
        }finally {
        }
    }

    public String[] exec3(String cmd,String task_logs_id,String job_id) throws IOException, JSchException {
        this.cmd=cmd;
        exec.setCommand(cmd);
        String line = Const.LINE_SEPARATOR;
        try {
            InputStream in=exec.getInputStream();
            InputStream error_in=exec.getErrStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(error_in, StandardCharsets.UTF_8));
            exec.connect();
            String buf;
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            while ((buf = reader.readLine()) != null) {
                System.out.println(buf);
                sb.append(buf);
                sb.append(line);

            }
            String errbuf;
            while ((errbuf = errorReader.readLine()) != null) {
                System.out.println(errbuf);
                sb2.append(errbuf);
                sb2.append(line);

            }
            return new String[]{sb2.toString(),sb.toString()};
        } catch (JSchException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            throw e;
        } catch (IOException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            throw e;
        }finally {
        }
    }

    public static void main(String[] args) throws SftpException, IOException, JSchException {
        SSHUtil sftp = new SSHUtil("zyc", "123456", "192.168.110.10", 22);
        sftp.login();   
        //byte[] buff = sftp.download("/opt", "start.sh");   
        //System.out.println(Arrays.toString(buff));
        System.out.println(sftp.exec3("script -q -c \"scp -r /home/zyc/zdh_server/release/* zyc@127.0.0.1:/home/zyc/zdh_server_build\"","1","1")[1]);
        sftp.logout();   
    }   
}