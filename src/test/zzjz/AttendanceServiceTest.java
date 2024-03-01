package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.AttendanceRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.PunchedCard;
import zzjz.bean.PunchedInfo;
import zzjz.mapper.AttendanceMapper;
import zzjz.service.AttendanceService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/27.
 */
public class AttendanceServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    AttendanceMapper attendanceMapper;

    /**
     * 测试批量添加考勤信息.
     */
    @Test
    @Rollback()
    public void testAddAttendanceList() {
        List<AttendanceExcel> res = new ArrayList<>();
        AttendanceExcel e = new AttendanceExcel();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        res.add(e);
        boolean rs = attendanceService.addAttendanceList(res);
        assert rs;
    }

    /**
     * 测试分页获取考勤信息列表.
     */
    @Test
    @Rollback()
    public void testGetAttendanceList() {
        AttendanceRequest request = new AttendanceRequest();
        request.setCheckDate("2017-03-28");
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Attendance> list = attendanceService.getAttendanceList(request);
        assert list != null;
    }

    /**
     * 测试根据日期用户修改考勤.
     */
    @Test
    @Rollback()
    public void testInputAtt() {
        AttendanceRequest e = new AttendanceRequest();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        boolean rs = attendanceService.inputAtt(e);
        assert rs;
    }

    /**
     * 测试添加签到时间.
     */
    @Test
    @Rollback()
    public void testInputSignTime() {
        AttendanceRequest e = new AttendanceRequest();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        boolean rs = attendanceService.inputSignTime(e);
        assert rs;
    }

    /**
     * 测试添加签退时间.
     */
    @Test
    @Rollback()
    public void testInputLeaveTime() {
        AttendanceRequest e = new AttendanceRequest();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        boolean rs = attendanceService.inputLeaveTime(e);
        assert rs;
    }

    /**
     * 测试校验考勤信息.
     */
    @Test
    @Rollback()
    public void testCheckAttendanceData() {
        Attendance e = new Attendance();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        e = attendanceService.checkAttendanceData(e, null);
        assert e != null;
    }

    /**
     * 测试检验导入考勤信息.
     */
    @Test
    @Rollback()
    public void testExcelCheckAttendanceData() {
        AttendanceExcel e = new AttendanceExcel();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        e = attendanceService.checkAttendanceData(e);
        assert e != null;
    }

    /**
     * 测试更新考勤信息.
     */
    @Test
    @Rollback()
    public void testUpdateAttentionInfo() {
        AttendanceExcel e = new AttendanceExcel();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        e = attendanceService.updateAttentionInfo(e);
        assert e != null;
    }

    /**
     * 测试查询考勤信息.
     */
    @Test
    @Rollback()
    public void testSelectByPrimaryKey() {
        Attendance e = new Attendance();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        e = attendanceService.selectByPrimaryKey(e);
        assert e != null;
    }

    /**
     * 测试更新考勤信息.
     */
    @Test
    @Rollback()
    public void testUpdateByPrimaryKey() {
        Attendance e = new Attendance();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        boolean rs = attendanceService.updateByPrimaryKey(e);
        assert rs;
    }

    /**
     * 测试获取考勤异常列表.
     */
    @Test
    @Rollback()
    public void testGetRemindList() {
        List<Attendance> list = attendanceService.getRemindList("62");
        assert list != null;
    }

    /**
     * 测试获取考勤异常列表.
     */
    @Test
    @Rollback()
    public void testGetErrorByDate() {
        Attendance attendance = new Attendance();
        attendance.setBeginTime("2017-03-28");
        attendance.setEndTime("2017-10-15");
        List<Attendance> list = attendanceService.getErrorByDate(attendance);
        assert list != null;
    }

    /**
     * 测试根据校验日期获取考勤列表.
     */
    @Test
    @Rollback()
    public void testGetAttendanceByCheckDate() {
        List<Attendance> list = attendanceService.getAttendanceByCheckDate("2017-03-28");
        assert list != null;
    }

    /**
     * 测试删除考勤信息.
     */
    @Test
    @Rollback()
    public void testDeleteAttInfo() {
        AttendanceRequest e = new AttendanceRequest();
        e.setUserId("62");
        e.setUserName("彭鹏");
        e.setDepart("41");
        e.setCheckDate("2017-03-28");
        e.setBeginTime("08:30");
        e.setEndTime("17:30");
        e.setSignTime("08:07");
        e.setLeaveTime("17:35");
        e.setEarly(0.0);
        e.setLate(0.0);
        e.setAbsent(0.0);
        e.setOverTime(0.0);
        e.setAllDay(null);
        e.setMorDay(null);
        e.setAftDay(null);
        e.setEveDay(null);
        e.setIsError("0");
        e.setErrorDesc("");
        e.setWorkDay("1");
        boolean rs = attendanceService.deleteAttInfo(e);
        assert rs;
    }

    /**
     * 测试添加打卡信息.
     */
    @Test
    @Rollback()
    public void testAddPunchInfo() {
        Attendance attendance = new Attendance();
        attendance.setCheckDate("2017-03-28");
        attendance.setUserId("62");
        attendanceMapper.deleteByPrimaryKey(attendance);
        PunchedInfo punchedInfo = new PunchedInfo();
        PunchedCard punchedCard = new PunchedCard();
        punchedCard.setUid("62");
        punchedCard.setYear("2017");
        punchedCard.setMonth("03");
        punchedCard.setDay("28");
        punchedCard.setHour("08");
        punchedCard.setMinute("07");
        punchedCard.setSecond("23");
        List<PunchedCard> list = new ArrayList<>();
        list.add(punchedCard);
        punchedInfo.setLogs(list);
        boolean rs = attendanceService.addPunchInfo(punchedInfo);
        attendanceMapper.deleteByPrimaryKey(attendance);
        assert rs;
    }
}
