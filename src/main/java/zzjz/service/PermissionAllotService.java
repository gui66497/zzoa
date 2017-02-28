package zzjz.service;

import zzjz.bean.PermissionAllotInfo;
import zzjz.bean.User;

import java.util.List;

public interface PermissionAllotService {

	/** 
	* @Title: getTotalCount
	* @Description: 获取总记录数
	* @param deleteFlag
	* @return
	* @throws 
	*/
	int getTotalCount(String deleteFlag);

	/** 
	* @Title: list
	* @Description: 分页获取用户角色
	* @param deleteFlag
	* @param index
	* @param pageSize
	* @return
	* @throws 
	*/
	List<PermissionAllotInfo> list(String deleteFlag, int index, int pageSize);

	/** 
	* @Title: insert
	* @Description: 新增用户角色 
	* @param request
	* @return
	* @throws 
	*/
	String insert(String userId, long roleId, String creator);

	/** 
	* @Title: getPermissionAllotInfoById
	* @Description: 根据主键获取用户角色 
	* @param key
	* @return
	* @throws 
	*/
	PermissionAllotInfo getPermissionAllotInfoById(String key, int deleteFlag);

	/** 
	* @Title: delete
	* @Description: 根据用户角色ID更新删除标记
	* @param userRoleId
	* @param deleteFlag
	* @throws 
	*/
	void delete(long userRoleId, String deleteFlag);

	/** 
	* @Title: update
	* @Description: 根据用户角色ID，更新除删除标记以外的所有数据 
	* @param request
	* @param 
	* @return
	* @throws 
	*/
	String update(String userRoleId, String userId, long roleId, String creator);

	/** 
	* @Title: getUserByRoleId
	* @Description: 根据角色ID获取角色对应的用户
	* @param roleId
	* @return
	* @throws 
	*/
	List<User> getUserByRoleId(long roleId);

	/** 
	* @Title: getTotalCountBySystemId
	* @Description: 根据系统ID获取系统下角色的总数
	* @param systemId
	* @param deleteFlag
	* @return
	* @throws 
	*/
	int getTotalCountBySystemId(long systemId, String deleteFlag);

	/** 
	* @Title: listBySystemId
	* @Description: 根据系统ID获取角色信息
	* @param systemId
	* @param deleteFlag
	* @param index
	* @param pageSize
	* @return
	* @throws 
	*/
	List<PermissionAllotInfo> listBySystemId(long systemId, String deleteFlag,
											 int index, int pageSize);

	/** 
	* @Title: delete
	* @Description: 从用户角色表中中删除与角色关联的用户记录
	* @param roleId
	* @throws 
	*/
	void delete(long roleId);

}
