package zzjz.service;

import zzjz.bean.SystemInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/** 
* @ClassName: SystemInfoService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 李飞
* @date 2015年5月8日 上午9:07:59
*/
public interface SystemInfoService {

	/** 
	* @Title: getTotalCount
	* @Description: 获取指定删除标记的系统信息记录数 
	* @param deleteFlag
	* @return
	* @throws 
	*/
	int getTotalCount(String deleteFlag);
	/** 
	* @Title: getTotalCount
	* @Description: 获取指定删除标记的系统信息记录数 
	* @param userId
	* @param authorityName
	* @return
	* @throws 
	*/
	int getTotalCountList(String userId, String authorityName);
	int  getTotalCountAdmList();
	List<Long>  getTotal(String deleteFlag);
	/** 
	* @Title: list
	* @Description: 分页获取系统信息 
	* @param deleteFlag
	* @param index
	* @param pageSize
	* @return
	* @throws 
	*/
	List<SystemInfo> list(String deleteFlag, int index, int pageSize);

	/** 
	* @Title: checkUnique
	* @Description: 根据系统名和系统标示检查系统的唯一性 
	* @param systemName
	* @param systemSign
	* @return 主键ID
	* @throws 
	*/
	SystemInfo checkUnique(String systemName, String systemSign);

	/** 
	* @Title: insert
	* @Description: 插入系统该信息 
	* @param request
	* @return 返回主键ID
	* @throws 
	*/
	String insert(SystemInfo request);

	/** 
	* @Title: getSystemInfoById
	* @Description: 根据主键ID获取系统信息
	* @param key
	* @return
	* @throws 
	*/
	SystemInfo getSystemInfoById(String key, int deleteFlag);

	/** 
	* @Title: delete
	* @Description:  根据系统ID更新删除标记
	* @param systemId
	* @param deleteFlag
	* @throws 
	*/
	void delete(long systemId, String deleteFlag);

	/** 
	* @Title: update
	* @Description: 根据系统ID，更新除删除标记以外的所有数据 
	* @param request
	* @return
	* @throws 
	*/
	String update(SystemInfo request, boolean isUpdateDeleteFlag);

	/**根据用户，系统名，系统标识，权限名称获取系统信息
	 * @param systemList
	 * @param deleteFlag
	 * @param index
	 * @param pageSize
	 * @return
	 */
	List<SystemInfo> list(String userId, String permissionName,
						  String deleteFlag, int index, int pageSize);

	List<SystemInfo> list(String userId, String systemName, String systemSign,
						  String permissionName, String deleteFlag, int index, int pageSize);

	int getTotalCount(String userId, String systemName, String systemSign,
					  String permissionName, String deleteFlag);

	int getTotalCount(String userId, String permissionName, String deleteFlag);

	/** 
	* @Title: checkDefaultSystem
	* @Description: 判断系统是否是默认系统
	* @param systemId
	* @return
	* @throws 
	*/
	boolean checkDefaultSystem(String targetId);

	void delete(long systemId);

	Map<String, String> getColumnValues(long systemId);

	/**
	 * 通过systemid获取系统信息
	 * @param systemId
	 * @return
	 */
	SystemInfo getSystemInfoById(long systemId);

	/**
	 * 根据用户的查看权限查询相关联的系统列表
	 * @return
	 */
	public List<SystemInfo> getSystemListByUserId(String authorityName, String userId);
	public List<SystemInfo> getSystemListByUserId(int index, int pageSize, String authorityName, String userId);

	/**
	 * 添加统一权限默认权限
	 * @param path		文件路径
	 * @param userId	用户ID
	 * @param systemId	系统ID
	 */
	public void addDefaultAuthority(String path, String userId, String systemId) throws SQLException, IOException;

	/**
	 * 通过systemSign获取系统信息
	 * @param systemSign
	 * @return
	 */
	SystemInfo getSystemInfoBySign(String systemSign);
}
