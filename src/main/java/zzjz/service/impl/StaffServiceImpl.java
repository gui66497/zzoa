package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Column;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.mapper.ColumnMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.StaffService;

import java.util.List;

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

    @Autowired
    private ColumnMapper columnMapper;

    @Override
    public Page<Staff> getStaffListPage(StaffRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Staff> staffPage = (Page<Staff>) staffMapper.getStaffList(request);
        long total = staffPage.getTotal();
        return staffPage;
    }

    @Override
    public boolean addStaff(Staff staff) {
        int res = staffMapper.insertSelective(staff);
        return res > 0;
    }

    @Override
    public List<Column> getAllColumn() {
        return columnMapper.getAllColumn();
    }

    @Override
    public boolean updateCol(Column column) {
        return columnMapper.updateByName(column) > 0;
    }

    @Override
    public boolean delByStaffId(long staffId) {
        return staffMapper.delByStaffId(staffId) > 0;
    }
}
