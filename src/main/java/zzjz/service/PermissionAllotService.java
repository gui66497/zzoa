package zzjz.service;

import zzjz.bean.PermissionAllotInfo;
import zzjz.bean.User;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年05月09日16:21
 */
public interface PermissionAllotService {

    /**
     * 获取总记录数.
     *
     * @param deleteFlag deleteFlag
     * @return int
     */
    int getTotalCount(String deleteFlag);

    /**
     * 分页获取用户角色.
     *
     * @param deleteFlag deleteFlag
     * @param index      index
     * @param pageSize   pageSize
     * @return List
     */
    List<PermissionAllotInfo> list(String deleteFlag, int index, int pageSize);

    /**
     * 新增用户角色.
     *
     * @param userId  userId
     * @param roleId  roleId
     * @param creator creator
     * @return String
     */
    String insert(String userId, long roleId, String creator);

    /**
     * 根据主键获取用户角色.
     *
     * @param key        key
     * @param deleteFlag deleteFlag
     * @return PermissionAllotInfo
     */
    PermissionAllotInfo getPermissionAllotInfoById(String key, int deleteFlag);

    /**
     * 根据用户角色ID更新删除标记.
     *
     * @param userRoleId userRoleId
     * @param deleteFlag deleteFlag
     */
    void delete(long userRoleId, String deleteFlag);

    /**
     * 根据用户角色ID，更新除删除标记以外的所有数据.
     *
     * @param userRoleId userRoleId
     * @param userId     userId
     * @param roleId     roleId
     * @param creator    creator
     * @return String
     */
    String update(String userRoleId, String userId, long roleId, String creator);

    /**
     * 根据角色ID获取角色对应的用户.
     *
     * @param roleId roleId
     * @return List
     */
    List<User> getUserByRoleId(long roleId);

    /**
     * 根据系统ID获取系统下角色的总数.
     *
     * @param systemId   systemId
     * @param deleteFlag deleteFlag
     * @return int
     */
    int getTotalCountBySystemId(long systemId, String deleteFlag);

    /**
     * 根据系统ID获取角色信息.
     *
     * @param systemId   systemId
     * @param deleteFlag deleteFlag
     * @param index      index
     * @param pageSize   pageSize
     * @return List
     */
    List<PermissionAllotInfo> listBySystemId(long systemId, String deleteFlag,
                                             int index, int pageSize);

    /**
     * 从用户角色表中中删除与角色关联的用户记录.
     *
     * @param roleId roleId
     */
    void delete(long roleId);

}
