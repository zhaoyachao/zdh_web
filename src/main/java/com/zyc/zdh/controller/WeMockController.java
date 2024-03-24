package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.WeMockDataMapper;
import com.zyc.zdh.dao.WeMockTreeMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

/**
 * mock数据服务
 */
@Controller
public class WeMockController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WeMockTreeMapper weMockTreeMapper;

    @Autowired
    private WeMockDataMapper weMockDataMapper;
    @Autowired
    private Environment ev;

    /**
     * mock数据首页
     * @return
     */
    @RequestMapping("/wemock_index")
    public String wemock_index() {

        return "wemock/wemock_index";
    }

    /**
     * mock数据新增页面
     * @return
     */
    @RequestMapping("/wemock_jstree_add_index")
    public String wemock_add_index() {

        return "wemock/wemock_jstree_add_index";
    }


    /**
     * wemock获取树形节点
     * @param product_code 产品code
     * @return
     */
    @SentinelResource(value = "wemock_jstree_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_jstree_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WeMockTreeInfo>> wemock_jstree_node(String product_code) {

        try{
            if(StringUtils.isEmpty(product_code)){
                throw new Exception("参数产品代码必填");
            }
            Example example=new Example(WeMockTreeInfo.class);

            Example.Criteria criteria=example.createCriteria();

            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            List<WeMockTreeInfo> weMockTreeInfos=weMockTreeMapper.selectByExample(example);
            weMockTreeInfos.sort(Comparator.comparing(WeMockTreeInfo::getOrderN));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockTreeInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * wemock增加根
     * @param wmti mock树形信息
     * @return
     */
    @SentinelResource(value = "wemock_add_root_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_add_root_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> wemock_add_root_node(WeMockTreeInfo wmti) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {

            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getProduct_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }

            //校验是否当前产品下已经存在根
            WeMockTreeInfo resourceTreeInfo=new WeMockTreeInfo();
            resourceTreeInfo.setProduct_code(wmti.getProduct_code());
            resourceTreeInfo.setParent("#");
            resourceTreeInfo = weMockTreeMapper.selectOne(resourceTreeInfo);
            if(resourceTreeInfo != null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前产品下已存在根", null);
            }

            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            wmti.setId(id);
            wmti.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wmti.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wmti.setIs_delete(Const.NOT_DELETE);
            wmti.setOwner(getOwner());
            wmti.setResource_desc("");
            if(StringUtils.isEmpty(wmti.getNotice_title())){
                wmti.setNotice_title("");
            }
            if(StringUtils.isEmpty(wmti.getEvent_code())){
                wmti.setEvent_code("");
            }
            if(StringUtils.isEmpty(wmti.getIcon())){
                wmti.setIcon("fa fa-folder");
            }

            weMockTreeMapper.insertSelective(wmti);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }


    /**
     * 根据主键获取mock资源信息
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wemock_jstree_get_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_jstree_get_node",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockTreeInfo> wemock_jstree_get_node(String id) {
        try{
            //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
            WeMockTreeInfo weMockTreeInfo=weMockTreeMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockTreeInfo);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * mock数据更新
     * @param wmti
     * @return
     */
    @SentinelResource(value = "wemock_update_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_update_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockTreeInfo> wemock_update_node(WeMockTreeInfo wmti) {

        try{

            //校验product_code
            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getProduct_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getParent())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "父节点不可为空", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getId())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "主键不可为空", null);
            }

            wmti.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wmti.setIs_delete(Const.NOT_DELETE);
            wmti.setOwner(getOwner());

            if(StringUtils.isEmpty(wmti.getNotice_title())){
                wmti.setNotice_title("");
            }
            if(StringUtils.isEmpty(wmti.getEvent_code())){
                wmti.setEvent_code("");
            }
            if(StringUtils.isEmpty(wmti.getIcon())){
                wmti.setIcon("fa fa-folder");
            }
            if(StringUtils.isEmpty(wmti.getUrl())){
                wmti.setUrl("");
            }
            weMockTreeMapper.updateByPrimaryKeySelective(wmti);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wmti);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * mock数据新增
     * @param wmti
     * @return
     */
    @SentinelResource(value = "wemock_add_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_add_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockTreeInfo> wemock_add_node(WeMockTreeInfo wmti) {

        try{

            //校验product_code
            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getProduct_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "产品代码不可为空", null);
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(wmti.getParent())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "父节点不可为空", null);
            }

            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            wmti.setId(id);
            wmti.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wmti.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wmti.setIs_delete(Const.NOT_DELETE);
            wmti.setOwner(getOwner());

            if(StringUtils.isEmpty(wmti.getNotice_title())){
                wmti.setNotice_title("");
            }
            if(StringUtils.isEmpty(wmti.getEvent_code())){
                wmti.setEvent_code("");
            }
            if(StringUtils.isEmpty(wmti.getIcon())){
                wmti.setIcon("fa fa-folder");
            }
            if(StringUtils.isEmpty(wmti.getUrl())){
                wmti.setUrl("");
            }
            weMockTreeMapper.insertSelective(wmti);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wmti);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * mock数据删除
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wemock_del_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_del_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_del_node(String id) {

        try {
            //校验是否根节点
            WeMockTreeInfo wmti = weMockTreeMapper.selectByPrimaryKey(id);
            if(wmti.getLevel().equalsIgnoreCase("1")){
                throw new Exception("根节点不可删除");
            }
            weMockTreeMapper.deleteLogicByIds("we_mock_tree_info", new String[]{id}, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", "");
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }

    }

    /**
     * 更新资源层级
     * @param id 资源ID
     * @param parent_id 资源父ID
     * @param level 资源层级
     * @return
     */
    @SentinelResource(value = "wemock_update_parent", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_update_parent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> wemock_update_parent(String id, String parent_id, String level) {
        //{ "id" : "ajson1", "parent" : "#", "text" : "Simple root node" },
        try {
            weMockTreeMapper.updateParentById(id, parent_id, level);
            //递归修改层级
            update_level(id, Integer.parseInt(level));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), getBaseException());
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), getBaseException(e));
        }
    }

    public void update_level(String id, int level){
        Example example=new Example(WeMockTreeInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parent", id);
        List<WeMockTreeInfo> resourceTreeInfos = weMockTreeMapper.selectByExample(example);
        if(resourceTreeInfos!=null && resourceTreeInfos.size()>0){
            for (WeMockTreeInfo rti:resourceTreeInfos){
                int next_level = level+1;
                weMockTreeMapper.updateParentById(rti.getId(), id, String.valueOf(next_level));
                update_level(rti.getId(), next_level);
            }
        }
    }


    /**
     * wemock获取mock数据明细 todo 待改造
     * @param mock_tree_id 树形节点,部门ID
     * @param limit 分页大小
     * @param offset 起始下标
     * @return
     */
    @SentinelResource(value = "wemock_data_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_data_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WeMockDataInfo>>> wemock_data_list(String mock_tree_id, int limit, int offset, String wemock_context) {

        try{
            if(StringUtils.isEmpty(mock_tree_id)){
                throw new Exception("主键必填");
            }

            Example example=new Example(WeMockDataInfo.class);

            Example.Criteria criteria=example.createCriteria();

            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            if(!StringUtils.isEmpty(wemock_context)){
                criteria.andLike("wemock_context", getLikeCondition(wemock_context));
            }

            //判断是否根节点
            WeMockTreeInfo weMockTreeInfo=weMockTreeMapper.selectByPrimaryKey(mock_tree_id);
            String product_code = weMockTreeInfo.getProduct_code();
            //获取这个产品下所有的信息
            if(weMockTreeInfo.getLevel().equalsIgnoreCase("1")){
                criteria.andEqualTo("product_code", product_code);
            }else{
                criteria.andEqualTo("mock_tree_id", mock_tree_id);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = weMockDataMapper.selectCountByExample(example);

            List<WeMockDataInfo> weMockDataInfos=weMockDataMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<WeMockDataInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(weMockDataInfos);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * mock数据-配置信息页面
     * @return
     */
    @RequestMapping("/wemock_data_add_index")
    public String wemock_data_add_index() {

        return "wemock/wemock_data_add_index";
    }


    /**
     * mock数据-信息明细
     * @param id 主键
     * @return
     */
    @SentinelResource(value = "wemock_data_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_data_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockDataInfo> wemock_data_detail(String id) {

        try{
            if(StringUtils.isEmpty(id)){
                throw new Exception("主键必填");
            }
            WeMockDataInfo weMockDataInfo=weMockDataMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockDataInfo);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * mock数据-新增配置
     * @param weMockDataInfo
     * @return
     */
    @SentinelResource(value = "wemock_data_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_data_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockDataInfo> wemock_data_add(WeMockDataInfo weMockDataInfo) {

        try{
            if(StringUtils.isEmpty(weMockDataInfo.getProduct_code())){
                throw new Exception("产品必填");
            }
            if(StringUtils.isEmpty(weMockDataInfo.getMock_tree_id())){
                throw new Exception("mock树节点必填");
            }
            weMockDataInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            weMockDataInfo.setOwner(getOwner());
            weMockDataInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            weMockDataInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            weMockDataInfo.setIs_delete(Const.NOT_DELETE);
           weMockDataMapper.insertSelective(weMockDataInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockDataInfo);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * mock数据-更新配置
     * @param weMockDataInfo
     * @return
     */
    @SentinelResource(value = "wemock_data_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_data_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WeMockDataInfo> wemock_data_update(WeMockDataInfo weMockDataInfo) {

        try{
            if(StringUtils.isEmpty(weMockDataInfo.getId())){
                throw new Exception("主键必填");
            }
            if(StringUtils.isEmpty(weMockDataInfo.getProduct_code())){
                throw new Exception("产品必填");
            }
            if(StringUtils.isEmpty(weMockDataInfo.getMock_tree_id())){
                throw new Exception("mock树节点必填");
            }
            weMockDataInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            weMockDataInfo.setOwner(getOwner());
            weMockDataInfo.setIs_delete(Const.NOT_DELETE);

            weMockDataMapper.updateByPrimaryKeySelective(weMockDataInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockDataInfo);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * mock数据-删除配置
     * @param ids
     * @return
     */
    @SentinelResource(value = "wemock_data_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wemock_data_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> wemock_data_delete(String[] ids) {

        try{
            if(ids==null || ids.length==0){
                throw new Exception("请选择删除的数据");
            }
            weMockDataMapper.deleteLogicByIds("we_mock_data_info", ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", "");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * mock数据-短链生成页面
     * @return
     */
    @RequestMapping("/short_url_index")
    public String short_url_generator() {
        return "wemock/short_url_index";
    }

    /**
     * 短链生成
     * @param param
     * @return
     */
    @SentinelResource(value = "short_url_generator", blockHandler = "handleReturn")
    @RequestMapping(value = "/short_url_generator", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<JSONObject> short_url_generator(String param) {
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", param);
            String host = ConfigUtil.getValue("zdh.wemock.short.host", "http://127.0.0.1:9001");
            String path = ConfigUtil.getValue("zdh.wemock.short.generator", "/api/short/generator");
            String ret = HttpUtil.postJSON(host+path, jsonObject.toJSONString());
            return ReturnInfo.buildSuccess(JSONObject.parseObject(ret));
        }catch (Exception e){
            e.printStackTrace();
            return ReturnInfo.buildError(e);
        }
    }
}
