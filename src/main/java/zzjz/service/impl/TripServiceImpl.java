package zzjz.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.Depart;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.Trip;
import zzjz.bean.TripDetail;
import zzjz.bean.TripRequest;
import zzjz.mapper.AttendanceMapper;
import zzjz.mapper.TripDetailMapper;
import zzjz.mapper.TripMapper;
import zzjz.service.ActivitiService;
import zzjz.service.AttendanceService;
import zzjz.service.CalendarService;
import zzjz.service.CommonService;
import zzjz.service.DepartService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
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
 * @version 2017年04月13日14:38
 * @ClassName: TripServiceImpl
 * @Description: 出差方法实现类
 */
@Service
public class TripServiceImpl implements TripService {

    private static final Double DOU05 = 0.5;

    private static final Integer INT420 = 420;

    private static final Integer INT12 = 12;

    private static final Integer INT10 = 10;

    private static final long INT1000 = 1000L;

    private static final Integer INT3600 = 3600;

    private static final Integer INT24 = 24;

    private static final Integer INT60 = 60;

    private static final Integer INT465 = 465;

    private static final Integer INT15 = 15;

    private static final Integer INT16 = 16;

    private static final Double MISTAKE = .0000001;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    TripMapper tripMapper;

    @Autowired
    TripDetailMapper tripDetailMapper;

    @Autowired
    CalendarService calendarService;

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    CommonService commonService;

    @Autowired
    StaffService staffService;

    @Autowired
    DepartService departService;

    @Override
    public Page<Trip> getTripListPage(TripRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Trip> tripPage = (Page<Trip>) tripMapper.getTripList(request);
        return tripPage;
    }

