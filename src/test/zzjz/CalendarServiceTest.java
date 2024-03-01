package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Calendar;
import zzjz.service.CalendarService;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class CalendarServiceTest extends JunitBaseTest {

    private static final Integer DAY5 = 5;

    @Autowired
    CalendarService calendarService;

    /**
     * 测试初始化流程信息.
     */
    @Test
    @Rollback()
    public void testGetCalendarList() {
        Calendar calendar = new Calendar();
        calendar.setCurDay("2017-10-01");
        Object obj = calendarService.getCalendarList(calendar);
        assert obj != null;
    }

    /**
     * 测试根据日期修改节假日.
     */
    @Test
    @Rollback()
    public void testUpdateHoliday() {
        boolean rs = calendarService.updateHoliday("2017-10-01", "0");
        assert rs;
    }

    /**
     * 测试根据日期修改工作日.
     */
    @Test
    @Rollback()
    public void testUpdateWorkday() {
        boolean rs = calendarService.updateWorkday("2017-10-01", "1");
        assert rs;
    }

    /**
     * 测试根据日期获取是否工作日.
     */
    @Test
    @Rollback()
    public void testIsWorkDay() {
        boolean rs = calendarService.isWorkDay("2017-10-01");
        assert !rs;
    }

    /**
     * 测试根据日期获取是否节假日.
     */
    @Test
    @Rollback()
    public void testIsHoliDay() {
        boolean rs = calendarService.isHoliDay("2017-10-01");
        assert rs;
    }

    /**
     * 测试查询日期间的天数.
     */
    @Test
    @Rollback()
    public void testDaysBetween() {
        Integer c = calendarService.daysBetween("2017-10-01", "2017-10-02", "1");
        assert c == 2;
    }

    /**
     * 测试查询日期.
     */
    @Test
    @Rollback()
    public void testAddDays() {
        String s = calendarService.addDays("2017-10-01", DAY5, "1");
        assert !"".equals(s);
    }
}
