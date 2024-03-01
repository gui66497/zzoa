package zzjz.service.impl;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.Process;
import zzjz.mapper.ActHisMapper;
import zzjz.service.ActivitiService;
import zzjz.service.ProcessService;
import zzjz.service.SpecialActivitiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月06日14:31
 * @ClassName: ActivitiServiceImpl
 * @Description: 工作流实现类
 */
@Service
public class SpecialActivitiServiceImpl implements SpecialActivitiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialActivitiServiceImpl.class);

    private static final int INT0000 = 10000;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    ActHisMapper actHisMapper;

    @Autowired
    ProcessService processService;

    @Override
    public boolean submitTask(String actId, String user, String actTyp) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", "agree");
            String id = activitiService.findTaskById(actId).getId();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if (user == null || "undefined".equals(user) || "".equals(user)) {
                map.put("user", "");
                while (!activitiService.isEndProcess(actId)) {
                    id = activitiService.findTaskById(actId).getId();
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
            } else {
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public boolean approveTask(String actId, String actTyp, String reason, Map<String, Object> map) {
        boolean result = true;
        try {
            String id;
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if ("agree".equals(map.get("msg"))) {
                id = activitiService.findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");
            } else {
                String userId = activitiService.findLastUserWithNull(actId);
                id = activitiService.findTaskById(actId).getId();
                result = backToUpper(id, map);
                //更新历史操作
                updateHistoryOption(id, "disagree", reason);
                id = activitiService.findTaskById(actId).getId();
                actHisMapper.updateLastActHis(actId, userId);
                actHisMapper.updateRuTask(actId, userId);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public List<Process> getNextPaymentList(String actId, String actTyp, String msg, String conditionType, int conditionValue) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(actId).list();
        String nextId = "";
        for (Task task : tasks) {
            RepositoryService rs = processEngine.getRepositoryService();
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rs)
                    .getDeployedProcessDefinition(task.getProcessDefinitionId());
            List<ActivityImpl> activityList = def.getActivities();
            String excId = task.getExecutionId();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
            String activitiId = execution.getActivityId();
            for (ActivityImpl activityImpl : activityList) {
                String id = activityImpl.getId();
                if (activitiId.equals(id)) {
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                    for (PvmTransition tr : outTransitions) {
                        //默认取条件为agree的分支
                        String s = String.valueOf(tr.getProperty("conditionText"));
                        if (StringUtils.contains(s, "msg") && StringUtils.contains(s, "msg=='" + msg + "'")) {
                            if ((conditionValue >= INT0000 && StringUtils.contains(s, "price >= 10000"))
                                    || (conditionValue < INT0000 && StringUtils.contains(s, "price < 10000"))
                                    || !StringUtils.contains(s, "price")) {
                                PvmActivity ac = tr.getDestination();
                                nextId = ac.getId();
                            }
                        } else if (!StringUtils.contains(s, "msg")) {
                            if ((conditionValue >= INT0000 && StringUtils.contains(s, "price >= 10000"))
                                    || (conditionValue < INT0000 && StringUtils.contains(s, "price < 10000"))
                                    || !StringUtils.contains(s, "price")) {
                                PvmActivity ac = tr.getDestination();
                                nextId = ac.getId();
                            }
                        }
                    }
                    break;
                }
            }
        }

        return processService.getStaffList(actTyp, nextId);
    }

    @Override
    public Boolean backToUpper(String taskId, Map<String, Object> map) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        HistoryService historyService = processEngine.getHistoryService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
                return false;
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask
                            .getProcessDefinitionId());
            if (definition == null) {
                return false;
            }
            // 取得上一步活动
            ActivityImpl currActivity = definition
                    .findActivity(currTask.getTaskDefinitionKey());

            //也就是节点间的连线
            List<PvmTransition> nextTransitionList = currActivity
                    .getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<>();
            //新建一个节点连线关系集合
            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<>();

            /*****特殊情况处理处：1、供应商流程支付阶段，根据金额选择回退路线 *******/
            if ("payment".equals(instance.getProcessDefinitionKey()) && "pay".equals(instance.getActivityId())) {
                String choose;
                if (Integer.parseInt(map.get("price").toString()) < INT0000) {
                    choose = "general1";
                } else {
                    choose = "general2";
                }
                for (PvmTransition nextTransition : nextTransitionList) {
                    PvmActivity nextActivity = nextTransition.getSource();
                    if (choose.equals(nextActivity.getId())) {
                        ActivityImpl nextActivityImpl = definition
                                .findActivity(nextActivity.getId());
                        TransitionImpl newTransition = currActivity
                                .createOutgoingTransition();
                        newTransition.setDestination(nextActivityImpl);
                        newTransitions.add(newTransition);
                    }
                }
            } else {
                for (PvmTransition nextTransition : nextTransitionList) {
                    PvmActivity nextActivity = nextTransition.getSource();
                    ActivityImpl nextActivityImpl = definition
                            .findActivity(nextActivity.getId());
                    TransitionImpl newTransition = currActivity
                            .createOutgoingTransition();
                    newTransition.setDestination(nextActivityImpl);
                    newTransitions.add(newTransition);
                }
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                task.setAssignee("59");
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean backToAppoint(String taskId, String taskKey) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        HistoryService historyService = processEngine.getHistoryService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
                LOGGER.info("流程结束");
                LOGGER.error("出错啦！流程已经结束");
                return false;
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask
                            .getProcessDefinitionId());
            if (definition == null) {
                LOGGER.info("流程定义未找到");
                LOGGER.error("出错啦！流程定义未找到");
                return false;
            }
            //取得当前活动节点
            ActivityImpl currActivity = definition.findActivity(currTask.getTaskDefinitionKey());

            LOGGER.info("currActivity" + currActivity);
            // 取得上一步活动
            //也就是节点间的连线
            //获取来源节点的关系
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<>();
            //新建一个节点连线关系集合
            //获取出口节点的关系
            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();

                LOGGER.info("nextActivity" + nextActivity);

                LOGGER.info("nextActivity.getId()" + nextActivity.getId());

                //destTaskkey
                ActivityImpl nextActivityImpl = definition
                        .findActivity(taskKey);
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            LOGGER.info("OK");
            LOGGER.info("流程结束");
        } catch (Exception e) {
            LOGGER.error("失败");
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    private void updateHistoryOption(String taskId, String msg, String reason) {
        ActHis actHis = actHisMapper.selectByTaskId(taskId);
        actHis.setOption(msg);
        actHis.setReason(reason);
        actHisMapper.updateByPrimaryKeySelective(actHis);
    }
}