    /**
     * 添加出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    @Override
    public String addTripInfo(Trip trip) {
        String userId = trip.getUserId();
        Staff staff = staffService.getStaffByUserId(userId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(userId, Long.parseLong(defaultDepartId));

        String actId;
        //普通员工和总管出差走一般流程，其余人员走特殊流程
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            actId = activitiService.flowStart("common", userId);
        } else {
            actId = activitiService.flowStart("trip", userId);
        }

        if (actId == null || "".equals(actId)) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creditTime = sdf.format(new Date());
        trip.setUserId(userId);
        trip.setActId(actId);
        trip.setCreateTime(creditTime);
        trip.setStatus("00");
        //计算请假天数
        Double days = betweenDays(trip);
        trip.setTripDay(days);
        if (tripMapper.selectByPrimaryKey(trip.getId()) != null) {
            return tripMapper.updateByPrimaryKey(trip) > 0 ? actId : "";
        } else {
            return tripMapper.insert(trip) > 0 ? actId : "";
        }
    }

    /**
     * 更新出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    @Override
    public boolean updateTripInfo(Trip trip) {
        Trip tmpTrip = tripMapper.selectByPrimaryKey(trip.getId());
        trip.setCreateTime(tmpTrip.getCreateTime());
        trip.setUserName(tmpTrip.getUserName());
        trip.setUserId(tmpTrip.getUserId());
        trip.setActId(tmpTrip.getActId());
        //计算请假天数
        Double days = betweenDays(trip);
        trip.setTripDay(days);
        return tripMapper.updateByPrimaryKey(trip) > 0;
    }

    /**
     * 提交出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    @Override
    public boolean submitTripTask(Trip trip, String user, String staffId) throws ParseException {
        trip = tripMapper.selectByPrimaryKey(trip.getId());
        String actId = trip.getActId();

        Staff staff = staffService.getStaffByUserId(staffId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(staffId, Long.parseLong(defaultDepartId));

        boolean rs;
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            rs = activitiService.submitCommonTask(actId, user);
        } else {
            rs = activitiService.submitTask(actId, user, "trip");
        }

        if (rs) {
            trip.setStatus("01");
            rs = tripMapper.updateByPrimaryKey(trip) > 0;
        }
        if (activitiService.isEndProcess(trip.getActId())) {
            trip.setStatus("99");
            rs = tripMapper.updateByPrimaryKey(trip) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initTripDetailInfo(trip) && initAttendanceInfo(trip);
            }
        }
        return rs;
    }

    @Override
    public boolean approveTripTask(Trip trip, String user, String msg, String reason) {
        trip = tripMapper.selectByPrimaryKey(trip.getId());
        String actId = trip.getActId();
        boolean rs = activitiService.approveTask(actId, user, "trip", msg, reason);

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                trip.setStatus("03");
            } else {
                trip.setStatus("02");
            }
            rs = tripMapper.updateByPrimaryKey(trip) > 0;
        } else if (activitiService.isEndProcess(trip.getActId())) {
            trip.setStatus("99");
            rs = tripMapper.updateByPrimaryKey(trip) > 0;
            //流程结束后，生成明细，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initTripDetailInfo(trip) && initAttendanceInfo(trip);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public boolean terminationTripTask(Trip trip) {
        trip = tripMapper.selectByPrimaryKey(trip.getId());
        String actId = trip.getActId();
        boolean rs = activitiService.terminationTask(actId, "trip");
        if (rs) {
            trip.setStatus("99");
            rs = tripMapper.updateByPrimaryKey(trip) > 0;
            //流程结束后，生成明细，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initTripDetailInfo(trip) && initAttendanceInfo(trip);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> getTripMapList(Trip trip) {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Staff staff = staffService.getStaffByUserId(trip.getUserId()); //申请人信息
        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId())); //申请人机构
        Depart parentDepart = departService.selectByPrimaryKey(depart.getParentDepart()); //上级机构
        List<ActHis> actHisList = activitiService.getActHisList(trip.getActId()); //流程信息

        map.put("userName", trip.getUserName());
        map.put("address", trip.getAddress());
        map.put("tripDate", trip.getTripDate() + ("0".equals(trip.getTripPart()) ? "上午" : "下午") +
                "至" + trip.getBackDate() + ("0".equals(trip.getBackPart()) ? "上午" : "下午"));
        try {
            map.put("tripTime", sdf1.format(sdf.parse(trip.getStartTime())) + "至" +
                    sdf1.format(sdf.parse(trip.getEndTime())) + "，共" + trip.getTripDay() + "天 注：出差起止时间为车票票面的出发时间");
            map.put("createTime", sdf2.format(sdf.parse(trip.getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map.put("position", staff.getPosition().replaceAll("\n", ";").split(";")[0]);
        map.put("depart", depart.getDepartName());
        if (parentDepart != null) {
            map.put("parentDepart", parentDepart.getDepartName());
        }
        map.put("tools", trip.getTools() == null ? "" : trip.getTools());
        map.put("intends", trip.getIntends());

        String firstPath = "";
        String firstTime = "";
        String secondPath = "";
        String secondTime = "";
        String thirdPath = "";
        String thirdTime = "";
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(trip.getUserId(), Long.parseLong(defaultDepartId));
        for (ActHis actHis : actHisList) {
            //部门经理或总经办成员
            if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
                if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                    thirdPath = staffService.getSignPath(actHis.getAssignee());
                    thirdTime = sdf2.format(actHis.getEndTime());
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
    public List<Trip> getTripRemindList() {
        // 考勤周期改为自然月
        String beginDate = DateTime.now().dayOfMonth().withMinimumValue().toString("yyyy-MM-dd");
        String endDate = DateTime.now().dayOfMonth().withMaximumValue().toString("yyyy-MM-dd");
        return tripMapper.getTripRemindList(beginDate, endDate);
    }

    @Override
    public boolean hrSave(Trip trip) throws ParseException {
        boolean rs = true;
        //先删除出差信息的明细以及考勤相关
        rs = deleteAttendanceInfo(trip);
        tripDetailMapper.deleteById(trip.getId());
        if (!rs) {
            return rs;
        }
        //计算请假天数
        Double days = betweenDays(trip);
        trip.setTripDay(days);
        rs = tripMapper.updateByPrimaryKey(trip) > 0;
        if (rs) {
            rs = initTripDetailInfo(trip) && initAttendanceInfo(trip);
        }
        return rs;
    }

    @Override
    public boolean ignore(Integer id) {
        Trip trip = tripMapper.selectByPrimaryKey(id);
        trip.setStatus("93");
        return tripMapper.updateByPrimaryKey(trip) > 0;
    }

    //设置考勤
    private boolean initAttendanceInfo(Trip trip) {
        Integer tripId = trip.getId();
        List<TripDetail> tripDetails = tripDetailMapper.getByTripId(tripId);
        for (TripDetail tripDetail : tripDetails) {
            Attendance attendance = new Attendance();
            attendance.setUserId(tripDetail.getUserId());
            if (tripDetail.getTripDate().length() > INT12) {
                attendance.setCheckDate(tripDetail.getTripDate().substring(0, INT10));
            } else {
                attendance.setCheckDate(tripDetail.getTripDate());
            }
            attendance = attendanceService.selectByPrimaryKey(attendance);
            if (attendance != null) {
                //全天
                if ("all".equals(tripDetail.getTripPart())) {
                    attendance.setAllDay("20");
                } else if ("mor".equals(tripDetail.getTripPart())) { //上午
                    attendance.setMorDay("20");
                } else if ("aft".equals(tripDetail.getTripPart())) { //下午
                    attendance.setAftDay("20");
                }
                attendance = attendanceService.checkAttendanceData(attendance, null);
                attendanceService.updateByPrimaryKey(attendance);
            }
        }
        return true;
    }

    /**
     * 根据主键获取出差信息.
     *
     * @param id 主键
     * @return 出差信息
     */
    @Override
    public Trip getTripInfoById(Integer id) {
        return tripMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Trip trip, long departId, String msg) {
        List<Staff> list = new ArrayList<Staff>();
        trip = tripMapper.selectByPrimaryKey(trip.getId());
        String actId = trip.getActId();
        if ("agree".equals(msg)) {
            list = activitiService.getNextLeaderByRole(trip.getUserId(), actId, false, "trip", "");
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    /**
     * 计算并生成出差明细表.
     *
     * @param trip 出差信息
     * @return 结果
     * @throws ParseException 日期异常
     */
    @Override
    public boolean initTripDetailInfo(Trip trip) throws ParseException {
        Integer id = trip.getId();
        //无论有没有数据，先删除数据
        tripDetailMapper.deleteById(id);

        if (trip.getTripDate() == null || trip.getBackDate() == null) { //起止信息不全，则不做计算
            return true;
        }

        String userId = trip.getUserId();
        if (trip.getTripDate().equals(trip.getBackDate())) { //只出差一天
            String tripPart = trip.getTripPart() == null ? "0" : trip.getTripPart();
            String backPart = trip.getBackPart() == null ? "1" : trip.getBackPart();
            TripDetail tripDetail = new TripDetail();
            tripDetail.setId(id);
            tripDetail.setUserId(userId);
            tripDetail.setTripDate(trip.getTripDate());
            if ("0".equals(tripPart) && "0".equals(backPart)) {
                tripDetail.setTripPart("mor");
                tripDetail.setTripDay(DOU05);
            } else if ("1".equals(tripPart) && "1".equals(backPart)) {
                tripDetail.setTripPart("aft");
                tripDetail.setTripDay(DOU05);
            } else {
                tripDetail.setTripPart("all");
                tripDetail.setTripDay(1.0);
            }
            tripDetailMapper.insert(tripDetail);
        } else { //多天出差，首尾两天要单独计算，需要检测出差半天的情况
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(trip.getTripDate()));
            //第一天
            TripDetail tripDetail = new TripDetail();
            String tripPart = trip.getTripPart() == null ? "0" : trip.getTripPart();
            tripDetail.setId(id);
            tripDetail.setUserId(userId);
            tripDetail.setTripDate(trip.getTripDate());
            tripDetail.setTripPart("0".equals(tripPart) ? "all" : "aft");
            if ("0".equals(tripPart)) {
                tripDetail.setTripDay(1.0);
            } else {
                tripDetail.setTripDay(DOU05);
            }
            tripDetailMapper.insert(tripDetail);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            //中间天
            while (sdf.parse(sdf.format(calendar.getTime())).before(sdf.parse(trip.getBackDate()))) {
                tripDetail.setId(id);
                tripDetail.setUserId(userId);
                tripDetail.setTripDate(sdf.format(calendar.getTime()));
                tripDetail.setTripPart("all");
                tripDetail.setTripDay(1.0);
                tripDetailMapper.insert(tripDetail);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
            //最后一天
            String backPart = trip.getBackPart() == null ? "1" : trip.getBackPart();
            tripDetail.setId(id);
            tripDetail.setUserId(userId);
            tripDetail.setTripDate(trip.getBackDate());
            tripDetail.setTripPart("1".equals(backPart) ? "all" : "mor");
            if ("1".equals(backPart)) {
                tripDetail.setTripDay(1.0);
            } else {
                tripDetail.setTripDay(DOU05);
            }
            tripDetailMapper.insert(tripDetail);

            //更新出差明细至考勤表
            tripDetailMapper.updateAllDayToAttendance(id);
            tripDetailMapper.updateMorDayToAttendance(id);
            tripDetailMapper.updateAftDayToAttendance(id);

            //校验事项
            calendar.setTime(sdf.parse(trip.getTripDate()));
            while (sdf.parse(sdf.format(calendar.getTime())).before(sdf.parse(trip.getBackDate()))
                    || sdf.parse(sdf.format(calendar.getTime())).equals(sdf.parse(trip.getBackDate()))) {
                Attendance attendance = new Attendance();
                attendance.setCheckDate(sdf.format(calendar.getTime()));
                attendance.setUserId(userId);
                attendance = attendanceMapper.selectByPrimaryKey(attendance);
                if (attendance != null) {
                    attendance = attendanceService.checkAttendanceData(attendance, null);
                    attendanceMapper.updateByPrimaryKey(attendance);
                }
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }
        }
        return true;
    }

    /**
     * 计算出差天数.
     *
     * @param trip 出差信息
     * @return 出差天数
     */
    @Override
    public Double betweenDays(Trip trip) {
        Double days;
        //如果没有开始时间结束时间，则不计算天数
        if (trip.getTripDate() == null || trip.getBackDate() == null) {
            return 0.0;
        }
        //如果出差只有一天
        if (trip.getTripDate().equals(trip.getBackDate())) {
            //只按照上午 下午不太对 如11点出去 4点多回来 应算半天 这里改为小时数 房桂堂20180911
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isBlank(trip.getStartTime())) {
                if (trip.getTripPart().equals(trip.getBackPart())) {
                    return DOU05;
                } else {
                    return 1.0;
                }
            } else {
                //原先的逻辑还是要的 否则在申请假期的时候不能计算出请假天数
                int minutes = Minutes.minutesBetween(DateTime.parse(trip.getStartTime(), format),
                        DateTime.parse(trip.getEndTime(), format)).getMinutes();
                // 20191127 原先是420 现在全部改为465 fangguitang
                if (minutes > 0 && minutes < INT465) {
                    return DOU05;
                } else {
                    return 1.0;
                }
            }
            /*if (trip.getTripPart().equals(trip.getBackPart())) {
                return DOU05;
            } else {
                return 1.0;
            }*/
        } else {
            days = calendarService.daysBetween(trip.getTripDate(), trip.getBackDate(), "1") - 2.0;

            String leavePart = trip.getTripPart() == null ? "0" : trip.getTripPart();
            String backPart = trip.getBackPart() == null ? "1" : trip.getBackPart();
            days = days + ("0".equals(leavePart) ? 1.0 : DOU05) + ("1".equals(backPart) ? 1.0 : DOU05);
        }

        return days;
    }

    @Override
    public List<TripDetail> selectTripInfo(TripDetail trip) {
        return tripDetailMapper.querySelective(trip);
    }

    @Override
    public Trip initTripInfo(Trip trip, String staffId) {
        if (trip.getTripDate() != null && trip.getBackDate() != null) {
            trip.setTripPart(trip.getTripPart() == null ? "0" : trip.getTripPart());
            trip.setBackPart(trip.getBackPart() == null ? "1" : trip.getBackPart());
            Double days = betweenDays(trip);
            trip.setTripDay(days);
        } else {
            trip.setTripDay(0.0);
        }
        return trip;
    }

    @Override
    public Trip initForTripInfo(Trip trip, String staffId) {
        if (trip.getStartTime() != null && trip.getEndTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date startDate = sdf.parse(trip.getStartTime());
                Date endDate = sdf.parse(trip.getEndTime());
                if (!startDate.before(endDate)) {
                    trip.setEndTime(trip.getStartTime());
                    trip.setTripDay(0.0);
                } else {
                    trip.setTripDay(daysBetween(startDate, endDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            trip.setTripDay(0.0);
        }
        return trip;
    }

    /**
     * 计算两个日期之间相差的天数.
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException ParseException
     */
    public static Double daysBetween(Date smdate, Date bdate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        Double betweenDays = Double.parseDouble(String.valueOf((time2 - time1) / (INT1000 * INT3600 * INT24))); //出差小时除24小时计算出差的整天
        long leftMin = (long) ((time2 - time1 - betweenDays * INT1000 * INT3600 * INT24) / (INT1000 * INT60)); //出差小时除24小时计算出差的剩余分钟
        if (leftMin >= INT465) {
            betweenDays += 1;
        } else if (leftMin > INT15) {
            betweenDays += DOU05;
        }
        return betweenDays;
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        Trip trip = tripMapper.queryByActId(actId);
        tripMapper.deleteByActId(actId);
        activitiService.deleteActinstByActId(actId);
        deleteAttendanceInfo(trip);
        tripDetailMapper.deleteById(trip.getId());
        return true;
    }

    @Override
    public boolean forTrip(Trip trip) throws ParseException {
        boolean rs = true;
        //先删除出差信息的明细以及考勤相关
        rs = deleteAttendanceInfo(trip);
        //tripDetailMapper.deleteById(trip.getId());
        if (!rs) {
            return rs;
        }
        //计算出差时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(sdf.format(sdf1.parse(trip.getStartTime()))));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(sdf.format(sdf1.parse(trip.getEndTime()))));
        long time2 = cal.getTimeInMillis();
        // 这个exactBetweenDays是两个日期之间的相差天数 是整数 不考虑小时数
        Double exactBetweenDays = Double.parseDouble((time2 - time1) / (INT1000 * INT3600 * INT24) + "") + 1;
        if (exactBetweenDays - trip.getTripDay() == 0) { //出差天数等于实际天数，直接算全天出差
            trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
            trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
            trip.setTripPart("0");
            trip.setBackPart("1");
        } else if (Math.abs(exactBetweenDays - trip.getTripDay() - DOU05) < MISTAKE) { //出差天数比实际天数少0.5天
            trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
            trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
            //10:05之前，算上午开始出差 20191127 fangguitang 原来是八点半
            if (sdf1.parse(sdf.format(sdf1.parse(trip.getStartTime())) + " 10:05:00").after(sdf1.parse(trip.getStartTime()))) {
                trip.setTripPart("0");
                trip.setBackPart("0");
            } else {
                //08:30之后，算下午开始出差
                // 20191127 这里直接设置 不再进行校验 感觉没有必要 而且校验的也不对 没有考虑自身的情况 fangguitang
                trip.setTripPart("1");
                trip.setBackPart("1");
                /*//先检查最后半天是否有事项，如果没有则记录
                boolean boo = commonService.checkOption(trip.getUserId(), trip.getBackDate(), trip.getBackDate(), "aft", "aft");
                if (boo) {
                    trip.setTripPart("1");
                    trip.setBackPart("1");
                } else { //如果有记录，向前推半天
                    boo = commonService.checkOption(trip.getUserId(), trip.getTripDate(), trip.getTripDate(), "mor", "mor");
                    if (boo) { //如果没有则记录
                        trip.setTripPart("0");
                        trip.setBackPart("0");
                    } else { //如果有，则强行记录
                        trip.setTripPart("1");
                        trip.setBackPart("1");
                    }
                }*/
            }
        } else if (exactBetweenDays - trip.getTripDay() == 1.0) {
            trip = checkTripInfo(trip);
        }
        //修改出差信息
        trip.setStatus("98");
        rs = updateTripInfo(trip);
        if (rs) {
            //生成新的明细及考勤信息
            rs = initTripDetailInfo(trip) && initAttendanceInfo(trip);
        }
        return rs;
    }

    private Trip checkTripInfo(Trip trip) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sdf1.parse(sdf.format(sdf1.parse(trip.getStartTime())) + " 08:30:00").after(sdf1.parse(trip.getStartTime()))) {
            trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
            trip.setBackDate(getAddDay(sdf.format(sdf1.parse(trip.getEndTime())), -1));
            trip.setTripPart("0");
            trip.setBackPart("1");
        } else if (sdf1.parse(trip.getStartTime()).after(sdf1.parse(sdf.format(sdf1.parse(trip.getStartTime())) + " 08:30:00"))
                && sdf1.parse(sdf.format(sdf1.parse(trip.getStartTime())) + " 13:30:00").after(sdf1.parse(trip.getStartTime()))) {
            //首尾各半天
            //检查最后一天上午时候有没有事项
            boolean boo = commonService.checkOption(trip.getUserId(), trip.getBackDate(), trip.getBackDate(), "mor", "mor");
            if (boo) { //如果没有，则记录
                //检测第一天下午有没有事项
                boo = commonService.checkOption(trip.getUserId(), trip.getTripDate(), trip.getTripDate(), "aft", "aft");
                if (boo) {
                    trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                    trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                    trip.setTripPart("1");
                    trip.setBackPart("0");
                } else {
                    //检测最后一天下午有没有事项
                    boo = commonService.checkOption(trip.getUserId(), trip.getBackDate(), trip.getBackDate(), "aft", "aft");
                    if (boo) {
                        trip.setTripDate(getAddDay(sdf.format(sdf1.parse(trip.getStartTime())), 1));
                        trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                        trip.setTripPart("0");
                        trip.setBackPart("1");
                    } else {
                        trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                        trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                        trip.setTripPart("1");
                        trip.setBackPart("0");
                    }
                }
            } else {
                //如果有，则检查第一天上午是否有事项
                boo = commonService.checkOption(trip.getUserId(), trip.getTripDate(), trip.getTripDate(), "mor", "mor");
                if (boo) { //如果没有，则记录
                    trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                    trip.setBackDate(getAddDay(sdf.format(sdf1.parse(trip.getEndTime())), -1));
                    trip.setTripPart("0");
                    trip.setBackPart("1");
                } else { //如果有，则强行按原计划记录
                    trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                    trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                    trip.setTripPart("1");
                    trip.setBackPart("0");
                }
            }
        } else { //除去第一天
            //检查最后一天有没有事项
            boolean boo = commonService.checkOption(trip.getUserId(), trip.getBackDate(), trip.getBackDate(), "mor", "aft");
            if (boo) {
                trip.setTripDate(getAddDay(sdf.format(sdf1.parse(trip.getStartTime())), 1));
                trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                trip.setTripPart("0");
                trip.setBackPart("1");
            } else {
                //检查最后一天上午有没有事项
                boo = commonService.checkOption(trip.getUserId(), trip.getBackDate(), trip.getBackDate(), "0", "1");
                if (boo) {
                    //检测第一天下午有没有事项
                    boo = commonService.checkOption(trip.getUserId(), trip.getTripDate(), trip.getTripDate(), "1", "1");
                    if (boo) {
                        trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                        trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                        trip.setTripPart("1");
                        trip.setBackPart("0");
                    } else {
                        trip.setTripDate(getAddDay(sdf.format(sdf1.parse(trip.getStartTime())), 1));
                        trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                        trip.setTripPart("0");
                        trip.setBackPart("1");
                    }
                } else {
                    //检测第一天有没有事项
                    boo = commonService.checkOption(trip.getUserId(), trip.getTripDate(), trip.getTripDate(), "mor", "aft");
                    if (boo) {
                        trip.setTripDate(sdf.format(sdf1.parse(trip.getStartTime())));
                        trip.setBackDate(getAddDay(sdf.format(sdf1.parse(trip.getEndTime())), -1));
                        trip.setTripPart("0");
                        trip.setBackPart("1");
                    } else {
                        trip.setTripDate(getAddDay(sdf.format(sdf1.parse(trip.getStartTime())), 1));
                        trip.setBackDate(sdf.format(sdf1.parse(trip.getEndTime())));
                        trip.setTripPart("0");
                        trip.setBackPart("1");
                    }
                }
            }
        }
        return trip;
    }

    @Override
    public List<TripDetail> getTripList(String userId, String tripDate, String part) {
        List<TripDetail> tripDetailList = null;
        if ("all".equals(part)) {
            tripDetailList = tripDetailMapper.getTripDetailList(userId, tripDate);
        } else {
            tripDetailList = tripDetailMapper.getTripDetailWithPartList(userId, tripDate, part);
        }
        return tripDetailList;
    }

    /**
     * @param date date
     * @param day  day
     * @return String
     * @throws ParseException ParseException
     */
    public String getAddDay(String date, int day) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = sdf.format(calendar.getTime());
        return date;
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        tripMapper.deleteByActId(actId);
        activitiService.deleteRecordByActId(actId);
        return true;
    }

    private boolean deleteAttendanceInfo(Trip trip) {
        Integer tripId = trip.getId();
        List<TripDetail> tripDetails = tripDetailMapper.getByTripId(tripId);
        for (TripDetail tripDetail : tripDetails) {
            Attendance attendance = new Attendance();
            attendance.setUserId(tripDetail.getUserId());
            if (tripDetail.getTripDate().length() > INT12) {
                attendance.setCheckDate(tripDetail.getTripDate().substring(0, INT10));
            } else {
                attendance.setCheckDate(tripDetail.getTripDate());
            }
            attendance = attendanceService.selectByPrimaryKey(attendance);
            if (attendance != null) {
                //全天
                if ("all".equals(tripDetail.getTripPart())) {
                    List<Map<String, String>> list = tripMapper.queryOtherList(tripDetail.getUserId(), attendance.getCheckDate(), tripDetail.getId());
                    if (list.size() > 0) {
                        outer:
                        for (int i = 0; i < list.size(); i++) {
                            Map<String, String> map = list.get(i);
                            if ("mor".equals(map.get("part"))) {
                                attendance.setAllDay(null);
                                attendance.setMorDay(map.get("type"));
                            } else if ("aft".equals(map.get("part"))) {
                                attendance.setAllDay(null);
                                attendance.setAftDay(map.get("type"));
                            } else {
                                attendance.setAllDay(map.get("type"));
                                attendance.setMorDay(null);
                                attendance.setAftDay(null);
                                break outer;
                            }
                        }
                    } else {
                        attendance.setAllDay(null);
                    }
                } else if ("mor".equals(tripDetail.getTripPart())) { //上午
                    List<Map<String, String>> list = tripMapper.queryOtherWithPartList(tripDetail.getUserId(),
                            attendance.getCheckDate(), tripDetail.getId(), "mor");
                    if (list.size() > 0) {
                        Map<String, String> map = list.get(0);
                        attendance.setMorDay(map.get("type"));
                    } else {
                        attendance.setMorDay(null);
                    }
                } else if ("aft".equals(tripDetail.getTripPart())) { //下午
                    List<Map<String, String>> list = tripMapper.queryOtherWithPartList(tripDetail.getUserId(),
                            attendance.getCheckDate(), tripDetail.getId(), "all");
                    if (list.size() > 0) {
                        Map<String, String> map = list.get(0);
                        attendance.setAftDay(map.get("type"));
                    } else {
                        attendance.setAftDay(null);
                    }
                }
                attendance = attendanceService.checkAttendanceData(attendance, tripDetail.getId());
                attendanceService.updateByPrimaryKey(attendance);
            }
        }
        return true;
    }
}
