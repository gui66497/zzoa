package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 汪浩
 * @version 2017/5/27
 */
@XmlRootElement
public class PunchedCard {
    private String day;

    private String hour;

    private String minute;

    private String second;

    private String uid;

    private String year;

    private String month;

    private String mode;

    /**
     * 转换后的日期
     */
    private String date;

    /**
     * getDay.
     *
     * @return DAY
     */
    public String getDay() {
        return day;
    }

    /**
     * setMonth.
     *
     * @return MONTH
     */
    public String getMonth() {
        return month;
    }

    /**
     * setMonth.
     *
     * @param month MONTH
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * setDay.
     *
     * @param day DAY
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * getHour.
     *
     * @return HOUR
     */
    public String getHour() {
        return hour;
    }

    /**
     * setHour.
     *
     * @param hour HOUR
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * getMinute.
     *
     * @return MINUTE
     */
    public String getMinute() {
        return minute;
    }

    /**
     * setMinute.
     *
     * @param minute MINUTE
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }

    /**
     * getSecond.
     *
     * @return SECOND
     */
    public String getSecond() {
        return second;
    }

    /**
     * setSecond.
     *
     * @param second SECOND
     */
    public void setSecond(String second) {
        this.second = second;
    }

    /**
     * getUid.
     *
     * @return UID
     */
    public String getUid() {
        return uid;
    }

    /**
     * setUid.
     *
     * @param uid UID
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * getYear.
     *
     * @return YEAR
     */
    public String getYear() {
        return year;
    }

    /**
     * setYear.
     *
     * @param year YEAR
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * getMode.
     *
     * @return MODE
     */
    public String getMode() {
        return mode;
    }

    /**
     * setMode.
     *
     * @param mode MODE
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     *
     * @return 日期
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date date
     */
    public void setDate(String date) {
        this.date = date;
    }
}
