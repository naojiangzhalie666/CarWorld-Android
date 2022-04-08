package com.liansheng.carworld.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author xincheng
 */
public class DateParserHelper {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat simpleDateFormatOfYearAndMonth = new SimpleDateFormat(
            "yyyy-MM", Locale.getDefault());
    private static final SimpleDateFormat simpleDateFormatOfDay = new SimpleDateFormat(
            "dd", Locale.getDefault());
    private static final String DAYS[] = {"周日", "周一", "周二", "周三", "周四", "周五",
            "周六"};

    private static final SimpleDateFormat numberDateFormat = new SimpleDateFormat(
            "yyyyMMdd", Locale.getDefault());
    private static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat simpleTimeFormat2 = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat simpleTimeFormat3 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat TFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    /**
     * 平年月天数数组
     */
    private static int commonYearMonthDay[] = {31, 28, 31, 30, 31, 30, 31, 31,
            30, 31, 30, 31};
    /**
     * 闰年月天数数组
     */
    private static int leapYearMonthDay[] = {31, 29, 31, 30, 31, 30, 31, 31,
            30, 31, 30, 31};

    private static final SimpleDateFormat simpleMonthAndDayFormat = new SimpleDateFormat(
            "MM月 dd日", Locale.getDefault());
    private static final SimpleDateFormat simpleYearAndMonthAndDayFormat = new SimpleDateFormat(
            "yyyy年MM月dd日", Locale.getDefault());
    private static final SimpleDateFormat simpleHHAndMMFormat = new SimpleDateFormat(
            "HH:mm", Locale.getDefault());
    private static final SimpleDateFormat simpleMM_dd_HH_mm_Format = new SimpleDateFormat(
            "MM-dd HH:mm", Locale.getDefault());
    private static final SimpleDateFormat simpleMonthAndDayAndTimeFormat = new SimpleDateFormat(
            "MM/dd", Locale.getDefault());
    private static final SimpleDateFormat simpleMonthAndDayAndTimeFormat2 = new SimpleDateFormat(
            "MM月dd日", Locale.getDefault());
    private static final SimpleDateFormat simpleYearMonthAndDayAndTimeFormat = new SimpleDateFormat(
            "yyyy/MM/dd", Locale.getDefault());

