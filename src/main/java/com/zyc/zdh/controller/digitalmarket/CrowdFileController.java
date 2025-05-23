package com.zyc.zdh.controller.digitalmarket;

import cn.hutool.core.io.FileUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdFileMapper;
import com.zyc.zdh.entity.CrowdFileInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import io.minio.MinioClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能营销-人群文件服务
 */
@Controller
public class CrowdFileController extends BaseController {

    @Autowired
    private CrowdFileMapper crowdFileMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    @Autowired
    private RedisUtil redisUtil;


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
    @SentinelResource(value = "crowd_file_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  ReturnInfo<PageResult<List<CrowdFileInfo>>> crowd_file_list(String file_name,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(CrowdFileInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(file_name)){
                criteria2.orLike("file_name", getLikeCondition(file_name));
                criteria2.orLike("file_url", getLikeCondition(file_name));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = crowdFileMapper.selectCountByExample(example);

            List<CrowdFileInfo> crowdFileInfos = crowdFileMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, crowdFileInfos);

            PageResult<List<CrowdFileInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(crowdFileInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "crowd_file_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<CrowdFileInfo> crowd_file_detail(String id) {
        try {
            CrowdFileInfo crowdFileInfo = crowdFileMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, crowdFileInfo.getProduct_code(), crowdFileInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", crowdFileInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 过滤更新
     * @param crowdFileInfo
     * @return
     */
    @SentinelResource(value = "crowd_file_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CrowdFileInfo> crowd_file_update(CrowdFileInfo crowdFileInfo) {
        try {

            CrowdFileInfo oldCrowdFileInfo = crowdFileMapper.selectByPrimaryKey(crowdFileInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, crowdFileInfo.getProduct_code(), crowdFileInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldCrowdFileInfo.getProduct_code(), oldCrowdFileInfo.getDim_group(), getAttrEdit());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            crowdFileInfo.setOwner(oldCrowdFileInfo.getOwner());
            crowdFileInfo.setCreate_time(oldCrowdFileInfo.getCreate_time());
            crowdFileInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            crowdFileInfo.setIs_delete(Const.NOT_DELETE);
            crowdFileMapper.updateByPrimaryKeySelective(crowdFileInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", crowdFileInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 过滤新增
     * @param crowdFile
     * @return
     */
    @SentinelResource(value = "crowd_file_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_file_add(CrowdFileInfo crowdFile, MultipartFile[] jar_files) {
        try {

            crowdFile.setOwner(getOwner());
            crowdFile.setIs_delete(Const.NOT_DELETE);
            crowdFile.setCreate_time(new Timestamp(System.currentTimeMillis()));
            crowdFile.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, crowdFile.getProduct_code(), crowdFile.getDim_group(), getAttrAdd());

            //校验文件名称是否已经存在

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
                    String fileName = jar_file.getOriginalFilename();
                    if(fileName==null||fileName.trim().equalsIgnoreCase("")){
                        continue;
                    }

                    int lastIndex = fileName.lastIndexOf(".");
                    if(lastIndex>0){
                        String fileExtension = fileName.substring(lastIndex + 1);
                        if(!crowdFile.getFile_name().endsWith("."+fileExtension)){
                            throw new Exception("特殊类型文件,文件名称必须以上传的文件类型后缀结尾,比如 xxx.xlsx");
                        }
                    }


                    System.out.println("上传文件不为空:"+fileName);
                    String tmpPath = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_TMP_PATH, "/home/tmp/file");//env.getProperty("digitalmarket.tmp.path","/home/tmp/file");

                    File tempFile = new File( tmpPath + fileName);

                    crowdFile.setFile_url(fileName);
                    if(tempFile.exists()){
                        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "文件名重复", "文件名重复");
                    }

                    String crowdFileName = crowdFile.getFile_name();
                    try {

                        String store = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_STORE_TYPE, "local");//env.getProperty("digitalmarket.store.type","local");
                        String host = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_HOST, "");//env.getProperty("digitalmarket.sftp.host","");
                        String port = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_PORT, "22");//env.getProperty("digitalmarket.sftp.port","22");
                        String username = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_USERNAME, "");//env.getProperty("digitalmarket.sftp.username","");
                        String password = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_PASSWORD, "");//env.getProperty("digitalmarket.sftp.password","");
                        String localPath = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_LOCAL_PATH, "/home/data/file");//env.getProperty("digitalmarket.local.path","/home/data/file");
                        String path = localPath+"/crowd_file";

                        if(store.equalsIgnoreCase("local")){
                            //本地目录
                            File fileDir = new File(path);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            File localFile = new File( path + "/" +crowdFileName);
                            LogUtil.info(this.getClass(), "crowd file upload store type: {}, path: {}", store, localFile.getAbsolutePath());
                            FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(localFile.toPath()));

                        }else if(store.equalsIgnoreCase("sftp")){
                            LogUtil.info(this.getClass(), "crowd file upload store type: {}, path: {}", store, path + "/" + crowdFileName);
                            FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                            //sftp
                            SFTPUtil sftp = new SFTPUtil(username, password,
                                    host, new Integer(port));
                            sftp.login();
                            InputStream is = new FileInputStream(tempFile);
                            sftp.upload(path, fileName, is);
                            sftp.logout();
                        }else if(store.equalsIgnoreCase("minio")){
                            LogUtil.info(this.getClass(), "crowd file upload store type: {}, path: {}", store, path + "/" + crowdFile.getFile_name());
                            String object_name = path+"/"+crowdFile.getFile_name();
                            String ak = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_MINIO_AK);//env.getProperty("digitalmarket.minio.ak");
                            String sk = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_MINIO_SK);//env.getProperty("digitalmarket.minio.sk");
                            String endpoint = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_MINIO_ENDPOINT);//env.getProperty("digitalmarket.minio.endpoint");
                            String region = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_MINIO_REGION);//env.getProperty("digitalmarket.minio.region");
                            String bucket = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_MINIO_BUCKET);//env.getProperty("digitalmarket.minio.bucket");
                            MinioClient minioClient = MinioUtil.buildMinioClient(ak, sk, endpoint);
                            MinioUtil.putObject(minioClient, bucket, region, "application/octet-stream", object_name, jar_file.getInputStream(), null);
                        }

                        crowdFileMapper.insertSelective(crowdFile);
                    } catch (Exception e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    }
                }
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 过滤删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "crowd_file_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_file_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, crowdFileMapper, crowdFileMapper.getTable(), ids, getAttrDel());
            crowdFileMapper.deleteLogicByIds(crowdFileMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    @SentinelResource(value = "crowd_file_refash2redis", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_refash2redis", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_file_refash2redis(String id) {
        try {
            CrowdFileInfo oldCrowdFileInfo = crowdFileMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldCrowdFileInfo.getProduct_code(), oldCrowdFileInfo.getDim_group(), getAttrEdit());

            //读取数据
            String store =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_STORE_TYPE, "local");
            //env.getProperty("digitalmarket.store.type","local");
//            String host = env.getProperty("digitalmarket.sftp.host","");
//            String port = env.getProperty("digitalmarket.sftp.port","22");
//            String username = env.getProperty("digitalmarket.sftp.username","");
//            String password = env.getProperty("digitalmarket.sftp.password","");
            String host =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_HOST, ""); //env.getProperty("digitalmarket.sftp.host");
            String port =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_PORT, "22");//env.getProperty("digitalmarket.sftp.port");
            String username =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_USERNAME, "");//env.getProperty("digitalmarket.sftp.username");
            String password =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_PASSWORD, "");//env.getProperty("digitalmarket.sftp.password");

            String path = ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_SFTP_PATH, "");//env.getProperty("digitalmarket.sftp.path","");
            List<String> lines = new ArrayList<>();
            if(store.equalsIgnoreCase("local")){
                //本地目录
                String localPath =  ConfigUtil.getValue(ConfigUtil.DIGITALMARKET_LOCAL_PATH, "/home/data/file");//env.getProperty("digitalmarket.local.path","/home/data/file");
                File fileDir = new File(localPath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                File localFile = new File( fileDir + "/" +oldCrowdFileInfo.getFile_url());
                LogUtil.info(this.getClass(), "crowd file upload store type: {}, path: {}", store, localFile.getAbsolutePath());
                lines = FileUtil.readLines(localFile, "utf-8");

            }else if(store.equalsIgnoreCase("sftp")){
                LogUtil.info(this.getClass(), "crowd file upload store type: {}, path: {}", store, path + oldCrowdFileInfo.getFile_url());
                //sftp
                SFTPUtil sftp = new SFTPUtil(username, password,
                        host, new Integer(port));
                sftp.login();
                sftp.download(path, oldCrowdFileInfo.getFile_url(), "/tmp/"+oldCrowdFileInfo.getFile_url());
                lines = FileUtil.readLines("/tmp/"+oldCrowdFileInfo.getFile_url(), "utf-8");
                sftp.logout();
            }

            //遍历lines 写入redis
            if(lines.size() > 100000){
                throw new Exception("人群文件量级超过100000,禁止同步redis");
            }

            for (String line: lines){
                redisUtil.set(oldCrowdFileInfo.getProduct_code()+"_crowd_file_"+oldCrowdFileInfo.getId()+"_"+line, line);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "同步成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e.getMessage());
        }
    }

}
