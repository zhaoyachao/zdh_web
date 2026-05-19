package com.zyc.zdh.controller.digitalmarket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Lists;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CustomerManagerMapper;
import com.zyc.zdh.entity.CustomerManagerInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import com.zyc.zdh.util.ResoveExcel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 客户信息表服务
 */
@Controller
public class CustomerManagerController extends BaseController {

    @Autowired
    private CustomerManagerMapper customerManagerMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 客户信息表列表首页
     * @return
     */
    @RequestMapping(value = "/customer_manager_index", method = RequestMethod.GET)
    public String customer_manager_index() {

        return "digitalmarket/customer_manager_index";
    }

    /**
     * 客户信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "customer_manager_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<CustomerManagerInfo>> customer_manager_list(String context, String product_code) {
        try{
            Example example=new Example(CustomerManagerInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<CustomerManagerInfo> customerManagerInfos = customerManagerMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, customerManagerInfos);

            return ReturnInfo.buildSuccess(customerManagerInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("客户信息表列表查询失败", e);
        }

    }


    /**
    * 客户信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "customer_manager_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<CustomerManagerInfo>>> customer_manager_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(CustomerManagerInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = customerManagerMapper.selectCountByExample(example);

            List<CustomerManagerInfo> customerManagerInfos = customerManagerMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, customerManagerInfos);

            PageResult<List<CustomerManagerInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(customerManagerInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("客户信息表列表分页查询失败", e);
        }

    }

    /**
     * 客户信息表新增首页
     * @return
     */
    @RequestMapping(value = "/customer_manager_add_index", method = RequestMethod.GET)
    public String customer_manager_add_index() {

        return "digitalmarket/customer_manager_add_index";
    }

