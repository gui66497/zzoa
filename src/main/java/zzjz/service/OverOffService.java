package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.OverOff;

import java.text.ParseException;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:17
 * @ClassName: ActivitiService
 * @Description: 工作流服务接口
 */
@Service
public interface OverOffService {
    /**
     * 初始化员工加班调休情况.
     *
     * @param userId    userId
     * @param monthDate monthDate
     * @return boolean
     * @throws ParseException ParseException
     */
    boolean initOverOffInfo(String userId, String monthDate) throws ParseException;

    /**
     * 初始化全部员工加班调休情况.
     *
     * @param monthDate monthDate
     * @return boolean
     */
    boolean batchInitOverOffInfo(String monthDate);

    /**
     * 更新加班调休信息.
     *
     * @param overOff OverOff
     * @return boolean
     */
    boolean updateOverOffInfo(OverOff overOff);

    /**
     * 查询加班调休信息.
     *
     * @param userId    userId
     * @param monthDate monthDate
     * @return OverOff
     */
    OverOff queryOverOffInfo(String userId, String monthDate);

    /**
     * 清除加班结余.
     *
     * @param userId    userId
     * @param monthDate monthDate
     * @return boolean
     */
    boolean clearLastLeftDay(String userId, String monthDate);

    /**
     * 计算已使用调休天数并更新.
     *
     * @param overOff    OverOff
     * @param deleteDays deleteDays
     * @param leaveDays  leaveDays
     * @return boolean
     */
    boolean updateOverOffWithUseInfo(OverOff overOff, Double deleteDays, Double leaveDays);

    /**
     * 查询加班申请之后月份的加班调休列表.
     *
     * @param userId    userId
     * @param monthDate monthDate
     * @return List
     */
    List<OverOff> queryOverOffList(String userId, String monthDate);
}
