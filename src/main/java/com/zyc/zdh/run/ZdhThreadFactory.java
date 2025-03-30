package com.zyc.zdh.run;

import java.util.concurrent.ThreadFactory;

/**
 * zdh自定义线程工厂
 */
public class ZdhThreadFactory implements ThreadFactory {

    private String namePrefix;

    private ThreadGroup threadGroup;

    public ZdhThreadFactory(String namePrefix){
        this.namePrefix = namePrefix;
    }

    public ZdhThreadFactory(String namePrefix, ThreadGroup threadGroup){
        this.namePrefix = namePrefix;
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread;
        if(threadGroup != null){
            thread=new Thread(threadGroup, r);
        }else{
            thread=new Thread(r);
        }
        thread.setName(namePrefix);
        return thread;
    }
}
