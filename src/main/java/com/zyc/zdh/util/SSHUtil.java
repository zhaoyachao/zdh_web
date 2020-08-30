package com.zyc.zdh.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class SSHUtil {
    private transient Logger log = LoggerFactory.getLogger(this.getClass());

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

    /**
     * 构造基于密码认证的sftp对象
     * @param username
     * @param password
     * @param host
     * @param port
     */
    public SSHUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
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
                log.info("ssh connect,path of private key file：{}" , privateKey);
            }   
            log.info("ssh connect by host:{} username:{}",host,username);
       
            session = jsch.getSession(username, host, port);   
            log.info("Session is build");   
            if (password != null) {   
                session.setPassword(password);     
            }   
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");   
                   
            session.setConfig(config);   
            session.connect();   
            log.info("Session is connected");   
                 
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            exec =channel;
            log.info(String.format("ssh server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified ssh server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }   
    }     
       
    /** 
     * 关闭连接 server  
     */   
    public void logout(){   
        if (exec != null) {
            if (exec.isConnected()) {
                exec.disconnect();
                log.info("ssh is closed already");
            }   
        }   
        if (session != null) {   
            if (session.isConnected()) {   
                session.disconnect();   
                log.info("sshSession is closed already");   
            }   
        }   
    }

    public String[] exec(String cmd) throws IOException, JSchException {
        exec.setCommand(cmd);
        try {
            exec.connect();
            InputStream in=exec.getInputStream();
            InputStream error_in=exec.getErrStream();
            return new String[]{IOUtils.toString(error_in,"GBK"),IOUtils.toString(in,"GBK")};
        } catch (JSchException e) {
            e.printStackTrace();
           throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }finally {
         logout();
        }
    }
       

    public static void main(String[] args) throws SftpException, IOException, JSchException {
        SSHUtil sftp = new SSHUtil("zyc", "123456", "localhost", 22);
        sftp.login();   
        //byte[] buff = sftp.download("/opt", "start.sh");   
        //System.out.println(Arrays.toString(buff));
        System.out.println(sftp.exec("lsa")[0]);
        sftp.logout();   
    }   
}