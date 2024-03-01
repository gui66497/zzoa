package zzjz.service;

import zzjz.bean.SystemInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 李飞
 * @version 2015年5月8日 上午9:07:59
 * @ClassName: SystemInfoService
 * @Description: 这里用一句话描述这个类的作用
 */
public interface SystemInfoService {

    /**
     * 获取指定删除标记的系统信息记录数.
     *
     * @param deleteFlag deleteFlag
     * @return int
     */
    int getTotalCount(String deleteFlag);

    /**
     * 获取指定删除标记的系统信息记录数.
     *
     * @param userId        userId
     * @param authorityName authorityName
     * @return int
     */
    int getTotalCountList(String userId, String authorityName);

    /**
     * @return int
     */
    int getTotalCountAdmList();

    /**
     * @param deleteFlag deleteFlag
     * @return List
     */
    List<Long> getTotal(String deleteFlag);

    /**
     * 分页获取系统信息.
     *
     * @param deleteFlag deleteFlag
     * @param index      index
     * @param pageSize   pageSize
     * @return List
     */
    List<SystemInfo> list(String deleteFlag, int index, int pageSize);

    /**
     * 根据系统名和系统标示检查系统的唯一性.
     *
     * @param systemName systemName
     * @param systemSign systemSign
     * @return 主键ID
     */
    SystemInfo checkUnique(String systemName, String systemSign);

    /**
     * 插入系统该信息.
     *
     * @param request SystemInfo
     * @return 返回主键ID
     */
    String insert(SystemInfo request);

    /**
     * 根据主键ID获取系统信息.
     *
     * @param key        key
     * @param deleteFlag deleteFlag
     * @return SystemInfo
     */
    SystemInfo getSystemInfoById(String key, int deleteFlag);

    /**
     * 根据系统ID更新删除标记.
     *
     * @param systemId   systemId
     * @param deleteFlag deleteFlag
     */
    void delete(long systemId, String deleteFlag);

    /**
     * 根据系统ID，更新除删除标记以外的所有数据.
     *
     * @param request            SystemInfo
     * @param isUpdateDeleteFlag boolean
     * @return String
     */
    String update(SystemInfo request, boolean isUpdateDeleteFlag);

    /**
     * 根据用户，系统名，系统标识，权限名称获取系统信息.
     *
     * @param userId         userId
     * @param permissionName permissionName
     * @param deleteFlag     deleteFlag
     * @param index          index
     * @param pageSize       pageSize
     * @return List
     */
    List<SystemInfo> list(String userId, String permissionName,
                          String deleteFlag, int index, int pageSize);

    /**
     * @param userId         userId
     * @param systemName     systemName
     * @param systemSign     systemSign
     * @param permissionName permissionName
     * @param deleteFlag     deleteFlag
     * @param index          index
     * @param pageSize       pageSize
     * @return List
     */
    List<SystemInfo> list(String userId, String systemName, String systemSign,
                          String permissionName, String deleteFlag, int index, int pageSize);

    /**
     * @param userId         userId
     * @param systemName     systemName
     * @param systemSign     systemSign
     * @param permissionName permissionName
     * @param deleteFlag     deleteFlag
     * @return int
     */
    int getTotalCount(String userId, String systemName, String systemSign,
                      String permissionName, String deleteFlag);

    /**
     * @param userId         userId
     * @param permissionName permissionName
     * @param deleteFlag     deleteFlag
     * @return int
     */
    int getTotalCount(String userId, String permissionName, String deleteFlag);

    /**
     * 判断系统是否是默认系统.
     *
     * @param targetId targetId
     * @return boolean
     */
    boolean checkDefaultSystem(String targetId);

    /**
     * @param systemId systemId
     */
    void delete(long systemId);

    /**
     * @param systemId systemId
     * @return Map
     */
    Map<String, String> getColumnValues(long systemId);

    /**
     * 通过systemid获取系统信息.
     *
     * @param systemId systemId
     * @return SystemInfo
     */
    SystemInfo getSystemInfoById(long systemId);

    /**
     * 根据用户的查看权限查询相关联的系统列表.
     *
     * @param authorityName authorityName
     * @param userId        userId
     * @return List
     */
    public List<SystemInfo> getSystemListByUserId(String authorityName, String userId);

    /**
     * @param index         index
     * @param pageSize      pageSize
     * @param authorityName authorityName
     * @param userId        userId
     * @return List
     */
    public List<SystemInfo> getSystemListByUserId(int index, int pageSize, String authorityName, String userId);

    /**
     * 添加统一权限默认权限.
     *
     * @param path     文件路径
     * @param userId   用户ID
     * @param systemId 系统ID
     * @throws SQLException SQLException
     * @throws IOException  IOException
     */
    public void addDefaultAuthority(String path, String userId, String systemId) throws SQLException, IOException;

    /**
     * 通过systemSign获取系统信息.
     *
     * @param systemSign systemSign
     * @return SystemInfo
     */
    SystemInfo getSystemInfoBySign(String systemSign);
}
