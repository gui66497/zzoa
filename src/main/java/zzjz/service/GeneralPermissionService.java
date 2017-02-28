package zzjz.service;

import zzjz.bean.PermissionInfo;

import java.util.List;

/**
 * @ClassName: GeneralPermissionService
 * @Description: 统一权限接口
 * @author 顾征根
 * @date 2015年5月20日 下午14:22:04
 */

/**
* @ClassName: GeneralPermissionService 
* @Description:  统一权限接口
* @author 顾征根
* @date 2015年5月20日 下午14:30:04
*/
public interface GeneralPermissionService {
	/** 
	* @Title: getPermissionInfoList
	* @Description: 获取根据用户ID、类别ID获取该系统下的该类别的所有权限
	* @param userId 	用户ID
	* @param categoryId 分类ID
	* @param systemName 系统名称
	* @param systemSign 系统标识
	*/
	public List<PermissionInfo> getPermissionInfoList(String userId, String systemId);
	
	/** 
	* @Title: checkPermission
	* @Description: 获取改用户拥有该系统查看权限的所有类别
	* @param userId			用户ID
	* @param permissionName	权限名称
	* @param systemName 	系统名称
	* @param systemSign 	系统标识
	*/
	public boolean checkPermission(String userId, String permissionName, String systemId);

	
}
