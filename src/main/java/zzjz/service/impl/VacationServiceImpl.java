package zzjz.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Annual;
import zzjz.bean.Attendance;
import zzjz.bean.Depart;
import zzjz.bean.OverOff;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.TripDetail;
import zzjz.bean.Vacation;
import zzjz.bean.VacationDetail;
import zzjz.bean.VacationRequest;
import zzjz.mapper.AnnualMapper;
import zzjz.mapper.AttendanceMapper;
import zzjz.mapper.VacationDetailMapper;
import zzjz.mapper.VacationMapper;
import zzjz.service.ActivitiService;
import zzjz.service.AnnualService;
import zzjz.service.AttendanceService;
import zzjz.service.CalendarService;
import zzjz.service.DepartService;
import zzjz.service.OverOffService;
import zzjz.service.OverService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
import zzjz.service.VacationService;
import zzjz.util.DateUtil;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日16:34
 * @ClassName: VacationServiceImpl
 * @Description: 请假信息方法
 */
@Service
public class VacationServiceImpl implements VacationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VacationServiceImpl.class);

    private static final Double DOU05 = 0.5;

    private static final Double DOU5 = 5.0;

    private static final Double DOU3 = 3.0;

    private static final Double DOU2 = 2.0;

    private static final Double DOU10 = 10.0;

    private static final Integer INT7 = 7;

    private static final Integer INT16 = 16;

    private static final Integer FINT15 = -15;

    @Autowired
    VacationMapper vacationMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    VacationDetailMapper vacationDetailMapper;

    @Autowired
    CalendarService calendarService;

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    AnnualMapper annualMapper;

    @Autowired
    StaffService staffService;

    @Autowired
    AnnualService annualService;

    @Autowired
    OverOffService overOffService;

    @Autowired
    TripService tripService;

    @Autowired
    DepartService departService;

    /**
     * 分页获取假期列表信息.
     *
     * @param request request
     * @return 结果
     */
    @Override
    public Page<Vacation> getVacationListPage(VacationRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Vacation> tripPage = (Page<Vacation>) vacationMapper.getVacationList(request);
        return tripPage;
    }

    @Override
    public List<Vacation> getVacationList(VacationRequest vacation) {
        return vacationMapper.getVacationList(vacation);
    }

    /**
     * 添加请假信息.
     *
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public String addVacationInfo(Vacation vacation) {
        String userId = vacation.getUserId();
        String userName = vacation.getUserName();

        Staff staff = staffService.getStaffByUserId(userId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(userId, Long.parseLong(defaultDepartId));

        String actId;
        //普通员工和总管请假走一般流程，其余人员走特殊流程
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            actId = activitiService.flowStart("common", userId);
        } else {
            actId = activitiService.flowStart("vacation", userId);
        }

        if (actId == null || "".equals(actId)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creditTime = sdf.format(new Date());
        vacation.setUserId(userId);
        vacation.setUserName(userName);
        vacation.setActId(actId);
        vacation.setCreateTime(creditTime);
        //计算请假天数
        Double days = betweenDays(vacation);
        vacation.setLeaveDay(days);
        vacation.setStatus("00");
        if (vacationMapper.selectByPrimaryKey(vacation.getId()) != null) {
            return vacationMapper.updateByPrimaryKey(vacation) > 0 ? actId : "";
        } else {
            return vacationMapper.insert(vacation) > 0 ? actId : "";
        }
    }

    /**
     * 修改请假信息.
     *
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public boolean updateVacationInfo(Vacation vacation) {
        Vacation tmpVacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        vacation.setCreateTime(tmpVacation.getCreateTime());
        vacation.setUserName(tmpVacation.getUserName());
        vacation.setUserId(tmpVacation.getUserId());
        vacation.setActId(tmpVacation.getActId());
        //计算请假天数
        Double days = betweenDays(vacation);
        vacation.setLeaveDay(days);
        return vacationMapper.updateByPrimaryKey(vacation) > 0;
    }

    @Override
    public boolean forVacationInfo(Vacation vacation) throws ParseException {
        boolean rs = true;
        //先删除请假信息的明细以及考勤相关
        rs = deleteAttendanceInfo(vacation);
        //vacationDetailMapper.deleteById(vacation.getId());
        if (rs) {
            //修改请假信息
            vacation.setStatus("98");
            rs = updateVacationInfo(vacation);
            if (rs) {
                //生成新的明细及考勤信息
                rs = initVacationDetailInfo(vacation) && initAttendanceInfo(vacation);
            }
        }
        return rs;
    }

    /**
     * 提交请假信息.
     *
     * @param vacation 假期信息
     * @return 结果
     */
    @Override
    public boolean submitVacationInfo(Vacation vacation, String user, String staffId) {
        vacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        String actId = vacation.getActId();

        Staff staff = staffService.getStaffByUserId(staffId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(staffId, Long.parseLong(defaultDepartId));

        boolean rs;
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            rs = activitiService.submitCommonTask(actId, user);
        } else {
            rs = activitiService.submitVacationTask(actId, user, vacation.getLeaveDay());
        }

        if (rs) {
            vacation.setStatus("01");
            rs = vacationMapper.updateByPrimaryKey(vacation) > 0;
        }
        if (activitiService.isEndProcess(vacation.getActId())) {
            vacation.setStatus("99");
            rs = vacationMapper.updateByPrimaryKey(vacation) > 0;
            //流程结束后，生成明细，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initVacationDetailInfo(vacation) && initAttendanceInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public boolean approveVacationTask(Vacation vacation, String user, String msg, String reason) {
        vacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        String actId = vacation.getActId();
        boolean rs = activitiService.approveVacationTask(actId, user, msg, reason, vacation.getLeaveDay());

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                vacation.setStatus("03");
            } else {
                vacation.setStatus("02");
            }
            rs = vacationMapper.updateByPrimaryKey(vacation) > 0;
        } else if (activitiService.isEndProcess(vacation.getActId())) {
            vacation.setStatus("99");
            rs = vacationMapper.updateByPrimaryKey(vacation) > 0;
            //流程结束后，生成明细，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initVacationDetailInfo(vacation) && initAttendanceInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public boolean terminationVacationTask(Vacation vacation) {
        vacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        String actId = vacation.getActId();
        boolean rs = activitiService.terminationTask(actId, "vacation");
        if (rs) {
            vacation.setStatus("99");
            rs = vacationMapper.updateByPrimaryKey(vacation) > 0;
            //流程结束后，生成明细，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initVacationDetailInfo(vacation) && initAttendanceInfo(vacation);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public Double getLeftOffDays(String staffId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Double days = 0.0;
        try {
            days = queryLeftOffDays(sdf.format(new Date()), staffId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取进行中某个人的某个类型的请假天数
     * @param leaveType 请假类型
     * @param staffId 员工号
     * @return 结果
     */
    Double getGoingVacationDays(String leaveType, String staffId) {
        assert leaveType != null && staffId != null;
        VacationRequest request = new VacationRequest();
        request.setLeaveTyp(leaveType);
        request.setUserId(staffId);
        request.setStartTime(DateUtil.getYearStart());
        request.setEndTime(DateUtil.getYearEnd());
        request.setStatus("01");
        List<Vacation> vacationList = getVacationList(request);
        Double d = 0D;
        for (Vacation v : vacationList) {
            d += v.getLeaveDay();
        }
        return d;
    }

    @Override
    public Double getLeftAnnualDays(String staffId) {
        Double days = 0.0;
        //查询是否转正，如试用期，则不给请年假
        Staff staff = staffService.getStaffByUserId(staffId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (staff.getFormalDate() != null) {
            //查询剩余年假
            Annual annual = new Annual();
            annual.setUserId(staffId);
            String year = "";
            // 如果是大于当前年的4月了 就显示当前年份的 否则就是去年
            DateTime now = new DateTime();
            int yearNow = Integer.parseInt(now.toString("yyyy"));
            DateTime marchLast = new DateTime(yearNow, 3, 31, 0, 0, 0);
            year = now.isAfter(marchLast) ? String.valueOf(yearNow) : String.valueOf(yearNow - 1);
            annual.setYear(year);
            annual = annualMapper.selectByPrimaryKey(annual);
            if (annual == null) {
                annualService.initAnnual(staffId, year);
                annual = new Annual();
                annual.setUserId(staffId);
                annual.setYear(year);
                annual = annualMapper.selectByPrimaryKey(annual);
            }
            days = annual.getAnnualLeft();

            //20181225 申请时年假剩余时间应该要考虑进行中的年假
            Double annualGoing = getGoingVacationDays("01", staffId);
            //减去进行中的年假天数
            days = days - annualGoing;
        }
        return days;
    }

    @Override
    public Map<String, Object> getVacationMapList(Vacation vacation) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, FINT15);

        Staff staff = staffService.getStaffByUserId(vacation.getUserId()); //申请人信息
        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId())); //申请人机构
        List<ActHis> actHisList = activitiService.getActHisList(vacation.getActId()); //流程信息
        //Double annual = annualService.annualDays(vacation.getUserId(), sdf4.format(new Date()));    //年假剩余
        //查询剩余年假
        Annual annual = new Annual();
        annual.setUserId(vacation.getUserId());
        String year;
        try {
            if (sdf2.parse(vacation.getLeaveDate()).after(sdf2.parse(vacation.getLeaveDate().split("-")[0] + "-03-15"))) {
                year = vacation.getLeaveDate().split("-")[0];
            } else {
                year = Integer.parseInt(vacation.getLeaveDate().split("-")[0]) - 1 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            year = "";
        }
        annual.setYear(year);
        annual = annualMapper.selectByPrimaryKey(annual);
        Double overOffDay = 0d;  //调休剩余
        OverOff overOff = overOffService.queryOverOffInfo(vacation.getUserId(), sdf5.format(c.getTime()));
        if (overOff != null) {
            overOffDay = overOff.getLeftDay();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userName", vacation.getUserName());
        map.put("annual", annual.getAnnualLeft());
        map.put("overOffDay", overOffDay);
        switch (vacation.getLeaveTyp()) {
            case "00":
                map.put("type", "调休");
                break;
            case "01":
                map.put("type", "年假");
                break;
            case "02":
                map.put("type", "事假");
                break;
            case "03":
                map.put("type", "产假");
                break;
            case "04":
                map.put("type", "婚假");
                break;
            case "05":
                map.put("type", "丧假");
                break;
            case "06":
                map.put("type", "病假");
                break;
            case "07":
                map.put("type", "工程调休");
                break;
            case "08":
                map.put("type", "产前假");
                break;
            default:
                map.put("type", "未知");
                break;
        }
        try {
            String leaveRange = "0".equals(vacation.getLeavePart()) ? "上午" : "下午";
            String backRange = "0".equals(vacation.getBackPart()) ? "上午" : "下午";
            map.put("vacationTime", sdf3.format(sdf2.parse(vacation.getLeaveDate())) + leaveRange + " 至 " +
                    sdf3.format(sdf2.parse(vacation.getBackDate())) + backRange + " ，共" + vacation.getLeaveDay() + "天");
            map.put("createTime", sdf2.format(sdf.parse(vacation.getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        map.put("position", staff.getPosition());
        map.put("depart", depart.getDepartName());
        map.put("reason", vacation.getReason());
        map.put("leaveDay", vacation.getLeaveDay());

        String firstPath = "";
        String firstTime = "";
        String secondPath = "";
        String secondTime = "";
        String thirdPath = "";
        String thirdTime = "";
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(vacation.getUserId(), Long.parseLong(defaultDepartId));
        for (ActHis actHis : actHisList) {
            //部门经理或总经办成员
            if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
                if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                    if ("firstAudit".equals(actHis.getActId())) {
                        thirdPath = staffService.getSignPath(actHis.getAssignee());
                        thirdTime = sdf2.format(actHis.getEndTime());
                    }
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
                    if ("thirdAudit".equals(actHis.getActId())) {
                        thirdPath = staffService.getSignPath(actHis.getAssignee());
                        thirdTime = sdf2.format(actHis.getEndTime());
                    }
                }
            }
        }
        ImageEntity firstImage = new ImageEntity();
        ImageEntity secondImage = new ImageEntity();
        ImageEntity thirdImage = new ImageEntity();
        try {
            if (StringUtils.isNotBlank(firstPath)) {
                firstImage.setData(FileUtils.readFileToByteArray(new File(firstPath)));
            }
            if (StringUtils.isNotBlank(secondPath)) {
                secondImage.setData(FileUtils.readFileToByteArray(new File(secondPath)));
            }
            if (StringUtils.isNotBlank(thirdPath)) {
                thirdImage.setData(FileUtils.readFileToByteArray(new File(thirdPath)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("firstImage", firstImage);
        map.put("firstTime", firstTime);
        map.put("secondImage", secondImage);
        map.put("secondTime", secondTime);
        map.put("thirdImage", thirdImage);
        map.put("thirdTime", thirdTime);
        return map;
    }

    @Override
    public boolean ignore(Integer id) {
        Vacation vacation = vacationMapper.selectByPrimaryKey(id);
        vacation.setStatus("93");
        return vacationMapper.updateByPrimaryKey(vacation) > 0;
    }

    private boolean initAttendanceInfo(Vacation vacation) {
        Boolean rs = true;
        VacationDetail vacationDetailRequest = new VacationDetail();
        vacationDetailRequest.setId(vacation.getId());
        List<VacationDetail> vacationList = vacationDetailMapper.querySelective(vacationDetailRequest);
        if (vacationList != null && vacationList.size() > 0) {
            for (VacationDetail vacationDetail : vacationList) {
                Attendance attendance = new Attendance();
                attendance.setUserId(vacationDetail.getUserId());
                attendance.setCheckDate(vacationDetail.getLeaveDate());
                attendance = attendanceService.selectByPrimaryKey(attendance);
                if (attendance != null) {
                    if ("all".equals(vacationDetail.getLeavePart())) { //全天
                        attendance.setAllDay(vacationDetail.getLeaveTyp());
                    } else if ("mor".equals(vacationDetail.getLeavePart())) { //上午
                        attendance.setMorDay(vacationDetail.getLeaveTyp());
                    } else if ("aft".equals(vacationDetail.getLeavePart())) { //下午
                        attendance.setAftDay(vacationDetail.getLeaveTyp());
                    }
                    attendance = attendanceService.checkAttendanceData(attendance, null);
                    attendanceService.updateByPrimaryKey(attendance);
                }
            }
        }
        return rs;
    }

    /**
     * 根据主键获取请假信息.
     *
     * @param id 主键
     * @return 请假信息
     */
    @Override
    public Vacation getVacationInfoById(Integer id) {
        return vacationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Vacation vacation, long departId, String msg) {
        List<Staff> list = new ArrayList<Staff>();
        vacation = vacationMapper.selectByPrimaryKey(vacation.getId());
        String actId = vacation.getActId();
        if ("agree".equals(msg)) {
            String condition;
            if (vacation.getLeaveDay() < DOU5) {
                condition = "day < 5";
            } else {
                condition = "day >= 5";
            }
            list = activitiService.getNextLeaderByRole(vacation.getUserId(), actId, false, "vacation", condition);
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    /**
     * 计算并生成假期明细表.
     *
     * @param vacation 请假信息
     */
    @Override
    public boolean initVacationDetailInfo(Vacation vacation) throws ParseException {
        Integer id = vacation.getId();
        String leaveTyp = vacation.getLeaveTyp();
        //如果是年假调休，优先查询删除年假调休天数；
        Double deleteDays = vacationDetailMapper.countByUserId(id);
        if (deleteDays == null) {
            deleteDays = 0.0;
        }
        Double leaveDays = vacation.getLeaveDay();
        if (leaveDays == null) {
            leaveDays = 0.0;
        }

        //无论有没有数据，先删除数据
        vacationDetailMapper.deleteById(id);

        if (vacation.getLeaveDate() == null || vacation.getBackDate() == null) { //起止信息不全，则不做计算
            return true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String userId = vacation.getUserId();
        if (vacation.getLeaveDate().equals(vacation.getBackDate())) { //只请假一天
            String leavePart = vacation.getLeavePart() == null ? "0" : vacation.getLeavePart();
            String backPart = vacation.getBackPart() == null ? "1" : vacation.getBackPart();
            VacationDetail vacationDetail = new VacationDetail();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getLeaveDate());
            if ("0".equals(leavePart) && "0".equals(backPart)) {
                vacationDetail.setLeavePart("mor");
                vacationDetail.setLeaveDay(DOU05);
            } else if ("1".equals(leavePart) && "1".equals(backPart)) {
                vacationDetail.setLeavePart("aft");
                vacationDetail.setLeaveDay(DOU05);
            } else {
                vacationDetail.setLeavePart("all");
                vacationDetail.setLeaveDay(1.0);
            }
            vacationDetailMapper.insert(vacationDetail);
        } else { //多天请假，首尾两天要单独计算，需要检测请假半天的情况
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(vacation.getLeaveDate()));
            //第一天
            VacationDetail vacationDetail = new VacationDetail();
            String leavePart = vacation.getLeavePart() == null ? "0" : vacation.getLeavePart();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getLeaveDate());
            vacationDetail.setLeavePart("0".equals(leavePart) ? "all" : "aft");
            if ("0".equals(leavePart)) {
                vacationDetail.setLeaveDay(1.0);
            } else {
                vacationDetail.setLeaveDay(DOU05);
            }
            vacationDetailMapper.insert(vacationDetail);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            //中间天
            while (sdf.parse(sdf.format(calendar.getTime())).before(sdf.parse(vacation.getBackDate()))) {
                boolean isWorkday = calendarService.isWorkDay(sdf.format(calendar.getTime()));
                boolean isHoliday = calendarService.isHoliDay(sdf.format(calendar.getTime()));
                if (isWorkday || (!isWorkday && !isHoliday && ("03".equals(leaveTyp) ||
                        "04".equals(leaveTyp))) || (!isWorkday && isHoliday && "03".equals(leaveTyp))) {
                    vacationDetail.setId(id);
                    vacationDetail.setUserId(userId);
                    vacationDetail.setLeaveTyp(leaveTyp);
                    vacationDetail.setLeaveDate(sdf.format(calendar.getTime()));
                    vacationDetail.setLeavePart("all");
                    vacationDetail.setLeaveDay(1.0);
                    vacationDetailMapper.insert(vacationDetail);
                }
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
            //最后一天
            String backPart = vacation.getBackPart() == null ? "1" : vacation.getBackPart();
            vacationDetail.setId(id);
            vacationDetail.setUserId(userId);
            vacationDetail.setLeaveTyp(leaveTyp);
            vacationDetail.setLeaveDate(vacation.getBackDate());
            vacationDetail.setLeavePart("1".equals(backPart) ? "all" : "mor");
            if ("1".equals(backPart)) {
                vacationDetail.setLeaveDay(1.0);
            } else {
                vacationDetail.setLeaveDay(DOU05);
            }
            vacationDetailMapper.insert(vacationDetail);

            //更新请假明细至考勤表
            vacationDetailMapper.updateAllDayToAttendance(id);
            vacationDetailMapper.updateMorDayToAttendance(id);
            vacationDetailMapper.updateAftDayToAttendance(id);

            //校验事项
            calendar.setTime(sdf.parse(vacation.getLeaveDate()));
            while (sdf.parse(sdf.format(calendar.getTime())).before(sdf.parse(vacation.getBackDate()))
                    || sdf.parse(sdf.format(calendar.getTime())).equals(sdf.parse(vacation.getBackDate()))) {
                Attendance attendance = new Attendance();
                attendance.setCheckDate(sdf.format(calendar.getTime()));
                attendance.setUserId(userId);
                attendance = attendanceMapper.selectByPrimaryKey(attendance);
                if (attendance != null) {
                    attendanceService.checkAttendanceData(attendance, null);
                }
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
        }

        if ("00".equals(leaveTyp)) { //更新加班调休天数
            //查询加班调休信息
            OverOff overOff = overOffService.queryOverOffInfo(userId, dateToMonth(vacation.getLeaveDate()));
            if (overOff == null) {
                overOffService.initOverOffInfo(userId, dateToMonth(vacation.getLeaveDate()));
                overOff = overOffService.queryOverOffInfo(userId, dateToMonth(vacation.getLeaveDate()));
            }
            overOff.setOffDay(overOff.getOffDay() - deleteDays + leaveDays);
            overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
            overOffService.updateOverOffWithUseInfo(overOff, deleteDays, leaveDays);
            List<OverOff> list = overOffService.queryOverOffList(userId, dateToMonth(vacation.getLeaveDate()));
            if (list.size() > 0) {
                Double lastLeft = overOff.getLeftDay();
                for (OverOff oo : list) {
                    oo.setLastLeft(lastLeft);
                    oo.setLeftDay(lastLeft + oo.getOverDay() - oo.getOffDay());
                    overOffService.updateOverOffInfo(oo);
                    lastLeft = oo.getLeftDay();
                }
            }
        } else if ("01".equals(leaveTyp)) { //更新年假天数
            //查询年假信息
            Annual annual = new Annual();
            annual.setUserId(userId);
            String year;
            if (sdf.parse(vacation.getLeaveDate()).after(sdf.parse(vacation.getLeaveDate().split("-")[0] + "-03-15"))) {
                year = vacation.getLeaveDate().split("-")[0];
            } else {
                year = Integer.parseInt(vacation.getLeaveDate().split("-")[0]) - 1 + "";
            }
            annual.setYear(year);

            annual = annualMapper.selectByPrimaryKey(annual);
            if (annual == null) {
                annualService.initAnnual(userId, year);
                annual = new Annual();
                annual.setUserId(userId);
                annual.setYear(year);
                annual = annualMapper.selectByPrimaryKey(annual);
            }
            annual.setAnnualUse(annual.getAnnualUse() - deleteDays + leaveDays);
            annual.setAnnualLeft(annual.getAnnualAll() - annual.getAnnualUse());
            annualMapper.updateByPrimaryKey(annual);
        }
        return true;
    }

    /**
     * 计算请假天数.
     *
     * @param vacation 请假信息
     * @return 天数
     */
    @Override
    public Double betweenDays(Vacation vacation) {
        Double days;
        //如果没有开始时间结束时间，则不计算天数
        if (vacation.getLeaveDate() == null || vacation.getBackDate() == null) {
            return 0.0;
        }
        String leaveTyp = vacation.getLeaveTyp();
        //如果请假只有一天
        if (vacation.getLeaveDate().equals(vacation.getBackDate())) {
            if (vacation.getLeavePart().equals(vacation.getBackPart())) {
                return DOU05;
            } else {
                return 1.0;
            }
        } else {
            if ("03".equals(leaveTyp)) { //（陪）产假，节假日不顺延
                days = calendarService.daysBetween(vacation.getLeaveDate(), vacation.getBackDate(), "1") - 2.0;
            } else if ("04".equals(leaveTyp)) { //婚假，节日顺延，假日不顺延
                days = calendarService.daysBetween(vacation.getLeaveDate(), vacation.getBackDate(), "2") - 2.0;
            } else { //节假日顺延
                days = calendarService.daysBetween(vacation.getLeaveDate(), vacation.getBackDate(), "0") - 2.0;
            }
            String leavePart = vacation.getLeavePart() == null ? "0" : vacation.getLeavePart();
            String backPart = vacation.getBackPart() == null ? "1" : vacation.getBackPart();
            days = days + ("0".equals(leavePart) ? 1.0 : DOU05) + ("1".equals(backPart) ? 1.0 : DOU05);
        }

        return days;
    }

    @Override
    public List<VacationDetail> selectVacationInfo(VacationDetail vacation) {
        return vacationDetailMapper.querySelective(vacation);
    }

    @Override
    public Vacation initVacationInfo(Vacation vacation, String staffId) throws ParseException {
        String leaveTyp = vacation.getLeaveTyp();
        if (!StringUtils.isBlank(leaveTyp) && vacation.getLeaveDate() != null && !"".equals(vacation.getLeaveDate())) {
            boolean isWorkday = calendarService.isWorkDay(vacation.getLeaveDate());
            if (!isWorkday) {
                vacation.setLeaveDay(-DOU3);
                return vacation;
            }
            Double days;
            vacation.setLeavePart(vacation.getLeavePart() == null ? "0" : vacation.getLeavePart());
            vacation.setBackPart(vacation.getBackPart() == null ? "1" : vacation.getBackPart());
            switch (leaveTyp) {
                //调休
                case "00":
                    if (vacation.getBackDate() != null) {
                        //查询剩余调休假
                        Double leftOffDays = queryLeftOffDays(vacation.getLeaveDate(), staffId);
                        days = betweenDays(vacation);
                        if (days <= leftOffDays) {
                            vacation.setLeaveDay(days);
                        } else {
                            vacation.setLeaveDay(-1.0);
                        }
                    } else {
                        vacation.setLeaveDay(0.0);
                    }
                    break;
                //年假
                case "01":
                    if (vacation.getBackDate() != null) {
                        //查询是否转正，如试用期，则不给请年假
                        Staff staff = staffService.getStaffByUserId(staffId);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        if (staff.getFormalDate() == null || sdf.parse(vacation.getLeaveDate()).before(sdf.parse(staff.getFormalDate()))) {
                            vacation.setLeaveDay(-DOU2);
                            return vacation;
                        }
                        //查询剩余年假
                        Annual annual = new Annual();
                        annual.setUserId(staffId);
                        String year;
                        if (sdf.parse(vacation.getLeaveDate()).after(sdf.parse(vacation.getLeaveDate().split("-")[0] + "-03-15"))) {
                            year = vacation.getLeaveDate().split("-")[0];
                        } else {
                            year = Integer.parseInt(vacation.getLeaveDate().split("-")[0]) - 1 + "";
                        }
                        annual.setYear(year);
                        annual = annualMapper.selectByPrimaryKey(annual);
                        if (annual == null) {
                            annualService.initAnnual(staffId, year);
                            annual = new Annual();
                            annual.setUserId(staffId);
                            annual.setYear(year);
                            annual = annualMapper.selectByPrimaryKey(annual);
                        }
                        //减去进行中的年假天数
                        Double goingAnnualDays = getGoingVacationDays("01", staffId);
                        Double annualLeft = annual.getAnnualLeft() - goingAnnualDays;
                        days = betweenDays(vacation);
                        if (days <= (annual == null ? 0 : annualLeft)) {
                            vacation.setLeaveDay(days);
                        } else {
                            vacation.setLeaveDay(-1.0);
                        }
                    } else {
                        vacation.setLeaveDay(0.0);
                    }
                    break;
                //事假
                case "02":
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setBackDate(vacation.getLeaveDate());
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    }
                    break;
                //产假
                case "03":
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setBackDate(vacation.getLeaveDate());
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    }
                    break;
                //婚假
                case "04":
                    days = DOU10;
                    if (vacation.getBackDate() != null && days > betweenDays(vacation)) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setLeaveDay(days);
                        vacation.setLeavePart("0");
                        vacation.setBackPart("1");
                        vacation.setBackDate(calendarService.addDays(vacation.getLeaveDate(), days.intValue(), "2"));
                    }
                    break;
                //丧假
                case "05":
                    days = DOU3;
                    if (vacation.getBackDate() != null && days > betweenDays(vacation)) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setLeavePart("0");
                        vacation.setLeaveDay(days);
                        vacation.setBackPart("1");
                        vacation.setBackDate(calendarService.addDays(vacation.getLeaveDate(), days.intValue(), "1"));
                    }
                    break;
                //病假
                case "06":
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setLeaveDay(0.0);
                    }
                    break;
                //工程调休
                case "07":
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setBackDate(vacation.getLeaveDate());
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    }
                    break;
                //产前假
                case "08":
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    } else {
                        vacation.setBackDate(vacation.getLeaveDate());
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    }
                    break;
                default:
                    if (vacation.getBackDate() != null) {
                        days = betweenDays(vacation);
                        vacation.setLeaveDay(days);
                    }
                    break;
            }
        }
        return vacation;
    }

    @Override
    public boolean deleteRecordByActId(String actId) throws ParseException {
        Vacation vacation = vacationMapper.queryByActId(actId);
        // 调休
        if ("00".equals(vacation.getLeaveTyp()) && ("97".equals(vacation.getStatus()) || "98".equals(vacation.getStatus())
                || "99".equals(vacation.getStatus()))) { //重新计算加班调休
            //查询加班调休信息
            OverOff overOff = overOffService.queryOverOffInfo(vacation.getUserId(), dateToMonth(vacation.getLeaveDate()));
            overOff.setOffDay(overOff.getOffDay() - vacation.getLeaveDay());
            overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
            overOff.setUseDay(overOff.getLastLeft() - overOff.getLeftDay());
            overOffService.updateOverOffWithUseInfo(overOff, vacation.getLeaveDay(), 0.0);
            List<OverOff> list = overOffService.queryOverOffList(vacation.getUserId(), dateToMonth(vacation.getLeaveDate()));
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
        // 年假
        if ("01".equals(vacation.getLeaveTyp()) && ("97".equals(vacation.getStatus())
                || "98".equals(vacation.getStatus()) || "99".equals(vacation.getStatus()))) { //重新计算年假
            //查询年假信息
            Annual annual = new Annual();
            annual.setUserId(vacation.getUserId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String year;
            if (sdf.parse(vacation.getLeaveDate()).after(sdf.parse(vacation.getLeaveDate().split("-")[0] + "-03-15"))) {
                year = vacation.getLeaveDate().split("-")[0];
            } else {
                year = Integer.parseInt(vacation.getLeaveDate().split("-")[0]) - 1 + "";
            }
            annual.setYear(year);

            annual = annualMapper.selectByPrimaryKey(annual);
            annual.setAnnualUse(annual.getAnnualUse() - vacation.getLeaveDay());
            annual.setAnnualLeft(annual.getAnnualLeft() + vacation.getLeaveDay());
            annualMapper.updateByPrimaryKey(annual);
        }
        vacationMapper.deleteByActId(actId);
        activitiService.deleteActinstByActId(actId);
        deleteAttendanceInfo(vacation);
        vacationDetailMapper.deleteById(vacation.getId());
        return true;
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        vacationMapper.deleteByActId(actId);
        activitiService.deleteRecordByActId(actId);
        return true;
    }

    /**
     * @param date   date
     * @param userId userId
     * @return Double
     * @throws ParseException ParseException
     */
    public Double queryLeftOffDays(String date, String userId) throws ParseException {
        OverOff overOff = overOffService.queryOverOffInfo(userId, dateToMonth(date));
        if (overOff == null) {
            overOffService.initOverOffInfo(userId, dateToMonth(date));
            overOff = overOffService.queryOverOffInfo(userId, dateToMonth(date));
        }
        //减去进行中调休天数
        Double goingOffDays = getGoingVacationDays("00", userId);
        return overOff.getLeftDay() - goingOffDays;
    }

    private boolean deleteAttendanceInfo(Vacation vacation) {
        Boolean rs = true;
        VacationDetail vacationDetailRequest = new VacationDetail();
        vacationDetailRequest.setId(vacation.getId());
        List<VacationDetail> vacationList = vacationDetailMapper.querySelective(vacationDetailRequest);
        if (vacationList != null && vacationList.size() > 0) {
            for (VacationDetail vacationDetail : vacationList) {
                Attendance attendance = new Attendance();
                attendance.setUserId(vacationDetail.getUserId());
                attendance.setCheckDate(vacationDetail.getLeaveDate());
                attendance = attendanceService.selectByPrimaryKey(attendance);
                if (attendance == null) {
                    continue;
                }
                if ("all".equals(vacationDetail.getLeavePart())) { //全天
                    //查找是否同时存在出差信息
                    List<TripDetail> list = tripService.getTripList(vacationDetail.getUserId(), vacationDetail.getLeaveDate(), "all");
                    if (list.size() > 0) {
                        outer:
                        for (TripDetail tripDetail : list) {
                            if ("mor".equals(tripDetail.getTripPart())) {
                                attendance.setAllDay(null);
                                attendance.setMorDay("20");
                            } else if ("aft".equals(tripDetail.getTripPart())) {
                                attendance.setAllDay(null);
                                attendance.setAftDay("20");
                            } else {
                                attendance.setAllDay("20");
                                attendance.setMorDay(null);
                                attendance.setAftDay(null);
                                break outer;
                            }
                        }
                    } else {
                        attendance.setAllDay(null);
                    }
                } else if ("mor".equals(vacationDetail.getLeavePart())) { //上午
                    //查找是否同时存在出差信息
                    List<TripDetail> list = tripService.getTripList(vacationDetail.getUserId(), vacationDetail.getLeaveDate(), "mor");
                    if (list.size() > 0) {
                        attendance.setMorDay("20");
                    } else {
                        attendance.setMorDay(null);
                    }
                } else if ("aft".equals(vacationDetail.getLeavePart())) { //下午
                    //查找是否同时存在出差信息
                    List<TripDetail> list = tripService.getTripList(vacationDetail.getUserId(), vacationDetail.getLeaveDate(), "aft");
                    if (list.size() > 0) {
                        attendance.setAftDay("20");
                    } else {
                        attendance.setAftDay(null);
                    }
                }
                attendance = attendanceService.checkAttendanceData(attendance, vacationDetail.getId());
                attendanceService.updateByPrimaryKey(attendance);
            }
        }
        return rs;
    }

    public String dateToMonth(String date) {
        //考勤周期改为自然月
        return date.substring(0, INT7);
    }

    public String dateToMonthOld(String date) {
        //计算月份
        String monthDate = date.substring(0, INT7);
        if (Integer.parseInt(date.split("-")[2]) < INT16) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Calendar c = java.util.Calendar.getInstance();
            try {
                c.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(java.util.Calendar.MONTH, -1);
            monthDate = sdf.format(c.getTime()).substring(0, INT7);
        }
        return monthDate;
    }
}
