package zzjz.service;

import zzjz.bean.PermissionInfo;
import zzjz.bean.RoleInfo;

import java.util.List;
import java.util.Map;


/** 
* @ClassName: RoleService 
* @Description: 角色服务类
* @author 曹雪东
* @date 2015年5月9日 下午3:27:57
*/
public interface RoleService {

	int getTotalCount(String deleteFlag);

	int getTotalCountBySystemId(long systemId, String deleteFlag);

	List<RoleInfo> list(String deleteFlag, int index, int pageSize);

	List<RoleInfo> listBySystemId(long systemId, String deleteFlag, int index,
								  int pageSize);

	RoleInfo checkUnique(String roleName, String roleSign, String systemId);

	String insert(RoleInfo request);

	RoleInfo getRoleInfoById(String roleId, int deleteFlag);

	void delete(long roleId, String deleteFlag);

	void deleteRoleAuthority(long roleId);
	
	void deleteRoleAuthoritybyTargetId(String roleId, String systemId);

	String update(RoleInfo request);

	String insertRoleAuthority(long roleId, PermissionInfo permissionInfo);

	Map<String, String> getColumnValues(long roleId);

	void delete(long roleId);

	/**获取角色拥有的权限目标
	 * @param roleId
	 * @param permissionId
	 * @return
	 */
	List<Map<String, String>> queryTargetList(String roleId, String permissionId);

	/**
	 * 通过userId获取用户所有角色
	 * @param userId
	 * @author 房桂堂
	 * @return
	 */
	List<RoleInfo> getUserAllRoles(long userId);

	/**
	 * 根据权限名称，获取角色列表总数
	 * @param authorityName	查看权限名称
	 * @param userId		用户Id
	 * @return
	 */
	public int getTotalCount(String authorityName, String userId);

	/**
	 * 根据权限名称，获取角色列表
	 * @param authorityName	查看权限名称
	 * @param userId		用户Id
	 * @param index			开始位置
	 * @param pageSize		每页大小
	 * @return
	 */
	public List<RoleInfo> getRoleList(String authorityName, String userId, int index, int pageSize);

	/**
	 * 根据用户ID,系统ID,数据源,权限ID获取外部数据目标Id列表
	 * @param userId		用户ID
	 * @param systemId 		系统ID
	 * @param dataSource	数据源
	 * @param authorityId	权限ID
	 * @return	外部数据目标Id列表
	 */
	public List<Long> getTargetIdList(String userId, long systemId, String dataSource, String authorityId);

	/**
	 * 根据系统id和用户id获取指定用户已分配的所有角色
	 * @param systemId
	 * @param userId
	 * @return
	 */
	List<RoleInfo> getRoleListByUserIdAndSysId(long systemId, String userId, int index, int pageSize);

	/**
	 *
	 * @param systemId
	 * @param userId
	 * @return
	 */
	int getTotalCountByUserIdAndSysId(long systemId, String userId);

	/**
	 * 根据权限Id和系统Id判断权限是否存在
	 * @param roleId	权限Id
	 * @param systemId		系统Id
	 * @return 判断权限是否存在
	 */
	boolean hasRoleInSystem(long roleId, long systemId);

	/**
	 * 根据id判断角色权限关系是否存在
	 * @param roleId		角色ID
	 * @param authorityId	权限ID
	 * @param targetId		目标ID
	 * @return
	 */
	boolean hasRoleAuthority(long roleId, long authorityId, long targetId);

	/**
	 * 根据id判断角色权限关系是否存在
	 * @param id 角色权限ID
	 * @return
	 */
	boolean hasRoleAuthority(long id);
}
