package zzjz.service;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月06日14:17
 */
@Service
public interface ActivitiService {

    /**
     * 流程部署定义
     * @param flowName 工作流名称 request
     * @param fileName 工作流文件名称 request
     * @return 结果
     */
    boolean deployFlow (String flowName,String fileName);

    /**
     * 启动流程
     * @param key 流程定义的key值 request
     * @param user 操作员 request
     * @return 编号
     */
    String flowStart (String key, String user);

    /**
     * 提交流程
     * @param id 任务ID request
     * @param user 操作员 request
     * @param msg 操作 request
     * @return 结果
     */
    boolean submitTask(String id,String user,String msg);

    /**
     * 删除流程定义
     * @param deploymentId request
     * @param force request
     * @return 结果
     */
    boolean deleteProcessDefinition(String deploymentId ,boolean force);

    /**
     * 获取流程变量
     * @param taskId 任务ID request
     * @param objId 查询变量 request
     * @return 流程变量
     */
    Object getVariables(String taskId,String objId);

    /**
     * 查询当前人的任务
     * @param userId request
     * @return 任务列表
     */
    List<Task> findTaskByUserId(String userId);

    /**
     * 根据ID查询任务
     * @param id request
     * @return 任务列表
     */
    Task findTaskById(String id);

    /**
     * 查询流程定义
     * @param definitionName request
     * @return 流程列表
     */
    List findProcessDefinition(String definitionName);

    /**
     * 查询流程明细
     * @param
     * @return 明细列表
     */
    List<ActHis> getActHisList(String actId);
}
