package com.app.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WUJINGWEI on 2018/1/22.
 */

public class StringUtil {
    /**
     * 填充方向
     */
    public enum PADDING_ORIENTATION {
        LEFT, RIGHT
    }

    /**
     * 对原始字符串按填充方向、填充字符串及填充需达到的总长度生成新字符串
     *
     * @param originalStr 原始字符串
     * @param orientation 填充方向
     * @param padStr      填充字符串
     * @param totalLength 填充到的总长度
     * @return 填充后的完整字符串
     */
    public static String PadString(String originalStr, PADDING_ORIENTATION orientation, String padStr, int totalLength) {
        if (originalStr == null || padStr == null) return null;
        String generateStr = "";
        int count = totalLength - originalStr.length(); //需填充的次数
        for (int i = 0; i < count; i++) {
            generateStr += padStr;
        }
        switch (orientation) {
            case LEFT:
                generateStr = generateStr + originalStr;
                break;
            case RIGHT:
                generateStr = originalStr + generateStr;
                break;
        }
        return generateStr;
    }

    /**
     * 将传入的日期时间字符串按指定格式和偏移秒数生成新日期时间字符串
     *
     * @param datetime      原始日期时间字符串
     * @param format        转换格式
     * @param offsetSeconds 偏移秒数，往前推需为负数，往后推为正数
     * @return 新日期时间字符串
     */
    public static String getDateTimeStr(String datetime, String format, int offsetSeconds) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date nowDate = null;
        try {
            nowDate = df.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate = new Date(nowDate.getTime() + (long) offsetSeconds * 1000);
        return df.format(newDate);
    }

    /**
     * 获取日期时间字符串对应时间差值
     *
     * @param beginDateTime 起始日期时间字符串
     * @param endDateTime   结束日期时间字符串
     * @return 毫秒数时间差
     */
    public static long GetDateTimeStringDiff(String beginDateTime, String endDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date beginDT = null;
        try {
            beginDT = sdf.parse(beginDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDT = null;
        try {
            endDT = sdf.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (beginDT != null && endDT != null) {
            long diff = endDT.getTime() - beginDT.getTime();
            return diff;
        } else return Long.MIN_VALUE;
    }

    /**
     * 获取日期时间字符串对应时间差值
     *
     * @param beginDateTime 起始日期时间字符串
     * @param endDateTime   结束日期时间字符串
     * @param convertFormat 转换格式，如yyyy/MM/dd HH:mm:ss
     * @return 毫秒数时间差
     */
    public static long GetDateTimeStringDiff(String beginDateTime, String endDateTime, String convertFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(convertFormat);
        Date beginDT = null;
        try {
            beginDT = sdf.parse(beginDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDT = null;
        try {
            endDT = sdf.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (beginDT != null && endDT != null) {
            long diff = endDT.getTime() - beginDT.getTime();
            return diff;
        } else return Long.MIN_VALUE;
    }

    /**
     * 将字符串转成指定格式的日期时间字符串
     *
     * @param dateStr       原始字符串
     * @param dateFormatStr 原始字符串对应日期时间格式
     * @param formatStr     将要格式化成的日期时间格式
     * @return 格式化后的日期时间字符串
     */
    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);
        return s.format(date);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @param format 转换格式 如："yyyy/MM/dd HH:mm:ss"
     * @return 当前日期时间字符串
     */
    public static String GetCurrentDateTime(String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date(System.currentTimeMillis());
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 日期时间字符串转为日期对象
     *
     * @param dateStr 日期时间字符串
     * @param format  转换格式
     * @return 日期对象
     */
    public static Date parseDate(String dateStr, String format) {
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dt = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * 获取异常对象的堆栈追踪信息
     *
     * @param t 异常对象
     * @return 堆栈追踪信息
     */
    public static String getStackTrace(Throwable t) {
        if (t == null) return "";
        Writer wr = new StringWriter();
        PrintWriter pWriter = new PrintWriter(wr);
        t.printStackTrace(pWriter);
        return wr.toString();
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
