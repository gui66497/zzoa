package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Attendance;
import zzjz.bean.Depart;
import zzjz.bean.Out;
import zzjz.bean.OutRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.TripDetail;
import zzjz.mapper.OutMapper;
import zzjz.service.ActivitiService;
import zzjz.service.AttendanceService;
import zzjz.service.DepartService;
import zzjz.service.OutService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
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
 * @ClassName: OutServiceImpl
 * @Description: 外出方法实现类
 */
@Service
public class OutServiceImpl implements OutService {

    private static final Double DOU05 = 0.5;

    @Autowired
    OutMapper outMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    TripService tripService;

    @Autowired
    StaffService staffService;

    @Autowired
    DepartService departService;

    @Override
    public Page<Out> getOutList(OutRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Out> pageList = (Page<Out>) outMapper.getOutPageList(request);
        return pageList;
    }

    @Override
    public String addOutInfo(Out out) {
        String userId = out.getUserId();
        String actId = activitiService.flowStart("out", userId);

        if (actId == null || "".equals(actId)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        out.setCreateTime(createTime);
        out.setStatus("00");
        out.setActId(actId);
        if ("mor".equals(out.getOutPart()) || "aft".equals(out.getOutPart())) {
            out.setOutDay(DOU05);
        } else if ("all".equals(out.getOutPart())) {
            out.setOutDay(1.0);
        } else {
            out.setOutDay(0.0);
        }
        if (outMapper.selectByPrimaryKey(out.getId()) != null) {
            return outMapper.updateByPrimaryKey(out) > 0 ? actId : "";
        } else {
            return outMapper.insert(out) > 0 ? actId : "";
        }
    }

    @Override
    public boolean updateOutInfo(Out out) {
        if ("mor".equals(out.getOutPart()) || "aft".equals(out.getOutPart())) {
            out.setOutDay(DOU05);
        } else if ("all".equals(out.getOutPart())) {
            out.setOutDay(1.0);
        } else {
            out.setOutDay(0.0);
        }
        return outMapper.updateByPrimaryKey(out) > 0;
    }

    @Override
    public boolean submitOutTask(Out out, String user) {
        out = outMapper.selectByPrimaryKey(out.getId());
        String actId = out.getActId();
        boolean rs = activitiService.submitTask(actId, user, "out");
        if (rs) {
            out.setStatus("01");
            rs = outMapper.updateByPrimaryKey(out) > 0;
        }
        if (activitiService.isEndProcess(out.getActId())) {
            out.setStatus("99");
            rs = outMapper.updateByPrimaryKey(out) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(out);
            }
        }
        return rs;
    }

    @Override
    public boolean approveOutTask(Out out, String user, String msg, String reason) {
        out = outMapper.selectByPrimaryKey(out.getId());
        String actId = out.getActId();
        boolean rs = activitiService.approveTask(actId, user, "out", msg, reason);

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                out.setStatus("03");
            } else {
                out.setStatus("02");
            }
            rs = outMapper.updateByPrimaryKey(out) > 0;
        } else if (activitiService.isEndProcess(out.getActId())) {
            out.setStatus("99");
            rs = outMapper.updateByPrimaryKey(out) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(out);
            }
        }
        return rs;
    }

    @Override
    public boolean terminationOutTask(Out out) {
        out = outMapper.selectByPrimaryKey(out.getId());
        String actId = out.getActId();
        boolean rs = activitiService.terminationTask(actId, "out");
        if (rs) {
            out.setStatus("99");
            rs = outMapper.updateByPrimaryKey(out) > 0;
            //流程结束后，更新数据到考勤表，并进行校验
            if (rs) {
                rs = initAttendanceInfo(out);
            }
        }
        return rs;
    }

    @Override
    public Map<String, Object> getOutMapList(Out out) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        Staff staff = staffService.getStaffByUserId(out.getUserId()); //申请人信息
        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId())); //申请人机构
        List<ActHis> actHisList = activitiService.getActHisList(out.getActId()); //流程信息

        Map<String, Object> map = new HashMap<>();
        map.put("userName", out.getUserName());
        map.put("depart", depart.getDepartName());
        map.put("position", staff.getPosition());
        map.put("reason", out.getReason());
        switch (out.getOutPart()) {
            case "mor":
                map.put("outPeriod", "上午");
                break;
            case "aft":
                map.put("outPeriod", "下午");
                break;
            case "all":
                map.put("outPeriod", "全天");
                break;
            default:
                map.put("outPeriod", "未知");
                break;
        }
        map.put("outDate", out.getOutDate());

        //和忘打卡共用的逻辑
        Map<String, Object> map2 = attendanceService.signGenerate(staff, actHisList);
        map.putAll(map2);

        try {
            map.put("createTime", sdf2.format(sdf.parse(out.getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean ignore(Integer id) {
        Out out = outMapper.selectByPrimaryKey(id);
        out.setStatus("93");
        return outMapper.updateByPrimaryKey(out) > 0;
    }

    private boolean initAttendanceInfo(Out out) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(out.getUserId());
        attendance.setCheckDate(out.getOutDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            if ("mor".equals(out.getOutPart())) {
                attendance.setMorDay("21");
            } else if ("aft".equals(out.getOutPart())) {
                attendance.setAftDay("21");
            } else if ("all".equals(out.getOutPart())) {
                attendance.setAllDay("21");
            }
            attendance = attendanceService.checkAttendanceData(attendance, null);
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }

    @Override
    public Out getOutInfoById(Integer id) {
        return outMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Out out, long departId, String msg) {
        List<Staff> list = new ArrayList<Staff>();
        out = outMapper.selectByPrimaryKey(out.getId());
        String actId = out.getActId();
        if ("agree".equals(msg)) {
            list = activitiService.getNextLeaderByRole(out.getUserId(), actId, false, "out", "");
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    @Override
    public List<Out> selectOutInfo(Out out) {
        return outMapper.querySelective(out);
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        Out out = outMapper.queryByActId(actId);
        outMapper.deleteByActId(actId);
        activitiService.deleteActinstByActId(actId);
        deleteAttendanceInfo(out);
        return true;
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        outMapper.deleteByActId(actId);
        activitiService.deleteRecordByActId(actId);
        return true;
    }

    private boolean deleteAttendanceInfo(Out out) {
        Boolean rs = true;
        Attendance attendance = new Attendance();
        attendance.setUserId(out.getUserId());
        attendance.setCheckDate(out.getOutDate());
        attendance = attendanceService.selectByPrimaryKey(attendance);
        if (attendance != null) {
            if ("all".equals(out.getOutPart())) {
                //查找是否同时存在出差信息
                List<TripDetail> list = tripService.getTripList(out.getUserId(), out.getOutDate(), "all");
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
            } else if ("mor".equals(out.getOutPart())) {
                //查找是否同时存在出差信息
                List<TripDetail> list = tripService.getTripList(out.getUserId(), out.getOutDate(), "mor");
                if (list.size() > 0) {
                    attendance.setMorDay("20");
                } else {
                    attendance.setMorDay(null);
                }
            } else if ("aft".equals(out.getOutPart())) {
                //查找是否同时存在出差信息
                List<TripDetail> list = tripService.getTripList(out.getUserId(), out.getOutDate(), "aft");
                if (list.size() > 0) {
                    attendance.setAftDay("20");
                } else {
                    attendance.setAftDay(null);
                }
            }
            attendance = attendanceService.checkAttendanceData(attendance, out.getId());
            rs = attendanceService.updateByPrimaryKey(attendance);
        }
        return rs;
    }
}
