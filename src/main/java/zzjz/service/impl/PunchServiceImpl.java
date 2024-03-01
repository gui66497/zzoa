package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.Depart;
import zzjz.bean.Notice;
import zzjz.bean.PagingEntity;
import zzjz.bean.Punch;
import zzjz.bean.PunchRequest;
import zzjz.bean.Staff;
import zzjz.mapper.PunchMapper;
import zzjz.service.ActivitiService;
import zzjz.service.AttendanceService;
import zzjz.service.DepartService;
import zzjz.service.NoticeService;
import zzjz.service.PunchService;
import zzjz.service.StaffService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年05月10日9:49
 * @ClassName: PunchServiceImpl
 * @Description: 忘打卡方法实现类
 */
@Service
public class PunchServiceImpl implements PunchService {

    @Autowired
    PunchMapper punchMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    StaffService staffService;

    @Autowired
    DepartService departService;

    @Override
    public Page<Punch> getPunchPageList(PunchRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Punch> pageList = (Page<Punch>) punchMapper.getPunchPageList(request);
        return pageList;
    }

    @Override
    public String addPunchInfo(Punch punch) {
        String userId = punch.getUserId();
        String actId = activitiService.flowStart("punch", userId);

        if (actId == null || "".equals(actId)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        punch.setStatus("00");
        punch.setCreateTime(createTime);
        punch.setActId(actId);
        if (punchMapper.selectByPrimaryKey(punch.getId()) != null) {
            return punchMapper.updateByPrimaryKey(punch) > 0 ? actId : "";
        } else {
            return punchMapper.insert(punch) > 0 ? actId : "";
        }
    }

    @Override
    public boolean updatePunchInfo(Punch punch) {
        return punchMapper.updateByPrimaryKey(punch) > 0;
    }

    @Override
    public boolean submitPunchTask(Punch punch, String user) {
        punch = punchMapper.selectByPrimaryKey(punch.getId());
        String actId = punch.getActId();
        boolean rs = activitiService.submitTask(actId, user, "punch");
        if (rs) {
            punch.setStatus("01");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
        }
        if (activitiService.isEndProcess(punch.getActId())) {
            punch.setStatus("99");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(punch);
            }
        }
        return rs;
    }

    @Override
    public boolean noticePunchTask(Punch punch, String user) {
        punch = punchMapper.selectByPrimaryKey(punch.getId());
        String actId = punch.getActId();
        boolean rs = activitiService.submitTask(actId, "", "punch");
        if (rs) {
            punch.setStatus("01");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
        }
        if (activitiService.isEndProcess(punch.getActId())) {
            punch.setStatus("99");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(punch);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //告知
                Notice notice = new Notice();
                notice.setNoticeId(Long.parseLong(user));
                notice.setStaffId(Long.parseLong(punch.getUserId()));
                notice.setCreateTime(sdf.format(new Date()));
                String noticeDesc = punch.getUserName() + "于" + punch.getPunchDate();
                if ("0".equals(punch.getPunchTyp())) {
                    noticeDesc += " 上午签到 ";
                } else if ("1".equals(punch.getPunchTyp())) {
                    noticeDesc += " 上午签退 ";
                } else if ("2".equals(punch.getPunchTyp())) {
                    noticeDesc += " 下午签到 ";
                } else if ("3".equals(punch.getPunchTyp())) {
                    noticeDesc += " 下午签退 ";
                }
                noticeDesc += "忘记打卡";
                if (punch.getReason() != null && !"".equals(punch.getReason())) {
                    noticeDesc += "，原因是：" + punch.getReason();
                }
                noticeDesc += "。";
                notice.setNoticeDesc(noticeDesc);
                noticeService.addNoticeInfo(notice);
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> getPunchMapList(Punch punch) {
        Staff staff = staffService.getStaffByUserId(punch.getUserId()); //申请人信息
        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId())); //申请人单位
        List<ActHis> actHisList = activitiService.getActHisList(punch.getActId()); //流程信息

        Map<String, Object> map = new HashMap<>();
        map.put("userName", punch.getUserName());
        switch (punch.getPunchTyp()) {
            case "0":
                map.put("punchType", "上午签到忘刷卡");
                map.put("realTime", "8:30");
                break;
            case "1":
                map.put("punchType", "上午签退忘刷卡");
                map.put("realTime", "11:30");
                break;
            case "2":
                map.put("punchType", "下午签到忘刷卡");
                map.put("realTime", "1:30");
                break;
            case "3":
                map.put("punchType", "下午签退忘刷卡");
                map.put("realTime", "17:30");
                break;
            default:
                map.put("punchType", "未知");
                map.put("realTime", "未知");
                break;
        }
        map.put("depart", depart.getDepartName());
        map.put("time", punch.getPunchDate());
        map.put("witness", punch.getWitness());

        //和忘打卡共用的逻辑
        Map<String, Object> map2 = attendanceService.signGenerate(staff, actHisList);
        map.putAll(map2);
        return map;
    }

