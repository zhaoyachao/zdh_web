package com.zyc.zdh.datax_generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataxConfig {

    private Map<String,Object> core;

    private Job job;

    public Map<String, Object> getCore() {
        return core;
    }

    public void setCore(Map<String, Object> core) {
        this.core = core;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public static DataxConfig build(List<Content> content, Map<String,Object> setting,Map<String,Object> core){
        Job job=new Job();
        job.setContent(content);
        job.setSetting((Map<String,Object>)setting.getOrDefault("setting",new HashMap<>()));
        DataxConfig dataxConfig = new DataxConfig();
        dataxConfig.setJob(job);
        if(core.containsKey("core")){
            dataxConfig.setCore((Map<String,Object>)core.get("core"));
        }
        return dataxConfig;
    }
}

class Job{
    private Map<String,Object> setting;
    private List<Content> content;

    public Map<String, Object> getSetting() {
        return setting;
    }

    public void setSetting(Map<String, Object> setting) {
        this.setting = setting;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }
}