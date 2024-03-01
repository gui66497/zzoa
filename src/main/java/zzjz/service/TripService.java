package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Staff;
import zzjz.bean.Trip;
import zzjz.bean.TripDetail;
import zzjz.bean.TripRequest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日14:15
 * @ClassName: TripService
 * @Description: 出差管理服务接口
 */
@Service
public interface TripService {
    /**
     * 分页获取出差列表信息.
     *
     * @param request request
     * @return 出差列表信息
     */
    Page<Trip> getTripListPage(TripRequest request);

    /**
     * 添加并保存出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    String addTripInfo(Trip trip);

    /**
     * 修改出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    boolean updateTripInfo(Trip trip);

    /**
     * 提交出差信息.
     *
     * @param trip    出差信息
     * @param user    user
     * @param staffId staffId
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean submitTripTask(Trip trip, String user, String staffId) throws ParseException;

    /**
     * 根据主键获取出差信息.
     *
     * @param id 主键
     * @return Trip
     */
    Trip getTripInfoById(Integer id);

    /**
     * 计算并生成出差明细表.
     *
     * @param trip 出差信息
     * @return boolean
     * @throws ParseException ParseException
     */
    boolean initTripDetailInfo(Trip trip) throws ParseException;

    /**
     * 计算出差天数.
     *
     * @param trip 出差信息
     * @return 天数
     */
    Double betweenDays(Trip trip);

    /**
     * 审批流程.
     *
     * @param trip   出差信息
     * @param user   处理人
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     */
    boolean approveTripTask(Trip trip, String user, String msg, String reason);

    /**
     * 获取下一处理人列表.
     *
     * @param trip     Trip
     * @param departId departId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Trip trip, long departId, String msg);

    /**
     * 查询出差明细信息列表.
     *
     * @param trip TripDetail
     * @return List
     */
    List<TripDetail> selectTripInfo(TripDetail trip);

    /**
     * 计算出差（时间模块）信息.
     *
     * @param trip    出差信息
     * @param staffId 用户ID
     * @return 出差信息
     */
    Trip initTripInfo(Trip trip, String staffId);

    /**
     * 计算出差（时分）信息.
     *
     * @param trip    出差信息
     * @param staffId 用户ID
     * @return 出差信息
     */
    Trip initForTripInfo(Trip trip, String staffId);

    /**
     * 根据流程ID删除出差信息和流程信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 撤销出差信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 出差核销.
     *
     * @param trip Trip
     * @return boolean
     * @throws ParseException ParseException
     */
    boolean forTrip(Trip trip) throws ParseException;

    /**
     * 查询出差信息.
     *
     * @param userId    userId
     * @param leaveDate leaveDate
     * @param part      part
     * @return List
     */
    List<TripDetail> getTripList(String userId, String leaveDate, String part);

    /**
     * 审批并结束出差信息.
     *
     * @param trip Trip
     * @return boolean
     */
    boolean terminationTripTask(Trip trip);

    /**
     * 出差信息导出内容.
     *
     * @param trip Trip
     * @return List
     */
    Map<String, Object> getTripMapList(Trip trip);

    /**
     * 获取出差未核销提醒事项列表.
     *
     * @return 列表
     */
    List<Trip> getTripRemindList();

    /**
     * 人事修改员工出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     * @throws ParseException ParseException
     */
    boolean hrSave(Trip trip) throws ParseException;

    /**
     * 忽略出差提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);
}