    public static String getMonthAndDayAndTime(String dateText) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleMonthAndDayAndTimeFormat.format(date) + "\n";

    }

    public static long dateToMilliseconds(String dateText) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();

    }

    public static boolean isOut7Days(long time) {
        time = time + 7 * 24 * 3600 * 1000;
        Date date = new Date();
        long curTime = date.getTime();
        return curTime >= time;
    }

    public static long getNewUpdataTime(long time) {
        Date date = new Date();
        long curTime = date.getTime();
        while ((time + 7 * 24 * 3600 * 1000) < curTime) {
            time = time + 7 * 24 * 3600 * 1000;
        }
        return time;
    }


    public static String getMonthAndDayAndTime2(long dateText) {
        Date date = new Date(dateText);
        return simpleMonthAndDayAndTimeFormat2.format(date);

    }

    public static String getYearMonthAndDayAndTime(String dateText) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleYearMonthAndDayAndTimeFormat.format(date);
    }

    public static String getYearMonthAndDay(String dateText) {
        if (TextUtils.isEmpty(dateText)) {
            return "";
        }
        Date date = null;
        try {
            date = TFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleYearMonthAndDayAndTimeFormat.format(date);
    }

    public static String getYearAndMonthAndDayAndTime(String dateText) {
        if (TextUtils.isEmpty(dateText)) {
            return "";
        }
        Date date = null;
        try {
            date = TFormat.parse(dateText);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleTimeFormat2.format(date);
    }

    public static String getYearAndMonthAndDayAndTimeAndMin(String dateText) {
        Date date = null;
        try {
            date = TFormat.parse(dateText);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleTimeFormat.format(date);
    }

    public static String getYearAndMonthAndDay(String dateText) {
        Date date = null;
        try {
            date = TFormat.parse(dateText);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleYearAndMonthAndDayFormat.format(date);
    }

    public static String getYearAndMonthAndDay2(String dateText) {
        if (TextUtils.isEmpty(dateText)) {
            return "";
        }
        Date date = null;
        try {
            date = TFormat.parse(dateText);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    //    public static int getDay(String date1,String date2) {
//        Date date = null;
//        Date date0 = null;
//        try {
//            date = TFormat.parse(date1);
//            date0 = TFormat.parse(date2);
//
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    public static int daysBetween(String bdate) throws ParseException {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String getTime(String dateText) {
        Date date = null;
        try {
            date = simpleTimeFormat2.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleHHAndMMFormat.format(date);

    }

    public static String getMMddHHmmFormat(long milliseconds) {
        Date date = new Date(milliseconds);
        return simpleMM_dd_HH_mm_Format.format(date);
    }

    public static String getConversationTime(long sTime) {
        Date date = new Date();
        Date sDate = new Date(sTime);
        long curTime = date.getTime();
        long l = 24 * 3600 * 1000;
        long td = curTime / l - sTime / l;
        if (td < 1) {
            return simpleHHAndMMFormat.format(sDate);
        } else if (td >= 1 && td < 2) {
            return "昨天" + simpleHHAndMMFormat.format(sDate);
        } else if (td >= 2 && td < 3) {
            return "前天" + simpleHHAndMMFormat.format(sDate);
        } else if (td > 3) {
            return simpleMonthAndDayFormat.format(sDate);
        }
        return simpleMonthAndDayFormat.format(sDate);
    }

    public static int getDayPositionOfWeek(String dateText) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            return 6;
        } else {
            return day - 2;
        }
    }

    /**
     * 通过年月，获取这个月一共有多少天
     */
    public static int getDays(int year, int month) {
        int days = 0;

        if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
            if (month > 0 && month <= 12) {
                days = leapYearMonthDay[month - 1];
            }
        } else {
            if (month > 0 && month <= 12) {
                days = commonYearMonthDay[month - 1];
            }
        }
        return days;
    }

    public static String getMonthOfYear() {
        Date date = new Date();
        return simpleDateFormatOfYearAndMonth.format(date);
    }

    public static String getnumberDateFormat(String strDate) {
        Date date = null;
        try {
            date = numberDateFormat.parse(strDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }


    public static String getYearAndMonthFormat(long milliseconds) {
        Date date = new Date(milliseconds);
        return simpleDateFormatOfYearAndMonth.format(date);
    }

    public static String getYearAndMonthHMFormat(long milliseconds) {
        Date date = new Date(milliseconds * 1000L);
        return simpleTimeFormat3.format(date);
    }

    public static String getMonthDDFormat(long milliseconds) {
        Date date = new Date(milliseconds * 1000L);
        return simpleMonthAndDayFormat.format(date);
    }

    public static String getDayFormat(long milliseconds) {
        Date date = new Date(milliseconds);
        return simpleDateFormatOfDay.format(date);
    }

    public static String getDateFormat(long milliseconds) {
        Date date = new Date(milliseconds);
        return simpleDateFormat.format(date);
    }

    public static String getTimeFormat(long milliseconds) {
        Date date = new Date(milliseconds);
        return simpleTimeFormat.format(date);
    }

    public static String getDateFormat() {
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String getDayofWeek() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        return DAYS[day - 1];
    }

    public static String getDayOfWeek(long milliseconds) {
        Date date = new Date(milliseconds);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        return DAYS[day - 1];
    }

    public static String getDayofWeek(String dateText) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateText);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);
        return DAYS[day - 1];
    }

    public static int getDays(String fromDate, String toDate) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(fromDate);
            date2 = simpleDateFormat.parse(toDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal2.getTimeInMillis() - cal2.getTimeInMillis() >= 0) {
            int dayCount = (int) ((cal2.getTimeInMillis() - cal1
                    .getTimeInMillis()) / (3600 * 24 * 1000));
            return dayCount;
        }
        return -1;
    }

    public static long getTimeMillis(String mTime) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(mTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    public static Calendar getDateFromString(String mTime) {
        Date date = null;
        try {
            date = simpleDateFormat.parse(mTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar getDateFromStringChinese(String mTime) {
        Date date = null;
        try {
            date = simpleYearAndMonthAndDayFormat.parse(mTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String convertDataForT(String inputData) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(simpleDateFormat.parse(inputData));
    }
}
