package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.User;

import java.util.List;

/** 
* @ClassName: UserService 
* @Description: 用户服务接口
* @author 梅宏振
* @date 2015年3月2日 上午9:10:41
*/
@Service
public interface UserService {

	User getUserById2(String userId);

	List<User> getUserList2();

	List<User> getUserInfoById(List<Integer> userIds);
	
	boolean deleteUser(long userId);
	
	/**
	* @Title: getUserInfoById
	* @Description: 根据用户ID获取用户信息 
	* @param userId
	* @return
	* @throws 
	*/
	List<User> getUserInfoById(String userId);

	/**
	 * 通过系统id获取用户详细信息
	 * @author 房桂堂
	 * @param systemId
	 * @return
	 */
	List<User> getUserDetailBySystemId(String systemId);

	/** 
	* @Title: getRecordIdList
	* @Description: 获取当前用户拥有的或者编辑的词条列表  
	* @param userId	用户ID
	* @param title	词条标题  
	* @param statisticsType	统计类型
	* @param paging	分页实体类
	* @return 当前用户拥有的或者编辑的词条列表  
	* @author 顾征根
	* @date 2015年3月2日 下午2:56:06
	*/
	List<Long> getRecordIdList(String userId, String title, int statisticsType, PagingEntity paging);

	/** 
	* @Title: getCountByUserId
	* @Description: 获取当前用户拥有的或者编辑的词条记录数 
	* @param userId	用户ID
	* @return 当前用户拥有的或者编辑的词条记录数 	
	* @author 顾征根
	* @date 2015年3月2日 下午2:56:51
	*/
	long getCountByUserId(String userId, String title);
	
	/** 
	* @Title: getTotalCount
	* @Description: 获取用户表中没有删除的用户总数
	* @return
	* @throws 
	*/
	int getTotalCount(String adminUserId);

	/** 
	* @Title: getRecordIdList
	* @Description: 获取用户创建和编辑的词条ID （按词条的 有用度倒序）
	* @param authorId
	* @param statisticsType
	* @param index
	* @param pageSize
	* @return
	* @throws 
	*/
	List<Long> getRecordIdList(String authorId, int statisticsType,
									  int index, int pageSize);

	/** 
	* @Title: getCreateAndEditTotalCount
	* @Description: 获取用户创建和编辑的词条总数
	* @param authorId
	* @return
	* @throws 
	*/
	int getCreateAndEditTotalCount(String authorId);

	/**
	 * 用户登陆接口
	 * @param userName
	 * @param password
	 * @return
	 */
	boolean login(String userName, String password);

	/**
	 * 通过name获取user信息
	 * @author 房桂堂
	 * @param userName
	 * @return
	 */
	User getUserByName(String userName);

	/**
	 * 修改用户信息
	 * @author 房桂堂
	 * @param userEntity
	 * @return
	 */
	boolean update(User userEntity);

	/**
	 * 为用户分配角色
	 * @param systemId 系统id
	 * @param userId 用户id
	 * @param roleId 多个角色id，以逗号相隔
	 * @param cUserId 创建者id
	 * @return
	 */
	boolean allocateRole(String systemId, String userId, String roleId, String cUserId);

	User getUserByName2(String userName);

	boolean addUser2(User user);

	Page<User> getUserListPage(PagingEntity pagingEntity);

	Page<User> getUserListPageWithName(String userName, PagingEntity pagingEntity);
}
