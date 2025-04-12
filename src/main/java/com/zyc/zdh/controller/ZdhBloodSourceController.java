package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Lists;
import com.zyc.zdh.dao.BloodSourceMapper;
import com.zyc.zdh.entity.BloodSourceInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.CheckBloodSourceJob;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.DAG;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 血缘服务
 */
@Controller
public class ZdhBloodSourceController extends BaseController{

    @Autowired
    private BloodSourceMapper bloodSourceMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 血缘首页
     * @return
     */
    @RequestMapping("/blood_source_index")
    public String etl_task_jdbc_index() {

        return "etl/blood_source_index";
    }

    /**
     * 血缘分析首页
     * @return
     */
    @RequestMapping("/blood_source_detail_index")
    public String blood_source_detail_index() {

        return "etl/blood_source_detail_index";
    }

    /**
     * 血缘上报首页
     * @return
     */
    @RequestMapping("/blood_source_report_index")
    public String blood_source_report_index() {

        return "etl/blood_source_report_index";
    }

    /**
     * 血缘上报
     * @return
     */
    @SentinelResource(value = "blood_source_report", blockHandler = "handleReturn")
    @RequestMapping(value = "/blood_source_report", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> blood_source_report(String product_code,String context, String data_sources_choose_input, String data_sources_file_name_input, String data_sources_choose_output,String data_sources_file_name_output) {
        //获取数据权限
        try{
            checkPermissionByProduct(zdhPermissionService,product_code);
            BloodSourceInfo bloodSourceInfo = CheckBloodSourceJob.report(product_code, context, data_sources_choose_input, data_sources_file_name_input, data_sources_choose_output, data_sources_file_name_output, getOwner(), "-1");

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("上报失败",e);
        }

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"上报完成", null);
    }

