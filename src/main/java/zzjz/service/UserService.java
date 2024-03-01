package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.User;

import java.util.List;

/**
 * @author 梅宏振
 * @version 2015年3月2日 上午9:10:41
 * @ClassName: UserService
 * @Description: 用户服务接口
 */
@Service
public interface UserService {

    /**
     * @param userId userId
     * @return User
     */
    User getUserById2(String userId);

    /**
     * @return List
     */
    List<User> getUserList2();

    /**
     * @param userIds List
     * @return List
     */
    List<User> getUserInfoById(List<Integer> userIds);

    /**
     * @param userId userId
     * @return boolean
     */
    boolean deleteUser(long userId);

    /**
     * 根据用户ID获取用户信息.
     *
     * @param userId userId
     * @return List
     */
    List<User> getUserInfoById(String userId);

    /**
     * 通过系统id获取用户详细信息.
     *
     * @param systemId systemId
     * @return List
     */
    List<User> getUserDetailBySystemId(String systemId);

    /**
     * 获取当前用户拥有的或者编辑的词条列表.
     *
     * @param userId         用户ID
     * @param title          词条标题
     * @param statisticsType 统计类型
     * @param paging         分页实体类
     * @return List
     */
    List<Long> getRecordIdList(String userId, String title, int statisticsType, PagingEntity paging);

    /**
     * 获取当前用户拥有的或者编辑的词条记录数.
     *
     * @param userId 用户ID
     * @param title  title
     * @return 当前用户拥有的或者编辑的词条记录数
     */
    long getCountByUserId(String userId, String title);

    /**
     * 获取用户表中没有删除的用户总数.
     *
     * @param adminUserId adminUserId
     * @return int
     */
    int getTotalCount(String adminUserId);

    /**
     * 获取用户创建和编辑的词条ID （按词条的 有用度倒序）.
     *
     * @param authorId       authorId
     * @param statisticsType statisticsType
     * @param index          index
     * @param pageSize       pageSize
     * @return List
     */
    List<Long> getRecordIdList(String authorId, int statisticsType,
                               int index, int pageSize);

    /**
     * 获取用户创建和编辑的词条总数.
     *
     * @param authorId authorId
     * @return int
     */
    int getCreateAndEditTotalCount(String authorId);

    /**
     * 用户登陆接口.
     *
     * @param userName userName
     * @param password password
     * @return boolean
     */
    boolean login(String userName, String password);

    /**
     * 修改用户信息.
     *
     * @param userEntity userEntity
     * @return boolean
     */
    boolean update(User userEntity);

    /**
     * 为用户分配角色.
     *
     * @param systemId 系统id
     * @param userId   用户id
     * @param roleId   多个角色id，以逗号相隔
     * @param cUserId  创建者id
     * @return boolean
     */
    boolean allocateRole(String systemId, String userId, String roleId, String cUserId);

    /**
     * 通过name获取user信息.
     *
     * @param userName userName
     * @return User
     */
    User getUserByName(String userName);

    /**
     * @param user User
     * @return boolean
     */
    boolean addUser2(User user);

    /**
     * @param pagingEntity PagingEntity
     * @return Page
     */
    Page<User> getUserListPage(PagingEntity pagingEntity);

    /**
     * @param userName     userName
     * @param pagingEntity PagingEntity
     * @return Page
     */
    Page<User> getUserListPageWithName(String userName, PagingEntity pagingEntity);
}
