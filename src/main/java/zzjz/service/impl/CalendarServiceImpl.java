package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Calendar;
import zzjz.mapper.CalendarMapper;
import zzjz.service.CalendarService;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日10:35
 */
@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private CalendarMapper calendarMapper;

    @Override
    public List<Calendar> getCalendarList(Calendar calendar) {
        return calendarMapper.getCalendarList(calendar);
    }

    /**
     * 根据日期修改为节假日
     * @param curDay 日期
     * @return
     */
    @Override
    public boolean updateHoliday(String curDay) {
        return calendarMapper.updateHoliday(curDay);
    }

    /**
     * 根据日期修改取消节假日
     * @param curDay 日期
     * @return 结果
     */
    @Override
    public boolean updateHolidayCancel(String curDay) {
        return calendarMapper.updateHolidayCancel(curDay);
    }

    /**
     * 根据日期修改为工作日
     * @param curDay 日期
     * @return 结果
     */
    @Override
    public boolean updateWorkday(String curDay) {
        return calendarMapper.updateWorkday(curDay);
    }

    /**
     * 根据日期修改取消工作日
     * @param curDay 日期
     * @return 结果
     */
    @Override
    public boolean updateWorkdayCancel(String curDay) {
        return calendarMapper.updateWorkdayCancel(curDay);
    }

    /**
     * 根据日期获取是否工作日
     * @param curDay 日期
     * @return
     */
    @Override
    public boolean isWorkDay(String curDay) {
        Calendar calendar = new Calendar();
        calendar.setCurDay(curDay);
        calendar = calendarMapper.selectByPrimaryKey(calendar);
        boolean rs = false;
        if("0".equals(calendar.getIsWorkday())&&"0".equals(calendar.getIsHoliday())){
            rs = true;
        }
        return rs;
    }

    /**
     * 根据日期获取是否节假日
     * @param curDay 日期
     * @return
     */
    @Override
    public boolean isHoliDay(String curDay) {
        Calendar calendar = new Calendar();
        calendar.setCurDay(curDay);
        calendar = calendarMapper.selectByPrimaryKey(calendar);
        return "1".equals(calendar.getIsHoliday());
    }
}
