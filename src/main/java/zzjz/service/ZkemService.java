package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.PunchedCard;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * 打卡机调用service.
 *
 * @author fgt
 * @version 2017/7/5 16:43
 */
@Service
public interface ZkemService {

    /**
     * 获取今日最新打卡数据.
     *
     * @return 打卡记录
     * @throws ConnectException ConnectionException
     */
    List<PunchedCard> getLogsToday() throws ConnectException;

    /**
     * 获取指定日期之后的打卡数据.
     *
     * @param date 日期 (年月日 时分秒)
     * @return 打卡记录
     * @throws ConnectException ConnectException
     */
    List<PunchedCard> getLogAfter(Date date);

    /**
     * 获取指定日期的打卡数据.
     *
     * @param date 日期 (年月日 时分秒)
     * @return 打卡记录
     * @throws ConnectException ConnectException
     */
    List<PunchedCard> getOneDayLog(Date date) throws ConnectException;

    /**
     * 获取所有用户.
     *
     * @return List
     * @throws ConnectException ConnectionException
     */
    //List<ZkUserInfo> getAllUserInfo() throws ConnectException;

    /**
     * 根据userId获取用户信息.
     *
     * @param userId userId
     * @return 用户信息
     * @throws ConnectException ConnectionException
     */
    //ZkUserInfo getZkUserInfoByUid(String userId) throws ConnectException;

    /**
     * 获取本地mysql中的用户打卡机数据.
     *
     * @return 用户数据
     */
    //List<ZkUserInfo> getAllUserInfoLocal();

    /**
     * 通过姓名获取其考勤id.
     * @param name 姓名
     * @return id
     */
    String getUserIdByName(String name);

    /**
     * 连接.
     *
     * @return String
     */
    //String onConnect();

}
