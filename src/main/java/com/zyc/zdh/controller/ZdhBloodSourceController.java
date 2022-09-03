package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.BloodSourceMapper;
import com.zyc.zdh.dao.EtlTaskJdbcMapper;
import com.zyc.zdh.entity.BloodSourceInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.CheckBloodSourceJob;
import com.zyc.zdh.util.DAG;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 血缘服务
 */
@Controller
public class ZdhBloodSourceController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BloodSourceMapper bloodSourceMapper;
    @Autowired
    EtlTaskJdbcMapper etlTaskJdbcMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

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
     * 生成血缘关系
     * @return
     */
    @RequestMapping(value = "/blood_source_create", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo blood_source_create() {
        CheckBloodSourceJob.Check();
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"生成血源关系", null);
    }

    /**
     * 模糊查询输入源
     * @param input 表名/文件名
     * @return
     */
    @RequestMapping(value = "/blood_source_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo blood_source_list(String input) {

        List<Map<String,Object>>  rs = jdbcTemplate.queryForList("select max(version) as version from blood_source_info");
        if(rs==null || rs.size()<1){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "未找到任何血源分析数据", "未找到任何血源分析数据");
        }

        String version = rs.get(0).getOrDefault("version", "0").toString();
        Example example=new Example(BloodSourceInfo.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.orLike("input",getLikeCondition(input));
        criteria.orLike("output",getLikeCondition(input));
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
                    jsonObject.put("input_md5", bsi.getInput_md5());
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
                    jsonObject1.put("input_md5", bsi.getOutput_md5());
                    jsonArray.add(jsonObject1);
                }
            }

        }
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", jsonArray);
    }

    /**
     * 数据血缘明细
     * @param input 表名/文件名
     * @param level 血源深度
     * @param stream_type 上游/下游  1：上游,2：下游, 3：全部
     * @return
     */
    @RequestMapping(value = "/blood_source_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String blood_source_detail(String input, String input_md5, String level, String stream_type) {
        DAG dag=new DAG();
        Map<String, String> source_json = new HashMap<>();
        JSONObject jsonObject=new JSONObject();
        List<BloodSourceInfo> bloodSourceInfos = bloodSourceMapper.selectAll();
        for (BloodSourceInfo bsi: bloodSourceInfos){
            if(!StringUtils.isEmpty(bsi.getInput()) && !StringUtils.isEmpty(bsi.getOutput()) && !bsi.getInput().equalsIgnoreCase(bsi.getOutput())){
                System.out.println(bsi.getInput()+"====="+bsi.getOutput());

                boolean result = dag.addEdge(bsi.getInput()+"__-__"+bsi.getInput_md5(),bsi.getOutput()+"__-__"+bsi.getOutput_md5());
                source_json.put(bsi.getInput()+"__-__"+bsi.getInput_md5(), bsi.getInput_json());
                source_json.put(bsi.getOutput()+"__-__"+bsi.getOutput_md5(), bsi.getOutput_json());
                if(!result){
                    jsonObject.put("result","失败");
                    return jsonObject.toJSONString();
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
        JSONArray jsonArray=new JSONArray();

        //生成根节点
        JSONObject j2=new JSONObject();
        j2.put("id", DigestUtils.md5DigestAsHex((input+"__-__"+input_md5).getBytes()));
        j2.put("topic", input);
        j2.put("source_json", JSON.parseObject(source_json.get(input+"__-__"+input_md5)));
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
                j3.put("topic", chilren.split("__-__")[0]);
                j3.put("source_json", JSON.parseObject(source_json.get(chilren)));
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
                j1.put("topic", upstream.split("__-__")[0]);
                j1.put("source_json", JSON.parseObject(source_json.get(upstream)));
                j1.put("background-color", "#00bb00");
                j1.put("parentid", DigestUtils.md5DigestAsHex(key.getBytes()));
                j1.put("direction","left");
                jsonArray.add(j1);
            }
        }

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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
