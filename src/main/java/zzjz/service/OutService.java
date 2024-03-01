package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Out;
import zzjz.bean.OutRequest;
import zzjz.bean.Staff;

import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年05月09日16:21
 * @ClassName: OutService
 * @Description: 外出管理服务接口
 */
@Service
public interface OutService {
    /**
     * 根据条件查询外出列表.
     *
     * @param out 外出信息
     * @return 列表
     */
    Page<Out> getOutList(OutRequest out);

    /**
     * 添加外出信息.
     *
     * @param out 外出信息
     * @return 结果
     */
    String addOutInfo(Out out);

    /**
     * 修改外出信息.
     *
     * @param out 外出信息
     * @return 结果
     */
    boolean updateOutInfo(Out out);

    /**
     * 提交流程.
     *
     * @param out  外出信息
     * @param user 处理人
     * @return 结果
     */
    boolean submitOutTask(Out out, String user);

    /**
     * 审批流程.
     *
     * @param out    外出信息
     * @param user   处理人
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     */
    boolean approveOutTask(Out out, String user, String msg, String reason);

    /**
     * 根据ID查询外出信息.
     *
     * @param id id
     * @return Out
     */
    Out getOutInfoById(Integer id);

    /**
     * 获取下一处理人.
     *
     * @param out      Out
     * @param departId departId
     * @param msg      msg
     * @return List
     */
    List<Staff> getNextUser(Out out, long departId, String msg);

    /**
     * 查询外出信息.
     *
     * @param out Out
     * @return List
     */
    List<Out> selectOutInfo(Out out);

    /**
     * 根据流程ID删除外出信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 根据流程ID撤销外出信息.
     *
     * @param actId actId
     * @return boolean
     */
    boolean revokeRecordByActId(String actId);

    /**
     * 审批并结束外出信息.
     *
     * @param out Out
     * @return boolean
     */
    boolean terminationOutTask(Out out);

    /**
     * 外出信息导出内容.
     * @param out 外出信息
     * @return 结果
     */
    Map<String, Object> getOutMapList(Out out);

    /**
     * 忽略外出提醒.
     *
     * @param id 主键
     * @return 结果
     */
    boolean ignore(Integer id);
}
