package com.zyc.zdh.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Logger logger= LoggerFactory.getLogger(DateUtil.class);

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
     * 天级处理
     * @param start
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
     * @param start
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
     * @param start
     * @param num
     * @return
     */
    public static Timestamp addMinute(String start,Integer num){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(Timestamp.valueOf(start).getTime());
        beginDate.add(Calendar.MINUTE, num);
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

    public static String getCurrentTime(){
        return df_time.format(new Timestamp(new Date().getTime()));
    }

    public static Date pase(String date,String format){
        try {
            return FastDateFormat.getInstance(format).parse(date);
        } catch (ParseException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(error, e.getCause());
            return null;
        }
    }

    public static String year(Date date) {
        return format(date).substring(0, 4);
    }

    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int day(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.DAY_OF_MONTH);
    }

    public static int hour(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.HOUR_OF_DAY);
    }

    public static int minute(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.MINUTE);
    }

    public static int second(Timestamp date){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTimeInMillis(date.getTime());
        return beginDate.get(Calendar.SECOND);
    }


}
