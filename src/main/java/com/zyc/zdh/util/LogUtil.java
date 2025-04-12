package com.zyc.zdh.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    private static CallerInfo getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 5) {
            StackTraceElement caller = stackTrace[4];
            return new CallerInfo(caller.getClassName(), caller.getMethodName(), caller.getLineNumber());
        }
        return new CallerInfo(LogUtil.class.getName(), "unknown", -1);
    }

    public static String getPre(Class cls){
        String before="unknown";
        try{
            CallerInfo callerInfo = getCallerInfo();

            if(callerInfo.getClassName().equalsIgnoreCase(cls.getName())){
                before = callerInfo.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return before;
    }

    public static void error(Class cls, String message,Object... args){
        String formattedStr = StrUtil.format(message, args);
        String error =  "{} error: {}";
        Logger log = LoggerFactory.getLogger(cls);
        log.error(error,getPre(cls),formattedStr);
    }

    public static void error(Class cls, String message, Exception e,Object... args){
        String formattedStr = StrUtil.format(message, args);
        String error ="{} error: {}";
        Logger log = LoggerFactory.getLogger(cls);
        log.error(error,getPre(cls),formattedStr, e);
    }

    public static void error(Class cls, Exception e){
        String error = "{} error: ";
        Logger log = LoggerFactory.getLogger(cls);
        log.error(error, getPre(cls), e);
    }

    public static void error(Class cls, Throwable e){
        String error = "{} error: ";
        Logger log = LoggerFactory.getLogger(cls);
        log.error(error,getPre(cls), e);
    }

    public static void info(Class cls, String message, Object... args){
        String formattedStr = StrUtil.format(message, args);
        String info = "{} info: {}";
        Logger log = LoggerFactory.getLogger(cls);
        log.info(info,getPre(cls), formattedStr);
    }

    public static void warn(Class cls, String message, Object... args){
        String formattedStr = StrUtil.format(message, args);
        String info = "{} warn: {}";
        Logger log = LoggerFactory.getLogger(cls);
        log.warn(info,getPre(cls), formattedStr);
    }

    public static void debug(Class cls, String message, Object... args){
        String formattedStr = StrUtil.format(message, args);
        String info = "{} debug: {}";
        Logger log = LoggerFactory.getLogger(cls);
        log.debug(info,getPre(cls), formattedStr);
    }

    public static class CallerInfo{
        private String className;
        private String methodName;
        private int lineNumber;

        public CallerInfo(){

        }

        public CallerInfo(String className, String methodName, int lineNumber){
            this.className = className;
            this.methodName = methodName;
            this.lineNumber = lineNumber;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString(){
            return this.className+"."+this.methodName+"["+lineNumber+"]";
        }
    }

}