    @Override
    public boolean ignore(Integer id) {
        Punch punch = punchMapper.selectByPrimaryKey(id);
        punch.setStatus("93");
        return punchMapper.updateByPrimaryKey(punch) > 0;
    }

    @Override
    public boolean approvePunchTask(Punch punch, String user, String msg, String reason) {
        punch = punchMapper.selectByPrimaryKey(punch.getId());
        String actId = punch.getActId();
        boolean rs = activitiService.approveTask(actId, user, "punch", msg, reason);

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                punch.setStatus("03");
            } else {
                punch.setStatus("02");
            }
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
        } else if (activitiService.isEndProcess(punch.getActId())) {
            punch.setStatus("99");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(punch);
            }
        }
        return rs;
    }

    @Override
    public boolean terminationPunchTask(Punch punch) {
        punch = punchMapper.selectByPrimaryKey(punch.getId());
        String actId = punch.getActId();
        boolean rs = activitiService.terminationTask(actId, "punch");
        if (rs) {
            punch.setStatus("99");
            rs = punchMapper.updateByPrimaryKey(punch) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(punch);
            }
        }
        return rs;
    }

    private boolean initAttendanceInfo(Punch punch) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(punch.getUserId());
        attendance.setCheckDate(punch.getPunchDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            if ("0".equals(punch.getPunchTyp())) {
                attendance.setSignTime(attendance.getBeginTime());
            } else if ("1".equals(punch.getPunchTyp())) {
                attendance.setLeaveTime("12:30");
            } else if ("2".equals(punch.getPunchTyp())) {
                attendance.setSignTime("12:30");
            } else if ("3".equals(punch.getPunchTyp())) {
                attendance.setLeaveTime(attendance.getEndTime());
            }
            attendance = attendanceService.checkAttendanceData(attendance, null);
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }

    @Override
    public Punch getPunchInfoById(Integer id) {
        return punchMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Punch punch, long departId, String msg) {
        List<Staff> list = new ArrayList<Staff>();
        punch = punchMapper.selectByPrimaryKey(punch.getId());
        String actId = punch.getActId();
        if ("agree".equals(msg)) {
            list = activitiService.getNextLeaderByRole(punch.getUserId(), actId, false, "punch", "");
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    @Override
    public List<Punch> selectPunchInfo(Punch punch) {
        return punchMapper.querySelective(punch);
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        Punch punch = punchMapper.queryByActId(actId);
        punchMapper.deleteByActId(actId);
        activitiService.deleteActinstByActId(actId);
        deleteAttendanceInfo(punch);
        return true;
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        punchMapper.deleteByActId(actId);
        activitiService.deleteRecordByActId(actId);
        return true;
    }

    private boolean deleteAttendanceInfo(Punch punch) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(punch.getUserId());
        attendance.setCheckDate(punch.getPunchDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            if ("0".equals(punch.getPunchTyp()) || "2".equals(punch.getPunchTyp())) {
                attendance.setSignTime(null);
            } else if ("1".equals(punch.getPunchTyp()) || "3".equals(punch.getPunchTyp())) {
                attendance.setLeaveTime(null);
            }
            attendance = attendanceService.checkAttendanceData(attendance, null);
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }
}
