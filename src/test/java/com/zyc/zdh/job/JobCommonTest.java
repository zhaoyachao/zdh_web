package com.zyc.zdh.job;

import com.zyc.zdh.entity.TaskLogInstance;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class JobCommonTest {

    @Test
    public void resolveQuartzExpr() throws ParseException {

       for(Date d: JobCommon2.resolveQuartzExpr("100s")){
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           System.out.println(dateFormat.format(d));
       }


    }

    @Test
    public void sub_task_log_instance() throws ParseException {

        SnowflakeIdWorker.init(1,1);
        TaskLogInstance tli=new TaskLogInstance();
        tli.setId("001");
        tli.setJsmind_data("{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719629297702146048\",\"topic\":\"单分割符自带标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"749279343477264384\",\"topic\":\"mydb#account_info\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}");
        tli.setJsmind_data("{\"meta\":{\"name\":\"jsMind remote\",\"author\":\"\",\"version\":\"0.2\"},\"format\":\"node_array\",\"data\":[{\"id\":\"root\",\"topic\":\"root\",\"expanded\":true,\"isroot\":true},{\"id\":\"719619870378954752\",\"topic\":\"单分割符无标题\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728996795115376640\",\"topic\":\"单分割符无标题3\",\"expanded\":true,\"parentid\":\"719619870378954752\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"},{\"id\":\"728647407415332864\",\"topic\":\"单分割符无标题2\",\"expanded\":true,\"parentid\":\"root\",\"direction\":\"right\",\"foreground-color\":\"black\",\"more_task\":\"单源ETL\"}]}");

       //JobCommon.sub_task_log_instance(tli);


    }

}