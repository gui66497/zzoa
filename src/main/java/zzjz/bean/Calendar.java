package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月14日11:04
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

    public String getCurDay() {
        return curDay;
    }

    public void setCurDay(String curDay) {
        this.curDay = curDay;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public String getNextDay() {
        return nextDay;
    }

    public void setNextDay(String nextDay) {
        this.nextDay = nextDay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getIsWorkday() {
        return isWorkday;
    }

    public void setIsWorkday(String isWorkday) {
        this.isWorkday = isWorkday;
    }

    public String getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(String isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
}
