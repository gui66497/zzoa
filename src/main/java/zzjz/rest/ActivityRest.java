package zzjz.rest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.MyProcessDefinition;
import zzjz.bean.ResultCode;
import zzjz.service.ActivitiService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author 彭鹏
 * @version 2017年03月31日10:20
 * @ClassName: ActivityRest
 * @Description: 流程rest
 */
@Component
@Path("activity")
public class ActivityRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ActivityRest.class);

    @Autowired
    ActivitiService activitiService;

    /**
     * 初始化流程信息.
     *
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("init")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deployFlow() {
        LOGGER.info("开始调用/activity/init");
        BaseResponse<String> response = new BaseResponse<>();
        /*activitiService.deployFlow("加班流程", "over/over");
        activitiService.deployFlow("忘打卡流程", "punch/punch");
        activitiService.deployFlow("外出流程", "out/out");
        activitiService.deployFlow("请假流程", "vacation/vacation");
        activitiService.deployFlow("出差流程", "trip/trip");
        activitiService.deployFlow("通用流程", "common/common");*/
        activitiService.deployFlow("下载流程", "down/down");
        activitiService.deployFlow("付款申请流程", "payment/payment");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("初始化成功");
        LOGGER.info("调用完成/activity/init");
        return response;
    }

    /**
     * 获取所有流程定义.
     *
     * @return 流程定义
     * @author 彭鹏
     */
    @GET
    @Path("processDefinition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyProcessDefinition> getAllProcessDefinition() {
        LOGGER.info("开始调用/activity/processDefinition");
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<MyProcessDefinition> myProcessDefinitionList = new ArrayList<>();
        List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createProcessDefinitionQuery()//创建一个流程定义查询
                .orderByProcessDefinitionVersion().asc()//按照版本的升序排列
                .list(); //返回一个集合列表，封装流程定义

        Map<String, ProcessDefinition> map = new LinkedHashMap<>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd: list) {
                map.put(pd.getKey(), pd);
            }
        }
        List<ProcessDefinition> pdList = new ArrayList<>(map.values());

        if (pdList != null && pdList.size() > 0) {
            for (ProcessDefinition processDefinition : pdList) {
                MyProcessDefinition myProcessDefinition = new MyProcessDefinition();
                myProcessDefinition.setId(processDefinition.getId());
                myProcessDefinition.setName(processDefinition.getName());
                myProcessDefinition.setVersion(processDefinition.getVersion());
                myProcessDefinition.setKey(processDefinition.getKey());
                myProcessDefinitionList.add(myProcessDefinition);
                LOGGER.info("流程定义ID:" + processDefinition.getId()); //流程定义的key+版本+随机生成数
                LOGGER.info("流程定义名称:" + processDefinition.getName()); //对应HelloWorld.bpmn文件中的name属性值
                LOGGER.info("流程定义的key:" + processDefinition.getKey()); //对应HelloWorld.bpmn文件中的id属性值
                LOGGER.info("流程定义的版本:" + processDefinition.getVersion()); //当流程定义的key值相同的情况下，版本升级，默认从1开始
                LOGGER.info("资源名称bpmn文件:" + processDefinition.getResourceName());
                LOGGER.info("资源名称png文件:" + processDefinition.getDiagramResourceName());
                LOGGER.info("部署对象ID:" + processDefinition.getDeploymentId());
                LOGGER.info("################################");
            }
        }
        LOGGER.info("调用完成/activity/processDefinition");
        return myProcessDefinitionList;
    }
}
