package zzjz.rest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Group;
import zzjz.bean.ResultCode;
import zzjz.service.GroupService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;

/**
 * @author 房桂堂
 * @version 2017/8/28 13:46
 * @ClassName: GroupRest
 * @Description: 组管理rest
 */
@Component
@Path("group")
public class GroupRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRest.class);

    @Context
    HttpServletRequest httpServletRequest;

    @Autowired
    GroupService groupService;

    /**
     * 获取所有组信息.
     *
     * @return 组信息列表
     */
    @Path("list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Group> getResourceById() {
        LOGGER.info("开始调用/group/list");
        BaseResponse<Group> response = new BaseResponse<>();
        List<Group> groupList = groupService.queryAllGroup();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("组获取成功");
        response.setData(groupList);
        LOGGER.info("调用完成/group/list");
        return response;
    }

    /**
     * 修改组信息.
     *
     * @param group 组
     * @return 结果
     */
    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateGroup(Group group) {
        LOGGER.info("开始调用/group/update");
        BaseResponse<String> response = new BaseResponse<>();
        String newGroupName = group.getGroupName();
        if (StringUtils.isBlank(newGroupName)) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("参数错误,组名不能为空");
            return response;
        }
        Group groupTemp = groupService.queryByName(newGroupName);
        if (groupTemp != null && !Objects.equals(groupTemp.getGroupId(), group.getGroupId())) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("该组名已存在!");
            return response;
        }
        boolean rs = groupService.updateGroup(group);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("修改组信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改组信息失败！");
        }
        LOGGER.info("调用完成/group/update");
        return response;
    }

    /**
     * 新增组.
     *
     * @param group 组
     * @return 结果
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addGroup(Group group) {
        LOGGER.info("开始调用/group/add");
        BaseResponse<String> response = new BaseResponse<>();
        String groupName = group.getGroupName();
        if (StringUtils.isBlank(groupName)) {
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            response.setMessage("参数错误");
            return response;
        }
        Group groupTemp = groupService.queryByName(groupName);
        if (groupTemp != null) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("组名[" + groupName + "]已存在!");
            return response;
        }
        boolean rs = groupService.addGroup(group);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("新增组信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("新增组信息失败！");
        }
        LOGGER.info("调用完成/group/add");
        return response;
    }

    /**
     * 删除组信息.
     *
     * @param groupId 组id
     * @return 结果
     */
    @Path("{groupId}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteGroup(@PathParam("groupId") int groupId) {
        LOGGER.info("开始调用/group/" + groupId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        Group group = groupService.queryByIdWithStaff(groupId);
        if (group == null) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("组不存在!");
            return response;
        }
        if (group.getStaffList() != null && group.getStaffList().size() > 0) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("组下存成员,不能删除!");
            return response;
        }
        boolean res = groupService.deleteByGroupId(groupId);
        if (res) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除组信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除组信息失败！");
        }
        LOGGER.info("调用完成/group/delete");
        return response;
    }

    /**
     * 获取用户的所有分组.
     *
     * @param staffId 用户ID
     * @return 结果
     */
    @Path("{staffId}/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Group> getGroupByStaffId(@PathParam("staffId") String staffId) {
        LOGGER.info("开始调用/group/" + staffId + "/list");
        BaseResponse<Group> response = new BaseResponse<>();
        List<Group> groupList = groupService.queryGroupByStaffId(staffId);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("组获取成功");
        response.setData(groupList);
        LOGGER.info("调用完成/group/list");
        return response;
    }

}
