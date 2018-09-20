package com.hstypay.sandbox.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期的工具类
 *
 * @author fu.wen
 *         2015-12-31
 */
public class DateHelper {

	public static final String FULL_PATTERN = "yyyyMMddHHmmss";
	public static final String STANDARD_DATEREGEX = "yyyy-MM-dd";
	public static final String STANDARD_TIMEREGEX = "yyyy-MM-dd HH:mm:ss";
	
	public static final long SECOND = 1000L;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATE_FORMAT_YY_MM = "yyyy-MM";
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String DATETIME_FORMATHHMM = "yyyy-MM-dd HH:mm";
	public final static String DATETIME_LONG_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
	public final static String DATE_FORMAT_YYMM = "yyyy年MM月";
	public final static String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";
	public final static String YEAR_MONTH_FORMAT = "yyyyMM";
	public final static String YEAR_FORMAT = "yyyy";
	public final static String MONTH_DAY_FORMAT = "MMdd";
	public final static String MONTH_FORMAT = "MM";
	public final static String DAY_FORMAT = "dd";
	public final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public final static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	public final static String yyyyMMddHH24mmssSSS = "yyyyMMddHH24mmssSSS";
	public final static String yyyy_MM_ddHHmm = "yyyy/MM/dd HH:mm";
	public final static String yyyy_MM_ddHHmmss = "yyyy/MM/dd HH:mm:ss";
	
    public static void main(String[] args) {
        System.out.println(getTodayEndTime());
    }


    /**
     * dateNow:将当天时间转成当天日期
     *
     * @author hansong
     * @param d
     * @return
     */
    public static Date dateNow(Date d) {
		Calendar ca = Calendar.getInstance();
		if (d != null){
			ca.setTime(d);
		}
		ca.set(11, 0);
		ca.set(12, 0);
		ca.set(13, 0);
		ca.set(14, 0);
		Date now = ca.getTime();
		return now;
	}
	
    /**
     * @return 获取当天的结束时间，如2016-01-01 23:59:59
     */
    public static Date getTodayEndTime() {
        return getCalendar(new Date(), 23, 59, 59, 999).getTime();
    }

    /**
     * @return 获取当天的结束时间，如2016-01-01 00:00:00
     */
    public static Date getTodayStartTime() {
        return getCalendar(new Date(), 0, 0, 0, 0).getTime();
    }

    /**
     * @return 获取当天剩余时间，以milliseconds为单位
     */
    public static long getTodayLeftTime() {
        Date curDate = new Date();
        Date endTime = getCalendar(curDate, 23, 59, 59, 999).getTime();
        return endTime.getTime() - curDate.getTime();
    }

    /**
     * @return 获取第二天时间，如2016-01-01 00:00:00
     */
    public static Date getTomorrowTime() {
        return getCalendar(new Date(), 24, 0, 0, 0).getTime();
    }

    /**
     * 获取指定时间的前一天时间，如2016-01-01 00:00:00
     *
     * @return 如果date为null，则返回当前系统时间的前一天时间
     */
    public static Date getYesterdayTime(Date date) {
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = getCalendar(date, 0, 0, 0, 0);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定时间的第二天时间，如2016-01-01 00:00:00
     *
     * @return 如果date为null，则返回当前系统时间的第二天时间
     */
    public static Date getTomorrowTime(Date date) {
        if (null == date) {
            return getCalendar(new Date(), 24, 0, 0, 0).getTime();
        }
        return getCalendar(date, 24, 0, 0, 0).getTime();
    }

    /**
     * 字符串时间转时间
     *
     * @param dateString 日期字符串
     * @param format     格式化
     */
    public static Date getStrToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间转换字符串
     *
     * @param date   时间
     * @param format 格式化
     * @return 时间字符串
     */
    public static String getDateToStr(Date date, String format) {
        if (format == null || format.equals("")) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            date = new Date();
        }
        return sdf.format(date);
    }

