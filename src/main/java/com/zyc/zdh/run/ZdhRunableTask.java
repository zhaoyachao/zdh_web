package com.zyc.zdh.run;

/**
 * 统一线程任务,所有线程均通过此实现包装
 * 自定义线程名称实现
 */
public class ZdhRunableTask implements Runnable{

    private String name;

    private Runnable r;

    private boolean isOverWriteOldName=false;

    public ZdhRunableTask(String name, Runnable r){
        this.name = name;
        this.r = r;
    }

    public ZdhRunableTask(String name, boolean isOverWriteOldName, Runnable r){
        this.name = name;
        this.isOverWriteOldName = isOverWriteOldName;
        this.r = r;
    }

    public String name(){
        return name;
    }

    @Override
    public void run() {
        if(isOverWriteOldName){
            Thread.currentThread().setName(name()+Thread.currentThread().getId());
        }else{
            Thread.currentThread().setName(Thread.currentThread().getName()+name()+Thread.currentThread().getId());
        }
        r.run();
    }


    public static ZdhRunableTask quickBuild(String name, Runnable r){
        return new ZdhRunableTask(name, r);
    }
}
