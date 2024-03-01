package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.DepartRole;
import zzjz.bean.GroupDepartInfo;
import zzjz.bean.GroupDepartRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.service.GroupDepartService;
import zzjz.service.StaffService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.List;

/**
 * @author wanghao
 * @version 2017/5/15
 */
@Component
@Path("/groupDepart")
public class GroupDepartRest {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDepartRest.class);

    private String message = "";

    @Autowired
    private GroupDepartService groupDepartService;

    @Autowired
    StaffService staffService;

    /**
     * 根据部门id获取部门组信息.
     *
     * @param departId 部门id
     * @return 部门信息
     */
    @Path("{departId}/getListByDepartId")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GroupDepartInfo getListByDepartId(@PathParam("departId") Long departId) {
        LOGGER.info("开始调用/groupDepart/" + departId + "/getListByDepartId");
        GroupDepartInfo groupDepartInfo = groupDepartService.getGroupListByDepartId(departId);
        LOGGER.info("调用完成/groupDepart/getListByDepartId");
        return groupDepartInfo;
    }

    /**
     * 根据用户ID获取该用户的角色list.
     *
     * @param staffId 用户ID
     * @return 结果
     */
    @Path("{staffId}/getGroupList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<DepartRole> getGroupList(@PathParam("staffId") String staffId) {
        LOGGER.info("开始调用/groupDepart/" + staffId + "/getGroupList");
        BaseResponse<DepartRole> response = new BaseResponse<>();
        List<DepartRole> groupDeparts = groupDepartService.getGroupList(staffId);
        Staff staff = staffService.getStaffByUserId(staffId);
        String defaultDepartId = staff.getDepartId();
        for (DepartRole departRole : groupDeparts) {
            if (defaultDepartId.equals(String.valueOf(departRole.getDepartId()))) {
                departRole.setChecked(true);
                break;
            }
        }
        response.setData(groupDeparts);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/groupDepart/getGroupList");
        return response;
    }

    /**
     * 新增角色信息.
     *
     * @param groupDepartRequest 部门信息
     * @param headers            headers
     * @return 结果
     * @author 彭鹏
     * @throws ParseException ParseException
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addDepart(GroupDepartRequest groupDepartRequest, @Context HttpHeaders headers) throws ParseException {
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        LOGGER.info("开始调用/groupDepart/add");
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = groupDepartService.addGroupDepart(groupDepartRequest);
        if (res) {
            message = "设置角色成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "设置角色失敗!";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        response.setMessage(message);
        LOGGER.info("调用完成/groupDepart/add");
        return response;
    }


}
