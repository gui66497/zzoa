package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.Process;

import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年11月03日10:08
 * @ClassName: SpecialActivitiService
 * @Description: 特殊工作流服务接口
 */
@Service
public interface SpecialActivitiService {

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
     * 审批流程.
     *
     * @param actId  流程ID request
     * @param actTyp 流程类型
     * @param map    参数 request
     * @param reason reason
     * @return 结果
     */
    boolean approveTask(String actId, String actTyp, String reason, Map<String, Object> map);

    /**
     * 获取下一处理人.
     *
     * @param actId          actId
     * @param actTyp         actTyp
     * @param msg            msg
     * @param conditionType  conditionType
     * @param conditionValue conditionValue
     * @return List
     */
    List<Process> getNextPaymentList(String actId, String actTyp, String msg, String conditionType, int conditionValue);

    /**
     * 退回到上一节点.
     *
     * @param id  id
     * @param map map
     * @return Boolean
     */
    Boolean backToUpper(String id, Map<String, Object> map);

    /**
     * 退回到指定节点.
     *
     * @param id      id
     * @param taskKey taskKey
     * @return Boolean
     */
    Boolean backToAppoint(String id, String taskKey);

}
