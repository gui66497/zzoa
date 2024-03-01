package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Over;
import zzjz.bean.OverOff;
import zzjz.bean.OverRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.bean.Vacation;
import zzjz.bean.VacationRequest;
import zzjz.mapper.StaffMapper;
import zzjz.mapper.VacationDetailMapper;
import zzjz.service.CalendarService;
import zzjz.service.OverOffService;
import zzjz.service.OverService;
import zzjz.service.VacationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/29.
 */
public class OverOffServiceTest extends JunitBaseTest {

    @Autowired
    OverOffService overOffService;

    @Autowired
    VacationService vacationService;

    @Autowired
    OverService overService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    VacationDetailMapper vacationDetailMapper;

    private static final Integer INT7 = 7;

    private static final Integer INT16 = 16;

    /**
     * 测试初始化员工加班调休情况.
     */
    @Test
    //@Rollback()
    public void testInitOverOffInfo() {
        boolean rs = false;
        try {
            rs = overOffService.initOverOffInfo("37", "2021-03");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert rs;
    }

    /**
     * 测试初始化全部员工加班调休情况.
     */
    @Test
    @Rollback()
    public void testBatchInitOverOffInfo() {
        boolean rs = overOffService.batchInitOverOffInfo("2021-02");
        assert rs;
    }

    /**
     * 测试更新加班调休信息.
     */
    @Test
    @Rollback()
    public void testUpdateOverOffInfo() {
        OverOff overOff = new OverOff();
        overOff.setUserId("62");
        overOff.setMonthDate("2017-08");
        overOff.setLastLeft(0.0);
        overOff.setOverDay(0.0);
        overOff.setOffDay(0.0);
        overOff.setLeftDay(0.0);
        boolean rs = overOffService.updateOverOffInfo(overOff);
        assert rs;
    }

    /**
     * 测试查询加班调休信息.
     */
    @Test
    @Rollback()
    public void testQueryOverOffInfo() {
        OverOff overOff = overOffService.queryOverOffInfo("62", "2017-08");
        assert overOff != null;
    }

    /**
     * 测试清除加班结余.
     */
    @Test
    @Rollback()
    public void testClearLastLeftDay() {
        boolean rs = overOffService.clearLastLeftDay("62", "2017-08");
        assert rs;
    }

    /**
     * 测试计算已使用调休天数并更新.
     */
    @Test
    @Rollback()
    public void testUpdateOverOffWithUseInfo() {
        OverOff overOff = new OverOff();
        overOff.setUserId("62");
        overOff.setMonthDate("2017-08");
        overOff.setLastLeft(0.0);
        overOff.setOverDay(0.0);
        overOff.setOffDay(0.0);
        overOff.setLeftDay(0.0);
        boolean rs = overOffService.updateOverOffWithUseInfo(overOff, 0.0, 0.0);
        assert rs;
    }

    @Autowired
    StaffMapper staffMapper;

    @Test
    public void batchReMakeOverOff() throws ParseException {
        StaffRequest request = new StaffRequest();
        request.setOnJob(1);
        List<Staff> list = staffMapper.getStaffList(request);
        for (Staff staff : list) {
            reMakeOverOff(String.valueOf(staff.getStaffId()));
        }
    }

    /**
     * 2021-02-19 fgt
     * 考勤周期改为自然月
     * 将2月份现有的调休和请假都清理（因为按照原来逻辑2月1~2月15是属于1月考勤）
     * 然后再重新遍历调休和请假数据 重新计算考勤
     * @param userId 用户id
     * @throws ParseException
     */
    public void reMakeOverOff(String userId) throws ParseException {
        // 查询这个人2月份的所有调休
        VacationRequest request = new VacationRequest();
        request.setUserId(userId);
        request.setBackDate("2021-02");
        List<Vacation> vacationList = vacationService.getVacationList(request);
        for (Vacation vacation : vacationList) {
            // 是调休并且状态是已完成
            if ("00".equals(vacation.getLeaveTyp()) &&
                    ("97".equals(vacation.getStatus()) || "98".equals(vacation.getStatus()) || "99".equals(vacation.getStatus()))) {
                // 查询加班调休信息
                OverOff overOff = overOffService.queryOverOffInfo(vacation.getUserId(), vacationService.dateToMonthOld(vacation.getLeaveDate()));
                overOff.setOffDay(overOff.getOffDay() - vacation.getLeaveDay());
                overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
                overOff.setUseDay(overOff.getLastLeft() - overOff.getLeftDay());
                overOffService.updateOverOffWithUseInfo(overOff, vacation.getLeaveDay(), 0.0);
                // 无需调整2月数据 因为2月数据是要重新生成的
                // 略
                List<OverOff> list = overOffService.queryOverOffList(userId, vacationService.dateToMonthOld(vacation.getLeaveDate()));
                if (list.size() > 0) {
                    Double lastLeft = overOff.getLeftDay();
                    for (OverOff oo : list) {
                        oo.setLastLeft(lastLeft);
                        oo.setLeftDay(lastLeft + oo.getOverDay() - oo.getOffDay());
                        overOffService.updateOverOffInfo(oo);
                        lastLeft = oo.getLeftDay();
                    }
                }
            }
        }

        OverRequest overRequest = new OverRequest();
        overRequest.setUserId(userId);
        overRequest.setOverDate("2021-02");
        overRequest.setStatus("99");
        overRequest.setPaging(new PagingEntity(1,100));
        List<Over> overList = overService.getOverList(overRequest);
        for (Over over : overList) {
            //计算月份
            String monthDate = over.getOverDate().substring(0, INT7);
            if (Integer.parseInt(over.getOverDate().split("-")[2]) < INT16) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Calendar c = java.util.Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(over.getOverDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(java.util.Calendar.MONTH, -1);
                monthDate = sdf.format(c.getTime()).substring(0, INT7);
            }
            boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
            if (!isHoliday) {
                OverOff overOff = overOffService.queryOverOffInfo(over.getUserId(), monthDate);
                if (overOff == null) {
                    try {
                        overOffService.initOverOffInfo(over.getUserId(), monthDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    overOff = overOffService.queryOverOffInfo(over.getUserId(), over.getOverDate().substring(0, INT7));
                }
                overOff.setOverDay(overOff.getOverDay() - over.getOverDay());
                overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
                overOffService.updateOverOffInfo(overOff);
                // 无需调整2月数据 因为2月数据是要重新生成的
                // 略
            }
        }

        // 把二月份的调休和加班都清理了 下面开始重建2月份的over_off信息

        // 加班
        for(Over over: overList) {
            overService.initOverOffInfo(over);
        }

        // 调休
        for (Vacation vacation : vacationList) {
            if ("00".equals(vacation.getLeaveTyp()) &&
                    ("97".equals(vacation.getStatus()) || "98".equals(vacation.getStatus()) || "99".equals(vacation.getStatus()))) {
                Integer id = vacation.getId();
                Double leaveDays = vacation.getLeaveDay();
                if (leaveDays == null) {
                    leaveDays = 0.0;
                }
                //查询加班调休信息
                OverOff overOff = overOffService.queryOverOffInfo(userId, vacationService.dateToMonth(vacation.getLeaveDate()));
                if (overOff == null) {
                    overOffService.initOverOffInfo(userId, vacationService.dateToMonth(vacation.getLeaveDate()));
                    overOff = overOffService.queryOverOffInfo(userId, vacationService.dateToMonth(vacation.getLeaveDate()));
                }
                overOff.setOffDay(overOff.getOffDay() + leaveDays);
                overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
                //overOffService.updateOverOffWithUseInfo(overOff, deleteDays, leaveDays);
                overOffService.updateOverOffInfo(overOff);
                List<OverOff> list = overOffService.queryOverOffList(userId, vacationService.dateToMonth(vacation.getLeaveDate()));
                if (list.size() > 0) {
                    Double lastLeft = overOff.getLeftDay();
                    for (OverOff oo : list) {
                        oo.setLastLeft(lastLeft);
                        oo.setLeftDay(lastLeft + oo.getOverDay() - oo.getOffDay());
                        overOffService.updateOverOffInfo(oo);
                        lastLeft = oo.getLeftDay();
                    }
                }
            }
        }

    }
}
