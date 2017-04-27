package zzjz.service.impl;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.ActHis;
import zzjz.mapper.ActHisMapper;
import zzjz.rest.StaffRest;
import zzjz.service.ActivitiService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @ClassName: ActivitiServiceImpl
 * @Description: 工作流实现类
 * @date 2017年04月06日14:31
 */
@Service
public class ActivitiServiceImpl implements ActivitiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffRest.class);

    @Autowired
    ActHisMapper actHisMapper;

    /**
     * 流程部署定义
     * @param flowName 工作流名称 request
     * @param fileName 工作流文件名称 request
     * @return 结果
     */
    @Override
    public boolean deployFlow(String flowName, String fileName) {
        //如果文件名为空，则返回false
        if(flowName==null||"".equals(flowName)||fileName==null||"".equals(fileName)){
            return false;
        }

        //文件不存在，返回false
/*        File bpmnFile = new File("diagrams/"+fileName+".bpmn");
        File pngFIle = new File("diagrams/"+fileName+".bpmn");
        if(!bpmnFile.exists()||!pngFIle.exists()){
            return false;
        }*/

        boolean result = true;

        try{
            LOGGER.debug("开始流程部署定义");
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            Deployment deployment = processEngine.getRepositoryService()
                    .createDeployment()
                    .name(flowName)
                    .addClasspathResource("diagrams/"+fileName+".bpmn")
                    .addClasspathResource("diagrams/"+fileName+".png")
                    .deploy();
            LOGGER.debug("部署ID："+deployment.getId());
            LOGGER.debug("部署名称:"+deployment.getName());
            LOGGER.debug("流程部署定义结束");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 启动流程
     * @param key 流程定义的key值 request
     * @param user 操作员 request
     * @return 编号
     */
    @Override
    public String flowStart(String key, String user) {
        String id = "";
        try{
            LOGGER.debug("开始启动流程");
            Map<String,Object> map = new HashMap<>();
            map.put("user",user);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();// 与正在执行的流程实例和执行对象相关的Service
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key,map);// 使用流程定义的key启动流程实例，key对应bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
            id = processInstance.getId();
            LOGGER.debug("流程实例ID:"+processInstance.getId());
            LOGGER.debug("流程ID："+processInstance.getProcessDefinitionId());
            LOGGER.debug("启动流程结束");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    /**
     * 提交流程
     * @param id 任务ID request
     * @param user 操作员 request
     * @param msg 操作 request
     * @return 结果
     */
    @Override
    public boolean submitTask(String id,String user,String msg) {
        boolean result = true;
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("user",user);
            if(msg!=null&&!"".equals(msg.trim())){
                map.put("msg",msg);
            }
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getTaskService()//获取任务Service
                    .complete(id,map);
            LOGGER.debug("完成任务，任务ID："+id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 删除流程定义
     * @param deploymentId request
     * @param force request
     * @return 结果
     */
    @Override
    public boolean deleteProcessDefinition(String deploymentId,boolean force) {
        boolean result = true;
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            if(force){
                repositoryService.deleteDeployment(deploymentId,true);//删除流程定义，包括启动过的流程
            }else{
                repositoryService.deleteDeployment(deploymentId);//只能删除没有启动流程的流程定义，如果流程启动则抛出异常
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
    public Object getVariables(String taskId,String objId) {
        Object object = new Object();
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            object = taskService.getVariable(taskId,objId);
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
            if(taskList!=null && taskList.size()>0){
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
            ProcessDefinitionQuery processDefinitionQuery = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                    .createProcessDefinitionQuery();//创建一个流程定义查询
            if(definitionName!=null&&!"".equals(definitionName.trim())){
                list = processDefinitionQuery.processDefinitionNameLike(definitionName)//使用流程定义的名称模糊查询
                        .orderByProcessDefinitionVersion().asc()//按照版本的升序排列
                        .list();//返回一个集合列表，封装流程定义
            } else {
                list = processDefinitionQuery.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
                        .list();//返回一个集合列表，封装流程定义
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
}
