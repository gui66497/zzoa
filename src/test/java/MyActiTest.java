
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.mapper.VacationMapper;
import zzjz.service.ActivitiService;
import zzjz.service.impl.ActivitiServiceImpl;

import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月05日14:23
 */
public class MyActiTest {
    ActivitiService activitiService = new ActivitiServiceImpl();
    @Autowired
    VacationMapper vacationMapper;

    /**
     *流程部署定义
     */
    @Test
    public void deployFlow(){
        activitiService.deployFlow("helloworld入门程序","HelloWorld");
    }

    /**
     *启动流程
     */
    @Test
    public void flowStart(){
        String id = activitiService.flowStart("HelloWorldKey","张三");
        System.out.println(id);
    }

    /**
     * 提交流程
     */
    @Test
    public void completeTask(){
        Task task = activitiService.findTaskById("65001");
        if(task!=null){
            activitiService.submitTask(task.getId(),"赵11","agree");
        }
    }

    /**
     * 根据用户名查询任务列表
     */
    @Test
    public void findMyPersonTask(){
        List<Task> taskList = activitiService.findTaskByUserId("张三");
        if(taskList!=null && taskList.size()>0){
            for(Task task :taskList){
                System.out.println("待办任务ID："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务创建时间："+task.getCreateTime());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
            }
        }
    }

    /**
     * 根据主键查询任务
     */
    @Test
    public void findTaskById(){
        Task task = activitiService.findTaskById("17501");
        if( task!=null ){
            System.out.println("任务名称："+task.getName());
            System.out.println("任务创建时间："+task.getCreateTime());
            System.out.println("任务办理人："+task.getAssignee());
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("执行对象ID："+task.getExecutionId());
            System.out.println("流程定义ID："+task.getProcessDefinitionId());
            System.out.println(task.getDelegationState());
            System.out.println(task.getCategory());
        }
    }


    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinition(){
        List<ProcessDefinition> list = activitiService.findProcessDefinition("HelloWorldName");

        if(list != null && list.size()>0){
            for(ProcessDefinition processDefinition:list){
                System.out.println("流程定义ID:"+processDefinition.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义名称:"+processDefinition.getName());//对应HelloWorld.bpmn文件中的name属性值
                System.out.println("流程定义的key:"+processDefinition.getKey());//对应HelloWorld.bpmn文件中的id属性值
                System.out.println("流程定义的版本:"+processDefinition.getVersion());//当流程定义的key值相同的情况下，版本升级，默认从1开始
                System.out.println("资源名称bpmn文件:"+processDefinition.getResourceName());
                System.out.println("资源名称png文件:"+processDefinition.getDiagramResourceName());
                System.out.println("部署对象ID:"+processDefinition.getDeploymentId());
                System.out.println("################################");
            }
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefinition(){
        activitiService.deleteProcessDefinition("37501",true);
    }

    /**
     * 获取流程变量
     */
    @Test
    public void getVariables(){
        Object object = activitiService.getVariables("111","111");
        System.out.println(object);
    }
}
