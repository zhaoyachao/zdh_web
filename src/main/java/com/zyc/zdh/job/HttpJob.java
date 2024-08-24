package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hubspot.jinjava.Jinjava;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.GroovyFactory;
import com.zyc.zdh.util.HttpUtil;
import org.apache.commons.httpclient.URI;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpJob extends JobCommon2 {

    public static String jobType = "HTTP";


    /**
     * 当前http实现只支持同步类型的http
     * @param tli
     * @return
     */
    public static Boolean httpCommand(TaskLogInstance tli) {
        Boolean exe_status = true;
        //执行命令
        try {
            logger.info("http任务当前只支持同步http,异步http暂不支持");
            insertLog(tli,"info","http任务当前只支持同步http,异步http暂不支持");
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            String run_jsmind = tli.getRun_jsmind_data();
            String url = JSON.parseObject(run_jsmind).getString("url");
            String url_type = JSON.parseObject(run_jsmind).getString("url_type");
            String params = JSON.parseObject(run_jsmind).getString("params");
            params = jj.render(params, jinJavaParam);
            String header = JSON.parseObject(run_jsmind).getString("header");
            String cookie = JSON.parseObject(run_jsmind).getString("cookie");
            String proxy_url = JSON.parseObject(run_jsmind).getString("proxy_url");
            String res_expr = JSON.parseObject(run_jsmind).getString("res_expr");

            if(StringUtils.isEmpty(url_type)){
                throw new Exception("http任务请求类型为空");
            }
            if(!StringUtils.isEmpty(params)){
                try{
                    JSONObject jsonObject = JSON.parseObject(params);
                }catch (Exception e){
                    throw new Exception("请求参数必须是json格式");
                }
            }
            Map<String,String> header_map=new HashMap<>();
            if(!StringUtils.isEmpty(header)){
                try{
                    String[] headers = header.split("\\r?\\n");
                    for (String h:headers){
                        if(h.contains(":")){
                            header_map.put(h.split(":",1)[0], h.split(":")[1]);
                        }
                    }
                }catch (Exception e){
                    throw new Exception("header参数必须是kv格式");
                }
            }
            Map<String,String> cookie_map=new HashMap<>();
            if(!StringUtils.isEmpty(cookie)){
                try{
                    String[] cookies = cookie.split("\\r?\\n");
                    for (String c:cookies){
                        if(c.contains(":")){
                            cookie_map.put(c.split(":",1)[0], c.split(":",1)[1]);
                        }
                    }
                }catch (Exception e){
                    throw new Exception("cookie参数必须是kv格式");
                }
            }

            HttpHost httpHost = null;
            if(!StringUtils.isEmpty(proxy_url)){
                httpHost = HttpHost.create(proxy_url);
            }

            String result = null;
            //post请求
            if(url_type.equalsIgnoreCase(Const.HTTP_POST)){
                logger.info("[" + jobType + "] JOB ,开始执行[post]请求");
                insertLog(tli, "info", "[" + jobType + "] JOB ,开始执行[post]请求,请求地址: "+url+" ,参数: "+params);
                //校验是否有参数
                result = HttpUtil.postJSON(url, params, header_map, cookie_map, httpHost);
                insertLog(tli, "info", "[" + jobType + "] JOB ,请求结果: "+result);
            }
            if(url_type.equalsIgnoreCase(Const.HTTP_GET)){
                logger.info("[" + jobType + "] JOB ,开始执行[get]请求");
                insertLog(tli, "info", "[" + jobType + "] JOB ,开始执行[get]请求");
                List<NameValuePair> npl=new ArrayList<>();
                if(!StringUtils.isEmpty(params)){
                    JSONObject jsonObject = JSON.parseObject(params);
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if(value==null){
                            value = "";
                        }
                        npl.add(new BasicNameValuePair(key,value.toString()));
                    }
                }
                result = HttpUtil.getRequest(url, npl, header_map, cookie_map, httpHost);
                insertLog(tli, "info", "[" + jobType + "] JOB ,请求结果: "+result);
            }

            if(!StringUtils.isEmpty(res_expr)){
                insertLog(tli, "info", "[" + jobType + "] JOB ,校验结果表达式: "+res_expr);
                Map<String, Object> res_params = new HashMap<>();
                res_params.put("res", result);
                if(res_expr.contains("res.")){
                    res_params.put("res", JSONObject.parseObject(result));
                }
                Object ret = GroovyFactory.execExpress(res_expr, res_params);
                if(ret == null || StringUtils.isEmpty(ret.toString())){
                    throw new Exception("解析结果为空,请检查解析表达式是否正确,或者检查返回结果是否正常");
                }

                if(!ret.toString().equalsIgnoreCase("true")){
                    throw new Exception("解析结果不符合预期");
                }
            }
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            logger.error(e.getMessage());
            insertLog(tli, "error","[" + jobType + "] JOB ,"+ e.getMessage());
            jobFail(jobType,tli);
            exe_status = false;
        }
        return exe_status;
    }


    public static String run(String[] command) throws IOException {
        Scanner input = null;
        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result += input.nextLine() + "\n";
            }
            //加上命令本身，打印出来
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }


}