    /**
     * @param date        日期
     * @param hourOfDay   时
     * @param minute      分
     * @param second      秒
     * @param millisecond 毫秒
     * @return 时间
     */
    public static Calendar getCalendar(Date date, int hourOfDay, int minute, int second, int millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        return cal;
    }

    /**
     * @return 获取前一天时间 20151220
     */
    public static String getYesterdayDateString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(cal.getTime());
    }

    /**
     * @return 获取2天前的时间
     */
    public static Date getYesterDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -2);
        return DateHelper.getTomorrowTime(cal.getTime());
    }

    /**
     * 返回2个日期之前相差的天数
     *
     * @param start 开始时间(字符串型2017-01-01)
     * @param end   结束时间(字符串型2017-01-03)
     * @return 天数
     */
    public static Long getTimeInterval(String start, String end) {
        if (StringUtils.isBlank(start) || StringUtils.isBlank(end)) {
            return -1L;
        }
        Date startDate = getStrToDate(start, "yyyy-MM-dd");
        Date endDate = getStrToDate(end, "yyyy-MM-dd");
        return (long) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24.0) + 0.5);
    }

    /**
     * 返回2个日期之前相差的天数
     *
     * @param start 开始时间(时间型)
     * @param end   结束时间(时间型)
     * @return 天数
     */
    public static Long getTimeInterval(Date start, Date end) {
        if (null == start || null == end) {
            return -1L;
        }
        Date startDate = getCalendar(start, 0, 0, 0, 0).getTime();
        Date endDate = getCalendar(end, 0, 0, 0, 0).getTime();
        return (long) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24.0) + 0.5);
    }

    /**
     * 多少小时之后
     *
     * @param date  基准时间
     * @param hours 小时数
     * @return 时间
     */
    public static Date afterSomeHour(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 多少小时之后
     *
     * @param date  基准时间
     * @param hours 小时数
     * @return 时间
     */
    public static Date beforeSomeHour(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -hours);
        return calendar.getTime();
    }

    /**
     * 多少天之后
     *
     * @param date 基准时间
     * @param days 天数
     * @return 时间
     */
    public static Date afterSomeDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }


    /**
     * 多少天之前
     *
     * @param date 基准时间
     * @param days 天数
     * @return 时间
     */
    public static Date beforeSomeDay(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    /**
     * 多少月之前
     *
     * @param date   基准时间
     * @param months 天数
     * @return 时间
     */
    public static Date beforeSomeMonth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -months);
        return calendar.getTime();
    }


    /**
     * 多少分钟之后
     *
     * @param date 基准时间
     * @param mins 分钟
     * @return 时间
     */
    public static Date afterSomeMin(Date date, int mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    /**
     * 多少秒之后
     *
     * @param date 基准时间
     * @param secs 秒
     * @return 时间
     */
    public static Date afterSomeSec(Date date, int secs) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, secs);
        return calendar.getTime();
    }
    
    /**
     * 获取指定日期的开始时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getStartTime(Date date) {
        return getCalendar(date, 0, 0, 0, 0).getTime();
    }

    /**
     * 获取指定日期的结束时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getEndTime(Date date) {
        return getCalendar(date, 23, 59, 59, 999).getTime();
    }

    /**
     * 获取指定月份的开始时间
     *
     * @param year  年
     * @param month 月
     * @return 时间
     */
    public static Date getMonthBeginTime(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的结束时间
     *
     * @param year  年
     * @param month 月
     * @return 时间
     */
    public static Date getMonthEndTime(int year, int month) {
        int nextMonth = month + 1;
        if (nextMonth > 12) {
            year = year + 1;
            nextMonth = nextMonth - 12;
        }

        Date nextMonthDate = getMonthBeginTime(year, nextMonth);
        long monthEndTime = nextMonthDate.getTime() - 1;

        return new Date(monthEndTime);
    }

    /**
     * 获取指定月份的开始时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getMonthBeginTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return getCalendar(calendar.getTime(), 0, 0, 0, 0).getTime();
    }

    /**
     * 获取指定月份的开始时间
     *
     * @param date   基准时间
     * @param months 月份
     * @return 时间
     */
    public static Date getMonthBeginTime(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - months);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return getCalendar(calendar.getTime(), 0, 0, 0, 0).getTime();
    }

    /**
     * 获取指定月份的结束时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getMonthEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, value);
        return getCalendar(calendar.getTime(), 23, 59, 59, 999).getTime();
    }
    
	/**
	 * 比较是否同一天
	 * 
	 * @param localTime
	 *            本地时间
	 * @param remoteTime
	 *            服务器或者其他地方获取的时间
	 * @return
	 */
	public static boolean isSameDay(long localTime, long remoteTime) {
		Calendar todayC = Calendar.getInstance(Locale.CHINA);
		todayC.setTimeInMillis(localTime);
		int todayYear = todayC.get(Calendar.YEAR);
		int todayMonth = todayC.get(Calendar.MONTH) + 1;
		int todayDay = todayC.get(Calendar.DAY_OF_MONTH);

		Calendar compareTime = Calendar.getInstance();
		compareTime.setTimeInMillis(remoteTime);
		int year = compareTime.get(Calendar.YEAR);
		int month = compareTime.get(Calendar.MONTH) + 1;
		int day = compareTime.get(Calendar.DAY_OF_MONTH);

		if (year == todayYear && month == todayMonth && day == todayDay) {
			return true;
		}
		return false;
	}
	
	/**
	 * 设置传入日期的零点零分零秒
	 * CreateTime 2016年8月3日 下午6:46:41 
	 * @param date
	 * @return
	 */
	public static Date dateToDate0(Date date){
		if(date == null) return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}
	/**
	 * 设置传入日期的精确到小时
	 * CreateTime 2016年8月3日 下午6:46:41
	 * @param date
	 * @return
	 */
	public static Date dateToHour(Date date){
		if(date == null) return null;

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	/**
	 * 设置传入日期的23点59分59秒
	 * CreateTime 2016年8月3日 下午6:46:41 
	 * @param date
	 * @return
	 */
	public static Date dateToDate59(Date date){
		if(date == null) return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);

		return c.getTime();
	}
	
	/**
	 * 计算两个时间相差的秒数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getSeconds(Date startTime, Date endTime) {
		int s = (int) ((endTime.getTime() - startTime.getTime()) / 1000);
		return s;
	}

    /**
     * 获取传入时间的所在周第一天
     * @param dataStr
     * @return
     * @throws ParseException
     */
    public static Date getFirstOfWeek(Date dataStr)  {
        Calendar cal = Calendar.getInstance();

        cal.setTime(dataStr);

        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 所在周开始日期
        return cal.getTime();

    }
    /**
     * 获取传入时间的所在周最后一天
     * 以周一为每周的第一天
     * @param dataStr
     * @return
     * @throws ParseException
     */
    public static Date getLastOfWeek(Date dataStr) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(dataStr);

        int d = cal.get(Calendar.DAY_OF_WEEK);

        if (d == 1) {   // 默认是以周日为第一天的
            d=8;
        }

        // 所在周结束日期
        cal.add(Calendar.DATE, 7-d + 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();

    }



    /**
     * 获取上周日
     * @return
     * @author xhy
     */
    public static Date getLastWeekSunday(){

        Calendar date=Calendar.getInstance(Locale.CHINA);

        date.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天

        date.add(Calendar.WEEK_OF_MONTH,-1);//周数减一，即上周

        date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//日子设为星期天
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);
        return date.getTime();

    }

    /**
     * 获取上周一
     * @return
     * @author xhy
     */
    public static Date getLastWeekMonday(){

        Calendar date=Calendar.getInstance(Locale.CHINA);

        date.setFirstDayOfWeek(Calendar.MONDAY);//将每周第一天设为星期一，默认是星期天

        date.add(Calendar.WEEK_OF_MONTH,-1);//周数减一，即上周

        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//日子设为星期一
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();

    }
}
