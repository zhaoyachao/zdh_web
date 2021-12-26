package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingbase.util.MD5Digest;
import com.zyc.zdh.dao.BloodSourceMapper;
import com.zyc.zdh.dao.EtlTaskJdbcMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.CheckBloodSourceJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.DAG;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhBloodSourceController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BloodSourceMapper bloodSourceMapper;
    @Autowired
    EtlTaskJdbcMapper etlTaskJdbcMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/blood_source_index")
    public String etl_task_jdbc_index() {

        return "etl/blood_source_index";
    }

    @RequestMapping("/blood_source_detail_index")
    public String blood_source_detail_index() {

        return "etl/blood_source_detail_index";
    }

    @RequestMapping("/blood_source_create")
    @ResponseBody
    public String blood_source_create() {
        CheckBloodSourceJob.Check();
        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"生成血源关系", null);
    }

    /**
     * 模糊查询输入源
     * @param input 表名/文件名
     * @return
     */
    @RequestMapping(value = "/blood_source_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String blood_source_list(String input) {

        List<Map<String,Object>>  rs = jdbcTemplate.queryForList("select max(version) as version from blood_source_info");
        if(rs==null || rs.size()<1){
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("message", "未找到任何血源分析数据");
            return jsonObject.toJSONString();
        }

        String version = rs.get(0).getOrDefault("version", "0").toString();
        Example example=new Example(BloodSourceInfo.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.orLike("input","%"+input+"%");
        criteria.orLike("output","%"+input+"%");
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("version", version);

        JSONArray jsonArray=new JSONArray();
        List<String> inputs = new ArrayList<>();
        List<BloodSourceInfo> bloodSourceInfos = bloodSourceMapper.selectByExample(example);
        for (BloodSourceInfo bsi: bloodSourceInfos){

            if(!StringUtils.isEmpty(bsi.getInput()) && bsi.getInput().contains(input) && !StringUtils.isEmpty(bsi.getInput_type())){
                if(!inputs.contains(bsi.getInput_type()+bsi.getInput())){
                    JSONObject jsonObject= new JSONObject();
                    inputs.add(bsi.getInput_type()+bsi.getInput());
                    jsonObject.put("input_type", bsi.getInput_type());
                    jsonObject.put("input", bsi.getInput());
                    jsonArray.add(jsonObject);
                }
            }
            if(!StringUtils.isEmpty(bsi.getOutput()) && bsi.getOutput().contains(input) && !StringUtils.isEmpty(bsi.getOutput_type())){
                if(!inputs.contains(bsi.getOutput_type()+bsi.getOutput())){
                    inputs.add(bsi.getOutput_type()+bsi.getOutput());
                    JSONObject jsonObject1= new JSONObject();
                    inputs.add(bsi.getOutput_type()+bsi.getOutput());
                    jsonObject1.put("input_type", bsi.getOutput_type());
                    jsonObject1.put("input", bsi.getOutput());
                    jsonArray.add(jsonObject1);
                }
            }

        }
        return jsonArray.toJSONString();
    }

    /**
     *
     * @param input
     * @param level 血源深度
     * @param stream_type 上游/下游  1：上游,2：下游, 3：全部
     * @return
     */
    @RequestMapping(value = "/blood_source_detail", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String blood_source_detail(String input, String level, String stream_type) {
        DAG dag=new DAG();
        JSONObject jsonObject=new JSONObject();
        List<BloodSourceInfo> bloodSourceInfos = bloodSourceMapper.selectAll();
        for (BloodSourceInfo bsi: bloodSourceInfos){
            if(!StringUtils.isEmpty(bsi.getInput()) && !StringUtils.isEmpty(bsi.getOutput()) && !bsi.getInput().equalsIgnoreCase(bsi.getOutput())){
                System.out.println(bsi.getInput()+"====="+bsi.getOutput());
                boolean result = dag.addEdge(bsi.getInput(),bsi.getOutput());
                if(!result){
                    jsonObject.put("result","失败");
                    return jsonObject.toJSONString();
                }
            }
        }
        Set<String> upstreams = (Set<String>)dag.getParent(input);
        Set<String> downstreams = (Set<String>)dag.getChildren(input);
        Map<String,Set<String>> downstream_map = new HashMap<>();
        Map<String,Set<String>> upstream_map = new HashMap<>();
        downstream_map.put(input, downstreams);
        Queue queue=new LinkedList<String>();
        queue.addAll(downstreams);
        while (!queue.isEmpty()){
            String v = queue.poll().toString();
            Set<String> downstreams1 = (Set<String>)dag.getChildren(v);
            downstream_map.put(v, downstreams1);
            queue.addAll(downstreams1);
        }


        upstream_map.put(input, upstreams);
        Queue queue2=new LinkedList<String>();
        queue2.add(input);
        while (!queue.isEmpty()){
            String v = queue.poll().toString();
            Set<String> upstreams1 = (Set<String>)dag.getParent(v);
            upstream_map.put(v, upstreams1);
            queue2.addAll(upstreams1);
        }

        //生成血源数据
        JSONArray jsonArray=new JSONArray();

        //生成根节点
        JSONObject j2=new JSONObject();
        j2.put("id", DigestUtils.md5DigestAsHex(input.getBytes()));
        j2.put("topic", input);
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
                JSONObject j3=new JSONObject();
                j3.put("id", DigestUtils.md5DigestAsHex(chilren.getBytes()));
                j3.put("topic", chilren);
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
                JSONObject j1=new JSONObject();
                j1.put("id", DigestUtils.md5DigestAsHex(upstream.getBytes()) );
                j1.put("topic", upstream);
                j1.put("background-color", "#00bb00");
                j1.put("parentid", DigestUtils.md5DigestAsHex(key.getBytes()));
                j1.put("direction","left");
                jsonArray.add(j1);
            }
        }
//        for(String upstream:upstreams){
//            JSONObject j1=new JSONObject();
//            j1.put("id", DigestUtils.md5DigestAsHex(upstream.getBytes()) );
//            j1.put("topic", upstream);
//            j1.put("background-color", "#00bb00");
//            j1.put("parentid", DigestUtils.md5DigestAsHex(input.getBytes()));
//            j1.put("direction","left");
//            jsonArray.add(j1);
//        }

        //JSONObject jsonObject1=a(dag, input, "#C2DFFF");


        //System.out.println(jsonObject1.toJSONString());


        return jsonArray.toJSONString();
    }

    /**
     * 生成血源树,此方法暂时不用
     * @param dag
     * @param input
     * @param color
     * @return
     */
    private JSONObject blood_source_tree(DAG dag, String input, String color){
        JSONObject js=new JSONObject();
        js.put("id", DigestUtils.md5DigestAsHex(input.getBytes()));
        js.put("topic", input);
        js.put("background-color", color);
        js.put("children", new JSONArray());
        if(dag.getChildren(input).size()==0){
            return js;
        }
        Set<String> downstreams = (Set<String>)dag.getChildren(input);
        for (String downstream:downstreams){
            js.getJSONArray("children").add(blood_source_tree(dag, downstream, "#2B65EC"));
        }
        return js;
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
