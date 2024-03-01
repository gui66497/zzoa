package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Calendar;

/**
 * @author 彭鹏
 * @version 2017年04月12日10:29
 * @ClassName: CalendarService
 * @Description: 日期管理服务接口
 */
@Service
public interface CalendarService {

    /**
     * 根据日期修改为节假日.
     *
     * @param calendar 日期
     * @return 结果
     */
    Object getCalendarList(Calendar calendar);

    /**
     * 根据日期修改节假日.
     *
     * @param curDay 日期
     * @param hTyp   hTyp
     * @return 结果
     */
    boolean updateHoliday(String curDay, String hTyp);

    /**
     * 根据日期修改工作日.
     *
     * @param curDay 日期
     * @param wTyp   wTyp
     * @return 结果
     */
    boolean updateWorkday(String curDay, String wTyp);

    /**
     * 根据日期获取是否工作日.
     *
     * @param curDay 日期
     * @return 结果
     */
    boolean isWorkDay(String curDay);

    /**
     * 根据日期获取是否节假日.
     *
     * @param curDay 日期
     * @return 结果
     */
    boolean isHoliDay(String curDay);

    /**
     * 查询日期间的天数.
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param flag      0-节假日顺延；1-节假日不顺延；2-节日顺延，假日不顺延
     * @return 天数
     */
    Integer daysBetween(String beginDate, String endDate, String flag);

    /**
     * 查询日期.
     *
     * @param date date
     * @param days days
     * @param flag flag
     * @return String
     */
    String addDays(String date, int days, String flag);
}
