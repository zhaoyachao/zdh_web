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
import java.net.InetAddress;

public class ZdhInstanceIdGenerator implements InstanceIdGenerator {
    @Override
    public String generateInstanceId() throws SchedulerException {
        try {
            String myid=null;
            String instancename=null;
           // SystemCommandLineRunner systemCommandLineRunner = (SystemCommandLineRunner) SpringContext.getBean("systemCommandLineRunner");
            Resource resource = new ClassPathResource("application.properties");
            InputStream is = resource.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String data = null;
            while((data = br.readLine()) != null) {
               if(data.startsWith("spring.profiles.active")){
                   Resource app = new ClassPathResource("application-"+data.split("=")[1]+".properties");
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
                   break;
               }
            }
            br.close();
            isr.close();
            is.close();


            return instancename+"_"+myid;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SimpleInstanceIdGenerator().generateInstanceId();
    }
}
