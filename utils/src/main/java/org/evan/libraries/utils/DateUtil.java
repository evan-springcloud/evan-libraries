package org.evan.libraries.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具
 *
 * @author shen.wei
 * @version Date: 2010-10-16 上午11:23:38
 */
public class DateUtil {

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static final String FORMAT_SHORT_STRING = "yyyy-MM-dd";

    public static final SimpleDateFormat FORMAT_SHORT = new SimpleDateFormat(FORMAT_SHORT_STRING);
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static final String FORMAT_LONG_STRING = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static final String FORMAT_FULL_STRING = "yyyy-MM-dd HH:mm:ss.S";

    public static final SimpleDateFormat FORMAT_FULL = new SimpleDateFormat(FORMAT_FULL_STRING);
    /**
     * 中文简写 如：2010年12月01日
     */
    public static final String FORMAT_SHORT_STRING_CN = "yyyy年MM月dd";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static final String FORMAT_LONG_STRING_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static final String FORMAT_FULL_STRING_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    public static final ConcurrentHashMap<String, DateFormat> formatCache = new ConcurrentHashMap<String, DateFormat>();

    /**
     * 格式化日期 格式：yyyy-MM-dd
     *
     * @param date String
     */
    public static String format(Date date) {
        return format(date, getDefaultDatePattern());
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     *                String
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = getDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     *                Date
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDefaultDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     *                Date
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = getDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            throw new DateParseException(strDate, pattern, e);
        }
    }


    /**
     * 根据预设格式返回当前日期 格式：yyyy-MM-dd
     * String
     */
    public static String getNowForString() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     * String
     */
    public static String getNowForString(String format) {
        return format(new Date(), format);
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     *             Date
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     *             Date
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加小时数
     *
     * @param date 日期
     * @param n    要增加的小时数
     *             Date
     */
    public static Date addHour(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, n);
        return cal.getTime();
    }

    /**
     * @param bigDate
     * @param date
     * @return
     */
    public static int diffHour(Date bigDate, Date date) {
        return (int) ((bigDate.getTime() - date.getTime()) / (1000 * 3600));
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     *             String
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 验证是否是按默认格式的时间格式
     * boolean
     */
    public static boolean valid(String str) {
        if (str != null && str.trim().length() > 0) {
            boolean convertSuccess = true;
            try {
                FORMAT_SHORT.setLenient(false);
                FORMAT_SHORT.parse(str);
            } catch (ParseException e) {
                convertSuccess = false;
            }
            return convertSuccess;
        }
        return false;
    }

    /**
     * 是否时间格式
     * boolean
     */
    public static boolean validate(String str, String format) {
        if (str != null && str.trim().length() > 0) {
            boolean convertSuccess = true;
            SimpleDateFormat df = getDateFormat(format);
            try {
                df.setLenient(false);
                df.parse(str);
            } catch (ParseException e) {
                convertSuccess = false;
            }
            return convertSuccess;
        }
        return false;
    }

    /**
     * 根据传入的月份获取月初和月末时间
     * create at 2014年8月25日 上午10:13:57
     */
    public static String getMaxMonthDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = getDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获得默认的 date pattern
     * String
     */
    private static String getDefaultDatePattern() {
        return FORMAT_SHORT_STRING;
    }

    private static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat df = (SimpleDateFormat) formatCache.get(pattern);
        if (df == null) {
            df = new SimpleDateFormat(pattern);
            formatCache.put(pattern, df);
        }
        return df;
    }
}
