package zzjz.service;

import zzjz.bean.PagingEntity;
import zzjz.bean.Permission;
import zzjz.bean.PermissionInfo;

import java.util.List;
import java.util.Map;

/** 
* @ClassName: PermissionService 
* @Description: 权限接口服务类
* @author 曹雪东
* @date 2015年5月9日 上午10:32:25
*/
public interface PermissionService {

	/** 
	* @Title: insertPermission
	* @Description: 添加权限操作
	* @param perInfo
	* @throws 
	*/
	public long insertPermission(PermissionInfo perInfo);
	/** 
	* @Title: deletePermission
	* @Description: 删除权限操作
	* @param id
	* @param time
	* @param userId
	* @throws 
	*/
	public int deletePermission(long id, String userId);
	/** 
	* @Title: updatePenmission
	* @Description: 更新权限
	* @param perInfo
	* @throws 
	*/
	public long updatePenmission(PermissionInfo perInfo);
	/** 
	* @Title: queryList
	* @Description: 获取权限list
	* @return
	* @throws 
	*/
	/** 
	* @Title: queryList
	* @Description: 获取List 
	* @param index
	* @param size
	* @return
	* @throws 
	*/
	public List<PermissionInfo> queryList(int index, int size);
	/**
	 * @param systemId  
	* @Title: checkUnique
	* @Description: 判断数据是否已存在
	* @param name
	* @param sign
	* @return
	* @throws 
	*/
	public PermissionInfo checkUnique(String name, String sign, String systemId);
	
	/** 
	* @Title: getTotalCount
	* @Description: 获取从条数
	* @param deleteFlag
	* @return
	* @throws 
	*/
	public int getTotalCount(String deleteFlag);
	
	/** 
	* @Title: queryList
	* @Description: 根据系统ID获取权限信息
	* @param index
	* @param size
	* @param sysId
	* @return
	* @throws 
	*/
	public List<PermissionInfo> queryList(int index, int size, long sysId);
	
	/** 
	* @Title: queryById
	* @Description: 根据ID获取权限 
	* @param id
	* @return
	* @throws 
	*/
	public PermissionInfo queryById(long id, int deleteFlag);
	
	/** 
	* @Title: getTotalCountBySystemId
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param systemId
	* @param deleteFlag
	* @return
	* @throws 
	*/
	public int getTotalCountBySystemId(long systemId, String deleteFlag);
	
	
	/** 
	* @Title: getPermissionInfoListByTarget********
	* @Description: 获取根据用户ID、目标ID获取该系统下的所有权限
	* @param userId 	用户ID
	* @param targetId   目标ID
	* @param systemName 系统名称
	* @param systemSign 系统标识
	*/
	public List<PermissionInfo> getPermissionInfoListByTarget(String userId, String targetId, String systemName, String systemSign);
	
	/** 
	* @Title: getCategoryList*******
	* @Description: 获取该用户拥有该系统查看权限的所有对象
	* @param userId			用户ID
	* @param permissionName	权限名称
	* @param systemName 	系统名称
	* @param systemSign 	系统标识
	*/
	public List<Object> getCategoryList(String userId, String permissionName, String systemName, String systemSign);
	
	/**根据用户，系统名，系统标识，权限名称获取目标ID
	 * @param userId
	 * @param systemName
	 * @param systemSign
	 * @param permissionName
	 * @return
	 */
	public List<String> getTargetIdByPermissionName(String userId,
													String systemName, String systemSign, String permissionName);


	/**根据用户，系统名，系统标识，权限名称获取目标ID
	 * @param userId
	 * @param systemName
	 * @param systemSign
	 * @param permissionName
	 * @return
	 */
	public List<String> getTargetIdByPermissionName(String userId, String permissionName);

	/** 
	* @Title: getPermissionInfoList********
	* @Description: 获取根据用户ID获取该系统下的用户所有权限
	* @param userId 	用户ID
	* @param systemName 系统名称
	* @param systemSign 系统标识
	*/
	public List<PermissionInfo> getPermissionInfoList(String userId, String systemName, String systemSign);

	/**根据系统ID判断用户是否具有指定权限
	 * @param userId
	 * @param systemId
	 * @param permissionName
	 * @return
	 */
	public boolean checkPermission(String userId, String systemId,
								   String permissionName);
	
	/**返回框架权限对象的标识（该接口专为框架系统提供）
	 * @param userId
	 * @param permissionName
	 * @param systemName
	 * @param systemSign
	 * @return
	 */
	public List<String> getSystemSignList(String userId, String permissionName,
										  String systemName, String systemSign);
	
	public Map<String, String> getColumnValues(long id);
	
	public void delete(long id);

	/** 根据角色ID获取角色权限的总记录数
	 * @param roleId
	 * @param deleteFlag
	 * @return
	 */
	public int getTotalCountByRoleId(long roleId, String deleteFlag);
	
	/** 根据角色ID获取权限
	 * @param roleId
	 * @return
	 */
	public List<Map<String,String>> getPermissionInfoByRoleId(long roleId, String deleteFlag, String relation_flag);
	/** 根据角色ID获取权限
	 * @param roleId
	 * @return
	 */
	public List<Map<String,String>> getPermissionInfoByRoleId1(long roleId, String deleteFlag, String relation_flag);

	/** 根据角色ID获取权限
	 * @param roleId
	 * @return
	 */
	public List<PermissionInfo> getPermissionInfoListByRoleId(long roleId, PagingEntity pagingEntity);

	/** 根据角色ID获取权限的总数
	 * @param roleId
	 * @return
	 */
	public int getPermissionInfoListCountByRoleId(long roleId);
	/** 根据用户ID获取权限
	 * @return
	 */
	public List<String> getAuthorityName(String userId, Long systemId, String deleteFlag);

	/**
	 *
	 * @param systemId
	 * @param userId
	 * @return
	 */
	List<Permission> getPermissionsByUserIdAndSysId(long systemId, String userId);

	/**
	 * 根据权限Id和系统Id判断权限是否存在
	 * @param autorityId	权限Id
	 * @param systemId		系统Id
	 * @return 判断权限是否存在
	 */
	boolean hasPermissionInSystem(long autorityId, long systemId);

	/**
	 * 获取权限ID
	 * @param sign:		权限标识
	 * @param systemId:	系统ID
	 */
	public long getPermissionId(String sign, long systemId);
}