    /**
     * 客户信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "customer_manager_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<CustomerManagerInfo> customer_manager_detail(String id) {
        try {
            CustomerManagerInfo customerManagerInfo = customerManagerMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  customerManagerInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", customerManagerInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 客户信息表更新
     * @param customerManagerInfo
     * @return
     */
    @SentinelResource(value = "customer_manager_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CustomerManagerInfo> customer_manager_update(CustomerManagerInfo customerManagerInfo) {
        try {

            CustomerManagerInfo oldCustomerManagerInfo = customerManagerMapper.selectByPrimaryKey(customerManagerInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, customerManagerInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldCustomerManagerInfo.getProduct_code(), getAttrEdit());


            customerManagerInfo.setCreate_time(oldCustomerManagerInfo.getCreate_time());
            customerManagerInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            customerManagerInfo.setIs_delete(Const.NOT_DELETE);
            customerManagerMapper.updateByPrimaryKeySelective(customerManagerInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", customerManagerInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 客户信息表新增
     * @param customerManagerInfo
     * @return
     */
    @SentinelResource(value = "customer_manager_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CustomerManagerInfo> customer_manager_add(CustomerManagerInfo customerManagerInfo) {
        try {
            customerManagerInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            customerManagerInfo.setOwner(getOwner());
            customerManagerInfo.setIs_delete(Const.NOT_DELETE);
            customerManagerInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            customerManagerInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, customerManagerInfo.getProduct_code(), getAttrAdd());
            customerManagerMapper.insertSelective(customerManagerInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", customerManagerInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 客户信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "customer_manager_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo customer_manager_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, customerManagerMapper, customerManagerMapper.getTable(), ids, getAttrDel());
            customerManagerMapper.deleteLogicByIds(customerManagerMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }


    /**
     * 客户信息表批量新增首页
     * @return
     */
    @RequestMapping(value = "/customer_manager_batchadd_index", method = RequestMethod.GET)
    public String customer_manager_batchadd_index() {

        return "digitalmarket/customer_manager_batchadd_index";
    }

    /**
     * 客户信息表批量新增
     * @param product_code
     * @param jar_files
     * @return
     */
    @SentinelResource(value = "customer_manager_batchadd", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_batchadd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<CustomerManagerInfo> customer_manager_batchadd(String product_code, MultipartFile jar_files) {
        try {

            checkAttrPermissionByProduct(zdhPermissionService, product_code, getAttrAdd());
            List<String> cols = Lists.newArrayList("uid", "uid_type", "source", "param_code", "param_context", "param_type", "param_value", "param_return_type");
            List<Map<String, Object>> maps = ResoveExcel.importExcel(jar_files, cols);
            CustomerManagerInfo customerManagerInfo = new CustomerManagerInfo();
            customerManagerInfo.setProduct_code(product_code);
            customerManagerInfo.setIs_delete(Const.NOT_DELETE);

            //同账号合并
            Map<String, List<Map<String, Object>>> merge = new HashMap<>();
            for (Map<String, Object> rec: maps){
                Object uid = rec.get("uid");
                Object uid_type = rec.get("uid_type");
                Object source = rec.get("source");
                Object param_type = rec.get("param_type");
                if(uid == null || StringUtils.isEmpty(uid.toString())){
                    continue;
                }
                if(uid_type == null || StringUtils.isEmpty(uid_type.toString())){
                    continue;
                }
                if(source == null || StringUtils.isEmpty(source.toString())){
                    continue;
                }

                if(param_type == null || StringUtils.isEmpty(param_type.toString())){
                    continue;
                }

                if(!Lists.newArrayList("string,int,long,double,boolean,date,timestamp").contains(param_type.toString().toLowerCase())){
                    throw new Exception("错误的参数类型值: "+param_type.toString());
                }

                String key = StrUtil.format("{}&_&{}&_&{}", uid, uid_type, source);
                rec.remove("uid");
                rec.remove("uid_type");
                rec.remove("source");
                rec.put("param_operate", "=");

                if(merge.containsKey(key)){
                    merge.get(key).add(rec);
                }else{
                    ArrayList<Map<String, Object>> maps1 = Lists.newArrayList(rec);
                    merge.put(key, maps1);
                }
            }

            if(merge.isEmpty()){
                throw new Exception("解析内容为空");
            }

            List<CustomerManagerInfo> customerManagerInfos = customerManagerMapper.select(customerManagerInfo);
            Map<String, CustomerManagerInfo> customerManagerInfoMaps = customerManagerInfos.stream()
                    .collect(Collectors.toMap(c->c.getUid()+"_"+c.getUid_type()+"_"+c.getSource(), Function.identity()));

            List<CustomerManagerInfo> updates = new ArrayList<>(maps.size());
            List<CustomerManagerInfo> inserts = new ArrayList<>(maps.size());

            for (String key: merge.keySet()){
                String uid = key.split("&_&")[0];
                String uid_type = key.split("&_&")[1];
                String source = key.split("&_&")[2];
                List<Map<String, Object>> maps1 = merge.get(key);
                if(customerManagerInfoMaps.containsKey(key)){
                    CustomerManagerInfo update = customerManagerInfoMaps.get(key);
                    List<Map<String, Object>> param_json_object = update.getParam_json_object();

                    param_json_object.addAll(maps1);
                    update.setConfig(JsonUtil.formatJsonString(param_json_object));
                    update.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                    updates.add(update);
                }else{
                    CustomerManagerInfo insert = new CustomerManagerInfo();
                    insert.setId(SnowflakeIdWorker.getInstance().nextId()+"");
                    insert.setProduct_code(product_code);
                    insert.setUid(uid);
                    insert.setUid_type(uid_type);
                    insert.setSource(source);
                    insert.setIs_delete(Const.NOT_DELETE);
                    insert.setOwner(getOwner());
                    insert.setConfig(JsonUtil.formatJsonString(maps1));
                    insert.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    insert.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                    inserts.add(insert);
                }
            }

            for (CustomerManagerInfo update: updates){
                customerManagerMapper.updateByPrimaryKeySelective(update);
            }
            for (CustomerManagerInfo insert: inserts){
                customerManagerMapper.insertSelective(insert);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", customerManagerInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    private void syncUpdate(){

    }

    /**
     * 下载模板
     * @param response
     */
    @RequestMapping(value = "/customer_manager_document_download", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void download_file(HttpServletResponse response) {

        String fileName = "客户批量上传模板.xlsx";

        Resource resource = new ClassPathResource(fileName);

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
            //本地文件
            path = resource.getFile();
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(path));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }

        } catch (FileNotFoundException e) {
            LogUtil.error(this.getClass(), e);
        } catch (IOException e) {
            LogUtil.error(this.getClass(), e);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LogUtil.error(this.getClass(), e);
                }
            }
        }
    }
}
