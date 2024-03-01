package zzjz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Annual;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.mapper.AnnualMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.AnnualService;
import zzjz.service.StaffService;
import zzjz.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:31
 * @ClassName: AnnualServiceImpl
 * @Description: 年假实现类
 */
@Service
public class AnnualServiceImpl implements AnnualService {

    private static final Integer INT12 = 12;

    private static final Integer INT10 = 10;

    private static final Double DOU5 = 5.0;

    private static final Integer INT20 = 20;

    private static final Double DOU10 = 10.0;

    private static final Double DOU15 = 15.0;

    @Autowired
    StaffService staffService;

    @Autowired
    StaffMapper staffMapper;

    @Autowired
    AnnualMapper annualMapper;

    /**
     * 根据用户ID查询年假天数.
     *
     * @param userId 用户ID
     * @return 天数
     */
    @Override
    public Double annualDays(String userId, String year) {
        //根据用户ID查询用户信息
        Staff staff = staffService.getStaffByUserIdAll(userId);

        //根据用户信息，计算年假
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String inDate = staff.getEntryDate() == null ? sdf.format(new Date()) : staff.getEntryDate(); //入职日期计算
        Double pastYear = staff.getPreSeniority() == null ? 0.0 : staff.getPreSeniority() / INT12; //加入公司前已工作年限
        //Double inYear = staff.getSeniority()==null?0.0:staff.getSeniority(); //司龄
        Double inYear = Double.parseDouble(year) - Double.parseDouble(inDate.split("-")[0]);
        Double workYear = pastYear + inYear;
        Double leaveDays = 0.0;
        if (workYear >= 1 && workYear <= INT10) {
            leaveDays = DOU5;
        } else if (workYear > INT10 && workYear <= INT20) {
            leaveDays = DOU10;
        } else if (workYear > INT20) {
            leaveDays = DOU15;
        }
        try {
            Date joinDate = sdf.parse(inDate);
            if (inYear < 1) {
                Date beginDate;
                Date endDate;

                String nextYear = Integer.parseInt(year) + 1 + "";

                beginDate = sdf.parse(year + "-03-16");
                endDate = sdf.parse(nextYear + "-03-15");

                if (joinDate.after(beginDate) && joinDate.before(endDate)) {
                    int leftDays = DateUtil.daysBetween(joinDate, endDate);
                    int allDays = DateUtil.daysBetween(beginDate, endDate);
                    leaveDays = ((double) Math.floor(2 * leaveDays * leftDays / allDays) / 2);
                } else if (joinDate.after(endDate)) {
                    leaveDays = 0.0;
                }
            }
            if(workYear<1){
                leaveDays = ((double) Math.floor(DOU5*(1-workYear)*2) / 2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return leaveDays;
    }

    @Override
    public boolean initAnnual(String userId, String year) {
        Double annualDays = annualDays(userId, year);
        Annual annual = new Annual();
        annual.setUserId(userId);
        annual.setYear(year);
        annual.setAnnualAll(annualDays);
        annual.setAnnualUse(0.0);
        annual.setAnnualLeft(annualDays);
        if (annualMapper.selectByPrimaryKey(annual) == null) {
            return annualMapper.insert(annual) > 0;
        } else {
            return true;
        }
    }

    @Override
    public boolean initBatchAnnual(String year) {
        boolean result = false;
        try {
            List<Staff> list = staffMapper.getStaffList(new StaffRequest());
            for (Staff staff : list) {
                initAnnual(staff.getStaffId() + "", year);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean updateAnnualInfo(Annual annual) {
        int i = annualMapper.updateByPrimaryKey(annual);
        if (i > 0) {
            return true;
        } else if (i == 0) {
            return annualMapper.insert(annual) > 0;
        } else {
            return false;
        }
    }

    @Override
    public boolean queryAndInitAnnual(long staffId) {
        String year = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            if (sdf.parse(sdf.format(date)).after(sdf.parse(sdf.format(date).split("-")[0] + "-03-15"))) {
                year = sdf.format(date).split("-")[0];
            } else {
                year = Integer.parseInt(sdf.format(date).split("-")[0]) - 1 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //查询年假信息
        Annual annual = new Annual();
        annual.setUserId(staffId + "");
        annual.setYear(year);
        annual = annualMapper.selectByPrimaryKey(annual);
        if (annual == null) {
            return initAnnual(staffId + "", year);
        } else {
            Double annualDays = annualDays(staffId + "", year);
            annual.setUserId(staffId + "");
            annual.setYear(year);
            annual.setAnnualAll(annualDays);
            annual.setAnnualLeft(annualDays - annual.getAnnualUse());
            return updateAnnualInfo(annual);
        }
    }
}
