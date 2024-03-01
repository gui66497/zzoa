package zzjz.service;

import zzjz.bean.Group;

import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/8/30 16:52
 * @ClassName: GroupService
 * @Description: 组service
 */
public interface GroupService {

    /**
     * 获取所有组.
     *
     * @return 列表
     */
    List<Group> queryAllGroup();

    /**
     * 修改组信息.
     *
     * @param group 组
     * @return 结果
     */
    boolean updateGroup(Group group);

    /**
     * 新增组信息.
     *
     * @param group 组
     * @return 结果
     */
    boolean addGroup(Group group);

    /**
     * 根据组名查询组.
     *
     * @param groupName 组名
     * @return 组
     */
    Group queryByName(String groupName);

    /**
     * 根据组id获取组(包括其下员工).
     *
     * @param groupId 组id
     * @return 组
     */
    Group queryByIdWithStaff(int groupId);

    /**
     * 删除组.
     *
     * @param groupId 组id
     * @return 结果
     */
    boolean deleteByGroupId(int groupId);

    /**
     * 获取用户的所有分组.
     *
     * @param staffId 员工id
     * @return 组
     */
    List<Group> queryGroupByStaffId(String staffId);
}
