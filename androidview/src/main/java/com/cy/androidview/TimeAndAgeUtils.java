package com.cy.androidview;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lenovo on 2017/7/17.
 */

public class TimeAndAgeUtils {
    public static int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException(
                        "Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {

                age = age - 1;
            }
            if (now.get(Calendar.DAY_OF_YEAR) == born.get(Calendar.DAY_OF_YEAR)) {
                if (now.get(Calendar.DAY_OF_MONTH) < born
                        .get(Calendar.DAY_OF_MONTH)) {

                    age = age - 1;
                }
            }

        }
        return age;
    }

    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * Java将Unix时间戳转换成指定格式日期字符串
     *
     * @param ："1473048265";
     * @param formats        要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(Long timestamp, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        timestamp = timestamp * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取全部包括日期
     */
    public static String timeStamp2DateAll(String timestampString) {
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取年
     */
    public static String timeStamp2DateYear(String timestampString) {
        String formats = "yyyy";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取月
     */
    public static String timeStamp2DateMon(String timestampString) {
        String formats = "MM";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取日
     */
    public static String timeStamp2DateDay(String timestampString) {
        String formats = "dd";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取时间
     */
    public static String timeStamp2DateTime(String timestampString) {
        String formats = "HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*aaaaaaaqqwasdvnidheidnsidjeufninidkenidnhaonindhoa
    获取小时
     */
    public static String timeStamp2DateHour(String timestampString) {
        String formats = "HH";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取分钟
     */
    public static String timeStamp2DateMin(String timestampString) {
        String formats = "mm";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /*
    获取秒
     */
    public static String timeStamp2DateSec(String timestampString) {
        String formats = "ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

    /**
     * 两个时间戳相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：19534543
     * @param str2 时间参数 2 格式：235346547
     * @return String 返回值为：xx天xx小时xx分xx秒
     */

    public static String getDistanceTimeStamp(String str1, String str2) {
        str1 = timeStamp2DateAll(str1);
        str2 = timeStamp2DateAll(str2);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day + "天" + hour + ":" + min + ":" + sec;
    }

    /**
     * 距离当前系统时间戳相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：19534543
//     * @param str2 时间参数 2 格式：235346547
     * @return String 返回值为：xx天xx小时xx分xx秒
     */

    public static String getDistance2CurrentTimeStamp(String str1) {
        str1 = timeStamp2DateAll(str1);
        String str2 = timeStamp2DateAll(getCureentTimeStamp());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String string_hour = hour <= 9 ? ("0" + hour) : String.valueOf(hour);
        String string_min = min <= 9 ? ("0" + min) : String.valueOf(min);
        String string_sec = sec <= 9 ? ("0" + sec) : String.valueOf(sec);
        String string_day = "";
        if (day > 0) {

            string_day = day <= 9 ? ("0" + day) : String.valueOf(day);
            return string_day + "天" + string_hour + ":" + string_min + ":" + string_sec;

        } else {
            return string_hour + ":" + string_min + ":" + string_sec;

        }
    }
//    /**
//     * 距离当前系统时间戳相差距离小时多少分多少秒
//     *
//     * @param str1 时间参数 1 格式：19534543
//     * @param str2 时间参数 2 格式：235346547
//     * @return String 返回值为：xx天xx小时xx分xx秒
//     */
//
//    public static String getHMS2CurrentTimeStamp(String str1) {
//        str1 = timeStamp2DateAll(str1);
//        String str2 = timeStamp2DateAll(getCureentTimeStamp());
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date one;
//        Date two;
//        long day = 0;
//        long hour = 0;
//        long min = 0;
//        long sec = 0;
//        try {
//            one = df.parse(str1);
//            two = df.parse(str2);
//            long time1 = one.getTime();
//            long time2 = two.getTime();
//            long diff;
//            if (time1 < time2) {
//                diff = time2 - time1;
//            } else {
//                diff = time1 - time2;
//            }
//            day = diff / (24 * 60 * 60 * 1000);
//            hour = (diff / (60 * 60 * 1000) - day * 24);
//            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
//            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String string_hour = hour <= 9 ? ("0" + hour) : String.valueOf(hour);
//        String string_min = min <= 9 ? ("0" + min) : String.valueOf(min);
//        String string_sec = sec <= 9 ? ("0" + sec) : String.valueOf(sec);
//        String string_day = "";
//        if (day > 0) {
//
//            string_day = day <= 9 ? ("0" + day) : String.valueOf(day);
//            return string_day + "天" + string_hour + ":" + string_min + ":" + string_sec;
//
//        } else {
//            return string_hour + ":" + string_min + ":" + string_sec;
//
//        }
//    }

    /**
     * 距离当前系统时间戳相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：19534543
//     * @param str2 时间参数 2 格式：235346547
     * @return String 返回值为：xx天xx小时xx分xx秒
     */

    public static String[] getDistance2CurrentTimeStam(String str1) {
        str1 = timeStamp2DateAll(str1);
        String str2 = timeStamp2DateAll(getCureentTimeStamp());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String string_hour = hour <= 9 ? ("0" + hour) : String.valueOf(hour);
        String string_min = min <= 9 ? ("0" + min) : String.valueOf(min);
        String string_sec = sec <= 9 ? ("0" + sec) : String.valueOf(sec);
        String string_day = "";

        String[] str = {string_day, string_hour, string_min, string_sec};
        if (day > 0) {

            string_day = day <= 9 ? ("0" + day) : String.valueOf(day);
//            return string_day + "天" + string_hour + ":" + string_min + ":" + string_sec;


        } else {
//            return string_hour + ":" + string_min + ":" + string_sec;

        }

        return str;


    }

    public static String getDistanceTimeDate(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        String string_hour = hour <= 9 ? ("0" + hour) : String.valueOf(hour);
        String string_min = min <= 9 ? ("0" + min) : String.valueOf(min);
        String string_sec = sec <= 9 ? ("0" + sec) : String.valueOf(sec);
        String string_day = "";
        if (day > 0) {

            string_day = day <= 9 ? ("0" + day) : String.valueOf(day);
            return string_day + "天" + string_hour + ":" + string_min + ":" + string_sec;

        } else {
            return string_hour + ":" + string_min + ":" + string_sec;

        }
    }

    /*
        返回当前系统时间戳
     */
    public static String getCureentTimeStamp() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

        String str = String.valueOf(time);

        return str;

    }

    /*
        返回当前系统时间戳
     */
    public static Long getCureentTimeStampLong() {

        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳

        String str = String.valueOf(time);

        return Long.valueOf(str);

    }

    /*
     * 秒转化为天时分秒
     */
    public static String second2Time(Long s) {
        Integer ss = 1;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = s / dd;
        Long hour = (s - day * dd) / hh;
        Long minute = (s - day * dd - hour * hh) / mi;
        Long second = (s - day * dd - hour * hh - minute * mi) / ss;
//        Long milliSecond = s - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour <= 9 ? ("0" + hour + ":") : (hour + ":"));
        }
//        else {
//            sb.append("00:");
//        }
        if (minute > 0) {
            sb.append(minute <= 9 ? ("0" + minute + ":") : (minute + ":"));
        } else {
            sb.append("00:");
        }
        if (second > 0) {
            sb.append(second <= 9 ? ("0" + second) : second);
        } else {
            sb.append("00");
        }
//        if (milliSecond > 0) {
//            sb.append(milliSecond + "毫秒");
//        }
        return sb.toString();
    }

//    public static String gMT2Time(String strGMT) {
//        String time = "";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM ddHH:mm:ss 'GMT' yyyy", Locale.US);
//        try {
//            Date date = simpleDateFormat.parse(strGMT);
//            simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//            time = simpleDateFormat.format(date);
//        } catch (Exception e) {
//                LogUtils.log("Exception",e.getMessage());
//
//        }
//        return time;
//    }
}
