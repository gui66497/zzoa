package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.AttendanceRequest;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日15:20
 */
@Service
public interface AttendanceService {

    /**
     * 批量添加考勤信息
     */
    boolean addAttendanceList(List<AttendanceExcel> res);

    Page<Attendance> getAttendanceList(AttendanceRequest request);

    /**
     * 根据日期用户录入事项
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputAtt(AttendanceRequest request);

    /**
     * 根据日期用户清除事项信息
     * @param request 考勤信息
     * @return 结果
     */
    boolean deleteAttInfo(AttendanceRequest request);

    /**
     * 根据日期用户添加签到时间
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputSignTime(AttendanceRequest request);

    /**
     * 根据日期用户添加签退时间
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputLeaveTime(AttendanceRequest request);

    /**
     * 校验考勤信息
     * @param attendance
     * @return 结果集
     */
    Attendance checkAttendanceData(Attendance attendance);

    /**
     * 校验考勤信息
     * @param attendanceExcel
     * @return 结果集
     */
    AttendanceExcel checkAttendanceData(AttendanceExcel attendanceExcel);
}
