package com.zyc.zdh.job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class JobCommon2Test {

    @Test
    public void checkDep_hdfs() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.getLocal(conf);

        System.out.println(fs.exists(new Path("F://data/csv/h1.txt")));

    }

    @Test
    public void timestamp_check() throws IOException {

        System.out.println(Timestamp.valueOf("1970-01-01 00:00:00"));

    }
}