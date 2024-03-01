package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.OverOff;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.mapper.OverOffMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.OverOffService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:31
 * @ClassName: AnnualServiceImpl
 * @Description: 年假实现类
 */
@Service
public class OverOffServiceImpl implements OverOffService {

    private static final Integer INT7 = 7;

    @Autowired
    OverOffMapper overOffMapper;

    @Autowired
    StaffMapper staffMapper;

    @Override
    public boolean initOverOffInfo(String userId, String monthDate) throws ParseException {
        //如果本月已有数据，则直接跳过
        if (queryOverOffInfo(userId, monthDate) != null) {
            return true;
        }
        //先查询上个月剩余天数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(monthDate + "-01"));
        c.add(Calendar.MONTH, -1);
        String lastMonth = sdf.format(c.getTime()).substring(0, INT7);
        OverOff overOff = new OverOff();
        overOff.setUserId(userId);
        overOff.setMonthDate(lastMonth);
        overOff = overOffMapper.selectByPrimaryKey(overOff);
        Double lastLeft = 0.0;
        if (overOff != null) {
            if ( overOff.getLeftDay() == null ) {
                lastLeft = 0.0;
            } else {
                lastLeft = overOff.getLeftDay();
            }
        }
        overOff = new OverOff();
        overOff.setUserId(userId);
        overOff.setMonthDate(monthDate);
        overOff.setLastLeft(lastLeft);
        overOff.setOverDay(0.0);
        overOff.setOffDay(0.0);
        overOff.setUseDay(0.0);
        overOff.setLeftDay(lastLeft);
        return overOffMapper.insertSelective(overOff) > 0;
    }

    @Override
    public boolean batchInitOverOffInfo(String monthDate) {
        boolean result = false;
        try {
            StaffRequest request = new StaffRequest();
            request.setOnJob(1);
            List<Staff> list = staffMapper.getStaffList(request);
            for (Staff staff : list) {
                initOverOffInfo(staff.getStaffId() + "", monthDate);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateOverOffInfo(OverOff overOff) {
        return overOffMapper.updateByPrimaryKeySelective(overOff) > 0;
    }

    @Override
    public OverOff queryOverOffInfo(String userId, String monthDate) {
        OverOff overOff = new OverOff();
        overOff.setUserId(userId);
        overOff.setMonthDate(monthDate);
        return overOffMapper.selectByPrimaryKey(overOff);
    }

    @Override
    public boolean clearLastLeftDay(String userId, String monthDate) {
        OverOff overOff = queryOverOffInfo(userId, monthDate);
        if (overOff == null) {
            try {
                initOverOffInfo(userId, monthDate);
                overOff = queryOverOffInfo(userId, monthDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (overOff != null) {
            overOff.setLastLeft(0.0);
            overOff.setLeftDay(overOff.getLastLeft() + overOff.getOverDay() - overOff.getOffDay());
            return overOffMapper.updateByPrimaryKeySelective(overOff) > 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateOverOffWithUseInfo(OverOff overOff, Double deleteDays, Double leaveDays) {
        if (deleteDays > 0) {
            List<OverOff> list;
            //查询最近一次上月剩余为0的日期
            OverOff overOff1 = overOffMapper.queryLastLeftZeroDaysInfo(overOff.getUserId());
            if (overOff1 != null) {
                list = overOffMapper.queryListByMonthAndUserDesc(overOff.getUserId(), overOff1.getMonthDate());
            } else {
                list = overOffMapper.queryListByMonthAndUser(overOff.getUserId(), null);
            }
            for (OverOff o : list) {
                if (o.getUseDay() == 0) {
                    continue;
                }
                if (o.getUseDay() >= deleteDays) {
                    o.setUseDay(o.getUseDay() - deleteDays);
                    overOffMapper.updateByPrimaryKeySelective(o);
                    break;
                } else {
                    deleteDays = deleteDays - o.getUseDay();
                    o.setUseDay(0.0);
                    overOffMapper.updateByPrimaryKeySelective(o);
                }
            }
        }
        if (leaveDays > 0) {
            List<OverOff> list;
            //查询最近一次上月剩余为0的日期
            OverOff overOff1 = overOffMapper.queryLastLeftZeroDaysInfo(overOff.getUserId());
            if (overOff1 != null) {
                list = overOffMapper.queryListByMonthAndUser(overOff.getUserId(), overOff1.getMonthDate());
            } else {
                list = overOffMapper.queryListByMonthAndUser(overOff.getUserId(), null);
            }
            for (OverOff o : list) {
                if (o.getOverDay().equals(o.getUseDay()) ) {
                    continue;
                }
                if (o.getOverDay() - o.getUseDay() >= leaveDays) {
                    o.setUseDay(o.getUseDay() + leaveDays);
                    overOffMapper.updateByPrimaryKeySelective(o);
                    break;
                } else {
                    leaveDays = leaveDays - o.getOverDay() + o.getUseDay();
                    o.setUseDay(o.getOverDay());
                    overOffMapper.updateByPrimaryKeySelective(o);
                }
            }
        }
        return overOffMapper.updateByPrimaryKeySelective(overOff) > 0;
    }

    @Override
    public List<OverOff> queryOverOffList(String userId, String monthDate) {
        return overOffMapper.queryListBigger(userId, monthDate);
    }
}
