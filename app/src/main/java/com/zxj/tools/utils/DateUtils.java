package com.zxj.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by zxj on 2016/6/1.
 */
public class DateUtils {

    /**
     * 得到格式化时间
     *将秒转换为分钟秒
     * @param timeInSeconds
     * @return
     */
    public static String getFormatTimeMsg(int timeInSeconds) {
        int hours, minutes, seconds;
        hours = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hours * 3600);
        minutes = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minutes * 60);
        seconds = timeInSeconds;
        String minStr = String.valueOf(minutes);
        String secStr = String.valueOf(seconds);

        if (minStr.length() == 1)
            minStr = "0" + minStr;
        if (secStr.length() == 1)
            secStr = "0" + secStr;

        return (minStr + "分" + secStr + "秒");
    }


    /**
     * 返回指定的日期
     */
    public static String getSpecificDate(String date, int flag) {
        int sperator;
        if (StringUtils.isEmpty(date)) {
            return "";
        } else {
            if (0 == flag) {// 年
                String year = date.substring(0, 4);
                // System.out.println("年是:" + year);
                return year;
            } else if (1 == flag) {// 月
                sperator = date.indexOf("-");
                String month = date.substring(sperator + 1, sperator + 3);
                // System.out.println("月是:" + month);
                return month;
            } else if (2 == flag) {// 日
                sperator = date.lastIndexOf("-");
                String day = date.substring(sperator + 1, sperator + 3);
                // System.out.println("日是:" + day);
                return day;
            } else if (3 == flag) {// 时
                sperator = date.indexOf("T");
                String hour = date.substring(sperator + 1, sperator + 3);
                // System.out.println("时是:" + hour);
                return hour;
            } else if (4 == flag) {// 分
                sperator = date.indexOf(":");
                String minute = date.substring(sperator + 1, sperator + 3);
                // System.out.println("分是:" + minute);
                return minute;
            } else if (5 == flag) {// 秒
                sperator = date.lastIndexOf(":");
                String second = date.substring(sperator + 1, sperator + 3);
                // System.out.println("秒是:" + second);
                return second;
            } else {
                return date.replace('T', ' ');
            }
        }
    }

    /**
     * 根据年和月算出该月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthOfDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, 1);
        long time = c.getTimeInMillis();

        c.set(Calendar.MONTH, (month + 1));
        long nexttime = c.getTimeInMillis();

        long cha = nexttime - time;
        int s = (int) (cha / (24 * 60 * 60 * 1000));

        return s;
    }

    /**
     * 获取后天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getDayAfterTomorrow() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 2);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }
    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 将日期字符转成时间戳
     *
     * @param time
     * @return
     */

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }
    /**
     * 判断时间是否属于本周
     *
     * @return
     */
    public static int getWeekOfDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        sdf.format(cal.getTime());
        String fileTime = sdf.format(cal.getTime());
        cal.setTime(new Date());
        String currentTime = sdf.format(cal.getTime());
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        int result = fileTime.compareTo(currentTime);
        if (result != 0) {
            result = fileTime.compareTo(imptimeBegin);
            if (result == 0) {
                result = 2;
            }
        }
        return result;
    }

    /**
     * 获取明天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getTomorrow(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(myDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(d.getTime());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }
    /**
     * 获取昨天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getYesterday(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(myDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(d.getTime());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String yesterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return yesterDay;
    }

    /**
     * 获取前天时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getBeforeYesterday() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 2);

        String beforeYesterday = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return beforeYesterday;
    }


    /**
     * 获取上周下周时间
     * @param i
     * @return
     */
    public static Date getLastWeek(int i){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, i);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取上月下月的
     * @param i
     * @return
     * @throws ParseException
     */
    public static LinkedList<String> plusminusMonth(int i) throws ParseException {
        LinkedList<String> dataList = new LinkedList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, i);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        Date startDate = cal_1.getTime();
//        String  firstDay = format.format(cal_1.getTime());
//        System.out.println("-----1------firstDay:"+firstDay);
        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, i);
        cale.set(Calendar.DAY_OF_MONTH, cale.getActualMaximum(Calendar.DAY_OF_MONTH));
//        String  lastDay = format.format(cale.getTime());
//        System.out.println("-----2------lastDay:"+lastDay);
        Date endDate = cale.getTime();
        while (!startDate.after(endDate)) {
//            System.out.println("date:"+format.format(startDate));
            dataList.add(format.format(startDate));
            startDate = getNext(startDate);
        }
        return dataList;
    }
    private static Date getNext(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

}
