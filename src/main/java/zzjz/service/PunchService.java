package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Punch;
import zzjz.bean.PunchRequest;
import zzjz.bean.Staff;

import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年05月10日9:48
 * @ClassName: PunchService
 * @Description: 忘打卡管理服务接口
 */
@Service
public interface PunchService {
    /**
     * 分页获取忘打卡信息列表.
     *
     * @param punchRequest PunchRequest
     * @return Page
     */
    Page<Punch> getPunchPageList(PunchRequest punchRequest);

    /**
     * 添加忘打卡信息.
     *
     * @param punch 忘打卡信息
     * @return 结果
     */
    String addPunchInfo(Punch punch);

    /**
     * 修稿忘打卡信息.
     *
     * @param punch 忘打卡信息
     * @return 结果
     */
    boolean updatePunchInfo(Punch punch);

    /**
     * 提交流程.
     *
     * @param punch 忘打卡信息
     * @param user  处理人
     * @return 结果
     */
    boolean submitPunchTask(Punch punch, String user);

    /**
     * 审批流程.
     *
     * @param punch  忘打卡信息
     * @param user   处理人
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     */
    boolean approvePunchTask(Punch punch, String user, String msg, String reason);

    /**
     * 根据ID查询忘打卡信息.
     *
     * @param id id
     * @return Punch
     */
    Punch getPunchInfoById(Integer id);

    /**
     * 获取下一处理人列表.
     *
     * @param punch    Punch
     * @param departId departId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Punch punch, long departId, String msg);

    /**
     * 查询忘打卡信息.
     *
     * @param punch Punch
     * @return List
     */
    List<Punch> selectPunchInfo(Punch punch);

    /**
     * 根据流程ID删除忘打卡信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 根据流程ID撤销忘打卡信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 审批并结束忘打卡信息.
     *
     * @param punch Punch
     * @return boolean
     */
    boolean terminationPunchTask(Punch punch);

    /**
     * 告知忘打卡信息.
     *
     * @param punch Punch
     * @param user  user
     * @return boolean
     */
    boolean noticePunchTask(Punch punch, String user);

    /**
     * 忘打卡信息导出内容.
     * @param punch 打卡信息
     * @return 导出内容
     */
    Map<String, Object> getPunchMapList(Punch punch);

    /**
     * 忽略忘打卡提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);
}
