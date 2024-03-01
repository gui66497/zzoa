package zzjz.service.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.Depart;
import zzjz.bean.Over;
import zzjz.bean.OverOff;
import zzjz.bean.OverRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.mapper.OverMapper;
import zzjz.service.ActivitiService;
import zzjz.service.AttendanceService;
import zzjz.service.CalendarService;
import zzjz.service.DepartService;
import zzjz.service.OverOffService;
import zzjz.service.OverService;
import zzjz.service.StaffService;
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
 * @version 2017年05月09日16:38
 * @ClassName: OverServiceImpl
 * @Description: 加班方法实现类
 */
@Service
public class OverServiceImpl implements OverService {

    private static final Integer INT7 = 7;

    private static final Integer INT16 = 16;

    @Autowired
    OverMapper overMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    OverOffService overOffService;

    @Autowired
    StaffService staffService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    DepartService departService;

    @Override
    public Page<Over> getOverList(OverRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Over> pageList = (Page<Over>) overMapper.getOverPageList(request);
        return pageList;
    }

    @Override
    public String addOverInfo(Over over) {
        String userId = over.getUserId();

        Staff staff = staffService.getStaffByUserId(userId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(userId, Long.parseLong(defaultDepartId));
        //部门经理以上需要校验是否是节日加班，否则不给于请假，走特殊流程
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
            if (isHoliday) {
                String actId = activitiService.flowStart("common", userId);

                if (actId == null || "".equals(actId)) {
                    return "";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = sdf.format(new Date());
                over.setCreateTime(createTime);
                over.setStatus("00");
                over.setActId(actId);
                if (overMapper.selectByPrimaryKey(over.getId()) != null) {
                    return overMapper.updateByPrimaryKey(over) > 0 ? actId : "";
                } else {
                    return overMapper.insert(over) > 0 ? actId : "";
                }
            }
            return "error";
        } else if ("firstLeader".equals(role)) { //总监层要校验是否是节日加班，否则不给于请假，走加班流程
            boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
            if (isHoliday) {
                String actId = activitiService.flowStart("over", userId);

                if (actId == null || "".equals(actId)) {
                    return "";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = sdf.format(new Date());
                over.setCreateTime(createTime);
                over.setStatus("00");
                over.setActId(actId);
                if (overMapper.selectByPrimaryKey(over.getId()) != null) {
                    return overMapper.updateByPrimaryKey(over) > 0 ? actId : "";
                } else {
                    return overMapper.insert(over) > 0 ? actId : "";
                }
            }
            return "error";
        } else {
            String actId = activitiService.flowStart("over", userId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = sdf.format(new Date());
            over.setCreateTime(createTime);
            over.setStatus("00");
            over.setActId(actId);
            if (overMapper.selectByPrimaryKey(over.getId()) != null) {
                return overMapper.updateByPrimaryKey(over) > 0 ? actId : "";
            } else {
                return overMapper.insert(over) > 0 ? actId : "";
            }
        }
    }

    @Override
    public String updateOverInfo(Over over) {
        String userId = over.getUserId();

        Staff staff = staffService.getStaffByUserId(userId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(userId, Long.parseLong(defaultDepartId));
        //非普通员工需要校验是否是节日加班，否则不给于请假
        if ("user".equals(role) || "hr".equals(role)) {
            return overMapper.updateByPrimaryKey(over) > 0 ? over.getActId() : "";
        } else {
            boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
            if (isHoliday) {
                return overMapper.updateByPrimaryKey(over) > 0 ? over.getActId() : "";
            }
            return "error";
        }
    }

    @Override
    public boolean submitOverTask(Over over, String user, String staffId) {
        over = overMapper.selectByPrimaryKey(over.getId());
        boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
        String actId = over.getActId();

        Staff staff = staffService.getStaffByUserId(staffId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //根据部门和员工号，查询角色信息
        String role = staffService.getStaffByDepartAndUser(staffId, Long.parseLong(defaultDepartId));

        boolean rs;
        if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
            rs = activitiService.submitCommonTask(actId, user);
        } else {
            rs = activitiService.submitOverTask(actId, user, isHoliday);
        }

        if (rs) {
            over.setStatus("01");
            rs = overMapper.updateByPrimaryKey(over) > 0;
        }
        if (activitiService.isEndProcess(over.getActId())) {
            over.setStatus("99");
            rs = overMapper.updateByPrimaryKey(over) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initOverOffInfo(over) && initAttendanceInfo(over);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public boolean approveOverTask(Over over, String user, String msg, String reason) {
        over = overMapper.selectByPrimaryKey(over.getId());
        String actId = over.getActId();
        boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
        boolean rs = activitiService.approveOverTask(actId, user, msg, reason, isHoliday);

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                over.setStatus("03");
            } else {
                over.setStatus("02");
            }
            rs = overMapper.updateByPrimaryKey(over) > 0;
        } else if (activitiService.isEndProcess(over.getActId())) {
            over.setStatus("99");
            rs = overMapper.updateByPrimaryKey(over) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initOverOffInfo(over) && initAttendanceInfo(over);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public boolean terminationOverTask(Over over) {
        over = overMapper.selectByPrimaryKey(over.getId());
        String actId = over.getActId();
        boolean rs = activitiService.terminationTask(actId, "over");
        if (rs) {
            over.setStatus("99");
            rs = overMapper.updateByPrimaryKey(over) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                try {
                    rs = initOverOffInfo(over) && initAttendanceInfo(over);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> getOverMapList(Over over) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Staff staff = staffService.getStaffByUserId(over.getUserId()); //申请人信息
        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId())); //申请人机构
        List<ActHis> actHisList = activitiService.getActHisList(over.getActId()); //流程信息

        Map<String, Object> map = new HashMap<>();
        map.put("userName", over.getUserName());
        map.put("depart", depart.getDepartName());
        map.put("position", staff.getPosition());
        map.put("reason", over.getReason());
        map.put("overDay", over.getOverDay());
        map.put("overDate", over.getOverDate());

        String firstPath = "";
        String firstTime = "";
        String secondPath = "";
        String secondTime = "";
        String thirdPath = "";
        String thirdTime = "";
        String overDate = over.getOverDate();
        if (calendarService.isHoliDay(overDate)) {          //节假日
            map.put("overPeriod", "法定节假日加班");
            for (ActHis actHis : actHisList) {
                if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                    thirdPath = staffService.getSignPath(actHis.getAssignee());
                    thirdTime = sdf2.format(actHis.getEndTime());              //总经办
                }
            }
        } else {   //工作日或双休日
            map.put("overPeriod", calendarService.isWorkDay(overDate) ? "工作日加班" : "双休日加班");
            for (ActHis actHis : actHisList) {
                if ("firstAudit".equals(actHis.getActId())) {
                    if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                        firstPath = staffService.getSignPath(actHis.getAssignee());
                        firstTime = sdf2.format(actHis.getEndTime());           //直接主管
                    }
                }
                if ("secondAudit".equals(actHis.getActId())) {
                    if (!"/".equals(actHis.getAssignee()) && StringUtils.isNotBlank(actHis.getAssignee())) {
                        secondPath = staffService.getSignPath(actHis.getAssignee());
                        secondTime = sdf2.format(actHis.getEndTime());           //部门经理
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
        try {
            map.put("createTime", sdf2.format(sdf.parse(over.getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean ignore(Integer id) {
        Over over = overMapper.selectByPrimaryKey(id);
        over.setStatus("93");
        return overMapper.updateByPrimaryKey(over) > 0;
    }

    public boolean initOverOffInfo(Over over) throws ParseException {
        boolean result = true;
        //计算月份
        String monthDate = over.getOverDate().substring(0, INT7);
        //考勤周期改为自然月 不需要以下判断
        /*if (Integer.parseInt(over.getOverDate().split("-")[2]) < INT16) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(sdf.parse(over.getOverDate()));
            c.add(java.util.Calendar.MONTH, -1);
            monthDate = sdf.format(c.getTime()).substring(0, INT7);
        }*/

        OverOff overOff = overOffService.queryOverOffInfo(over.getUserId(), monthDate);
        if (overOff == null) {
            overOffService.initOverOffInfo(over.getUserId(), monthDate);
            overOff = overOffService.queryOverOffInfo(over.getUserId(), monthDate);
        }
        boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
        if (!isHoliday) {
            overOff.setOverDay(overOff.getOverDay() + over.getOverDay());
            overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
            result = overOffService.updateOverOffInfo(overOff);
        }
        if (result) {
            List<OverOff> list = overOffService.queryOverOffList(over.getUserId(), monthDate);
            if (list.size() > 0) {
                Double lastLeft = overOff.getLeftDay();
                for (OverOff oo : list) {
                    oo.setLastLeft(lastLeft);
                    oo.setLeftDay(lastLeft + oo.getOverDay() - oo.getOffDay());
                    result = overOffService.updateOverOffInfo(oo);
                    lastLeft = oo.getLeftDay();
                }
            }
        }
        return result;
    }

    private boolean initAttendanceInfo(Over over) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(over.getUserId());
        attendance.setCheckDate(over.getOverDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            attendance.setOverTime(over.getOverDay());
            if ("1".equals(attendance.getWorkDay())) {
                attendance.setEveDay("10");
            } else if ("0".equals(attendance.getWorkDay())) {
                attendance.setAllDay("10");
            }
            attendance = attendanceService.checkAttendanceData(attendance, null);
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }

    @Override
    public Over getOverInfoById(Integer id) {
        return overMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Over over, long departId, String msg) {
        List<Staff> list = new ArrayList<Staff>();
        over = overMapper.selectByPrimaryKey(over.getId());
        String actId = over.getActId();
        if ("agree".equals(msg)) {
            boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
            String condition;
            if (isHoliday) {
                condition = "isHoliday=='1'";
            } else {
                condition = "isHoliday=='0'";
            }
            list = activitiService.getNextLeaderByRole(over.getUserId(), actId, false, "over", condition);
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    @Override
    public Over selectOverInfo(Over over) {
        List<Over> overs = overMapper.querySelective(over);
        return overs.size() > 0 ? overs.get(0) : null;
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        Over over = overMapper.queryByActId(actId);
        overMapper.deleteByActId(actId);
        activitiService.deleteActinstByActId(actId);
        //对于已完成的流程进行删除加班调休信息操作
        if ("99".equals(over.getStatus())) {
            //计算月份
            String monthDate = over.getOverDate().substring(0, INT7);
            //考勤周期改为自然月 不需要以下判断
            /*if (Integer.parseInt(over.getOverDate().split("-")[2]) < INT16) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Calendar c = java.util.Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(over.getOverDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(java.util.Calendar.MONTH, -1);
                monthDate = sdf.format(c.getTime()).substring(0, INT7);
            }*/
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
                List<OverOff> list = overOffService.queryOverOffList(over.getUserId(), monthDate);
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
        deleteAttendanceInfo(over);
        return true;
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        overMapper.deleteByActId(actId);
        activitiService.deleteRecordByActId(actId);
        return true;
    }

    private boolean deleteAttendanceInfo(Over over) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(over.getUserId());
        attendance.setCheckDate(over.getOverDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            attendance.setOverTime(over.getOverDay());
            if ("1".equals(attendance.getWorkDay())) {
                attendance.setEveDay(null);
            } else if ("0".equals(attendance.getWorkDay())) {
                attendance.setAllDay(null);
            }
            attendance = attendanceService.checkAttendanceData(attendance, null);
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }
}
