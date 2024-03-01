package zzjz.service;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.ActIdGroup;
import zzjz.bean.Staff;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:17
 * @ClassName: ActivitiService
 * @Description: 工作流服务接口
 */
@Service
public interface ActivitiService {

    /**
     * 流程部署定义.
     *
     * @param flowName 工作流名称 request
     * @param fileName 工作流文件名称 request
     * @return 结果
     */
    boolean deployFlow(String flowName, String fileName);

    /**
     * 启动流程.
     *
     * @param key  流程定义的key值 request
     * @param user 操作员 request
     * @return 编号
     */
    String flowStart(String key, String user);

    /**
     * 申请流程提交.
     *
     * @param actId  流程ID request
     * @param user   操作员 request
     * @param actTyp actTyp
     * @return 结果
     */
    boolean submitTask(String actId, String user, String actTyp);

    /**
     * 申请流程提交.
     *
     * @param actId     流程ID request
     * @param user      操作员 request
     * @param isHoliday 是否节假日 request
     * @return 结果
     */
    boolean submitOverTask(String actId, String user, boolean isHoliday);

    /**
     * 请假申请流程提交.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @param day   请假天数 request
     * @return 结果
     */
    boolean submitVacationTask(String actId, String user, Double day);

    /**
     * 审批流程.
     *
     * @param actId  流程ID request
     * @param user   操作员 request
     * @param actTyp actTyp
     * @param msg    操作 request
     * @param reason reason
     * @return 结果
     */
    boolean approveTask(String actId, String user, String actTyp, String msg, String reason);

    /**
     * 加班审批流程.
     *
     * @param actId     流程ID request
     * @param user      操作员 request
     * @param msg       操作 request
     * @param reason    reason
     * @param isHoliday 是否节假日 request
     * @return 结果
     */
    boolean approveOverTask(String actId, String user, String msg, String reason, boolean isHoliday);

    /**
     * 请假审批流程.
     *
     * @param actId  流程ID request
     * @param user   操作员 request
     * @param msg    操作 request
     * @param reason reason
     * @param day    请假天数 request
     * @return 结果
     */
    boolean approveVacationTask(String actId, String user, String msg, String reason, Double day);

    /**
     * 删除流程定义.
     *
     * @param deploymentId request
     * @param force        request
     * @return 结果
     */
    boolean deleteProcessDefinition(String deploymentId, boolean force);

    /**
     * 获取流程变量.
     *
     * @param taskId 任务ID request
     * @param objId  查询变量 request
     * @return 流程变量
     */
    Object getVariables(String taskId, String objId);

    /**
     * 查询当前人的任务.
     *
     * @param userId request
     * @return 任务列表
     */
    List<Task> findTaskByUserId(String userId);

    /**
     * 根据ID查询任务.
     *
     * @param id request
     * @return 任务列表
     */
    Task findTaskById(String id);

    /**
     * 查询流程定义.
     *
     * @param definitionName request
     * @return 流程列表
     */
    List findProcessDefinition(String definitionName);

    /**
     * 查询流程明细.
     *
     * @param actId 流程号
     * @return 明细列表
     */
    List<ActHis> getActHisList(String actId);

    /**
     * 通过staffId和角色获取领导.
     *
     * @param staffId  staffId
     * @param roleName roleName
     * @return 员工信息
     */
    List<Staff> getLeaderByRole(String staffId, String roleName);

    /**
     * 通过staffId获取下级审批人.
     *
     * @param staffId 用户ID
     * @param actId   actId
     * @param isApply 是否是申请
     * @param actTyp  actTyp
     * @param condition 条件
     * @return 员工信息
     */
    List<Staff> getNextLeaderByRole(String staffId, String actId, boolean isApply, String actTyp, String condition);

    /**
     * 获取上一节点处理人.
     *
     * @param actId 流程ID
     * @return 员工信息
     */
    Staff getLastProcessor(String actId);

    /**
     * @param actId   actId
     * @param staffId staffId
     * @return boolean
     */
    boolean submitTask2(String actId, String staffId);

    /**
     * 根据ID查询流程是否结束.
     *
     * @param id id
     * @return 结果
     */
    boolean isEndProcess(String id);

    /**
     * 根据流程号删除流程信息记录.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteRecordByActId(String actId);

    /**
     * 根据流程号删除流程历史记录.
     *
     * @param actId actId
     * @return boolean
     */
    boolean deleteActinstByActId(String actId);

    /**
     * 校验流程是否处于申请阶段.
     *
     * @param actId actId
     * @return boolean
     */
    boolean isApply(String actId);

    /**
     * 获取所有流程组信息.
     *
     * @return List
     */
    List<ActIdGroup> getActIdGroupList();

    /**
     * 结束流程.
     *
     * @param actId    actId
     * @param vacation vacation
     * @return boolean
     */
    boolean terminationTask(String actId, String vacation);

    /**
     * 获取总经办除自己外员工列表.
     *
     * @param staffId staffId
     * @return List
     */
    List<Staff> getTopStaffList(String staffId);

    /**
     * 通用流程提交.
     *
     * @param actId actId
     * @param user  user
     * @return boolean
     */
    boolean submitCommonTask(String actId, String user);

    /**
     * 根据流程定义的id获取其key.
     *
     * @param definitionId 流程定义id
     * @return key
     */
    String getProcessDefinitionKeyByDefId(String definitionId);

    /**
     * 查找上一可为空的处理人.
     *
     * @param actId actId
     * @return String
     */
    String findLastUserWithNull(String actId);
}
