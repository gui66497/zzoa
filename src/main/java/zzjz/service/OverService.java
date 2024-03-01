package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Over;
import zzjz.bean.OverRequest;
import zzjz.bean.Staff;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年05月09日16:21
 * @ClassName: OverService
 * @Description: 加班管理服务接口
 */
@Service
public interface OverService {
    /**
     * 根据条件查询加班列表.
     *
     * @param over 加班信息
     * @return 列表
     */
    Page<Over> getOverList(OverRequest over);

    /**
     * 添加加班信息.
     *
     * @param over 加班信息
     * @return 结果
     */
    String addOverInfo(Over over);

    /**
     * 修改加班信息.
     *
     * @param over 加班信息
     * @return 结果
     */
    String updateOverInfo(Over over);

    /**
     * 提交流程.
     *
     * @param over    加班信息
     * @param user    处理人
     * @param staffId 用户ID
     * @return 结果
     */
    boolean submitOverTask(Over over, String user, String staffId);

    /**
     * 审批流程.
     *
     * @param over   加班信息
     * @param user   处理人
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     */
    boolean approveOverTask(Over over, String user, String msg, String reason);

    /**
     * 根据ID查询加班信息.
     *
     * @param id id
     * @return Over
     */
    Over getOverInfoById(Integer id);

    /**
     * 查询下一处理人.
     *
     * @param over     Over
     * @param departId departId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Over over, long departId, String msg);

    /**
     * 查询加班信息.
     *
     * @param over Over
     * @return Over
     */
    Over selectOverInfo(Over over);

    /**
     * 根据流程ID删除加班信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 根据流程ID撤销加班信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 删除关联的考勤信息.
     *
     * @param over Over
     * @return boolean
     */
    boolean terminationOverTask(Over over);

    /**
     * 加班信息导出内容.
     * @param over 加班信息
     * @return 结果
     */
    Map<String, Object> getOverMapList(Over over);

    /**
     * 忽略加班提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);

    boolean initOverOffInfo(Over over) throws ParseException;
}
