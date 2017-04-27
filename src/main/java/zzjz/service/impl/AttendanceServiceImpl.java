package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.*;
import zzjz.mapper.AttendanceMapper;
import zzjz.mapper.TripMapper;
import zzjz.mapper.VacationMapper;
import zzjz.service.AttendanceService;
import zzjz.service.CalendarService;
import zzjz.service.VacationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日15:28
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    VacationMapper vacationMapper;

    @Autowired
    VacationService vacationService;

    @Autowired
    TripMapper tripMapper;

    @Autowired
    CalendarService calendarService;

    @Override
    public boolean addAttendanceList(List<AttendanceExcel> res) {
        return attendanceMapper.addAttendanceList(res)> 0;
    }

    @Override
    public Page<Attendance> getAttendanceList(AttendanceRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Attendance> tripPage = (Page<Attendance>) attendanceMapper.getAttendancePageList(request);
        long total = tripPage.getTotal();
        return tripPage;
    }

    /**
     * 根据日期用户修改考勤
     * @param request 考勤信息
     * @return
     */
    @Override
    public boolean inputAtt(AttendanceRequest request) {

        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);

        if(request.getAllDay()!=null){
            if(attendance.getAllDay()!=null||attendance.getMorDay()!=null||attendance.getAftDay()!=null){
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc()+"事项录入冲突；");
                return false;
            }

            attendance.setAllDay(request.getAllDay());

            if("10".equals(attendance.getAllDay())){//加班

            } else if("20".equals(attendance.getAllDay())||"21".equals(attendance.getAllDay())){//出差外出
                Trip trip = new Trip();
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripDay(1.0);
                trip.setaTripDate(attendance.getCheckDate());
                tripMapper.insert(trip);
            } else {//请假
                Vacation vacation = new Vacation();
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getAllDay());
                vacation.setLeaveDay(1.0);
                vacation.setLeaveDate(attendance.getCheckDate());
                vacationMapper.insert(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(request.getMorDay()!=null){
            if(attendance.getAllDay()!=null||attendance.getMorDay()!=null){
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc()+"事项录入冲突；");
                return false;
            }

            attendance.setMorDay(request.getMorDay());

            if("10".equals(attendance.getMorDay())){//加班

            } else if("20".equals(attendance.getMorDay())||"21".equals(attendance.getMorDay())){//出差外出
                Trip trip = new Trip();
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripDay(0.5);
                trip.setaTripDate(attendance.getCheckDate());
                tripMapper.insert(trip);
            } else {//请假
                Vacation vacation = new Vacation();
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getAllDay());
                vacation.setLeaveDay(0.5);
                vacation.setLeaveDate(attendance.getCheckDate());
                vacationMapper.insert(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(request.getAftDay()!=null){
            if(attendance.getAllDay()!=null||attendance.getAftDay()!=null){
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc()+"事项录入冲突；");
                return false;
            }

            attendance.setAftDay(request.getAftDay());

            if("10".equals(attendance.getAftDay())){//加班

            } else if("20".equals(attendance.getAftDay())||"21".equals(attendance.getAftDay())){//出差外出
                Trip trip = new Trip();
                trip.setUserId(attendance.getUserId());
                trip.setUserName(attendance.getUserName());
                trip.setTripDay(0.5);
                trip.setaTripDate(attendance.getCheckDate());
                tripMapper.insert(trip);
            } else {//请假
                Vacation vacation = new Vacation();
                vacation.setUserId(attendance.getUserId());
                vacation.setUserName(attendance.getUserName());
                vacation.setLeaveTyp(attendance.getAllDay());
                vacation.setLeaveDay(0.5);
                vacation.setLeaveDate(attendance.getCheckDate());
                vacationMapper.insert(vacation);
                try {
                    vacationService.initVacationDetailInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(request.getEveDay()!=null){//晚上，校验加班情况，不存在其他情况
            if(attendance.getEveDay()!=null){
                attendance.setIsError("1");
                attendance.setErrorDesc(attendance.getErrorDesc()+"晚上事项录入冲突；");
                return false;
            }

            attendance.setEveDay(request.getEveDay());
        }

        //校验考勤信息
        attendance = checkAttendanceData(attendance);

        return attendanceMapper.updateByPrimaryKey(attendance)>0;
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
        trip.setaTripDate(attendance.getCheckDate());
        tripMapper.deleteSelective(trip);

        //校验考勤信息
        attendance = checkAttendanceData(attendance);

        return attendanceMapper.updateByPrimaryKey(attendance)>0;
    }

    @Override
    public boolean inputSignTime(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);
        attendance.setSignTime(request.getSignTime());

        //校验考勤信息
        attendance = checkAttendanceData(attendance);

        return attendanceMapper.updateByPrimaryKey(attendance)>0;
    }

    @Override
    public boolean inputLeaveTime(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUserId(request.getUserId());
        attendance.setCheckDate(request.getCheckDate());
        attendance = attendanceMapper.selectByPrimaryKey(attendance);
        attendance.setLeaveTime(request.getLeaveTime());

        //校验考勤信息
        attendance = checkAttendanceData(attendance);

        return attendanceMapper.updateByPrimaryKey(attendance)>0;
    }

    /**
     * 校验考勤信息
     * @param attendance
     * @return
     */
    public Attendance checkAttendanceData(Attendance attendance) {

        String errorDesc = "";
        attendance.setIsError("0");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        boolean isWorkday = calendarService.isWorkDay(attendance.getCheckDate());

        Object allDay = attendance.getAllDay();//全天事项
        Object morDay = attendance.getMorDay();//上午事项
        Object aftDay = attendance.getAftDay();//下午事项

        //有签到，有签退
        if(isWorkday&&attendance.getSignTime()!=null&&attendance.getLeaveTime()!=null){
            try {
                Date m_beginTime = sdf.parse(attendance.getBeginTime());//08:30
                Date m_endTime = sdf.parse("11:30");//11:30
                Date a_beginTime = sdf.parse("13:30");//13:30
                Date a_endTime = sdf.parse(attendance.getEndTime());//17:30
                Date signTime = sdf.parse(attendance.getSignTime());//签到时间
                Date leaveTime = sdf.parse(attendance.getLeaveTime());//签退时间
                //08:30前签到
                if(signTime.before(m_beginTime)||signTime.equals(m_beginTime)){
                    //11:30前签退，无效的签到签退
                    if(leaveTime.before(m_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                    }
                    //11:30-17:30签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //全天事项（上午事项+下午事项）
                        if(allDay!=null||(morDay!=null&&aftDay!=null)){
                            attendance.setIsError("1");
                            errorDesc += "考勤与事项冲突；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "考勤与事项冲突；早退；";
                        }
                    }
                    //17:30后签退,若存在事项，则考勤和事项冲突
                    if((leaveTime.after(a_endTime)||leaveTime.equals(a_endTime))&&(allDay!=null||morDay!=null||aftDay!=null)){
                        attendance.setIsError("1");
                        errorDesc += "考勤与事项冲突；";
                    }
                }

                //08:30-11:30签到
                if(signTime.after(m_beginTime)&&signTime.before(m_endTime)){
                    //08:30-11:30签退
                    if(leaveTime.after(m_beginTime)&&leaveTime.before(m_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //11:30-17:30签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；考勤与事项冲突；";
                        }
                    }
                }

                //11:30-13:30签到
                if((signTime.after(m_endTime)||signTime.equals(m_endTime))&&(signTime.before(a_beginTime)||signTime.equals(a_beginTime))){
                    //11:30-13:30签退，无效的签到签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&(leaveTime.before(a_beginTime)||leaveTime.equals(a_beginTime))){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //13:30-17:30签退
                    if(leaveTime.after(a_beginTime)&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //全天事项（上午事项+下午事项）
                        if(allDay!=null||(morDay!=null&&aftDay!=null)){
                            attendance.setIsError("1");
                            errorDesc += "迟到；考勤事项冲突；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                }

                //13:30-17:30签到
                if(signTime.after(a_beginTime)&&signTime.before(a_endTime)){
                    //13:30-17:30签退
                    if(leaveTime.after(a_beginTime)&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                    }
                }

                //17:30后签到，17:30后签退，无效的签到签退
                if((signTime.after(a_endTime)||signTime.equals(a_endTime))&&(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime))){
                    attendance.setIsError("1");
                    errorDesc += "无效的签到签退；";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(!isWorkday){//非工作日事项如果有签到，则必有签退
            if((attendance.getSignTime()==null&&attendance.getLeaveTime()!=null)||(attendance.getSignTime()!=null&&attendance.getLeaveTime()==null)){
                attendance.setIsError("1");
                errorDesc += "未签到或未签退；";
            }
        }

        //计算迟到早退旷工加班
        try{
            if(isWorkday){//工作日
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                if(attendance.getSignTime()==null||attendance.getLeaveTime()==null){//无签到或签退，无考勤事项，记为旷工
                    if(allDay==null&&morDay==null&&aftDay==null){
                        attendance.setAbsent(1.0);
                        attendance.setIsError("1");
                        errorDesc += "未签到或未签退；";
                    } else if((morDay!=null&&aftDay==null)||(morDay==null&&aftDay!=null)){
                        attendance.setAbsent(0.5);
                        errorDesc += "未签到或未签退；";
                    }
                    attendance.setOverTime(0.0);
                }else{
                    //全天无考勤事项
                    if(allDay==null&&morDay==null&&aftDay==null){
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse(attendance.getBeginTime()).getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(1.0);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(1.0);
                            }
                        }
                    }else if(morDay!=null&&aftDay==null){//上午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse("13:30").getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }
                    }else if(morDay==null&&aftDay!=null){//下午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse(attendance.getBeginTime()).getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse("11:30").getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }
                    }

                    //签退时间-下班时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()-sdf.parse(attendance.getEndTime()).getTime())/14400000.0)/2;
                    if(workDay<0){
                        workDay = 0.0;
                    }
                    attendance.setOverTime(workDay);
                    //校验加班单据，平日加班记录在晚上
                    if(workDay>0&&(attendance.getEveDay()==null||!"10".equals(attendance.getEveDay()))){
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                    }
                }
            }else{//非工作日计算加班信息，前提是必须存在签到和签退
                if(attendance.getSignTime()!=null&&attendance.getLeaveTime()!=null){
                    //签退时间-签到时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()-sdf.parse(attendance.getSignTime()).getTime())/14400000.0)/2;
                    if(workDay<0){
                        workDay = 0.0;
                    }
                    attendance.setOverTime(workDay);
                    //校验加班单据，非工作日加班记录在全天
                    if(workDay>0&&(attendance.getAllDay()==null||!"10".equals(attendance.getAllDay()))){
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        attendance.setErrorDesc(errorDesc);

        return attendance;
    }

    @Override
    public AttendanceExcel checkAttendanceData(AttendanceExcel attendance) {

        String errorDesc = "";
        attendance.setIsError("0");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        boolean isWorkday = calendarService.isWorkDay(attendance.getCheckDate());

        Object allDay = attendance.getAllDay();//全天事项
        Object morDay = attendance.getMorDay();//上午事项
        Object aftDay = attendance.getAftDay();//下午事项

        //有签到，有签退
        if(isWorkday&&attendance.getSignTime()!=null&&attendance.getLeaveTime()!=null){
            try {
                Date m_beginTime = sdf.parse(attendance.getBeginTime());//08:30
                Date m_endTime = sdf.parse("11:30");//11:30
                Date a_beginTime = sdf.parse("13:30");//13:30
                Date a_endTime = sdf.parse(attendance.getEndTime());//17:30
                Date signTime = sdf.parse(attendance.getSignTime());//签到时间
                Date leaveTime = sdf.parse(attendance.getLeaveTime());//签退时间
                //08:30前签到
                if(signTime.before(m_beginTime)||signTime.equals(m_beginTime)){
                    //11:30前签退，无效的签到签退
                    if(leaveTime.before(m_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                    }
                    //11:30-17:30签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //全天事项（上午事项+下午事项）
                        if(allDay!=null||(morDay!=null&&aftDay!=null)){
                            attendance.setIsError("1");
                            errorDesc += "考勤与事项冲突；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "考勤与事项冲突；早退；";
                        }
                    }
                    //17:30后签退,若存在事项，则考勤和事项冲突
                    if((leaveTime.after(a_endTime)||leaveTime.equals(a_endTime))&&(allDay!=null||morDay!=null||aftDay!=null)){
                        attendance.setIsError("1");
                        errorDesc += "考勤与事项冲突；";
                    }
                }

                //08:30-11:30签到
                if(signTime.after(m_beginTime)&&signTime.before(m_endTime)){
                    //08:30-11:30签退
                    if(leaveTime.after(m_beginTime)&&leaveTime.before(m_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //11:30-17:30签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；考勤与事项冲突；";
                        }
                    }
                }

                //11:30-13:30签到
                if((signTime.after(m_endTime)||signTime.equals(m_endTime))&&(signTime.before(a_beginTime)||signTime.equals(a_beginTime))){
                    //11:30-13:30签退，无效的签到签退
                    if((leaveTime.after(m_endTime)||leaveTime.equals(m_endTime))&&(leaveTime.before(a_beginTime)||leaveTime.equals(a_beginTime))){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //13:30-17:30签退
                    if(leaveTime.after(a_beginTime)&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //全天事项（上午事项+下午事项）
                        if(allDay!=null||(morDay!=null&&aftDay!=null)){
                            attendance.setIsError("1");
                            errorDesc += "迟到；考勤事项冲突；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                }

                //13:30-17:30签到
                if(signTime.after(a_beginTime)&&signTime.before(a_endTime)){
                    //13:30-17:30签退
                    if(leaveTime.after(a_beginTime)&&leaveTime.before(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                    }
                    //17:30后签退
                    if(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime)){
                        //无事项
                        if(allDay==null&&morDay==null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到早退；";
                        }
                        //上午事项
                        if(morDay!=null&&aftDay==null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                        //下午事项
                        if(morDay==null&&aftDay!=null){
                            attendance.setIsError("1");
                            errorDesc += "迟到；";
                        }
                    }
                }

                //17:30后签到，17:30后签退，无效的签到签退
                if((signTime.after(a_endTime)||signTime.equals(a_endTime))&&(leaveTime.after(a_endTime)||leaveTime.equals(a_endTime))){
                    attendance.setIsError("1");
                    errorDesc += "无效的签到签退；";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if(!isWorkday){//非工作日事项如果有签到，则必有签退
            if((attendance.getSignTime()==null&&attendance.getLeaveTime()!=null)||(attendance.getSignTime()!=null&&attendance.getLeaveTime()==null)){
                attendance.setIsError("1");
                errorDesc += "未签到或未签退；";
            }
        }

        //计算迟到早退旷工加班
        try{
            if(isWorkday){//工作日
                attendance.setAbsent(0.0);
                attendance.setLate(0.0);
                attendance.setEarly(0.0);
                if(attendance.getSignTime()==null||attendance.getLeaveTime()==null){//无签到或签退，无考勤事项，记为旷工
                    if(allDay==null&&morDay==null&&aftDay==null){
                        attendance.setAbsent(1.0);
                        attendance.setIsError("1");
                        errorDesc += "未签到或未签退；";
                    } else if((morDay!=null&&aftDay==null)||(morDay==null&&aftDay!=null)){
                        attendance.setAbsent(0.5);
                        errorDesc += "未签到或未签退；";
                    }
                    attendance.setOverTime(0.0);
                }else{
                    //全天无考勤事项
                    if(allDay==null&&morDay==null&&aftDay==null){
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse(attendance.getBeginTime()).getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(1.0);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(1.0);
                            }
                        }
                    }else if(morDay!=null&&aftDay==null){//上午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse("13:30").getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse(attendance.getEndTime()).getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }
                    }else if(morDay==null&&aftDay!=null){//下午考勤
                        Double lateTime = (sdf.parse(attendance.getSignTime()).getTime()-sdf.parse(attendance.getBeginTime()).getTime())/3600000.0;//迟到时间
                        Double earlyTime = (sdf.parse("11:30").getTime()-sdf.parse(attendance.getLeaveTime()).getTime())/3600000.0;//早退时间

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(lateTime>0&&lateTime<=0.25){
                                attendance.setLate(0.25);
                            } else if (lateTime>0.25&&lateTime<=0.5){
                                attendance.setLate(0.5);
                            } else if (lateTime>0.5&&lateTime<=1.0){
                                attendance.setLate(1.0);
                            } else if (lateTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }

                        if(attendance.getAbsent()==null||attendance.getAbsent()==0.0){
                            if(earlyTime>0&&earlyTime<=0.25){
                                attendance.setEarly(0.25);
                            } else if (earlyTime>0.25&&earlyTime<=0.5){
                                attendance.setEarly(0.5);
                            } else if (earlyTime>0.5&&earlyTime<=1.0){
                                attendance.setEarly(1.0);
                            } else if (earlyTime>1){
                                attendance.setAbsent(0.5);
                            }
                        }
                    }

                    //签退时间-下班时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()-sdf.parse(attendance.getEndTime()).getTime())/14400000.0)/2;
                    if(workDay<0){
                        workDay = 0.0;
                    }
                    attendance.setOverTime(workDay);
                    //校验加班单据，平日加班记录在晚上
                    if(workDay>0&&(attendance.getEveDay()==null||!"10".equals(attendance.getEveDay()))){
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                    }
                }
            }else{//非工作日计算加班信息，前提是必须存在签到和签退
                if(attendance.getSignTime()!=null&&attendance.getLeaveTime()!=null){
                    //签退时间-签到时间，除以四小时向下取整，再除二
                    Double workDay = Math.floor((sdf.parse(attendance.getLeaveTime()).getTime()-sdf.parse(attendance.getSignTime()).getTime())/14400000.0)/2;
                    if(workDay<0){
                        workDay = 0.0;
                    }
                    attendance.setOverTime(workDay);
                    //校验加班单据，非工作日加班记录在全天
                    if(workDay>0&&(attendance.getAllDay()==null||!"10".equals(attendance.getAllDay()))){
                        attendance.setIsError("1");
                        errorDesc += "缺少加班单据；";
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        attendance.setErrorDesc(errorDesc);

        return attendance;
    }
}
