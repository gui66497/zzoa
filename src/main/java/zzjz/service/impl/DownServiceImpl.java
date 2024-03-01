package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Down;
import zzjz.bean.DownRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Resource;
import zzjz.bean.Staff;
import zzjz.mapper.DownMapper;
import zzjz.service.ActivitiService;
import zzjz.service.DownService;
import zzjz.service.ResourceService;
import zzjz.service.SpecialActivitiService;
import zzjz.service.StaffService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * DownServiceImpl.
 *
 * @author Administrator
 * @version 2017/9/6 8:36
 */
@Service
public class DownServiceImpl implements DownService {

    @Autowired
    ActivitiService activitiService;

    @Autowired
    DownMapper downMapper;

    @Autowired
    ResourceService resourceService;

    @Autowired
    SpecialActivitiService specialActivitiService;

    @Autowired
    StaffService staffService;

    @Override
    public Page<Down> getDownPage(DownRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Down> pageList = (Page<Down>) downMapper.getDownPageList(request);
        return pageList;
    }

    @Override
    public Down addDownInfo(Down down) {
        String userId = down.getUserId();
        // 根据流程定义的id获取流程的key
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Resource resource = resourceService.queryResourceById(down.getResourceId());
        String processKey = processEngine.getRepositoryService().
                createProcessDefinitionQuery().
                processDefinitionId(resource.getActReId()).
                singleResult().getKey();
        String actId = activitiService.flowStart(processKey, userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        down.setCreateTime(createTime);
        down.setStatus("00");
        down.setActId(actId);

        if (downMapper.selectByPrimaryKey(down.getId()) != null) {
            downMapper.updateByPrimaryKey(down);
        } else {
            downMapper.insert(down);
        }
        return down;
    }

    @Override
    public boolean updateDownInfo(Down down) {
        return downMapper.updateByPrimaryKey(down) > 0;
    }

    @Override
    public boolean submitDownTask(Down down, String user, String staffId) {
        down = downMapper.selectByPrimaryKey(down.getId());
        String actId = down.getActId();
        // 根据流程定义的id获取流程的key
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Resource resource = resourceService.queryResourceById(down.getResourceId());
        String processKey = processEngine.getRepositoryService().
                createProcessDefinitionQuery().
                processDefinitionId(resource.getActReId()).
                singleResult().getKey();
        boolean rs = activitiService.submitTask(actId, user, processKey);
        if (rs) {
            down.setStatus("01");
            rs = downMapper.updateByPrimaryKey(down) > 0;
        }
        if (activitiService.isEndProcess(down.getActId())) {
            down.setStatus("99");
            rs = downMapper.updateByPrimaryKey(down) > 0;
        }
        return rs;
    }

    @Override
    public boolean deleteRecordByActId(String actId) {
        return downMapper.deleteByActId(actId) > 0 && activitiService.deleteActinstByActId(actId);
    }

    @Override
    public boolean revokeRecordByActId(String actId) {
        return downMapper.deleteByActId(actId) > 0 && activitiService.deleteRecordByActId(actId);
    }

    @Override
    public Down getDownInfoById(int id) {
        return downMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Staff> getNextUser(Down down, long departId, String staffId, String msg) {
        List<Staff> list = new ArrayList<>();
        down = downMapper.selectByPrimaryKey(down.getId());
        String actId = down.getActId();
        if ("agree".equals(msg)) {
            String taskDefinitionKey = activitiService.findTaskById(down.getActId()).getTaskDefinitionKey();
            if (!"secondAudit".equals(taskDefinitionKey)) {
                return new ArrayList<>();
            }
            Resource resource = resourceService.queryResourceById(down.getResourceId());
            if ("1".equals(resource.getIsSecret())) { //涉密，保密办主任
                list = staffService.getDirectorList();
            } else { //不涉密，总经办人员
                list = activitiService.getTopStaffList(staffId);
            }
        } else if ("disagree".equals(msg)) {
            Staff staff = activitiService.getLastProcessor(actId);
            list.add(staff);
        }
        return list;
    }

    @Override
    public boolean approveDownTask(Down down, String user, String msg, String reason) {
        down = downMapper.selectByPrimaryKey(down.getId());
        String actId = down.getActId();
        // 根据流程定义的id获取流程的key
        Resource resource = resourceService.queryResourceById(down.getResourceId());
        String processKey = activitiService.getProcessDefinitionKeyByDefId(resource.getActReId());
        boolean rs;
        if ("agree".equals(msg)) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("msg", msg);
            rs = specialActivitiService.approveTask(actId, "down", reason, map);
        } else {
            rs = activitiService.approveTask(actId, user, processKey, msg, reason);
        }

        if (rs && "disagree".equals(msg)) {
            if (activitiService.isApply(actId)) {
                down.setStatus("03");
            } else {
                down.setStatus("02");
            }
            rs = downMapper.updateByPrimaryKey(down) > 0;
        } else if (activitiService.isEndProcess(down.getActId())) {
            down.setStatus("99");
            rs = downMapper.updateByPrimaryKey(down) > 0;
        }
        return rs;
    }

    @Override
    public boolean terminationDownTask(Down down) {
        down = downMapper.selectByPrimaryKey(down.getId());
        String actId = down.getActId();
        // 根据流程定义的id获取流程的key
        Resource resource = resourceService.queryResourceById(down.getResourceId());
        String processKey = activitiService.getProcessDefinitionKeyByDefId(resource.getActReId());
        boolean rs = activitiService.terminationTask(actId, processKey);
        if (rs) {
            down.setStatus("99");
            rs = downMapper.updateByPrimaryKey(down) > 0;
        }
        return rs;
    }

    @Override
    public boolean ignore(Integer id) {
        Down down = downMapper.selectByPrimaryKey(id);
        down.setStatus("93");
        return downMapper.updateByPrimaryKey(down) > 0;
    }


}
