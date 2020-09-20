package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.shiro.SecurityUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhDownController extends BaseController{

    @Autowired
    ZdhDownloadMapper zdhDownloadMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ZdhNginxMapper zdhNginxMapper;

    @RequestMapping(value = "/download_index", method = RequestMethod.GET)
    public String download_index() {

        return "etl/download_index";
    }

    @RequestMapping(value = "/download_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String download_list(String file_name) {
        List<ZdhDownloadInfo> list = new ArrayList<>();
        list = zdhDownloadMapper.slectByOwner(getUser().getId(), file_name);
        return JSON.toJSONString(list);
    }


    @RequestMapping(value = "/download_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String download_delete(String[] ids) {

        System.out.println("开始删除下载数据");
        ZdhDownloadInfo zdhDownloadInfo = new ZdhDownloadInfo();

        List<ZdhDownloadInfo> zdhDownloadInfoList = new ArrayList<>();
        for (String id : ids) {
            zdhDownloadInfo.setId(id);
            zdhDownloadMapper.deleteByPrimaryKey(zdhDownloadInfo);
        }

        zdhDownloadInfoList = zdhDownloadMapper.slectByOwner(getUser().getId(), "");

        if (zdhDownloadInfoList.size() > 0) {
            redisUtil.set("zdhdownloadinfos_" + getUser().getId(), JSON.toJSONString(zdhDownloadInfoList));
        } else {
            redisUtil.remove("zdhdownloadinfos_" + getUser().getId());
        }

        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();
    }


    @RequestMapping(value = "/download_file", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void download_file(HttpServletResponse response, String id) {

        ZdhDownloadInfo zdhDownloadInfo = zdhDownloadMapper.selectByPrimaryKey(id);
        int start_index = zdhDownloadInfo.getFile_name().lastIndexOf('/');
        String fileName = zdhDownloadInfo.getFile_name().substring(start_index + 1);
        File path = null;
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] buff = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getUser().getId());
            if (zdhNginx.getHost() != null && !zdhNginx.getHost().equals("")) {
                //连接sftp 下载
                SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                        zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                sftp.login();
                byte[] buff1 = sftp.download(zdhDownloadInfo.getFile_name().substring(0, start_index), fileName);
                os = response.getOutputStream();
                os.write(buff1, 0, buff1.length);
                os.flush();
            } else {
                //本地文件
                path = new File(zdhDownloadInfo.getFile_name());
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(path));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            String json = redisUtil.get("zdhdownloadinfos_" + getUser().getId()).toString();
            List<ZdhDownloadInfo> zdhDownloadInfoList = JSON.parseArray(json, ZdhDownloadInfo.class);
            Iterator<ZdhDownloadInfo> iterator = zdhDownloadInfoList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(id)) {
                    iterator.remove();
                }
            }
            if (zdhDownloadInfoList.size() > 0) {
                redisUtil.set("zdhdownloadinfos_" + getUser().getId(), JSON.toJSONString(zdhDownloadInfoList));
            } else {
                redisUtil.remove("zdhdownloadinfos_" + getUser().getId());
            }

            zdhDownloadInfo.setDown_count(zdhDownloadInfo.getDown_count() + 1);
            zdhDownloadMapper.updateByPrimaryKey(zdhDownloadInfo);


            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

}
