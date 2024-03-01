package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Calendar;
import zzjz.mapper.CalendarMapper;
import zzjz.service.CalendarService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日10:35
 * @ClassName: CalendarServiceImpl
 * @Description: 日期控制实现类
 */
@Service
public class CalendarServiceImpl implements CalendarService {
    private static final Integer INT7 = 7;

    @Autowired
    private CalendarMapper calendarMapper;

    @Override
    public Object getCalendarList(Calendar calendar) {
        List<Calendar> list = calendarMapper.getCalendarList(calendar);
        int week = 1;
        for (int i = 0; i < list.size(); i++) {
            if ("7".equals(list.get(i).getWeek()) && i < list.size() - 1) {
                week++;
            }
        }
        Object[] obj = new Object[week + 1];
        List<Calendar> calList = new ArrayList<Calendar>();
        week = 1;
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                calList = calendarMapper.getCalendarBeforeList(list.get(i).getCurDay(), Integer.parseInt(list.get(i).getWeek()) - 1);
                for (Calendar cal : calList) {
                    cal.setDay(cal.getCurDay().split("-")[2]);
                    cal.setThisMonth("0");
                }
            }
            list.get(i).setDay(list.get(i).getCurDay().split("-")[2]);
            list.get(i).setThisMonth("1");
            calList.add(list.get(i));
            if ("7".equals(list.get(i).getWeek()) && i < list.size() - 1) {
                obj[week] = calList;
                week++;
                calList = new ArrayList<Calendar>();
            }
            if (i == list.size() - 1) {
                if (calList.size() != INT7) {
                    List<Calendar> tmpList = calendarMapper.getCalendarAfterList(list.get(i).getCurDay(), INT7 - calList.size());
                    for (int j = 0; j < tmpList.size(); j++) {
                        tmpList.get(j).setDay(tmpList.get(j).getCurDay().split("-")[2]);
                        tmpList.get(j).setThisMonth("0");
                        calList.add(tmpList.get(j));
                    }
                }
                obj[week] = calList;
            }
        }
        return obj;
    }

    /**
     * 根据日期修改节假日.
     *
     * @param curDay 日期
     * @return
     */
    @Override
    public boolean updateHoliday(String curDay, String hTyp) {
        if ("0".equals(hTyp)) {
            hTyp = "1";
        } else {
            hTyp = "0";
        }
        return calendarMapper.updateHoliday(curDay, hTyp);
    }

    /**
     * 根据日期修改为工作日.
     *
     * @param curDay 日期
     * @return 结果
     */
    @Override
    public boolean updateWorkday(String curDay, String wTyp) {
        if ("0".equals(wTyp)) {
            wTyp = "1";
        } else {
            wTyp = "0";
        }
        return calendarMapper.updateWorkday(curDay, wTyp);
    }

    /**
     * 根据日期获取是否工作日.
     *
     * @param curDay 日期
     * @return
     */
    @Override
    public boolean isWorkDay(String curDay) {
        Calendar calendar = new Calendar();
        calendar.setCurDay(curDay);
        calendar = calendarMapper.selectByPrimaryKey(calendar);
        boolean rs = false;
        if ("1".equals(calendar.getIsWorkday()) && "0".equals(calendar.getIsHoliday())) {
            rs = true;
        }
        return rs;
    }

    /**
     * 根据日期获取是否节假日.
     *
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

    /**
     * 查询日期间的天数.
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param flag      0-节假日顺延；1-节假日不顺延；2-节日顺延，假日不顺延
     * @return 天数
     */
    @Override
    public Integer daysBetween(String beginDate, String endDate, String flag) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        if ("0".equals(flag)) {
            return calendarMapper.selectWorkDaysBetween(map);
        } else if ("1".equals(flag)) {
            return calendarMapper.selectAllDaysBetween(map);
        } else {
            return calendarMapper.selectNoHoliDaysBetween(map);
        }
    }

    @Override
    public String addDays(String date, int days, String flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("days", days);
        if ("0".equals(flag)) {
            return calendarMapper.selectNextDate(map);
        } else if ("1".equals(flag)) {
            return calendarMapper.selectNextWorkDate(map);
        } else {
            return calendarMapper.selectNextNoHoliDate(map);
        }
    }
}
