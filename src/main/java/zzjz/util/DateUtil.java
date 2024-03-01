package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zzjz.rest.AttenSummaryRest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author 梅宏振
 * @version 2015年3月4日 下午1:37:28
 * @ClassName: DateUtil
 * @Description: 日期操作工具类
 */
public class DateUtil {

    private static final String DATE_FORMATE1 = "yyyy-MM-dd HH:mm:ss";

    private static final Logger LOGGER = LoggerFactory.getLogger(AttenSummaryRest.class);

    private static final Integer INT99999 = 99999;

    private static final Integer INT10000 = 10000;

    private static final long INT1000 = 1000L;

    private static final Integer INT3600 = 3600;

    private static final Integer INT24 = 24;

    private static final Integer INT10 = 24;

    private static final Integer INT3 = 3;

    private static final Integer INT4 = 4;

    private static final Integer INT15 = 15;

    /**
     * @param dateStr     dateStr
     * @param dateFormate dateFormate
     * @return 如果转换失败返回null
     * @Description: 字符串转日期
     */
    public static Date stringToDate(String dateStr, String dateFormate) {
        SimpleDateFormat sdft = new SimpleDateFormat(dateFormate);
        Date date = null;
        try {
            date = sdft.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param dateStr dateStr
     * @return Date
     * @Title: stringToDate
     * @Description: 按照 yyyy-MM-dd HH:mm:ss格式解析字符串为Date
     */
    public static Date stringToDate(String dateStr) {
        SimpleDateFormat sdft = new SimpleDateFormat(DATE_FORMATE1);
        Date date = null;
        try {
            date = sdft.parse(dateStr);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage() + "时间的格式不正确，应为：yyyy-MM-dd HH:mm:ss");
        }
        return date;
    }

    /**
     * @param dateStr dateStr
     * @return Timestamp
     * @Title: stringToTimestamp
     * @Description: 按照 yyyy-MM-dd HH:mm:ss格式解析字符串为Date
     */
    public static Timestamp stringToTimestamp(String dateStr) {
        SimpleDateFormat sdft = new SimpleDateFormat(DATE_FORMATE1);
        long time = 0;
        try {
            time = sdft.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(time);
    }

    /**
     * @param date        date
     * @param dateFormate dateFormate
     * @return String
     * @Title: dateToString
     * @Description: 日期转字符串
     */
    public static String dateToString(Date date, String dateFormate) {
        SimpleDateFormat sdft = new SimpleDateFormat(dateFormate);
        if (null == date) {
            date = new Date(0);
        }
        return sdft.format(date);
    }

    /**
     * @param date date
     * @return String
     * @Title: dateToString
     * @Description: 日期转字符串, 默认格式为yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date date) {
        return dateToString(date, DATE_FORMATE1);
    }

    /**
     * @return Date
     * @Title: getInitDate
     * @Description: 获取日期的初始化时间("1970-01-01 00:00:00")
     */
    public static Date getInitDate() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param dateStr  日期字符串
     * @param addYear  要转变的年数
     * @param addMonth 要转变的月数
     * @param addDate  要转变的日数
     * @return String
     * @throws Exception Exception
     * @Title: getDate
     * @Description: 取得当前日期的某个日期
     */
    public static String changeDate(String dateStr, int addYear, int addMonth,
                                    int addDate) throws Exception {
        try {
            SimpleDateFormat sdft = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date sourceDate = sdft.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sourceDate);
            cal.add(Calendar.YEAR, addYear);
            cal.add(Calendar.MONTH, addMonth);
            cal.add(Calendar.DATE, addDate);

            SimpleDateFormat returnSdf = new SimpleDateFormat(
                    "yyyy-MM-dd");
            String dateTmp = returnSdf.format(cal.getTime());
            //java.util.Date returnDate = returnSdf.parse(dateTmp);
            return dateTmp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数.
     *
     * @return String
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (INT99999 - INT10000 + 1)) + INT10000; // 获取5位随机数
        return rannum + str; // 当前时间
    }

    /**
     * @return String
     */
    public static String getNowDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateStr = dateFormat.format(now);
        return dateStr;
    }

    /**
     * 计算两个日期之间相差的天数.
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdft.parse(sdft.format(smdate));
        bdate = sdft.parse(sdft.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (INT1000 * INT3600 * INT24);

        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 年份后两位+时间戳后四位（如175662）.
     *
     * @return String
     */
    public static String getIdByDate() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        year = StringUtils.substring(year, year.length() - 2);

        Random random = new Random();
        String r = String.valueOf(random.nextInt(INT10));

        String now = String.valueOf(new Date().getTime());
        now = StringUtils.substring(now, now.length() - INT4);
        return year + r + now;

    }

    /**
     * 判断是否是当天.
     *
     * @param dateStr 日期（yyyy-MM-dd）
     * @return 是否
     */
    public static boolean isToday(String dateStr) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay();
        return dateStr.equals(dateTime.toString("yyyy-MM-dd"));
    }

    /**
     * 考勤几个时间选择器的时间格式化 如2017-12-16T03:04:46.000Z转2017-12-16.
     * @param timeStr 时间字符串
     * @return 时间字符串
     * @throws ParseException ParseException
     */
    public static String getAttendanceTime(String timeStr) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        timeStr = timeStr.replace("Z", " UTC");
        Date start = sdf2.parse(timeStr);
        return sdf1.format(start);
    }

    /**
     *
     * @return 一年的开始(如 2018-03-16)
     */
    public static String getYearStart() {
        String beginDate;
        // 考勤周期改为自然月 三月一号之前就算去年
        DateTime now = DateTime.now();
        int month = now.monthOfYear().get();
        if (month < INT3) {
            beginDate = now.minusYears(1).getYear() + "-03-01";
        } else {
            beginDate = now.getYear() + "-03-01";
        }
        return beginDate;
    }

    /**
     *
     * @return 一年的结束(如 2019-03-15)
     */
    public static String getYearEnd() {
        String endDate;
        // 考勤周期改为自然月 三月一号之前就算去年
        DateTime now = DateTime.now();
        int month = now.monthOfYear().get();
        if (month < INT3) {
            endDate = now.getYear() + "-03-01";
        } else {
            endDate = now.plusYears(1).getYear() + "-03-01";
        }
        return endDate;
    }

    public static String getMonthStart() {
        return DateTime.now().dayOfMonth().withMinimumValue().toString("yyyy-MM-dd");
    }

    public static String getMonthEnd() {
        return DateTime.now().dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");
    }

    /**
     * 获取本日的0点0分0秒.
     * @return 日期
     */
    public static Date getTodayZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*public static void main(String[] args) throws InterruptedException {
        System.out.println(isToday("2017-07-17"));
        System.out.println(getIdByDate());
        System.out.println(getIdByDate());
        System.out.println(getIdByDate());
    }*/
}

