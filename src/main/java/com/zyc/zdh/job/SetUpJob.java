package com.zyc.zdh.job;

import com.jcraft.jsch.JSchException;
import com.zyc.zdh.dao.ServerTaskInstanceMappeer;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.ServerTaskInfo;
import com.zyc.zdh.entity.ServerTaskInstance;
import com.zyc.zdh.util.SSHUtil;
import com.zyc.zdh.util.SpringContext;
import com.zyc.zdh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetUpJob {

    private static Logger logger = LoggerFactory.getLogger(SetUpJob.class);

    public static void run(ServerTaskInstance sti) {

        //第一步：登陆构建服务器
        //第二步：拉取git
        //第三步：执行构建命令
        //第四步：scp 远程服务器
        SSHUtil sshUtil=null;
        String system = System.getProperty("os.name");
        if(system.toLowerCase().startsWith("win")){
            sshUtil=new SSHUtil(sti.getBuild_username(),sti.getBuild_privatekey(),sti.getBuild_ip(),22);
        }else{
            sshUtil=new SSHUtil(sti.getBuild_username(),sti.getBuild_ip(),22,sti.getBuild_privatekey());
        }

        List<String> cmd_list=new ArrayList<>();
        cmd_list.add("echo command start");
        if(!StringUtils.isEmpty(sti.getGit_url())){
            String cmd="git clone -b "+sti.getBuild_branch() + " "+sti.getGit_url() +" "+sti.getBuild_path();
            cmd_list.add("echo command: "+cmd);
            cmd_list.add(cmd);
        }
        if(!StringUtils.isEmpty(sti.getBuild_path())){
            String cmd2="cd "+sti.getBuild_path();
            cmd_list.add("echo command: "+cmd2);
            cmd_list.add(cmd2);
        }
        if(!StringUtils.isEmpty(sti.getBuild_command())){
            String cmd3=sti.getBuild_command();
            cmd_list.add("echo "+cmd3);
            cmd_list.add(cmd3);

        }
        if(!StringUtils.isEmpty(sti.getRemote_path())){
            String cmd4="script -q -c \"scp -r "+sti.getBuild_path()+"/release/* "+sti.getBuild_username()+"@"+sti.getRemote_ip()+":"+sti.getRemote_path() +"\"";
            cmd_list.add("echo command: "+cmd4);
            cmd_list.add(cmd4);
        }
        cmd_list.add("echo command finish");
        String cmd=org.apache.commons.lang.StringUtils.join(cmd_list,";");
        try {
            sshUtil.login();
            System.out.println(cmd);
            String[] out=sshUtil.exec2(cmd,sti.getId(),sti.getTemplete_id());
            System.out.println(out[0]+"=="+out[1]);
            sshUtil.logout();
        } catch (IOException e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
            JobCommon2.insertLog(sti.getTemplete_id(),sti.getId(),"INFO",e.getMessage());
            setStatus(sti.getId(),"2");
        } catch (JSchException e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
            JobCommon2.insertLog(sti.getTemplete_id(),sti.getId(),"INFO",e.getMessage());
            setStatus(sti.getId(),"2");
        }

    }


    public static void setStatus(String id,String status){
        ServerTaskInstanceMappeer stim = (ServerTaskInstanceMappeer) SpringContext.getBean("serverTaskInstanceMappeer");
        stim.updateStatusById(id,status);
    }

}
