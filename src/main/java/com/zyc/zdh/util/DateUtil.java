package com.zyc.zdh.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final FastDateFormat df
            = FastDateFormat.getInstance("yyyy-MM-dd");
    public static final FastDateFormat df_nodash
            = FastDateFormat.getInstance("yyyyMMdd");
    public static final FastDateFormat df_time
            = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");


    /** *
     *
     * @example add("20180101",90),add("20180101",-90)
     * @param start  起始日期
     * @param dayNum 天数
     * @return
     */
    public static Date add(Date start,int dayNum){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(start);
        beginDate.add(Calendar.DAY_OF_MONTH, dayNum);
        return beginDate.getTime();
    }

    public static Timestamp add(Timestamp start, int dateType,int num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(start.getTime());
        beginDate.add(dateType, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     *
     * @param start
     * @param format
     * @param dateType
     * @param num
     * @return
     */
    public static Timestamp add(String start, String format,int dateType,int num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(pase(start, format).getTime());
        beginDate.add(dateType, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 天级处理
     * @param start yyyy-MM-dd HH:mm:ss 格式
     * @param num
     * @return
     */
    public static Timestamp addDay(String start,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(Timestamp.valueOf(start).getTime());
        beginDate.add(Calendar.DATE, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 小时级处理
     * @param start yyyy-MM-dd HH:mm:ss 格式
     * @param num
     * @return
     */
    public static Timestamp addHour(String start,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(Timestamp.valueOf(start).getTime());
        beginDate.add(Calendar.HOUR_OF_DAY, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 分钟级处理
     * @param start yyyy-MM-dd HH:mm:ss 格式
     * @param num
     * @return
     */
    public static Timestamp addMinute(String start,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(Timestamp.valueOf(start).getTime());
        beginDate.add(Calendar.MINUTE, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 秒级处理
     * @param start
     * @param num
     * @return
     */
    public static Timestamp addSecond(String start,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(Timestamp.valueOf(start).getTime());
        beginDate.add(Calendar.SECOND, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 天级处理
     * @param start
     * @param format
     * @param num
     * @return
     */
    public static Timestamp addDay(String start, String format,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(pase(start, format).getTime());
        beginDate.add(Calendar.DATE, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 小时级处理
     * @param start
     * @param format
     * @param num
     * @return
     */
    public static Timestamp addHour(String start, String format,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(pase(start, format).getTime());
        beginDate.add(Calendar.HOUR_OF_DAY, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 分钟级处理
     * @param start
     * @param format
     * @param num
     * @return
     */
    public static Timestamp addMinute(String start, String format,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(pase(start, format).getTime());
        beginDate.add(Calendar.MINUTE, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    /**
     * 秒级级处理
     * @param start
     * @param format
     * @param num
     * @return
     */
    public static Timestamp addSecond(String start, String format,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(pase(start, format).getTime());
        beginDate.add(Calendar.SECOND, num);
        return new Timestamp(beginDate.getTimeInMillis());
    }

    public static Date pase(String date) throws Exception{
        return df.parse(date);
    }


    public static String  format(Date date){
       return df.format(date);
    }

    public static String  format(Timestamp date){
        return df.format(date);
    }

    public static String  formatNodash(Date date){
        return df_nodash.format(date);
    }

    public static String  formatNodash(Timestamp date){
        return df_nodash.format(date);
    }

    public static String  formatTime(Timestamp dt){
        return df_time.format(dt);
    }

    public static String  format(Date date, String format){
        return FastDateFormat.getInstance(format).format(date);
    }

    public static String getCurrentTime(){
        return df_time.format(new Timestamp(System.currentTimeMillis()));
    }

    public static Date pase(String date,String format){
        try {
            return FastDateFormat.getInstance(format).parse(date);
        } catch (ParseException e) {
            LogUtil.error(DateUtil.class, e);
            return null;
        }
    }

    /**
     * 返回yyyyy格式
     * @param date
     * @return
     */
    public static String year(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 返回月份,1,2,3,..,12
     * @param date
     * @return
     */
    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 返回月份01,02,03,...,12
     * @param date
     * @return
     */
    public static String monthx(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        return month>=10?String.valueOf(month):"0"+month;
    }

    /**
     * 返回1,2,3,...,30,31
     * @param date
     * @return
     */
    public static int day(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 返回01,02,03,...,30,31
     * @param date
     * @return
     */
    public static String dayx(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        return day>=10?String.valueOf(day):"0"+day;
    }

    /**
     * 返回0,1,2...,21,22,23
     * @param date
     * @return
     */
    public static int hour(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 返回00,,01,02,...,21,22,23
     * @param date
     * @return
     */
    public static String hourx(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        int hour = beginDate.get(Calendar.HOUR_OF_DAY);
        return hour>=10?String.valueOf(hour):"0"+hour;
    }

    /**
     * 返回0,1,2...,58,59
     * @param date
     * @return
     */
    public static int minute(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.MINUTE);
    }

    /**
     * 返回0,1,2...,58,59
     * @param date
     * @return
     */
    public static String minutex(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        int minute = beginDate.get(Calendar.MINUTE);
        return minute>=10?String.valueOf(minute):"0"+minute;
    }

    /**
     * 返回0,1,2,...,58,59
     * @param date
     * @return
     */
    public static int second(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.SECOND);
    }

    /**
     * 返回00,01,02,...,58,59
     * @param date
     * @return
     */
    public static String secondx(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        int second = beginDate.get(Calendar.SECOND);
        return second>=10?String.valueOf(second):"0"+second;
    }

}
