package com.zyc.zdh.job;

import com.zyc.zdh.entity.QuartzJobInfo;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassJob extends JobCommon{

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
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
