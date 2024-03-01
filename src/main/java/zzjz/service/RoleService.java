package zzjz.service;

import zzjz.bean.PermissionInfo;
import zzjz.bean.RoleInfo;

import java.util.List;
import java.util.Map;


/**
 * @author 曹雪东
 * @version 2015年5月9日 下午3:27:57
 * @ClassName: RoleService
 * @Description: 角色服务类
 */
public interface RoleService {

    /**
     * @param deleteFlag deleteFlag
     * @return int
     */
    int getTotalCount(String deleteFlag);

    /**
     * @param systemId   systemId
     * @param deleteFlag deleteFlag
     * @return int
     */
    int getTotalCountBySystemId(long systemId, String deleteFlag);

    /**
     * @param deleteFlag deleteFlag
     * @param index      index
     * @param pageSize   pageSize
     * @return List
     */
    List<RoleInfo> list(String deleteFlag, int index, int pageSize);

    /**
     * @param systemId   systemId
     * @param deleteFlag deleteFlag
     * @param index      index
     * @param pageSize   pageSize
     * @return List
     */
    List<RoleInfo> listBySystemId(long systemId, String deleteFlag, int index,
                                  int pageSize);

    /**
     * @param roleName roleName
     * @param roleSign roleSign
     * @param systemId systemId
     * @return RoleInfo
     */
    RoleInfo checkUnique(String roleName, String roleSign, String systemId);

    /**
     * @param request RoleInfo
     * @return String
     */
    String insert(RoleInfo request);

    /**
     * @param roleId     roleId
     * @param deleteFlag deleteFlag
     * @return RoleInfo
     */
    RoleInfo getRoleInfoById(String roleId, int deleteFlag);

    /**
     * @param roleId     roleId
     * @param deleteFlag deleteFlag
     */
    void delete(long roleId, String deleteFlag);

    /**
     * @param roleId roleId
     */
    void deleteRoleAuthority(long roleId);

    /**
     * @param roleId   roleId
     * @param systemId systemId
     */
    void deleteRoleAuthoritybyTargetId(String roleId, String systemId);

    /**
     * @param request RoleInfo
     * @return String
     */
    String update(RoleInfo request);

    /**
     * @param roleId         roleId
     * @param permissionInfo permissionInfo
     * @return String
     */
    String insertRoleAuthority(long roleId, PermissionInfo permissionInfo);

    /**
     * @param roleId roleId
     * @return Map
     */
    Map<String, String> getColumnValues(long roleId);

    /**
     * @param roleId roleId
     */
    void delete(long roleId);

    /**
     * 获取角色拥有的权限目标.
     *
     * @param roleId       roleId
     * @param permissionId permissionId
     * @return List
     */
    List<Map<String, String>> queryTargetList(String roleId, String permissionId);

    /**
     * 通过userId获取用户所有角色.
     *
     * @param userId userId
     * @return List
     */
    List<RoleInfo> getUserAllRoles(long userId);

    /**
     * 根据权限名称，获取角色列表总数.
     *
     * @param authorityName 查看权限名称
     * @param userId        用户Id
     * @return int
     */
    public int getTotalCount(String authorityName, String userId);

    /**
     * 根据权限名称，获取角色列表.
     *
     * @param authorityName 查看权限名称
     * @param userId        用户Id
     * @param index         开始位置
     * @param pageSize      每页大小
     * @return List
     */
    public List<RoleInfo> getRoleList(String authorityName, String userId, int index, int pageSize);

    /**
     * 根据用户ID,系统ID,数据源,权限ID获取外部数据目标Id列表.
     *
     * @param userId      用户ID
     * @param systemId    系统ID
     * @param dataSource  数据源
     * @param authorityId 权限ID
     * @return 外部数据目标Id列表
     */
    public List<Long> getTargetIdList(String userId, long systemId, String dataSource, String authorityId);

    /**
     * 根据系统id和用户id获取指定用户已分配的所有角色.
     *
     * @param systemId systemId
     * @param userId   userId
     * @param index    index
     * @param pageSize pageSize
     * @return List
     */
    List<RoleInfo> getRoleListByUserIdAndSysId(long systemId, String userId, int index, int pageSize);

    /**
     * @param systemId systemId
     * @param userId   userId
     * @return int
     */
    int getTotalCountByUserIdAndSysId(long systemId, String userId);

    /**
     * 根据权限Id和系统Id判断权限是否存在.
     *
     * @param roleId   权限Id
     * @param systemId 系统Id
     * @return 判断权限是否存在
     */
    boolean hasRoleInSystem(long roleId, long systemId);

    /**
     * 根据id判断角色权限关系是否存在.
     *
     * @param roleId      角色ID
     * @param authorityId 权限ID
     * @param targetId    目标ID
     * @return boolean
     */
    boolean hasRoleAuthority(long roleId, long authorityId, long targetId);

    /**
     * 根据id判断角色权限关系是否存在.
     *
     * @param id 角色权限ID
     * @return boolean
     */
    boolean hasRoleAuthority(long id);
}
