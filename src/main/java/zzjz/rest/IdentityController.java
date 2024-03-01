package zzjz.rest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.ActIdGroup;
import zzjz.bean.BaseResponse;
import zzjz.bean.GroupRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.StaffGroupRequest;
import zzjz.service.ActivitiService;
import zzjz.service.StaffService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/5/4 16:37
 * @ClassName: IdentityController
 * @Description: 用户、组控制器
 */
@Component
@Path("identity")
public class IdentityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityController.class);

    @Autowired
    IdentityService identityService;

    @Autowired
    ActivitiService activitiService;

    @Context
    HttpServletRequest httpServletRequest;

    @Autowired
    StaffService staffService;

    /**
     * 内置角色 对应管理员 普通用户 人事 一级领导 二级领导 三级领导
     */
    //private static final String[] INNER_GROUPS = {"admin", "user", "hr", "deptLeader"};
    private static final String[] INNER_GROUPS = {"admin", "user", "hr", "firstLeader", "secondLeader", "thirdLeader"};

    private static final String[] INNER_USERS = {"admin", "kafeitu", "hruser", "leaderuser"};

    /**
     * 角色列表.
     *
     * @return ActIdGroup
     */
    @GET
    @Path("group/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<ActIdGroup> groupList() {
        LOGGER.info("开始调用/identity/group/list");
        BaseResponse<ActIdGroup> response = new BaseResponse<>();
        List<ActIdGroup> groupList = activitiService.getActIdGroupList();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("获取角色列表成功！");
        response.setData(groupList);
        LOGGER.info("调用完成/identity/group/list");
        return response;
    }

    /**
     * 保存Group.
     *
     * @param groupRequest 请求信息
     * @return 结果
     */
    @POST
    @Path("group/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> saveGroup(GroupRequest groupRequest) {
        LOGGER.info("开始调用/identity/group/save");
        BaseResponse<String> response = new BaseResponse<>();
        String groupId = groupRequest.getGroupId();
        String groupName = groupRequest.getGroupName();
        String type = groupRequest.getGroupType();
        Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if (group == null) {
            group = identityService.newGroup(groupId);
        }
        group.setName(groupName);
        group.setType(type);
        identityService.saveGroup(group);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("保存角色[" + groupName + "]成功！");
        LOGGER.info("调用完成/identity/group/save");
        return response;
    }

    /**
     * 删除Group.
     *
     * @param groupId 组id
     * @return 结果
     */
    @DELETE
    @Path("group/delete/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteGroup(@PathParam("groupId") String groupId) {
        LOGGER.info("开始调用/identity/group/delete/" + groupId);
        BaseResponse<String> response = new BaseResponse<>();
        if (ArrayUtils.contains(INNER_GROUPS, groupId)) {
            response.setMessage("角色[" + groupId + "]属于固定数据不可删除!");
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            return response;
        }
        if (StringUtils.isBlank(groupId)) {
            response.setMessage("角色id不正确！");
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            return response;
        }
        identityService.deleteGroup(groupId);
        response.setMessage("成功删除角色[" + groupId + "]");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/identity/group/delete");
        return response;
    }

    /**
     * 用户设置所属组.
     *
     * @param request 员工id,组id数组
     * @return 结果
     */
    @POST
    @Path("group/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> groupForUser(StaffGroupRequest request) {
        LOGGER.info("开始调用/identity/group/set");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = request.getStaffId();
        if (org.apache.commons.lang.StringUtils.isBlank(staffId)) {
            response.setMessage("staffId为空！");
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            return response;
        }
        List<String> groupIdList = request.getGroupIdList();
        List<Group> groupInDb = identityService.createGroupQuery().groupMember(staffId).list();
        for (Group group : groupInDb) {
            identityService.deleteMembership(staffId, group.getId());
        }
        if (groupIdList != null) {
            for (String groupId : groupIdList) {
                identityService.createMembership(staffId, groupId);
            }
        }
        response.setMessage("设置角色信息成功！");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/identity/group/set");
        return response;
    }

    /**
     * @return String
     */
    @GET
    @Path("staff/syn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> synStaffToAct() {
        LOGGER.info("开始调用/identity/staff/syn");
        BaseResponse<String> response = new BaseResponse<>();
        staffService.synStaffToAct();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("同步用户成功");
        LOGGER.info("调用完成/identity/staff/syn");
        return response;
    }

}
