package zzjz.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Group;
import zzjz.mapper.GroupMapper;
import zzjz.service.GroupService;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/8/30 16:53
 * @ClassName: GroupServiceImpl
 * @Description: 组service实现类
 */
@Service
public class GroupServiceImpl implements GroupService {


    @Autowired
    GroupMapper groupMapper;

    @Override
    public List<Group> queryAllGroup() {
        List<Group> groupList = groupMapper.queryAllGroup();
        return groupList;
    }

    @Override
    public boolean updateGroup(Group group) {
        //先删除原来的组和用户的关系
        deleteStaffGroup(group.getGroupId());
        //重新建立关系
        String staffIdStr = group.getStaffIdStr();
        if (StringUtils.isNotBlank(staffIdStr)) {
            for (String staffId : StringUtils.split(staffIdStr, ",")) {
                addGroupStaff(group.getGroupId(), staffId);
            }
        }
        return true;
    }

    @Override
    public boolean addGroup(Group group) {
        boolean rs = groupMapper.insertSelective(group) > 0;
        String staffIdStr = group.getStaffIdStr();
        //建立和员工的关系
        if (StringUtils.isNotBlank(staffIdStr)) {
            for (String staffId : StringUtils.split(staffIdStr, ",")) {
                addGroupStaff(group.getGroupId(), staffId);
            }
        }
        return rs;
    }

    @Override
    public Group queryByName(String groupName) {
        return groupMapper.queryByName(groupName);
    }

    @Override
    public Group queryByIdWithStaff(int groupId) {
        return groupMapper.queryByIdWithStaff(groupId);
    }

    @Override
    public boolean deleteByGroupId(int groupId) {
        return groupMapper.deleteByPrimaryKey(groupId) > 0;
    }

    @Override
    public List<Group> queryGroupByStaffId(String staffId) {
        return groupMapper.queryGroupByStaffId(staffId);
    }

    /**
     * 删除组和用户的关系
     *
     * @param groupId 组id
     * @return
     */
    boolean deleteStaffGroup(int groupId) {
        return groupMapper.deleteStaffGroup(groupId);
    }

    boolean addGroupStaff(int groupId, String staffId) {
        return groupMapper.addGroupStaff(groupId, staffId);
    }
}
