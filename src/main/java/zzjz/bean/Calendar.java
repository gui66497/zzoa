package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年04月14日11:04
 * @ClassName: zoa
 * @Description:
 */
@XmlRootElement
public class Calendar {
    private String curDay;

    private String lastDay;

    private String nextDay;

    private String week;

    private String isWorkday;

    private String isHoliday;

    private String holidayName;

    private String day;

    private String thisMonth;

    /**
     * getCurDay.
     *
     * @return CUR_DAY
     */
    public String getCurDay() {
        return curDay;
    }

    /**
     * setCurDay.
     *
     * @param curDay CUR_DAY
     */
    public void setCurDay(String curDay) {
        this.curDay = curDay;
    }

    /**
     * getLastDay.
     *
     * @return LAST_DAY
     */
    public String getLastDay() {
        return lastDay;
    }

    /**
     * setLastDay.
     *
     * @param lastDay LAST_DAY
     */
    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    /**
     * getNextDay.
     *
     * @return NEXT_DAY
     */
    public String getNextDay() {
        return nextDay;
    }

    /**
     * setNextDay.
     *
     * @param nextDay NEXT_DAY
     */
    public void setNextDay(String nextDay) {
        this.nextDay = nextDay;
    }

    /**
     * getWeek.
     *
     * @return WEEK
     */
    public String getWeek() {
        return week;
    }

    /**
     * setWeek.
     *
     * @param week WEEK
     */
    public void setWeek(String week) {
        this.week = week;
    }

    /**
     * getIsWorkday.
     *
     * @return IS_WORKDAY
     */
    public String getIsWorkday() {
        return isWorkday;
    }

    /**
     * setIsWorkday.
     *
     * @param isWorkday IS_WORKDAY
     */
    public void setIsWorkday(String isWorkday) {
        this.isWorkday = isWorkday;
    }

    /**
     * getIsHoliday.
     *
     * @return IS_HOLIDAY
     */
    public String getIsHoliday() {
        return isHoliday;
    }

    /**
     * setIsHoliday.
     *
     * @param isHoliday IS_HOLIDAY
     */
    public void setIsHoliday(String isHoliday) {
        this.isHoliday = isHoliday;
    }

    /**
     * getHolidayName.
     *
     * @return HOLIDAY_NAME
     */
    public String getHolidayName() {
        return holidayName;
    }

    /**
     * setHolidayName.
     *
     * @param holidayName HOLIDAY_NAME
     */
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    /**
     * getDay.
     *
     * @return DAY
     */
    public String getDay() {
        return day;
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
     * getThisMonth.
     *
     * @return THIS_MONTH
     */
    public String getThisMonth() {
        return thisMonth;
    }

    /**
     * setThisMonth.
     *
     * @param thisMonth THIS_MONTH
     */
    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
    }
}
