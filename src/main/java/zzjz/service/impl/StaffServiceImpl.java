package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.mapper.StaffMapper;
import zzjz.service.StaffService;

/**
 * @ClassName: StaffServiceImpl
 * @Description: 员工服务实现类
 * @author 房桂堂
 * @date 2017/3/2 15:46
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public Page<Staff> getStaffListPage(StaffRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Staff> staffPage = (Page<Staff>) staffMapper.getStaffList();
        long total = staffPage.getTotal();
        return staffPage;
    }

    @Override
    public boolean addStaff(Staff staff) {
        int res = staffMapper.insertSelective(staff);
        return res > 0;
    }
}
