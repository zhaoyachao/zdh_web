package com.zyc.zdh.util;


import com.zyc.zdh.job.SnowflakeIdWorker;
import org.springframework.util.DigestUtils;

public class MD5 {
    public static void main(String[] args) {
        SnowflakeIdWorker.init(1, 2);
        System.out.println("ak: "+DigestUtils.md5DigestAsHex((SnowflakeIdWorker.getInstance().nextId()+"").getBytes()));
        System.out.println("sk: "+DigestUtils.md5DigestAsHex((SnowflakeIdWorker.getInstance().nextId()+"").getBytes()));

    }
}
