package zzjz.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.DepartRole;
import zzjz.bean.GroupDepart;
import zzjz.bean.GroupDepartInfo;
import zzjz.bean.GroupDepartRequest;
import zzjz.mapper.GroupDepartMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.GroupDepartService;

import java.util.List;

/**
 * @author 汪浩
 * @version 2017/5/15
 */
@Service
public class GroupDepartServiceImpl implements GroupDepartService {

    @Autowired
    GroupDepartMapper groupDepartMapper;

    @Autowired
    StaffMapper staffMapper;

    @Override
    public GroupDepartInfo getGroupListByDepartId(long departId) {
        List<GroupDepart> list = groupDepartMapper.getGroupListByDepartId(departId);
        GroupDepartInfo groupDepartInfo = new GroupDepartInfo();
        for (int i = 0; i < list.size(); i++) {
            groupDepartInfo.setDepartId(list.get(i).getDepartId());
            groupDepartInfo.getList().add((list.get(i)));
        }
        return groupDepartInfo;
    }

    @Override
    public List<DepartRole> getGroupList(String staffId) {
        List<DepartRole> groupDeparts = staffMapper.getGroupList(staffId);
        return groupDeparts;
    }

    @Override
    public boolean addGroupDepart(GroupDepartRequest groupDepartRequest) {
        List<GroupDepart> groupDeparts = groupDepartMapper.getGroupListByDepartId(groupDepartRequest.getDepartId());
        if (groupDeparts.size() > 0) {
            groupDepartMapper.deleteByDepartId(groupDepartRequest.getDepartId());
        }
        return groupDepartMapper.insert(groupDepartRequest) > 0;
    }

}
