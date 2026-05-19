package com.zyc.zdh.hadoop;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharSet;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class HadoopUtilTest {

    @Test
    public void writeHdfs() throws Exception {
        FileInputStream file=new FileInputStream("C:\\Users\\Administrator\\Desktop\\新建 XLSX 工作表.xlsx");
        //FileInputStream file=new FileInputStream("C:\\Users\\Administrator\\Desktop\\a.txt");

        byte[] b = IOUtils.toByteArray(file);
        Dsi_Info dsi_info=new Dsi_Info();
        dsi_info.setUrl("hdfs://192.168.110.10:9001");
        dsi_info.setUser("zyc");
        HadoopUtil.writeHdfs(dsi_info,"/home/zyc/a.xlsx",b);
    }

    @Test
    public void readHdfs() throws Exception {
        Dsi_Info dsi_info=new Dsi_Info();
        dsi_info.setUrl("hdfs://192.168.110.10:9001");
        dsi_info.setUser("zyc");
        byte[] b = HadoopUtil.readHdfs(dsi_info,"/home/zyc/a.xlsx");

        FileCopyUtils.copy(b, new File("C:\\Users\\Administrator\\Desktop\\read.xlsx"));
    }

    @Test
    public void readTxtHdfs() throws Exception {
        Dsi_Info dsi_info=new Dsi_Info();
        dsi_info.setUrl("hdfs://192.168.110.10:9001");
        dsi_info.setUser("zyc");
        byte[] b = HadoopUtil.readHdfs(dsi_info,"/home/zyc/a.txt");

        FileCopyUtils.copy(b, new File("C:\\Users\\Administrator\\Desktop\\a1.txt"));
    }

}