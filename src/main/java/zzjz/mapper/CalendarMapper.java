package zzjz.mapper;

import zzjz.bean.Calendar;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日10:24
 */
public interface CalendarMapper {
    /**
     * 修改为节假日
     */
    boolean updateHoliday(String curDate);

    /**
     * 修改取消节假日
     */
    boolean updateHolidayCancel(String curDate);

    /**
     * 修改为工作日
     */
    boolean updateWorkday(String curDate);

    /**
     * 修改取消工作日
     */
    boolean updateWorkdayCancel(String curDate);

    /**
     * 获取日历列表
     */
    List<Calendar> getCalendarList (Calendar calendar);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_calendar
     *
     * @mbggenerated
     */
    Calendar selectByPrimaryKey(Calendar key);
}
