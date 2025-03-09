package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ZdhDownloadMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.ZdhDownloadInfo;
import com.zyc.zdh.entity.ZdhNginx;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件下载服务
 */
@Controller
public class ZdhDownController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ZdhDownloadMapper zdhDownloadMapper;
    @Autowired
    private ZdhNginxMapper zdhNginxMapper;

    /**
     * 文件下载首页
     * @return
     */
    @RequestMapping(value = "/download_index", method = RequestMethod.GET)
    public String download_index() {

        return "etl/download_index";
    }

    /**
     * 文件下载列表
     * @param file_name 文件名
     * @return
     */
    @SentinelResource(value = "download_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/download_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ZdhDownloadInfo>> download_list(String file_name) {
        try{
            List<ZdhDownloadInfo> list = new ArrayList<>();
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            list = zdhDownloadMapper.slectByOwner(getOwner(), file_name);
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("文件下载列表查询失败", e);
        }

    }

    /**
     * 删除下载
     * @param ids
     * @return
     */
    @SentinelResource(value = "download_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/download_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo download_delete(String[] ids) {

        try{
            System.out.println("开始删除下载数据");
            ZdhDownloadInfo zdhDownloadInfo = new ZdhDownloadInfo();

            List<ZdhDownloadInfo> zdhDownloadInfoList = new ArrayList<>();
            for (String id : ids) {
                zdhDownloadInfo.setId(id);
                zdhDownloadMapper.deleteByPrimaryKey(zdhDownloadInfo);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }

    }


    /**
     * 下载文件
     * @param response
     * @param id 下载任务ID
     */
    @RequestMapping(value = "/download_file", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
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
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getOwner());
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
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        } catch (IOException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        } finally {
            zdhDownloadInfo.setDown_count(zdhDownloadInfo.getDown_count() + 1);
            zdhDownloadMapper.updateByPrimaryKeySelective(zdhDownloadInfo);

            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
            }
        }
    }

}
