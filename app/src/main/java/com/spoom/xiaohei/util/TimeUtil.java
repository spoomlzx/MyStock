package com.spoom.xiaohei.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * package com.lan.ichat.util
 *
 * @author spoomlan
 * @date 04/02/2018
 */
public class TimeUtil {
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;
    private static final long DAY_MILLIS = 86400000L;
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimeString(long time) {
        String timeString;
        long now = new Date().getTime();
        long differ = now - time;
        if (differ >= 0L && isYesterday(time)) {
            timeString = "昨天 " + getDayTimeString(time);
            return timeString;
        }
        if (differ < 0L) {
            differ = -differ;
        }
        if (differ < DAY_MILLIS) {
            timeString = getDayTimeString(time);
        } else if (differ < DAY_MILLIS * 7) {
            timeString = getWeekString(time) + (new SimpleDateFormat("h:mm", Locale.CHINESE)).format(time);
        } else {
            timeString = (new SimpleDateFormat("yyyy年M月dd日 ", Locale.CHINESE)).format(time) + getDayTimeString(time);
        }
        return timeString;
    }

    private static final String[] CHINESE_WEEK =
            {"周日 ", "周一 ", "周二 ", "周三 ", "周四 ", "周五 ", "周六 "};

    public static String getWeekString(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return CHINESE_WEEK[week - 1];
    }

    public static String getDayTimeString(long time) {
        String timeString = (new SimpleDateFormat("h:mm", Locale.CHINESE)).format(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 6) {
            timeString = "凌晨" + timeString;
        } else if (hour < 12) {
            timeString = "早上" + timeString;
        } else if (hour < 14) {
            timeString = "中午" + timeString;
        } else if (hour < 18) {
            timeString = "下午" + timeString;
        } else {
            timeString = "晚上" + timeString;
        }
        return timeString;
    }

    public static boolean isCloseEnough(long time1, long time2) {
        long timeDiffer = time1 - time2;
        if (timeDiffer < 0L) {
            timeDiffer = -timeDiffer;
        }
        return timeDiffer < INTERVAL_IN_MILLISECONDS;
    }

    /**
     * 判断是否今天
     *
     * @param millis 毫秒时间戳
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isToday(final long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + DAY_MILLIS;
    }

    public static boolean isYesterday(final long millis) {
        long wee = getWeeOfYesterday();
        return millis >= wee && millis < wee + DAY_MILLIS;
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param date Date 类型时间
     * @return 1...54
     */
    public static int getWeekOfYear(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private static final String[] CHINESE_ZODIAC =
            {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    /**
     * 获取生肖
     *
     * @param date Date 类型时间
     * @return 生肖
     */
    public static String getChineseZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 获取生肖
     *
     * @param millis 毫秒时间戳
     * @return 生肖
     */
    public static String getChineseZodiac(final long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    /**
     * 获取生肖
     *
     * @param year 年
     * @return 生肖
     */
    public static String getChineseZodiac(final int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    /**
     * 将时间字符串转为 Date 类型
     * <p>time 格式为 format</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date 类型
     */
    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将时间戳转为 Date 类型
     *
     * @param millis 毫秒时间戳
     * @return Date 类型时间
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final String[] ZODIAC = {
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座",
            "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"
    };

    /**
     * 获取星座
     *
     * @param date Date 类型时间
     * @return 星座
     */
    public static String getZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getZodiac(month, day);
    }

    /**
     * 获取星座
     *
     * @param millis 毫秒时间戳
     * @return 星座
     */
    public static String getZodiac(final long millis) {
        return getZodiac(millis2Date(millis));
    }

    /**
     * 获取星座
     *
     * @param month 月
     * @param day   日
     * @return 星座
     */
    public static String getZodiac(final int month, final int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]
                ? month - 1
                : (month + 10) % 12];
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static long getWeeOfYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
