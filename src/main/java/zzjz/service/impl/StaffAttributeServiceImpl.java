package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.StaffAttribute;
import zzjz.bean.StaffAttributeRequest;
import zzjz.mapper.StaffAttributeMapper;
import zzjz.service.StaffAttributeService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: StaffAttributeService
 * @Description: 人员属性信息服务接口实现类
 */
@Service
public class StaffAttributeServiceImpl implements StaffAttributeService {

    @Autowired
    StaffAttributeMapper staffAttributeMapper;

    @Override
    public Page<StaffAttribute> getStaffAttributePageList(StaffAttributeRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<StaffAttribute> pageList = (Page<StaffAttribute>) staffAttributeMapper.getStaffAttributePageList(request);
        return pageList;
    }

    @Override
    public List<StaffAttribute> getStaffAttributeList(StaffAttribute staffAttribute) {
        return staffAttributeMapper.getStaffAttributeList(staffAttribute);
    }

    @Override
    public StaffAttribute query(Integer id) {
        return staffAttributeMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean add(StaffAttribute staffAttribute) {
        return staffAttributeMapper.insertSelective(staffAttribute) > 0;
    }

    @Override
    public boolean update(StaffAttribute staffAttribute) {
        return staffAttributeMapper.updateByPrimaryKeySelective(staffAttribute) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return staffAttributeMapper.deleteByPrimaryKey(id) > 0;
    }
}
