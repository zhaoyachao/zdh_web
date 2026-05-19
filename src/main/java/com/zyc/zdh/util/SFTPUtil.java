package com.zyc.zdh.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class SFTPUtil {
         
    private ChannelSftp sftp;
           
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
    public SFTPUtil(String username, String password, String host, int port) {   
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
    public SFTPUtil(String username, String host, int port, String privateKey) {   
        this.username = username;   
        this.host = host;   
        this.port = port;   
        this.privateKey = privateKey;   
    }   
       
    public SFTPUtil(){}   
       
    /** 
     * 连接sftp服务器
     * @throws Exception  
     */   
    public void login(){   
        try {   
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥   
                LogUtil.info(this.getClass(), "sftp connect,path of private key file：{}", privateKey);
            }
            LogUtil.info(this.getClass(), "sftp connect by host:{} username:{}", host, username);

            session = jsch.getSession(username, host, port);
            LogUtil.info(this.getClass(), "Session is build");
            if (password != null) {
                session.setPassword(password);     
            }   
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");   
                   
            session.setConfig(config);
            session.connect();
            LogUtil.info(this.getClass(), "Session is connected");

            ChannelSftp channel = (ChannelSftp)session.openChannel("sftp");
            channel.setPty(true);
            channel.connect();
            LogUtil.info(this.getClass(), "channel is connected");
       
            sftp = channel;
            LogUtil.info(this.getClass(), String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            LogUtil.error(this.getClass(), "Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }   
    }     
       
    /** 
     * 关闭连接 server  
     */   
    public void logout(){   
        if (sftp != null) {   
            if (sftp.isConnected()) {
                sftp.disconnect();
                LogUtil.info(this.getClass(), "sftp is closed already");
            }   
        }   
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                LogUtil.info(this.getClass(), "sshSession is closed already");
            }   
        }   
    }   
       
    /**  
     * 将输入流的数据上传到sftp作为文件
     * @param directory 上传到该目录  
     * @param sftpFileName sftp端文件名  
     * @param input 输入流
     * @throws SftpException   
     * @throws Exception  
     */     
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            LogUtil.warn(this.getClass(), "directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        LogUtil.info(this.getClass(), "file:{} is upload successful", sftpFileName);
    }   
       
    /**  
     * 上传单个文件
     * @param directory 上传到sftp目录  
     * @param uploadFile 要上传的文件,包括路径  
     * @throws FileNotFoundException 
     * @throws SftpException 
     * @throws Exception 
     */   
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }   
       
    /** 
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     * @param directory 上传到sftp目录 
     * @param sftpFileName 文件在sftp端的命名 
     * @param byteArr 要上传的字节数组 
     * @throws SftpException 
     * @throws Exception 
     */   
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{   
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }   
       
    /**  
     * 将字符串按照指定的字符编码上传到sftp
     * @param directory 上传到sftp目录 
     * @param sftpFileName 文件在sftp端的命名 
     * @param dataStr 待上传的数据 
     * @param charsetName sftp上的文件，按该字符编码保存 
     * @throws UnsupportedEncodingException 
     * @throws SftpException 
     * @throws Exception 
     */   
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{     
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }   
       
    /** 
     * 下载文件  
     * @param directory 下载目录  
     * @param downloadFile 下载的文件 
     * @param saveFile 存在本地的路径 
     * @throws SftpException 
     * @throws FileNotFoundException 
     * @throws Exception 
     */     
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{   
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        LogUtil.info(this.getClass(), "file:{} is download successful", downloadFile);
    }  
       
    /**  
     * 下载文件 
     * @param directory 下载目录 
     * @param downloadFile 下载的文件名 
     * @return 字节数组 
     * @throws SftpException 
     * @throws IOException 
     * @throws Exception 
     */   
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{   
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        LogUtil.info(this.getClass(), "file:{} is download successful", downloadFile);
        return fileData;
    }   
       
    /** 
     * 删除文件 
     * @param directory 要删除文件所在目录 
     * @param deleteFile 要删除的文件 
     * @throws SftpException 
     * @throws Exception 
     */   
    public void delete(String directory, String deleteFile) throws SftpException{   
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 重命名文件
     * @param oldPath
     * @param newPath
     * @throws SftpException
     */
    public void rename(String oldPath, String newPath) throws SftpException {
        sftp.rename(oldPath,newPath);
    }
       
    /** 
     * 列出目录下的文件
     * @param directory 要列出的目录
     * @return 
     * @throws SftpException 
     */   
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }   
         
    public static void main(String[] args) throws SftpException, IOException {   
        SFTPUtil sftp = new SFTPUtil("lanhuigu", "123456", "192.168.200.12", 22);
        sftp.login();
        //byte[] buff = sftp.download("/opt", "start.sh");
        //System.out.println(Arrays.toString(buff));
        File file = new File("D:\\upload\\index.html");
        InputStream is = new FileInputStream(file);

        sftp.upload("/data/work", "test_sftp_upload.csv", is);
        sftp.logout();
    }   
}