package com.zyc.zdh.job;

import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * ClassJob demo
 * 这只是个demo
 */
public class ClassJob extends JobCommon2{

    private static String jobType = "CLASS";

    public static void run(QuartzJobInfo quartzJobInfo) {

        String softPath="/home/zyc/a.jar";
        URLClassLoader classLoader = null;
        try {
            classLoader = new URLClassLoader(new URL[]{new URL(softPath)},Thread.currentThread().getContextClassLoader());
            Class demo = classLoader.loadClass("com.amx.test.Test");
            Object object = demo.newInstance();
            System.out.println(demo.getMethod("invoke",String.class).invoke(object,new Object[]{"amx"}));
        } catch (MalformedURLException e) {
            LogUtil.error(ClassJob.class, e);
        } catch (InstantiationException e) {
            LogUtil.error(ClassJob.class, e);
        } catch (InvocationTargetException e) {
            LogUtil.error(ClassJob.class, e);
        } catch (NoSuchMethodException e) {
            LogUtil.error(ClassJob.class, e);
        } catch (IllegalAccessException e) {
            LogUtil.error(ClassJob.class, e);
        } catch (ClassNotFoundException e) {
            LogUtil.error(ClassJob.class, e);
        }


    }
}
