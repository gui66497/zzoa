package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.AttendanceRequest;
import zzjz.bean.PunchedInfo;
import zzjz.bean.Staff;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:20
 * @ClassName: AttendanceService
 * @Description: 考勤管理服务接口
 */
@Service
public interface AttendanceService {

    /**
     * 批量添加考勤信息.
     *
     * @param res List
     * @return boolean
     */
    boolean addAttendanceList(List<AttendanceExcel> res);

    /**
     * 分页获取考勤信息列表.
     *
     * @param request AttendanceRequest
     * @return Page
     */
    Page<Attendance> getAttendanceList(AttendanceRequest request);

    /**
     * 根据日期用户录入事项.
     *
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputAtt(AttendanceRequest request);

    /**
     * 根据日期用户清除事项信息.
     *
     * @param request 考勤信息
     * @return 结果
     */
    boolean deleteAttInfo(AttendanceRequest request);

    /**
     * 根据日期用户添加签到时间.
     *
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputSignTime(AttendanceRequest request);

    /**
     * 根据日期用户添加签退时间.
     *
     * @param request 考勤信息
     * @return 结果
     */
    boolean inputLeaveTime(AttendanceRequest request);

    /**
     * 校验考勤信息.
     *
     * @param attendance 考勤信息
     * @param id         主键
     * @return 结果集
     */
    Attendance checkAttendanceData(Attendance attendance, Integer id);

    /**
     * 校验考勤信息.
     *
     * @param attendanceExcel 考勤信息
     * @return 结果集
     */
    AttendanceExcel checkAttendanceData(AttendanceExcel attendanceExcel);

    /**
     * 更新考勤信息.
     *
     * @param attendanceExcel 考勤信息
     * @return 结果集
     */
    AttendanceExcel updateAttentionInfo(AttendanceExcel attendanceExcel);

    /**
     * 查询考勤信息.
     *
     * @param attendance Attendance
     * @return Attendance
     */
    Attendance selectByPrimaryKey(Attendance attendance);

    /**
     * 更新考勤信息.
     *
     * @param attendance Attendance
     * @return boolean
     */
    boolean updateByPrimaryKey(Attendance attendance);

    /**
     * 添加打卡信息.
     *
     * @param punchedInfo PunchedInfo
     * @return boolean
     */
    boolean addPunchInfo(PunchedInfo punchedInfo);

    /**
     * 获取考勤异常列表.
     *
     * @param userId userId
     * @return List
     */
    List<Attendance> getRemindList(String userId);

    /**
     * 获取考勤异常列表.
     *
     * @param attendance Attendance
     * @return List
     */
    List<Attendance> getErrorByDate(Attendance attendance);

    /**
     * 获取checkDate当天的考勤记录.
     *
     * @param checkDate 日期
     * @return 考勤记录
     */
    List<Attendance> getAttendanceByCheckDate(String checkDate);

    /**
     * 外出和忘打卡公用的考勤导出用处理接口.
     * @param staff 员工信息
     * @param actHisList 流程
     * @return 结果
     */
    Map<String, Object> signGenerate(Staff staff, List<ActHis> actHisList);
}
