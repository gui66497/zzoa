package zzjz.service;

import zzjz.bean.PermissionInfo;

import java.util.List;

/**
 * @author 顾征根
 * @version 2015年5月20日 下午14:22:04
 * @ClassName: GeneralPermissionService
 * @Description: 统一权限接口
 */
public interface GeneralPermissionService {
    /**
     * @param userId   用户ID
     * @param systemId 系统ID
     * @return List
     * @Title: getPermissionInfoList
     * @Description: 获取根据用户ID、类别ID获取该系统下的该类别的所有权限
     */
    public List<PermissionInfo> getPermissionInfoList(String userId, String systemId);

    /**
     * @param userId         用户ID
     * @param permissionName 权限名称
     * @param systemId       系统ID
     * @return boolean
     * @Title: checkPermission
     * @Description: 获取改用户拥有该系统查看权限的所有类别
     */
    public boolean checkPermission(String userId, String permissionName, String systemId);


}
