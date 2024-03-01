package zzjz.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.AttendanceRequest;
import zzjz.bean.Depart;
import zzjz.bean.Out;
import zzjz.bean.OutRequest;
import zzjz.bean.Over;
import zzjz.bean.PagingEntity;
import zzjz.bean.Punch;
import zzjz.bean.PunchedCard;
import zzjz.bean.PunchedInfo;
import zzjz.bean.Staff;
import zzjz.bean.Trip;
import zzjz.bean.TripDetail;
import zzjz.bean.Vacation;
import zzjz.bean.VacationDetail;
import zzjz.bean.WhiteList;
import zzjz.mapper.AttendanceMapper;
import zzjz.mapper.TripMapper;
import zzjz.mapper.VacationMapper;
import zzjz.service.AttendanceService;
import zzjz.service.CalendarService;
import zzjz.service.CommonService;
import zzjz.service.DepartService;
import zzjz.service.OutService;
import zzjz.service.OverService;
import zzjz.service.PunchService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
import zzjz.service.VacationService;
import zzjz.service.WhiteListService;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:28
 * @ClassName: AttendanceServiceImpl
 * @Description: 考勤管理实现类
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Double DOU05 = 0.5;

    private static final Double DOU3600000 = 3600000.0;

    private static final Double DOU005 = 0.05;

    private static final Double DOU025 = 0.25;

    private static final Double DOU14400000 = 14400000.0;

    private static final Integer INT16 = 16;

    private static final int INT180000 = 180000;

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    VacationMapper vacationMapper;

    @Autowired
    VacationService vacationService;

    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripService tripService;

    @Autowired
    PunchService punchService;

    @Autowired
    OverService overService;

    @Autowired
    OutService outService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    CommonService commonService;

    @Autowired
    StaffService staffService;

    @Autowired
    DepartService departService;

    @Autowired
    WhiteListService whiteListService;

    @Override
    public boolean addAttendanceList(List<AttendanceExcel> res) {
        return attendanceMapper.addAttendanceList(res) > 0;
    }

    @Override
    public Page<Attendance> getAttendanceList(AttendanceRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Attendance> pageList = (Page<Attendance>) attendanceMapper.getAttendancePageList(request);
        return pageList;
    }

    /**
     * 根据日期用户修改考勤.
     *
     * @param request 考勤信息
     * @return 结果
     */
    @Override
    public boolean inputAtt(AttendanceRequest request) {

        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);

        if (request.getAllDay() != null) {
            if (attendance.getAllDay() != null || attendance.getMorDay() != null || attendance.getAftDay() != null) {
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc() + "事项录入冲突；");
                return false;
            }

            attendance.setAllDay(request.getAllDay());

            if ("10".equals(attendance.getAllDay())) { //加班

            } else if ("20".equals(attendance.getAllDay()) || "21".equals(attendance.getAllDay())) { //出差外出
                Trip trip = new Trip();
                trip.setId(commonService.getActSeq());
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripTyp(attendance.getAllDay());
                trip.setTripDate(attendance.getCheckDate());
                trip.setBackDate(attendance.getCheckDate());
                trip.setTripPart("0");
                tripService.addTripInfo(trip);
                try {
                    tripService.initTripDetailInfo(trip);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else { //请假
                Vacation vacation = new Vacation();
                vacation.setId(commonService.getActSeq());
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getAllDay());
                vacation.setLeaveDate(attendance.getCheckDate());
                vacation.setBackDate(attendance.getCheckDate());
                vacation.setLeavePart("0");
                vacationService.addVacationInfo(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (request.getMorDay() != null) {
            if (attendance.getAllDay() != null || attendance.getMorDay() != null) {
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc() + "事项录入冲突；");
                return false;
            }

            attendance.setMorDay(request.getMorDay());

            if ("10".equals(attendance.getMorDay())) { //加班

            } else if ("20".equals(attendance.getMorDay()) || "21".equals(attendance.getMorDay())) { //出差外出
                Trip trip = new Trip();
                trip.setId(commonService.getActSeq());
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripTyp(attendance.getMorDay());
                trip.setTripDate(attendance.getCheckDate());
                trip.setBackDate(attendance.getCheckDate());
                trip.setTripPart("1");
                tripService.addTripInfo(trip);
                try {
                    tripService.initTripDetailInfo(trip);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else { //请假
                Vacation vacation = new Vacation();
                vacation.setId(commonService.getActSeq());
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getMorDay());
                vacation.setLeaveDate(attendance.getCheckDate());
                vacation.setBackDate(attendance.getCheckDate());
                vacation.setLeavePart("1");
                vacationService.addVacationInfo(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (request.getAftDay() != null) {
            if (attendance.getAllDay() != null || attendance.getAftDay() != null) {
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc() + "事项录入冲突；");
                return false;
            }

            attendance.setAftDay(request.getAftDay());

            if ("10".equals(attendance.getAftDay())) { //加班

            } else if ("20".equals(attendance.getAftDay()) || "21".equals(attendance.getAftDay())) { //出差外出
                Trip trip = new Trip();
                trip.setId(commonService.getActSeq());
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripTyp(attendance.getAftDay());
                trip.setTripDate(attendance.getCheckDate());
                trip.setBackDate(attendance.getCheckDate());
                trip.setTripPart("2");
                tripService.addTripInfo(trip);
                try {
                    tripService.initTripDetailInfo(trip);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else { //请假
                Vacation vacation = new Vacation();
                vacation.setId(commonService.getActSeq());
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getAftDay());
                vacation.setLeaveDate(attendance.getCheckDate());
                vacation.setBackDate(attendance.getCheckDate());
                vacation.setLeavePart("2");
                vacationService.addVacationInfo(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (request.getEveDay() != null) { //晚上，校验加班情况，不存在其他情况
            if (attendance.getEveDay() != null) {
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc() + "晚上事项录入冲突；");
                return false;
            }

            attendance.setEveDay(request.getEveDay());
        }

        //校验考勤信息
        attendance = checkAttendanceData(attendance, null);

        return attendanceMapper.updateByPrimaryKey(attendance) > 0;
    }

    @Override
    public boolean deleteAttInfo(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);
        attendance.setAllDay(null);
        attendance.setMorDay(null);
        attendance.setAftDay(null);
        attendance.setEveDay(null);

        //删除对应的事项表信息
        Vacation vacation = new Vacation();
        vacation.setUserId(attendance.getUserId());
        vacation.setLeaveDate(attendance.getCheckDate());
        vacationMapper.deleteSelective(vacation);
        Trip trip = new Trip();
        trip.setUserId(attendance.getUserId());
        trip.setTripDate(attendance.getCheckDate());
        tripMapper.deleteSelective(trip);

        //校验考勤信息
        attendance = checkAttendanceData(attendance, null);

        return attendanceMapper.updateByPrimaryKey(attendance) > 0;
    }

    @Override
    public boolean inputSignTime(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);
        attendance.setSignTime(request.getSignTime());

        //校验考勤信息
        attendance = checkAttendanceData(attendance, null);

        return attendanceMapper.updateByPrimaryKey(attendance) > 0;
    }

    @Override
    public boolean inputLeaveTime(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);
        attendance.setLeaveTime(request.getLeaveTime());

        //校验考勤信息
        attendance = checkAttendanceData(attendance, null);

        return attendanceMapper.updateByPrimaryKey(attendance) > 0;
    }

    /**
     * 校验考勤信息.
     *
     * @param attendance 考勤信息
     * @param id         id
     * @return 考勤信息
     */
    @Override
    public Attendance checkAttendanceData(Attendance attendance, Integer id) {
        if (attendance.getBeginTime() == null || "".equals(attendance.getBeginTime())) {
            // 上海研发考勤与其它地区不一样 这里暂时写死 todo 后续改为可配置
            if ("上海研发中心".equals(attendance.getDepart())) {
                attendance.setBeginTime("09:00");
            } else {
                attendance.setBeginTime("08:30");
            }
        }
        if (attendance.getEndTime() == null || "".equals(attendance.getEndTime())) {
            // 上海的下班时间跟其他地区一致
            attendance.setEndTime("17:30");
            /*if ("上海研发中心".equals(attendance.getDepart())) {
                attendance.setEndTime("18:00");
            } else {
                attendance.setEndTime("17:30");
            }*/
        }
        String errorDesc = "";
        attendance.setIsError("0");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        boolean isWorkday = calendarService.isWorkDay(attendance.getCheckDate());

        Object allDay = attendance.getAllDay(); //全天事项
        Object morDay = attendance.getMorDay(); //上午事项
        Object aftDay = attendance.getAftDay(); //下午事项

        //有签到，有签退
        if (isWorkday && StringUtils.isNotBlank(attendance.getSignTime()) && StringUtils.isNotBlank(attendance.getLeaveTime())) {
            try {
                Date mBeginTime = sdf.parse(attendance.getBeginTime()); //08:30
                mBeginTime = sdf.parse(sdf.format(mBeginTime.getTime() + INT180000));
                Date aEndTime = sdf.parse(attendance.getEndTime()); //17:30
                Date signTime = sdf.parse(attendance.getSignTime()); //签到时间
                Date leaveTime = sdf.parse(attendance.getLeaveTime()); //签退时间
                errorDesc += checkCommon(signTime, leaveTime, mBeginTime, aEndTime, allDay, morDay, aftDay);
                if (!"".equals(errorDesc)) {
                    attendance.setIsError("1");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (!isWorkday && !(attendance.getSignTime() == null && attendance.getLeaveTime() == null)) { //非工作日事项如果有签到，则必有签退
            if ((attendance.getSignTime() == null && attendance.getLeaveTime() != null) ||
                    (attendance.getSignTime() != null && attendance.getLeaveTime() == null)
                    || attendance.getSignTime().equals(attendance.getLeaveTime())) {
                attendance.setIsError("1");
                errorDesc += "未签到或未签退；";
            }
        }

        //计算迟到早退旷工加班
        attendance = initOthers(isWorkday, attendance);

        errorDesc += attendance.getErrorDesc();

        if ((attendance.getAllDay() != null && "20".equals(attendance.getAllDay())) || (attendance.getMorDay() != null
                && "20".equals(attendance.getMorDay())) || (attendance.getAftDay() != null && "20".equals(attendance.getAftDay()))) {
            if (errorDesc.contains("缺少加班单据；")) {
                errorDesc = "缺少加班单据；";
            } else if (errorDesc.contains("加班与单据不匹配；")) {
                errorDesc = "加班与单据不匹配；";
            } else {
                attendance.setIsError("0");
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                errorDesc = "";
            }
        }

        //校验当天考勤项是否冲突
        String desc = checkConflictByDate(attendance.getCheckDate(), attendance.getUserId(), id);

        attendance.setErrorDesc(errorDesc + desc);

        return attendance;
    }

    private String checkCommon(Date signTime, Date leaveTime, Date mBeginTime, Date aEndTime, Object allDay, Object morDay, Object aftDay)
            throws ParseException {
        String errorDesc = "";

        // 出差
        if ((allDay != null && "20".equals(allDay)) || (morDay != null && "20".equals(morDay)) || (aftDay != null && "20".equals(aftDay))) {
            return "";
        }

        // 外出
        if (StringUtils.isNotBlank(String.valueOf(allDay)) && "21".equals(allDay)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date mEndTime = sdf.parse("11:30"); //11:30
        Date aBeginTime = sdf.parse("13:30"); //13:30
        //08:30前签到
        if (signTime.before(mBeginTime) || signTime.equals(mBeginTime)) {
            //11:30-17:30签退
            if ((leaveTime.after(mEndTime) || leaveTime.equals(mEndTime)) && leaveTime.before(aEndTime)) {
                //全天事项（上午事项+下午事项）
                if (allDay != null || (morDay != null && aftDay != null)) {
                    errorDesc += "考勤与事项冲突；";
                }
                //上午事项
                if (morDay != null && aftDay == null) {
                    errorDesc += "考勤与事项冲突；";
                }
            }
            //17:30后签退,若存在事项，则考勤和事项冲突
            if ((leaveTime.after(aEndTime) || leaveTime.equals(aEndTime)) &&
                    ((allDay != null && !"21".equals(allDay)) || (morDay != null && !"21".equals(morDay))
                            || (aftDay != null && !"21".equals(aftDay)))) {
                errorDesc += "考勤与事项冲突；";
            }
        }

        //08:30-11:30签到
        if (signTime.after(mBeginTime) && signTime.before(mEndTime)) {
            //17:30后签退
            if (leaveTime.after(aEndTime) || leaveTime.equals(aEndTime)) {
                //下午事项
                if (morDay == null && aftDay != null) {
                    errorDesc += "考勤与事项冲突；";
                }
            }
        }

        //11:30-13:30签到
        if ((signTime.after(mEndTime) || signTime.equals(mEndTime)) && (signTime.before(aBeginTime) || signTime.equals(aBeginTime))) {
            //17:30后签退
            if (leaveTime.after(aEndTime) || leaveTime.equals(aEndTime)) {
                //全天事项（上午事项+下午事项）
                if (allDay != null || (morDay != null && aftDay != null)) {
                    errorDesc += "考勤事项冲突；";
                }
            }
        }

        //17:30后签到，17:30后签退，无效的签到签退
        if ((signTime.after(aEndTime) || signTime.equals(aEndTime)) && (leaveTime.after(aEndTime) || leaveTime.equals(aEndTime))) {
            errorDesc += "无效的签到签退；";
        }

        return errorDesc;
    }

    private Attendance initOthers(boolean isWorkday, Attendance attendance) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Object allDay = attendance.getAllDay(); //全天事项
            Object morDay = attendance.getMorDay(); //上午事项
            Object aftDay = attendance.getAftDay(); //下午事项
            String errorDesc = "";
            if (isWorkday) { //工作日
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                if (StringUtils.isBlank(attendance.getSignTime()) || StringUtils.isBlank(attendance.getLeaveTime())
                        || attendance.getSignTime().equals(attendance.getLeaveTime())) { //无签到或签退，无考勤事项，记为旷工
                    if (allDay == null && morDay == null && aftDay == null) {
                        attendance.setAbsent(1.0);
                        attendance.setIsError("1");
                        errorDesc += "未签到或未签退；";
                    } else if ((morDay != null && aftDay == null) || (morDay == null && aftDay != null)) {
                        attendance.setAbsent(DOU05);
                        errorDesc += "未签到或未签退；";
                    }
                    attendance.setOverTime(0.0);
                } else {
                    //全天无考勤事项
                    if (allDay == null && morDay == null && aftDay == null) {
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()
                                - sdf.parse(attendance.getBeginTime()).getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()
                                - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间
                        boolean type = true;

                        //哺乳假上午推迟一小时
                        WhiteList wl1 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "1", attendance.getCheckDate());
                        if (wl1 != null) {
                            type = false;
                            lateTime--;
                        }
                        //哺乳假下午提前一小时
                        WhiteList wl4 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "4", attendance.getCheckDate());
                        if (wl4 != null) {
                            earlyTime--;
                        }

                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLate(attendance, earlyTime, lateTime, type);
                        }
                    } else if (morDay != null && aftDay == null) { //上午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()
                                - sdf.parse("13:30").getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()
                                - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间

                        //哺乳假下午推迟一小时
                        WhiteList wl3 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "3", attendance.getCheckDate());
                        if (wl3 != null) {
                            lateTime--;
                        }
                        //哺乳假下午提前一小时
                        WhiteList wl4 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "4", attendance.getCheckDate());
                        if (wl4 != null) {
                            earlyTime--;
                        }

                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLate(attendance, earlyTime, lateTime, false);
                        }
                    } else if (morDay == null && aftDay != null) { //下午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()
                                - sdf.parse(attendance.getBeginTime()).getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse("11:30").getTime()
                                - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间
                        boolean type = true;

                        //哺乳假上午推迟一小时
                        WhiteList wl1 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "1", attendance.getCheckDate());
                        if (wl1 != null) {
                            type = false;
                            lateTime--;
                        }
                        //哺乳假上午提前一小时
                        WhiteList wl2 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "2", attendance.getCheckDate());
                        if (wl2 != null) {
                            earlyTime--;
                        }

                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLate(attendance, earlyTime, lateTime, type);
                        }
                    }
                    //迟到早退旷工
                    if (attendance.getLate() > 0) {
                        errorDesc += "迟到；";
                        attendance.setIsError("1");
                    }
                    if (attendance.getEarly() > 0) {
                        errorDesc += "早退；";
                        attendance.setIsError("1");
                    }
                    if (attendance.getAbsent() > 0) {
                        errorDesc += "旷工；";
                        attendance.setIsError("1");
                    }
                    //签退时间-下班时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime() -
                            sdf.parse(attendance.getEndTime()).getTime()) / DOU14400000) / 2;

                    //默认部门
                    String defaultDepartId = staffService.getStaffByUserId(attendance.getUserId()).getDepartId();
                    //根据部门和员工号，查询角色信息
                    String role = staffService.getStaffByDepartAndUser(attendance.getUserId(), Long.parseLong(defaultDepartId));
                    if ("1".equals(defaultDepartId) || "secondLeader".equals(role) || "firstLeader".equals(role) || workDay < 0) {
                        workDay = 0.0;
                    }

                    attendance.setOverTime(workDay);
                    Over over = new Over();
                    over.setOverDate(attendance.getCheckDate());
                    over.setUserId(attendance.getUserId());
                    over = overService.selectOverInfo(over);
                    //校验加班单据，平日加班记录在晚上
                    if (workDay > 0 && (attendance.getEveDay() == null || !"10".equals(attendance.getEveDay()))) {
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                        attendance.setOverTime(0.0);
                    } else {
                        if (over != null && !workDay.equals(over.getOverDay())) {
                            //判断是否满足外出排除
                            if (!isOutExcept(attendance.getUserId(), attendance.getCheckDate())) {
                                attendance.setIsError("1");
                                errorDesc += "加班与单据不匹配；";
                                attendance.setOverTime(0.0);
                            }

                        }
                    }
                }
            } else { //非工作日计算加班信息，前提是必须存在签到和签退
                if (attendance.getSignTime() != null && attendance.getLeaveTime() != null) {
                    //签退时间-签到时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()
                            - sdf.parse(attendance.getSignTime()).getTime()) / DOU14400000) / 2;
                    Staff staff = staffService.getStaffByUserId(attendance.getUserId());
                    //默认部门
                    String defaultDepartId = staff.getDepartId();
                    //根据部门和员工号，查询角色信息
                    String role = staffService.getStaffByDepartAndUser(attendance.getUserId(), Long.parseLong(defaultDepartId));

                    boolean isHoliday = calendarService.isHoliDay(attendance.getCheckDate());

                    if (workDay < 0 || (!isHoliday && ("1".equals(defaultDepartId) || "secondLeader".equals(role) || "firstLeader".equals(role)))) {
                        workDay = 0.0;
                    }

                    if (workDay > 1.0) {
                        workDay = 1.0;
                    }
                    attendance.setOverTime(workDay);

                    Over over = new Over();
                    over.setOverDate(attendance.getCheckDate());
                    over.setUserId(attendance.getUserId());
                    over = overService.selectOverInfo(over);
                    //校验加班单据
                    if (workDay > 0 && over == null) {
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                        attendance.setOverTime(0.0);
                    } else {
                        if (!workDay.equals(over.getOverDay())) {
                            attendance.setIsError("1");
                            errorDesc += "加班与单据不匹配；";
                            attendance.setOverTime(0.0);
                        }
                    }
                }
            }
            attendance.setErrorDesc(errorDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attendance;
    }

    /**
     * 判断是否符合外出加班例外.
     * @param userId 用户id
     * @param date 日期
     * @return 结果
     */
    public boolean isOutExcept(String userId, String date) {
        //当天存在下午外出和全天外出(已通过或已打印)的则不会校验考勤时间
        OutRequest outRequest = new OutRequest();
        outRequest.setUserId(userId);
        outRequest.setOutDate(date);
        outRequest.setPaging(new PagingEntity());
        Page<Out> outs = outService.getOutList(outRequest);
        for (Out out : outs) {
            if ("99".equals(out.getStatus()) || "97".equals(out.getStatus())) {
                if ("all".equals(out.getOutPart()) || "aft".equals(out.getOutPart())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 迟到早退计算
     *
     * @param attendance attendance
     * @param earlyTime  earlyTime
     * @param lateTime   lateTime
     * @return Attendance
     */
    private Attendance checkEarlyLate(Attendance attendance, Double earlyTime, Double lateTime, boolean type) {
        if (((lateTime > DOU005 && type) || (lateTime > 0 && !type)) && lateTime <= DOU025) {
            attendance.setLate(DOU025);
        } else if (lateTime > DOU025 && lateTime <= DOU05) {
            attendance.setLate(DOU05);
        } else if (lateTime > DOU05 && lateTime <= 1.0) {
            attendance.setLate(1.0);
        } else if (lateTime > 1) {
            attendance.setAbsent(1.0);
        }
        if (earlyTime > 0 && earlyTime <= DOU025) {
            attendance.setEarly(DOU025);
        } else if (earlyTime > DOU025 && earlyTime <= DOU05) {
            attendance.setEarly(DOU05);
        } else if (earlyTime > DOU05 && earlyTime <= 1.0) {
            attendance.setEarly(1.0);
        } else if (earlyTime > 1) {
            attendance.setAbsent(1.0);
        }
        return attendance;
    }

    /**
     * 迟到早退计算Excel
     *
     * @param attendanceExcel attendanceExcel
     * @param earlyTime       earlyTime
     * @param lateTime        lateTime
     * @return AttendanceExcel
     */
    private AttendanceExcel checkEarlyLateExcel(AttendanceExcel attendanceExcel, Double earlyTime, Double lateTime, boolean type) {
        if (lateTime > 1) {
            attendanceExcel.setAbsent(1.0);
        } else if (lateTime > DOU05 && lateTime <= 1.0) {
            attendanceExcel.setLate(1.0);
        } else if (lateTime > DOU025 && lateTime <= DOU05) {
            attendanceExcel.setLate(DOU05);
        } else if (((lateTime > DOU005 && type) || (lateTime > 0 && !type)) && lateTime <= DOU025) {
            attendanceExcel.setLate(DOU025);
        }
        if (earlyTime > 1) {
            attendanceExcel.setAbsent(1.0);
        } else if (earlyTime > DOU05 && earlyTime <= 1.0) {
            attendanceExcel.setEarly(1.0);
        } else if (earlyTime > DOU025 && earlyTime <= DOU05) {
            attendanceExcel.setEarly(DOU05);
        } else if (earlyTime > 0 && earlyTime <= DOU025) {
            attendanceExcel.setEarly(DOU025);
        }
        return attendanceExcel;
    }

    @Override
    public AttendanceExcel checkAttendanceData(AttendanceExcel attendance) {
        if (attendance.getBeginTime() == null || "".equals(attendance.getBeginTime())) {
            // 上海研发考勤与其它地区不一样 这里暂时写死 todo 后续改为可配置
            if ("上海研发中心".equals(attendance.getDepart())) {
                attendance.setBeginTime("09:00");
            } else {
                attendance.setBeginTime("08:30");
            }
        }
        if (attendance.getEndTime() == null || "".equals(attendance.getEndTime())) {
            attendance.setEndTime("17:30");
        }
        String errorDesc = "";
        attendance.setIsError("0");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String checkDate;
        try {
            checkDate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(attendance.getCheckDate()));
        } catch (ParseException e) {
            checkDate = attendance.getCheckDate();
        }
        boolean isWorkday = calendarService.isWorkDay(checkDate);
        attendance.setWorkDay(isWorkday ? "1" : "0");
        attendance.setCheckDate(checkDate);

        Object allDay = attendance.getAllDay(); //全天事项
        Object morDay = attendance.getMorDay(); //上午事项
        Object aftDay = attendance.getAftDay(); //下午事项

        //有签到，有签退
        if (isWorkday && attendance.getSignTime() != null && attendance.getLeaveTime() != null) {
            try {
                Date mBeginTime = sdf.parse(attendance.getBeginTime()); //08:30
                mBeginTime = sdf.parse(sdf.format(mBeginTime.getTime() + INT180000));
                Date aEndTime = sdf.parse(attendance.getEndTime()); //17:30
                Date signTime = sdf.parse(attendance.getSignTime()); //签到时间
                Date leaveTime = sdf.parse(attendance.getLeaveTime()); //签退时间

                errorDesc += checkCommon(signTime, leaveTime, mBeginTime, aEndTime, allDay, morDay, aftDay);
                if (!"".equals(errorDesc)) {
                    attendance.setIsError("1");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (!isWorkday && !(attendance.getSignTime() == null && attendance.getLeaveTime() == null)) { //非工作日事项如果有签到，则必有签退
            if ((attendance.getSignTime() == null && attendance.getLeaveTime() != null) ||
                    (attendance.getSignTime() != null && attendance.getLeaveTime() == null) ||
                    (attendance.getSignTime() != null && attendance.getLeaveTime() != null
                            && attendance.getSignTime().equals(attendance.getLeaveTime()))) {
                attendance.setIsError("1");
                errorDesc += "未签到或未签退；";
            }
        }

        //计算迟到早退旷工加班
        attendance = initOthersExcel(isWorkday, attendance);

        errorDesc += attendance.getErrorDesc();

        if ((attendance.getAllDay() != null && "20".equals(attendance.getAllDay())) || (attendance.getMorDay() != null
                && "20".equals(attendance.getMorDay())) || (attendance.getAftDay() != null && "20".equals(attendance.getAftDay()))) {
            if (errorDesc.contains("缺少加班单据；")) {
                errorDesc = "缺少加班单据；";
            } else if (errorDesc.contains("加班与单据不匹配；")) {
                errorDesc = "加班与单据不匹配；";
            } else {
                attendance.setIsError("0");
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                errorDesc = "";
            }
        }

        //校验当天考勤项是否冲突
        String desc = checkConflictByDate(attendance.getCheckDate(), attendance.getUserId(), null);

        attendance.setErrorDesc(errorDesc + desc);

        return attendance;
    }

    private AttendanceExcel initOthersExcel(boolean isWorkday, AttendanceExcel attendance) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Object allDay = attendance.getAllDay(); //全天事项
            Object morDay = attendance.getMorDay(); //上午事项
            Object aftDay = attendance.getAftDay(); //下午事项
            String errorDesc = "";
            if (isWorkday) { //工作日
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                if (attendance.getSignTime() == null || attendance.getLeaveTime() == null
                        || attendance.getSignTime().equals(attendance.getLeaveTime())) { //无签到或签退，无考勤事项，记为旷工
                    if (allDay == null && morDay == null && aftDay == null) {
                        attendance.setAbsent(1.0);
                        attendance.setIsError("1");
                        errorDesc += "未签到或未签退；";
                    } else if ((morDay != null && aftDay == null) || (morDay == null && aftDay != null)) {
                        attendance.setAbsent(DOU05);
                        errorDesc += "未签到或未签退；";
                    }
                    attendance.setOverTime(0.0);
                } else {
                    //全天无考勤事项
                    if (allDay == null && morDay == null && aftDay == null) {
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()
                                - sdf.parse(attendance.getBeginTime()).getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()
                                - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间
                        boolean type = true;

                        //哺乳假上午推迟一小时
                        WhiteList wl1 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "1", attendance.getCheckDate());
                        if (wl1 != null) {
                            lateTime--;
                        }
                        //哺乳假下午提前一小时
                        WhiteList wl4 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "4", attendance.getCheckDate());
                        if (wl4 != null) {
                            earlyTime--;
                        }
                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLateExcel(attendance, earlyTime, lateTime, type);
                        }
                    } else if (morDay != null && aftDay == null) { //上午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime() - sdf.parse("13:30").getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()
                                - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间

                        //哺乳假下午推迟一小时
                        WhiteList wl3 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "3", attendance.getCheckDate());
                        if (wl3 != null) {
                            lateTime--;
                        }
                        //哺乳假下午提前一小时
                        WhiteList wl4 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "4", attendance.getCheckDate());
                        if (wl4 != null) {
                            earlyTime--;
                        }
                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLateExcel(attendance, earlyTime, lateTime, false);
                        }
                    } else if (morDay == null && aftDay != null) { //下午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()
                                - sdf.parse(attendance.getBeginTime()).getTime()) / DOU3600000; //迟到时间
                        Double earlyTime = (sdf.parse("11:30").getTime() - sdf.parse(attendance.getLeaveTime()).getTime()) / DOU3600000; //早退时间
                        boolean type = true;

                        //哺乳假上午推迟一小时
                        WhiteList wl1 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "1", attendance.getCheckDate());
                        if (wl1 != null) {
                            type = false;
                            lateTime--;
                        }
                        //哺乳假上午提前一小时
                        WhiteList wl2 = whiteListService.getWhiteListInfoByUserIdAndType(attendance.getUserId(), "2", attendance.getCheckDate());
                        if (wl2 != null) {
                            earlyTime--;
                        }

                        if (attendance.getAbsent() == null || attendance.getAbsent() == 0.0) {
                            attendance = checkEarlyLateExcel(attendance, earlyTime, lateTime, type);
                        }
                    }

                    //迟到早退旷工
                    if (attendance.getLate() > 0) {
                        errorDesc += "迟到；";
                        attendance.setIsError("1");
                    }
                    if (attendance.getEarly() > 0) {
                        errorDesc += "早退；";
                        attendance.setIsError("1");
                    }
                    if (attendance.getAbsent() > 0) {
                        errorDesc += "旷工；";
                        attendance.setIsError("1");
                    }

                    //签退时间-下班时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()
                            - sdf.parse(attendance.getEndTime()).getTime()) / DOU14400000) / 2;

                    Staff staff = staffService.getStaffByUserId(attendance.getUserId());
                    //默认部门
                    String defaultDepartId = staff.getDepartId();
                    //根据部门和员工号，查询角色信息
                    String role = staffService.getStaffByDepartAndUser(attendance.getUserId(), Long.parseLong(defaultDepartId));
                    if ("1".equals(defaultDepartId) || "secondLeader".equals(role) || "firstLeader".equals(role) || workDay < 0) {
                        workDay = 0.0;
                    }

                    if (workDay > 1.0) {
                        workDay = 1.0;
                    }

                    attendance.setOverTime(workDay);

                    Over over = new Over();
                    over.setOverDate(attendance.getCheckDate());
                    over.setUserId(attendance.getUserId());
                    over = overService.selectOverInfo(over);
                    //校验加班单据，平日加班记录在晚上
                    if (workDay > 0 && (attendance.getEveDay() == null || !"10".equals(attendance.getEveDay()))) {
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                        attendance.setOverTime(0.0);
                    } else {
                        if (over != null && !workDay.equals(over.getOverDay())) {
                            //判断是否满足外出排除
                            if (!isOutExcept(attendance.getUserId(), attendance.getCheckDate())) {
                                attendance.setIsError("1");
                                errorDesc += "加班与单据不匹配；";
                                attendance.setOverTime(0.0);
                            }
                        }
                    }
                }
            } else { //非工作日计算加班信息，前提是必须存在签到和签退
                if (attendance.getSignTime() != null && attendance.getLeaveTime() != null) {
                    //签退时间-签到时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()
                            - sdf.parse(attendance.getSignTime()).getTime()) / DOU14400000) / 2;
                    Staff staff = staffService.getStaffByUserId(attendance.getUserId());
                    //默认部门
                    String defaultDepartId = staff.getDepartId();
                    //根据部门和员工号，查询角色信息
                    String role = staffService.getStaffByDepartAndUser(attendance.getUserId(), Long.parseLong(defaultDepartId));

                    boolean isHoliday = calendarService.isHoliDay(attendance.getCheckDate());

                    if (workDay < 0 || (!isHoliday && ("1".equals(defaultDepartId) || "secondLeader".equals(role) || "firstLeader".equals(role)))) {
                        workDay = 0.0;
                    }
                    attendance.setOverTime(workDay);
                    Over over = new Over();
                    over.setOverDate(attendance.getCheckDate());
                    over.setUserId(attendance.getUserId());
                    over = overService.selectOverInfo(over);
                    //校验加班单据，非工作日加班记录在全天
                    if (workDay > 0 && (attendance.getAllDay() == null || !"10".equals(attendance.getAllDay()))) {
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                        attendance.setOverTime(0.0);
                    } else {
                        if (over != null && !workDay.equals(over.getOverDay())) {
                            attendance.setIsError("1");
                            errorDesc += "加班与单据不匹配；";
                            attendance.setOverTime(0.0);
                        }
                    }
                }
            }
            attendance.setErrorDesc(errorDesc);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return attendance;
    }

    private String checkConflictByDate(String checkDate, String userId, Integer id) {
        String result = "";
        //根据时间和用户查询当天的所有考勤事项
        List<Map<String, String>> list = null;
        if (id != null) {
            list = attendanceMapper.getAllInfoWithId(checkDate, userId, id);
        } else {
            list = attendanceMapper.getAllInfo(checkDate, userId);
        }
        if (list != null && list.size() > 1) {
            Map<String, String> morMap = new HashMap<String, String>();
            Map<String, String> aftMap = new HashMap<String, String>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                String optionPart = map.get("optionPart");
                String optionType = map.get("optionType");
                if ("mor".equals(optionPart)) {
                    morMap.put(i + "", optionType);
                } else if ("aft".equals(optionPart)) {
                    aftMap.put(i + "", optionPart);
                } else if ("all".equals(optionPart)) {
                    morMap.put(i + "", optionType);
                    aftMap.put(i + "", optionPart);
                }
            }
            if (morMap.size() > 1) {
                result += "上午事项冲突；";
            }
            if (aftMap.size() > 1) {
                result += "下午事项冲突；";
            }
        }
        return result;
    }

    @Override
    public AttendanceExcel updateAttentionInfo(AttendanceExcel attendance) {
        String userId = attendance.getUserId();
        String date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(attendance.getCheckDate()));
        } catch (ParseException e) {
            date = attendance.getCheckDate();
        }
        //更新签到签退信息（忘打卡）
        Punch punchRequest = new Punch();
        punchRequest.setUserId(userId);
        punchRequest.setPunchDate(date);
        List<Punch> punchList = punchService.selectPunchInfo(punchRequest);
        if (punchList != null && punchList.size() > 0) {
            for (Punch punch : punchList) {
                if ("0".equals(punch.getPunchTyp())) {
                    attendance.setSignTime(attendance.getBeginTime());
                }
                if ("1".equals(punch.getPunchTyp())) {
                    attendance.setLeaveTime("12:30");
                }
                if ("2".equals(punch.getPunchTyp())) {
                    attendance.setSignTime("12:30");
                }
                if ("3".equals(punch.getPunchTyp())) {
                    attendance.setLeaveTime(attendance.getEndTime());
                }
            }
        }

        //更新加班信息
        Over over = new Over();
        over.setUserId(userId);
        over.setOverDate(date);
        over = overService.selectOverInfo(over);
        if (over != null) {
            boolean isWorkday = calendarService.isWorkDay(date);
            if (isWorkday) {
                attendance.setEveDay("10");
            } else {
                attendance.setAllDay("10");
            }
        }

        //更新外出信息
        Out outRequest = new Out();
        outRequest.setUserId(userId);
        outRequest.setOutDate(date);
        List<Out> outList = outService.selectOutInfo(outRequest);
        if (outList != null && outList.size() > 0) {
            for (Out out : outList) {
                if ("mor".equals(out.getOutPart())) {
                    attendance.setMorDay("21");
                } else if ("aft".equals(out.getOutPart())) {
                    attendance.setAftDay("21");
                } else if ("all".equals(out.getOutPart())) {
                    attendance.setAllDay("21");
                }
            }
        }

        //更新出差信息
        TripDetail tripRequest = new TripDetail();
        tripRequest.setUserId(userId);
        tripRequest.setTripDate(date);
        List<TripDetail> tripList = tripService.selectTripInfo(tripRequest);
        if (tripList != null && tripList.size() > 0) {
            for (TripDetail trip : tripList) {
                if ("mor".equals(trip.getTripPart())) {
                    attendance.setMorDay("20");
                } else if ("aft".equals(trip.getTripPart())) {
                    attendance.setAftDay("20");
                } else if ("all".equals(trip.getTripPart())) {
                    attendance.setAllDay("20");
                }
            }
        }

        //更新请假信息
        VacationDetail vacationRequest = new VacationDetail();
        vacationRequest.setUserId(userId);
        vacationRequest.setLeaveDate(date);
        List<VacationDetail> vacationList = vacationService.selectVacationInfo(vacationRequest);
        if (vacationList != null && vacationList.size() > 0) {
            for (VacationDetail vacation : vacationList) {
                if ("mor".equals(vacation.getLeavePart())) {
                    attendance.setMorDay(vacation.getLeaveTyp());
                } else if ("aft".equals(vacation.getLeavePart())) {
                    attendance.setAftDay(vacation.getLeaveTyp());
                } else if ("all".equals(vacation.getLeavePart())) {
                    attendance.setAllDay(vacation.getLeaveTyp());
                }
            }
        }
        return attendance;
    }

    @Override
    public Attendance selectByPrimaryKey(Attendance attendance) {
        return attendanceMapper.selectByPrimaryKey(attendance);
    }

    @Override
    public boolean updateByPrimaryKey(Attendance attendance) {
        return attendanceMapper.updateByPrimaryKey(attendance) > 0;
    }


    @Override
    public boolean addPunchInfo(PunchedInfo punchedInfo) {
        List<AttendanceExcel> attendanceExcelList = new ArrayList<AttendanceExcel>();

        for (PunchedCard punchedCard : punchedInfo.getLogs()) {

            String date = punchedCard.getYear() + "-" + (punchedCard.getMonth().length() == 1 ? "0"
                    + punchedCard.getMonth() : punchedCard.getMonth()) + "-" + (punchedCard.getDay().length() == 1 ? "0"
                    + punchedCard.getDay() : punchedCard.getDay());
            String time = (punchedCard.getHour().length() == 1 ? "0" + punchedCard.getHour() : punchedCard.getHour()) + ":"
                    + (punchedCard.getMinute().length() == 1 ? "0" + punchedCard.getMinute() : punchedCard.getMinute());

            if (attendanceExcelList.size() > 0) {
                boolean isExist = false;
                for (AttendanceExcel attendanceExcel : attendanceExcelList) {

                    if (attendanceExcel.getUserId().equals(punchedCard.getUid()) && attendanceExcel.getCheckDate().equals(date)) {
                        isExist = true;

                        if (!compareTime(attendanceExcel.getSignTime(), time)) {
                            attendanceExcel.setSignTime(time);
                        }
                        if (compareTime(attendanceExcel.getLeaveTime(), time)) {
                            attendanceExcel.setLeaveTime(time);
                        }
                    }
                }
                if (!isExist) {
                    AttendanceExcel attendance = new AttendanceExcel();
                    attendance.setUserId(punchedCard.getUid());
                    Staff staff = staffService.getStaffByUserId(punchedCard.getUid());
                    if (staff != null) {
                        attendance.setUserName(staff.getName());
                        if (StringUtils.isNotBlank(staff.getDepartId())) {
                            Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId()));
                            attendance.setDepart(depart.getDepartName());
                        }

                    }
                    attendance.setCheckDate(date);
                    attendance.setSignTime(time);
                    attendance.setLeaveTime(time);
                    attendanceExcelList.add(attendance);
                }

            } else {
                AttendanceExcel attendance = new AttendanceExcel();
                attendance.setUserId(punchedCard.getUid());
                Staff staff = staffService.getStaffByUserId(punchedCard.getUid());
                if (staff != null) {
                    attendance.setUserName(staff.getName());
                    if (StringUtils.isNotBlank(staff.getDepartId())) {
                        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId()));
                        attendance.setDepart(depart.getDepartName());
                    }
                }
                attendance.setCheckDate(date);
                attendance.setSignTime(time);
                attendance.setLeaveTime(time);
                attendanceExcelList.add(attendance);
            }
        }
        for (AttendanceExcel attendance : attendanceExcelList) {
            // 对照其他考勤事项
            attendance = updateAttentionInfo(attendance);
            // 生成错误信息
            checkAttendanceData(attendance);
        }
        /*if (selectByPrimaryKey(attendance)) {
        }*/
        return addAttendanceList(attendanceExcelList);
        //return true;

    }

    @Override
    public List<Attendance> getRemindList(String userId) {
        // 考勤周期改为自然月
        String beginDate = DateTime.now().dayOfMonth().withMinimumValue().toString("yyyy-MM-dd");
        String endDate = DateTime.now().dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");
        return attendanceMapper.getRemindListByUserId(userId, beginDate, endDate);
    }

    @Override
    public List<Attendance> getErrorByDate(Attendance attendance) {
        return attendanceMapper.getErrorByDate(attendance);
    }

    @Override
    public List<Attendance> getAttendanceByCheckDate(String checkDate) {
        return attendanceMapper.getAttendanceByCheckDate(checkDate);
    }

    @Override
    public Map<String, Object> signGenerate(Staff staff, List<ActHis> actHisList) {
        Map<String, Object> map = new HashMap<>();
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(String.valueOf(staff.getStaffId()), Long.parseLong(defaultDepartId));
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String firstPath = "";
        String firstTime = "";
        String secondPath = "";
        String secondTime = "";
        for (ActHis actHis : actHisList) {
            //部门经理或总经办成员
            if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
                if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                    //部门经理自动审批 默认是没有审批人的 这段代码可省略
                    secondPath = staffService.getSignPath(actHis.getAssignee());
                    secondTime = sdf2.format(actHis.getEndTime());
                }
            } else {
                if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                    if ("firstAudit".equals(actHis.getActId())) {
                        firstPath = staffService.getSignPath(actHis.getAssignee());
                        firstTime = sdf2.format(actHis.getEndTime());
                    }
                    if ("secondAudit".equals(actHis.getActId())) {
                        secondPath = staffService.getSignPath(actHis.getAssignee());
                        secondTime = sdf2.format(actHis.getEndTime());
                    }
                }
            }
        }
        ImageEntity firstImage = new ImageEntity();
        ImageEntity secondImage = new ImageEntity();
        try {
            if (StringUtils.isNotBlank(firstPath)) {
                firstImage.setData(FileUtils.readFileToByteArray(new File(firstPath)));
            }
            if (StringUtils.isNotBlank(secondPath)) {
                secondImage.setData(FileUtils.readFileToByteArray(new File(secondPath)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("firstImage", firstImage);
        map.put("firstTime", firstTime);
        map.put("secondImage", secondImage);
        map.put("secondTime", secondTime);
        return map;
    }

    /**
     * 比较时间 targetTime大则为true
     *
     * @param originTime
     * @param targetTime
     * @return
     */
    boolean compareTime(String originTime, String targetTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        assert StringUtils.isNotBlank(originTime) && StringUtils.isNotBlank(targetTime);
        Date oTime = null;
        Date tTime = null;
        try {
            oTime = sdf.parse(originTime);
            tTime = sdf.parse(targetTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ((tTime == null ? 0 : tTime.getTime()) > (oTime == null ? 0 : oTime.getTime())) {
            return true;
        }
        return false;

    }
}
