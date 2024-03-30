package com.zyc.zdh.quartz;

import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleInstanceIdGenerator;
import org.quartz.spi.InstanceIdGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZdhInstanceIdGenerator implements InstanceIdGenerator {
    @Override
    public String generateInstanceId() throws SchedulerException {
        try {
            String myid=null;
            String instancename=null;
            String run_mode = System.getenv("ZDH_RUN_MODE");

            Resource app = new ClassPathResource("application-"+run_mode+".properties");
            InputStream is2 = app.getInputStream();
            InputStreamReader isr2 = new InputStreamReader(is2);
            BufferedReader br2 = new BufferedReader(isr2);
            String data2 = null;
            while((data2 = br2.readLine()) != null) {
                if(data2.startsWith("myid")){
                    myid= data2.split("=")[1].trim();
                }
                if(data2.startsWith("zdh.schedule.quartz.scheduler.instanceName")){
                    instancename= data2.split("=")[1].trim();
                }
            }
            br2.close();
            isr2.close();
            is2.close();
            return instancename+"_"+myid;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SimpleInstanceIdGenerator().generateInstanceId();
    }
}
