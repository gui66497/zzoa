package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Calendar;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日10:29
 */
@Service
public interface CalendarService {

    /**
     * 根据日期修改为节假日
     * @param calendar 日期
     * @return 结果
     */
    List<Calendar> getCalendarList (Calendar calendar);

    /**
     * 根据日期修改为节假日
     * @param curDay 日期
     * @return 结果
     */
    boolean updateHoliday(String curDay);

    /**
     * 根据日期修改取消节假日
     * @param curDay 日期
     * @return 结果
     */
    boolean updateHolidayCancel(String curDay);

    /**
     * 根据日期修改为工作日
     * @param curDay 日期
     * @return 结果
     */
    boolean updateWorkday(String curDay);

    /**
     * 根据日期修改取消工作日
     * @param curDay 日期
     * @return 结果
     */
    boolean updateWorkdayCancel(String curDay);

    /**
     * 根据日期获取是否工作日
     * @param curDay 日期
     * @return 结果
     */
    boolean isWorkDay(String curDay);

    /**
     * 根据日期获取是否节假日
     * @param curDay 日期
     * @return 结果
     */
    boolean isHoliDay(String curDay);
}
