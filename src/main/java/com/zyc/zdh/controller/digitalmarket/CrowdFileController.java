package com.zyc.zdh.controller.digitalmarket;

import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdFileMapper;
import com.zyc.zdh.entity.CrowdFileInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 智能营销-人群文件服务
 */
@Controller
public class CrowdFileController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrowdFileMapper crowdFileMapper;

    @Autowired
    private Environment env;

    /**
     * 人群文件列表首页
     * @return
     */
    @RequestMapping(value = "/crowd_file_index", method = RequestMethod.GET)
    public String crowd_file_index() {

        return "digitalmarket/crowd_file_index";
    }

    /**
     * 过滤列表
     * @param file_name 关键字
     * @return
     */
    @RequestMapping(value = "/crowd_file_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  ReturnInfo<PageResult<List<CrowdFileInfo>>> crowd_file_list(String file_name,int limit, int offset) {
        try{
            Example example=new Example(CrowdFileInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(file_name)){
                criteria2.orLike("file_name", getLikeCondition(file_name));
                criteria2.orLike("file_url", getLikeCondition(file_name));
            }
            example.and(criteria2);

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = crowdFileMapper.selectCountByExample(example);

            List<CrowdFileInfo> crowdFileInfos = crowdFileMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<CrowdFileInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(crowdFileInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 过滤新增首页
     * @return
     */
    @RequestMapping(value = "/crowd_file_add_index", method = RequestMethod.GET)
    public String crowd_file_add_index() {

        return "digitalmarket/crowd_file_add_index";
    }


    /**
     * 过滤明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/crowd_file_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<CrowdFileInfo> crowd_file_detail(String id) {
        try {
            CrowdFileInfo crowdFileInfo = crowdFileMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", crowdFileInfo);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 过滤更新
     * @param crowdFileInfo
     * @return
     */
    @RequestMapping(value = "/crowd_file_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CrowdFileInfo> crowd_file_update(CrowdFileInfo crowdFileInfo) {
        try {

            CrowdFileInfo oldCrowdFileInfo = crowdFileMapper.selectByPrimaryKey(crowdFileInfo.getId());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            crowdFileInfo.setOwner(oldCrowdFileInfo.getOwner());
            crowdFileInfo.setCreate_time(oldCrowdFileInfo.getCreate_time());
            crowdFileInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            crowdFileInfo.setIs_delete(Const.NOT_DELETE);
            crowdFileMapper.updateByPrimaryKey(crowdFileInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", crowdFileInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 过滤新增
     * @param crowdFile
     * @return
     */
    @RequestMapping(value = "/crowd_file_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_file_add(CrowdFileInfo crowdFile, MultipartFile[] jar_files) {
        try {

            crowdFile.setOwner(getOwner());
            crowdFile.setIs_delete(Const.NOT_DELETE);
            crowdFile.setCreate_time(new Timestamp(new Date().getTime()));
            crowdFile.setUpdate_time(new Timestamp(new Date().getTime()));

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
                    String fileName = jar_file.getOriginalFilename();
                    if(fileName==null||fileName.trim().equalsIgnoreCase("")){
                        continue;
                    }
                    System.out.println("上传文件不为空:"+fileName);
                    String tmpPath = env.getProperty("digitalmarket.tmp.path","/home/tmp/file");

                    File tempFile = new File( tmpPath + fileName);

                    crowdFile.setFile_url(fileName);
                    if(tempFile.exists()){
                        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "文件名重复", "文件名重复");
                    }
                    try {

                        String store = env.getProperty("digitalmarket.store.type","local");
                        String host = env.getProperty("digitalmarket.sftp.host","");
                        String port = env.getProperty("digitalmarket.sftp.port","22");
                        String username = env.getProperty("digitalmarket.sftp.username","");
                        String password = env.getProperty("digitalmarket.sftp.password","");
                        String path = env.getProperty("digitalmarket.sftp.path","");
                        if(store.equalsIgnoreCase("local")){
                            //本地目录
                            String localPath = env.getProperty("digitalmarket.local.path","/home/data/file");
                            File fileDir = new File(localPath);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            File localFile = new File( fileDir + "/" +fileName);
                            logger.info("crowd file upload store type: {}, path: {}", store, localFile.getAbsolutePath());
                            FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(localFile.toPath()));

                        }else if(store.equalsIgnoreCase("sftp")){
                            logger.info("crowd file upload store type: {}, path: {}", store, path+fileName);
                            FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                            //sftp
                            SFTPUtil sftp = new SFTPUtil(username, password,
                                    host, new Integer(port));
                            sftp.login();
                            InputStream is = new FileInputStream(tempFile);
                            sftp.upload(path, fileName, is);
                            sftp.logout();
                        }

                        crowdFileMapper.insert(crowdFile);
                    } catch (Exception e) {
                        logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    }
                }
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 过滤删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/crowd_file_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_file_delete(String[] ids) {
        try {
            crowdFileMapper.deleteLogicByIds("crowd_file_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
