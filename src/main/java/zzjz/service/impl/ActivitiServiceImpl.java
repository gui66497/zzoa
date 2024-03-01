package zzjz.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.bean.ActIdGroup;
import zzjz.bean.DepartRole;
import zzjz.bean.Staff;
import zzjz.mapper.ActHisMapper;
import zzjz.mapper.ActIdGroupMapper;
import zzjz.service.ActivitiService;
import zzjz.service.MailService;
import zzjz.service.StaffService;
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
public class ActivitiServiceImpl implements ActivitiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiServiceImpl.class);

    private static final Double DOU5 = 5.0;

    @Autowired
    ActHisMapper actHisMapper;

    @Autowired
    ActIdGroupMapper actIdGroupMapper;

    @Autowired
    StaffService staffService;

    @Autowired
    IdentityService identityService;

    @Autowired
    MailService mailService;

    /**
     * 流程部署定义.
     *
     * @param flowName 工作流名称 request
     * @param fileName 工作流文件名称 request
     * @return 结果
     */
    @Override
    public boolean deployFlow(String flowName, String fileName) {
        //如果文件名为空，则返回false
        if (flowName == null || "".equals(flowName) || fileName == null || "".equals(fileName)) {
            return false;
        }

        boolean result = true;

        try {
            LOGGER.debug("开始流程部署定义");
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            Deployment deployment = processEngine.getRepositoryService()
                    .createDeployment()
                    .name(flowName)
                    .addClasspathResource("diagrams/" + fileName + ".bpmn")
                    .addClasspathResource("diagrams/" + fileName + ".png")
                    .deploy();
            LOGGER.debug("部署ID：" + deployment.getId());
            LOGGER.debug("部署名称:" + deployment.getName());
            LOGGER.debug("流程部署定义结束");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 启动流程.
     *
     * @param key  流程定义的key值 request
     * @param user 操作员 request
     * @return 编号
     */
    @Override
    public String flowStart(String key, String user) {
        String id = "";
        try {
            LOGGER.debug("开始启动流程");
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            identityService.setAuthenticatedUserId(user);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService(); // 与正在执行的流程实例和执行对象相关的Service
            // 使用流程定义的key启动流程实例，key对应bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);
            id = processInstance.getId();
            LOGGER.debug("流程实例ID:" + processInstance.getId());
            LOGGER.debug("流程ID：" + processInstance.getProcessDefinitionId());
            LOGGER.debug("启动流程结束");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 提交流程.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @return 结果
     */
    @Override
    public boolean submitTask(String actId, String user, String actTyp) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", "agree");
            String id = findTaskById(actId).getId();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if (user == null || "undefined".equals(user) || "".equals(user)) {
                map.put("user", "");
                while (!isEndProcess(actId)) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");
                while (!role.equals(findNextRole(actId, actTyp, null, ""))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 触发邮件发送
                // 内容大致为 fgt向您申请了【名称】出差，出差日期为20190000，请及时登陆oa系统审批
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
            LOGGER.debug("完成任务，任务ID：" + id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 提交加班流程.
     *
     * @param actId     流程ID request
     * @param user      操作员 request
     * @param isHoliday 是否节假日 request
     * @return 结果
     */
    @Override
    public boolean submitOverTask(String actId, String user, boolean isHoliday) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", "agree");
            if (isHoliday) {
                map.put("isHoliday", "1");
            } else {
                map.put("isHoliday", "0");
            }
            String id = findTaskById(actId).getId();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if (user == null || "undefined".equals(user) || "".equals(user)) {
                map.put("user", "");
                while (!isEndProcess(actId)) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");
                String condition;
                if (isHoliday) {
                    condition = "isHoliday=='1'";
                } else {
                    condition = "isHoliday=='0'";
                }
                while (!role.equals(findNextRole(actId, "over", null, condition))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 触发邮件发送
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
            LOGGER.debug("完成任务，任务ID：" + id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 提交请假流程.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @param day   请假天数 request
     * @return 结果
     */
    @Override
    public boolean submitVacationTask(String actId, String user, Double day) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", "agree");
            map.put("day", day);
            String id = findTaskById(actId).getId();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if (user == null || "undefined".equals(user) || "".equals(user)) {
                map.put("user", "");
                while (!isEndProcess(actId)) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");

                String condition;
                if (day < DOU5) {
                    condition = "day < 5";
                } else {
                    condition = "day >= 5";
                }

                while (!role.equals(findNextRole(actId, "vacation", null, condition))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 触发邮件发送
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
            LOGGER.debug("完成任务，任务ID：" + id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public boolean submitTask2(String actId, String staffId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Map<String, Object> map = new HashMap<>();
        String id = findTaskById(actId).getId();
        map.put("msg", "agree");
        processEngine.getTaskService().complete(id, map);
        return true;
    }

    @Override
    public boolean submitCommonTask(String actId, String user) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", "agree");
            String id = findTaskById(actId).getId();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getTaskService().complete(id, map);
            //更新历史操作
            updateHistoryOption(id, "agree", "同意");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public String getProcessDefinitionKeyByDefId(String definitionId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getRepositoryService().
                createProcessDefinitionQuery().
                processDefinitionId(definitionId).
                singleResult().getKey();
    }

    /**
     * 审批流程.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @param msg   操作 request
     * @return 结果
     */
    @Override
    public boolean approveTask(String actId, String user, String actTyp, String msg, String reason) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            if (msg != null && !"".equals(msg.trim())) {
                map.put("msg", msg);
            }
            String id;
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if ("disagree".equals(msg)) {
                while (!findLastUserWithNull(actId).equals(user)) {
                    id = findTaskById(actId).getId();
                    map.put("user", "");
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, msg, reason);
                }
                if (user == null || "undefined".equals(user)) {
                    map.put("user", "");
                } else {
                    map.put("user", user);
                }
                id = findTaskById(actId).getId();
                processEngine.getTaskService().complete(id, map);
                LOGGER.debug("完成任务，任务ID：" + id);
                //更新历史操作
                updateHistoryOption(id, msg, reason);
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");
                while (!role.equals(findNextRole(actId, actTyp, null, ""))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 触发邮件发送
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 审批加班流程.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @param msg   操作 request
     * @return 结果
     */
    @Override
    public boolean approveOverTask(String actId, String user, String msg, String reason, boolean isHoliday) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            if (isHoliday) {
                map.put("isHoliday", "1");
            } else {
                map.put("isHoliday", "0");
            }
            if (msg != null && !"".equals(msg.trim())) {
                map.put("msg", msg);
            }
            String id;
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if ("disagree".equals(msg)) {
                while (!findLastUserWithNull(actId).equals(user)) {
                    id = findTaskById(actId).getId();
                    map.put("user", "");
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, msg, reason);
                }
                if (user == null || "undefined".equals(user)) {
                    map.put("user", "");
                } else {
                    map.put("user", user);
                }
                id = findTaskById(actId).getId();
                processEngine.getTaskService().complete(id, map);
                LOGGER.debug("完成任务，任务ID：" + id);
                //更新历史操作
                updateHistoryOption(id, msg, reason);
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");
                String condition;
                if (isHoliday) {
                    condition = "isHoliday=='1'";
                } else {
                    condition = "isHoliday=='0'";
                }
                while (!role.equals(findNextRole(actId, "over", null, condition))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 触发邮件发送
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 审批请假流程.
     *
     * @param actId 流程ID request
     * @param user  操作员 request
     * @param msg   操作 request
     * @return 结果
     */
    @Override
    public boolean approveVacationTask(String actId, String user, String msg, String reason, Double day) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("day", day);
            if (msg != null && !"".equals(msg.trim())) {
                map.put("msg", msg);
            }
            String id;
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            if ("disagree".equals(msg)) {
                while (!findLastUserWithNull(actId).equals(user)) {
                    id = findTaskById(actId).getId();
                    map.put("user", "");
                    processEngine.getTaskService().complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, msg, reason);
                }
                if (user == null || "undefined".equals(user)) {
                    map.put("user", "");
                } else {
                    map.put("user", user);
                }
                id = findTaskById(actId).getId();
                processEngine.getTaskService().complete(id, map);
                LOGGER.debug("完成任务，任务ID：" + id);
                //更新历史操作
                updateHistoryOption(id, msg, reason);
            } else {
                Staff staff = staffService.getStaffWithRoleByUserId(findTaskById(actId).getAssignee());
                //默认部门
                String defaultDepartId = staff.getDepartId();
                //根据角色查询下一节点处理角色是否是所选人角色，如果是，则提交至该人，如果不是，则执行自动提交一次，再进行判断
                String role = staffService.getStaffByDepartAndUser(user, Long.parseLong(defaultDepartId));
                map.put("user", "");

                String condition;
                if (day < DOU5) {
                    condition = "day < 5";
                } else {
                    condition = "day >= 5";
                }
                while (!role.equals(findNextRole(actId, "vacation", null, condition))) {
                    id = findTaskById(actId).getId();
                    processEngine.getTaskService()//获取任务Service
                            .complete(id, map);
                    //更新历史操作
                    updateHistoryOption(id, "agree", "同意");
                }
                map.put("user", user);
                id = findTaskById(actId).getId();
                processEngine.getTaskService()//获取任务Service
                        .complete(id, map);
                //更新历史操作
                updateHistoryOption(id, "agree", "同意");

                // 发送提醒邮件
                if (StringUtils.isNotBlank(user)) {
                    mailService.sendMail(actId);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public boolean terminationTask(String actId, String vacation) {
        boolean result = true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user", "");
            map.put("msg", "agree");
            String id;
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            while (!isEndProcess(actId)) {
                id = findTaskById(actId).getId();
                processEngine.getTaskService().complete(id, map);
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
    public List<Staff> getTopStaffList(String staffId) {
        List<Staff> staffList = staffService.getTopStaffList();
        staffList = removeItself(staffList, Long.parseLong(staffId));
        return staffList;
    }

    private void updateHistoryOption(String taskId, String msg, String reason) {
        ActHis actHis = actHisMapper.selectByTaskId(taskId);
        actHis.setOption(msg);
        actHis.setReason(reason);
        actHisMapper.updateByPrimaryKeySelective(actHis);
    }

    @Override
    public boolean isEndProcess(String id) {
        boolean result = false;
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance rpi = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(id).singleResult();
        if (rpi == null) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        return actHisMapper.deleteRecordByActId(actId) > 0 && actHisMapper.deleteActinstByActId(actId) > 0;
    }

    @Override
    public boolean deleteActinstByActId(String actId) {
        return actHisMapper.deleteActinstByActId(actId) > 0;
    }

    @Override
    public boolean isApply(String actId) {
        return actHisMapper.checkIsApplyByActId(actId) > 0;
    }

    @Override
    public List<ActIdGroup> getActIdGroupList() {
        return actIdGroupMapper.getActIdGroupList();
    }

    /**
     * 删除流程定义.
     *
     * @param deploymentId request
     * @param force        request
     * @return 结果
     */
    @Override
    public boolean deleteProcessDefinition(String deploymentId, boolean force) {
        boolean result = true;
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            if (force) {
                repositoryService.deleteDeployment(deploymentId, true); //删除流程定义，包括启动过的流程
            } else {
                repositoryService.deleteDeployment(deploymentId); //只能删除没有启动流程的流程定义，如果流程启动则抛出异常
            }
            LOGGER.debug("删除成功");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    @Override
    public Object getVariables(String taskId, String objId) {
        Object object = new Object();
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            object = taskService.getVariable(taskId, objId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public List<Task> findTaskByUserId(String userId) {
        List<Task> taskList = null;
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            taskList = processEngine.getTaskService()
                    .createTaskQuery().taskAssignee(userId).list();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return taskList;
    }

    @Override
    public Task findTaskById(String id) {
        Task task = null;
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            List<Task> taskList = processEngine.getTaskService()
                    .createTaskQuery().executionId(id).list();
            if (taskList != null && taskList.size() > 0) {
                task = taskList.get(0);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public List findProcessDefinition(String definitionName) {
        List<ProcessDefinition> list = null;
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService() //与流程定义和部署对象相关的Service
                    .createProcessDefinitionQuery(); //创建一个流程定义查询
            if (definitionName != null && !"".equals(definitionName.trim())) {
                list = processDefinitionQuery.processDefinitionNameLike(definitionName) //使用流程定义的名称模糊查询
                        .orderByProcessDefinitionVersion().asc() //按照版本的升序排列
                        .list(); //返回一个集合列表，封装流程定义
            } else {
                list = processDefinitionQuery.orderByProcessDefinitionVersion().asc() //按照版本的升序排列
                        .list(); //返回一个集合列表，封装流程定义
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ActHis> getActHisList(String actId) {
        List<ActHis> actHisList = actHisMapper.selectActListByActId(actId);
        return actHisList;
    }

    @Override
    public List<Staff> getLeaderByRole(String staffId, String roleName) {
        Staff staff = staffService.getStaffWithRoleByUserId(staffId);
        //默认部门
        String defaultDepartId = staff.getDepartId();
        //所处部门和角色
        List<DepartRole> departRoles = staff.getDepartRoles();
        List<Staff> staffResult = new ArrayList<>();
        //遍历部门
        for (DepartRole departRole : departRoles) {
            String departId = String.valueOf(departRole.getDepartId());
            if (!defaultDepartId.equals(departId)) {
                continue;
            }

            List<Staff> staffs = staffService.getStaffByDepartAndRole(roleName, departRole.getDepartId());
            String role = departRole.getGroupId();
            //自己的角色就是所请求的角色
            if (roleName.equals(role)) {
                //去除掉自己
                staffs = removeItself(staffs, staff.getStaffId());
            }
            staffResult.addAll(staffs);
        }
        return staffResult;
    }

    @Override
    public List<Staff> getNextLeaderByRole(String staffId, String actId, boolean isApply, String actTyp, String condition) {
        List<Staff> staffResult = new ArrayList<>();
        Staff staff = staffService.getStaffWithRoleByUserId(staffId);
        //默认部门
        long defaultDepartId = Long.parseLong(staff.getDepartId());
        //根据部门用户查询角色
        String role = staffService.getStaffByDepartAndUser(staffId, defaultDepartId);
        //如果是申请
        if (isApply) {
            if ("user".equals(role) || "hr".equals(role)) { //普通用户申请，根据下一角色
                //根据角色查询除自己外用户
                String nextRole = findNextRole(actId, actTyp, staff.getDepartId(), condition);
                if (nextRole != null && !"".equals(nextRole)) {
                    //根据角色查询除自己外用户
                    staffResult = staffService.getStaffByDepartAndRole(nextRole, defaultDepartId);
                }
            } else { //非普通用户申请
                //查看自身角色是否在流程包含的角色中
                if (actHisMapper.getNodeByRoleId(actTyp, role) > 0) {
                    staffResult = findNextStaffs(actId, role, staff.getStaffId(), defaultDepartId, actTyp);
                }
            }
        } else { //非申请
            //查找下一角色
            String nextRole = findNextRole(actId, actTyp, staff.getDepartId(), condition);
            if (nextRole != null && !"".equals(nextRole)) {
                //根据角色查询除自己外用户
                staffResult = staffService.getStaffByDepartAndRole(nextRole, defaultDepartId);
            }
        }

        return staffResult;
    }

    /**
     * 获取上一节点处理人.
     *
     * @param actId 流程ID
     * @return 员工信息
     */
    @Override
    public Staff getLastProcessor(String actId) {
        Staff staff = new Staff();
        String staffId = findLastUserNotNull(actId, "");
        staff.setStaffId(Long.parseLong(staffId));
        return staff;
    }

    /**
     * 查找上一不为空的处理人
     *
     * @param actId
     * @param activitiId
     * @return
     */
    private String findLastUserNotNull(String actId, String activitiId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(actId).list();
        String user = "";
        for (Task task : tasks) {
            RepositoryService rs = processEngine.getRepositoryService();
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rs)
                    .getDeployedProcessDefinition(task.getProcessDefinitionId());
            List<ActivityImpl> activityList = def.getActivities();
            if ("".equals(activitiId)) {
                String excId = task.getExecutionId();
                RuntimeService runtimeService = processEngine.getRuntimeService();
                ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
                activitiId = execution.getActivityId();
            }
            for (ActivityImpl activityImpl : activityList) {
                String id = activityImpl.getId();
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                for (PvmTransition tr : outTransitions) {
                    //默认取条件为agree的分支
                    String s = String.valueOf(tr.getProperty("conditionText"));
                    if (StringUtils.contains(s, "msg")) {
                        if (StringUtils.contains(s, "msg=='agree'")) {
                            PvmActivity ac = tr.getDestination();
                            if (activitiId.equals(ac.getId())) {
                                List<ActHis> list = actHisMapper.getUserByProcIdAndActId(actId, id);
                                if (list != null && list.size() > 0) {
                                    user = list.get(0).getAssignee();
                                }
                                if (user == null || "".equals(user)) {
                                    activitiId = id;
                                    user = findLastUserNotNull(actId, activitiId);
                                }
                            }
                        }
                    } else {
                        PvmActivity ac = tr.getDestination();
                        if (activitiId.equals(ac.getId())) {
                            List<ActHis> list = actHisMapper.getUserByProcIdAndActId(actId, id);
                            if (list != null && list.size() > 0) {
                                user = list.get(0).getAssignee();
                            }
                            if (user == null || "".equals(user)) {
                                activitiId = id;
                                user = findLastUserNotNull(actId, activitiId);
                            }
                        }
                    }
                }
            }
        }
        return user;
    }

    @Override
    public String findLastUserWithNull(String actId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(actId).list();
        String user = "";
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
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                for (PvmTransition tr : outTransitions) {
                    //默认取条件为agree的分支
                    String s = String.valueOf(tr.getProperty("conditionText"));
                    if (StringUtils.contains(s, "msg")) {
                        if (StringUtils.contains(s, "msg=='agree'")) {
                            PvmActivity ac = tr.getDestination();
                            if (activitiId.equals(ac.getId())) {
                                List<ActHis> list = actHisMapper.getUserByProcIdAndActId(actId, id);
                                if (list != null && list.size() > 0) {
                                    user = list.get(0).getAssignee();
                                }
                            }
                        }
                    } else {
                        PvmActivity ac = tr.getDestination();
                        if (activitiId.equals(ac.getId())) {
                            List<ActHis> list = actHisMapper.getUserByProcIdAndActId(actId, id);
                            if (list != null && list.size() > 0) {
                                user = list.get(0).getAssignee();
                            }
                        }
                    }
                }
            }
        }
        return user;
    }

    private String findNextRole(String actId, String actTyp, String defaultDepartId, String condition) {
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
            nextId = getNextId(activityList, activitiId, condition); //获取下一个角色位
            if (defaultDepartId != null) {
                while (nextId != null && !"".equals(nextId) &&
                        staffService.getStaffByDepartAndRole(actHisMapper.getRoleByNodeId(actTyp, nextId),
                                Long.parseLong(defaultDepartId)).size() == 0) {
                    nextId = getNextId(activityList, nextId, condition); //如果获取到的角色位没有用户，则继续获取下一个角色位，直到有用户为止
                }
            }
        }
        return actHisMapper.getRoleByNodeId(actTyp, nextId);
    }

    private String getNextId(List<ActivityImpl> activityList, String activitiId, String condition) {
        String nextId = "";
        for (ActivityImpl activityImpl : activityList) {
            String id = activityImpl.getId();
            if (activitiId.equals(id)) {
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                for (PvmTransition tr : outTransitions) {
                    //默认取条件为agree的分支
                    String s = String.valueOf(tr.getProperty("conditionText"));
                    if (StringUtils.contains(s, "msg") && StringUtils.contains(s, "msg=='agree'")) {
                        if ("".equals(condition) || !StringUtils.contains(s, "&&") || (!"".equals(condition) && StringUtils.contains(s, condition))) {
                            PvmActivity ac = tr.getDestination();
                            nextId = ac.getId();
                        }
                    } else if (!StringUtils.contains(s, "msg")) {
                        PvmActivity ac = tr.getDestination();
                        nextId = ac.getId();
                    }
                }
                break;
            }
        }
        return nextId;
    }

    private List<Staff> findNextStaffs(String actId, String roleId, long staffId, long departId, String actTyp) {
        List<Staff> staffList = new ArrayList<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(actId).list();
        for (Task task : tasks) {
            RepositoryService rs = processEngine.getRepositoryService();
            ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) rs)
                    .getDeployedProcessDefinition(task.getProcessDefinitionId());
            List<ActivityImpl> activityList = def.getActivities();
            for (ActivityImpl activityImpl : activityList) {
                String id = activityImpl.getId();
                if (roleId.equals(actHisMapper.getRoleByNodeId(actTyp, id))) {
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                    for (PvmTransition tr : outTransitions) {
                        //默认取条件为agree的分支
                        String s = String.valueOf(tr.getProperty("conditionText"));
                        if (StringUtils.contains(s, "msg")) {
                            if (StringUtils.contains(s, "msg=='agree'")) {
                                PvmActivity ac = tr.getDestination();
                                String nextRoleId = actHisMapper.getRoleByNodeId(actTyp, ac.getId());
                                //下级角色
                                staffList = staffService.getStaffByDepartAndRole(nextRoleId, departId);
                                //去除掉自己
                                staffList = removeItself(staffList, staffId);
                            }
                        } else {
                            PvmActivity ac = tr.getDestination();
                            String nextRoleId = actHisMapper.getRoleByNodeId(actTyp, ac.getId());
                            //下级角色
                            staffList = staffService.getStaffByDepartAndRole(nextRoleId, departId);
                            //去除掉自己
                            staffList = removeItself(staffList, staffId);
                        }
                    }
                    break;
                }
            }
        }
        return staffList;
    }

    /**
     * 排除自己的员工列表.
     *
     * @param staffs  staffs
     * @param staffId staffId
     * @return 员工列表
     */
    public List<Staff> removeItself(List<Staff> staffs, long staffId) {

        List<Staff> result = new ArrayList<>();
        for (Staff staff : staffs) {
            if (staffId != staff.getStaffId()) {
                result.add(staff);
            }
        }
        return result;
    }
}