    /**
     * 生成血缘关系
     * @return
     */
    @SentinelResource(value = "blood_source_create", blockHandler = "handleReturn")
    @RequestMapping(value = "/blood_source_create", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> blood_source_create() {
        try{
            String product_code = getProductCode();
            checkAttrPermissionByProduct(zdhPermissionService, product_code, getAttrEdit());
            CheckBloodSourceJob.Check(product_code);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"生成血源关系", null);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"生成血源关系失败", e);
        }
    }

    /**
     * 模糊查询输入源
     * @param input 表名/文件名
     * @return
     */
    @SentinelResource(value = "blood_source_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/blood_source_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> blood_source_list(String input) {

        try{
            String product_code = getProductCode();
            List<Map<String,Object>>  rs = jdbcTemplate.queryForList("select max(version) as version from blood_source_info where product_code=\""+product_code+"\"");
            if(rs==null || rs.size()<1){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "未找到任何血源分析数据", "未找到任何血源分析数据");
            }

            String version = rs.get(0).getOrDefault("version", "0").toString();
            Example example=new Example(BloodSourceInfo.class);

            Example.Criteria criteria = example.createCriteria();
            criteria.orLike("input",getLikeCondition(input));
            criteria.orLike("output",getLikeCondition(input));
            Example.Criteria criteria2 = example.createCriteria();
            criteria2.andIn("version", Lists.newArrayList(version, "-1"));
            example.and(criteria2);

            List<Map<String, Object>> jsonArray= JsonUtil.createEmptyListMap();
            List<String> inputs = new ArrayList<>();
            List<BloodSourceInfo> bloodSourceInfos = bloodSourceMapper.selectByExample(example);
            for (BloodSourceInfo bsi: bloodSourceInfos){

                if(!StringUtils.isEmpty(bsi.getInput()) && bsi.getInput().contains(input) && !StringUtils.isEmpty(bsi.getInput_type())){
                    if(!inputs.contains(bsi.getInput_type()+bsi.getInput())){
                        Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
                        inputs.add(bsi.getInput_type()+bsi.getInput());
                        jsonObject.put("input_type", bsi.getInput_type());
                        jsonObject.put("input", bsi.getInput());
                        jsonObject.put("input_md5", bsi.getInput_md5());
                        jsonArray.add(jsonObject);
                    }
                }
                if(!StringUtils.isEmpty(bsi.getOutput()) && bsi.getOutput().contains(input) && !StringUtils.isEmpty(bsi.getOutput_type())){
                    if(!inputs.contains(bsi.getOutput_type()+bsi.getOutput())){
                        inputs.add(bsi.getOutput_type()+bsi.getOutput());
                        Map<String, Object> jsonObject1= JsonUtil.createEmptyMap();
                        inputs.add(bsi.getOutput_type()+bsi.getOutput());
                        jsonObject1.put("input_type", bsi.getOutput_type());
                        jsonObject1.put("input", bsi.getOutput());
                        jsonObject1.put("input_md5", bsi.getOutput_md5());
                        jsonArray.add(jsonObject1);
                    }
                }

            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", jsonArray);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 数据血缘明细
     * @param input 表名/文件名
     * @param level 血源深度
     * @param stream_type 上游/下游  1：上游,2：下游, 3：全部
     * @return
     */
    @SentinelResource(value = "blood_source_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/blood_source_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<Map<String, Object>>> blood_source_detail(String input, String input_md5, String level, String stream_type) {
        try{
            String product_code = getProductCode();
            DAG dag=new DAG();
            Map<String, String> source_json = new HashMap<>();
            Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
            List<BloodSourceInfo> bloodSourceInfos = bloodSourceMapper.selectAll();
            for (BloodSourceInfo bsi: bloodSourceInfos){
                if(!bsi.getProduct_code().equalsIgnoreCase(product_code)){
                    continue;
                }
                if(!StringUtils.isEmpty(bsi.getInput()) && !StringUtils.isEmpty(bsi.getOutput()) && !bsi.getInput().equalsIgnoreCase(bsi.getOutput())){
                    //System.out.println(bsi.getInput()+"====="+bsi.getOutput());

                    boolean result = dag.addEdge(bsi.getInput()+"__-__"+bsi.getInput_md5(),bsi.getOutput()+"__-__"+bsi.getOutput_md5());
                    source_json.put(bsi.getInput()+"__-__"+bsi.getInput_md5(), bsi.getInput_json());
                    source_json.put(bsi.getOutput()+"__-__"+bsi.getOutput_md5(), bsi.getOutput_json());
                    if(!result){
                        jsonObject.put("result","失败");
                        return ReturnInfo.buildError(new Exception("解析失败"));
                    }
                }
            }
            Set<String> upstreams = (Set<String>)dag.getParent(input+"__-__"+input_md5);
            Set<String> downstreams = (Set<String>)dag.getChildren(input+"__-__"+input_md5);
            Map<String,Set<String>> downstream_map = new HashMap<>();
            Map<String,Set<String>> upstream_map = new HashMap<>();
            downstream_map.put(input+"__-__"+input_md5, downstreams);
            Queue queue=new LinkedList<String>();
            queue.addAll(downstreams);
            while (!queue.isEmpty()){
                String v = queue.poll().toString();
                Set<String> downstreams1 = (Set<String>)dag.getChildren(v);
                downstream_map.put(v, downstreams1);
                queue.addAll(downstreams1);
            }


            upstream_map.put(input+"__-__"+input_md5, upstreams);
            Queue queue2=new LinkedList<String>();
            queue2.add(input+"__-__"+input_md5);
            while (!queue.isEmpty()){
                String v = queue.poll().toString();
                Set<String> upstreams1 = (Set<String>)dag.getParent(v);
                upstream_map.put(v, upstreams1);
                queue2.addAll(upstreams1);
            }

            //生成血源数据
            List<Map<String, Object>> jsonArray=JsonUtil.createEmptyListMap();

            //生成根节点
            Map<String, Object> j2=JsonUtil.createEmptyMap();
            j2.put("id", DigestUtils.md5DigestAsHex((input+"__-__"+input_md5).getBytes()));
            j2.put("topic", input);
            j2.put("source_json", JsonUtil.toJavaMap(source_json.get(input+"__-__"+input_md5)));
            j2.put("background-color", "#C2DFFF");
            j2.put("isroot", true);
            jsonArray.add(j2);
            Map<String,String> flag=new HashMap<>();
            for (String key:downstream_map.keySet()){
                String color_key = "#C2DFFF";
                String color_value = "#FF9900";
                String isroot = "true";
                if(!key.equalsIgnoreCase(input)){
                    color_key = "#2B65EC";
                    isroot = "false";
                }

                for(String chilren:downstream_map.get(key)){
                    Map<String, Object> j3=JsonUtil.createEmptyMap();
                    j3.put("id", DigestUtils.md5DigestAsHex(chilren.getBytes()));
                    j3.put("topic", chilren.split("__-__")[0]);
                    j3.put("source_json", JsonUtil.toJavaMap(source_json.get(chilren)));
                    j3.put("background-color", color_value);
                    j3.put("parentid", DigestUtils.md5DigestAsHex(key.getBytes()));
                    j3.put("direction","right");
                    if(!flag.containsKey(chilren)){
                        jsonArray.add(j3);
                        flag.put(chilren,"");
                    }
                }
            }


            for (String key:upstream_map.keySet()){
                for(String upstream: upstream_map.get(key)){
                    Map<String, Object> j1=JsonUtil.createEmptyMap();
                    j1.put("id", DigestUtils.md5DigestAsHex(upstream.getBytes()) );
                    j1.put("topic", upstream.split("__-__")[0]);
                    j1.put("source_json", JsonUtil.toJavaMap(source_json.get(upstream)));
                    j1.put("background-color", "#00bb00");
                    j1.put("parentid", DigestUtils.md5DigestAsHex(key.getBytes()));
                    j1.put("direction","left");
                    jsonArray.add(j1);
                }
            }

            return ReturnInfo.buildSuccess(jsonArray);
        }catch (Exception e){
            return ReturnInfo.buildError(e.getMessage(), e);
        }
    }

    /**
     * 生成血源树,此方法暂时不用
     * @param dag
     * @param input
     * @param color
     * @return
     */
    private Map<String, Object> blood_source_tree(DAG dag, String input, String color){
        Map<String, Object> js=JsonUtil.createEmptyMap();
        js.put("id", DigestUtils.md5DigestAsHex(input.getBytes()));
        js.put("topic", input);
        js.put("background-color", color);
        js.put("children", Lists.newArrayList());
        if(dag.getChildren(input).size()==0){
            return js;
        }
        Set<String> downstreams = (Set<String>)dag.getChildren(input);
        for (String downstream:downstreams){
            ((List<Map<String, Object>>)js.get("children")).add(blood_source_tree(dag, downstream, "#2B65EC"));
        }
        return js;
    }

}
