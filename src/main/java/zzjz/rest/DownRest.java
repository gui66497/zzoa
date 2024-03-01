package zzjz.rest;

import com.github.pagehelper.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Down;
import zzjz.bean.DownRequest;
import zzjz.bean.Resource;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;
import zzjz.service.DownService;
import zzjz.service.ResourceService;
import zzjz.service.StaffService;
import zzjz.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/9/4 9:57
 * @ClassName: DownRest
 * @Description: 下载管理rest
 */
@Component
@Path("/down")
public class DownRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(DownRest.class);

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    CommonService commonService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    StaffService staffService;

    @Autowired
    DownService downService;

    @Autowired
    ResourceService resourceService;

    /**
     * 分页获取下载信息.
     *
     * @param request request
     * @return 下载信息列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Down> getDownList(DownRequest request) throws ParseException {
        LOGGER.info("开始调用/down/page");
        BaseResponse<Down> response = new BaseResponse<>();
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        request.setUserId(servletRequest.getSession().getAttribute("staffId").toString());
        Page<Down> downPage = downService.getDownPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) downPage.getTotal());
        response.setData(downPage);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/down/page");
        return response;
    }

    /**
     * 获取id.
     *
     * @return id
     */
    @Path("/getId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Integer getInitId() {
        LOGGER.info("开始调用/down/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/down/getId");
        return integer;
    }

    /**
     * 保存下载信息.
     *
     * @param down    下载信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author fangguitang
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addDown(Down down, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/down/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        down.setUserId(down.getUserId() == null ? staffId : down.getUserId());
        down.setUserName(down.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : down.getUserName());
        String actId = down.getActId();
        if (actId == null) {
            down = downService.addDownInfo(down);
            actId = down.getActId();
        } else {
            downService.updateDownInfo(down);
        }
        if (!StringUtils.isBlank(actId) && !"error".equals(actId)) {
            Staff staff = staffService.getStaffByUserId(staffId);
            //默认部门
            String defaultDepartId = staff.getDepartId();
            //根据部门和员工号，查询角色信息
            String role = staffService.getStaffByDepartAndUser(staffId, Long.parseLong(defaultDepartId));
            List<Staff> staffList = null;
            if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
                staffList = activitiService.getTopStaffList(staffId);
            } else {
                // 根据流程定义的id获取流程的key
                Resource resource = resourceService.queryResourceById(down.getResourceId());
                String processKey = activitiService.getProcessDefinitionKeyByDefId(resource.getActReId());
                staffList = activitiService.getNextLeaderByRole(staffId, actId, true, processKey, "");
            }
            response.setData(staffList);
            List<Object> actIdList = new ArrayList<>();
            actIdList.add(down.getId());
            response.setOtherData(actIdList);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/down/save");
        return response;
    }

    /**
     * 提交下载信息.
     *
     * @param down 下载信息
     * @param user 用户
     * @return 结果
     * @throws ParseException ParseException
     * @author fangguitang
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> submitDown(Down down, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/down/{" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = downService.submitDownTask(down, user, staffId);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("下载信息提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("下载信息提交异常！");
        }
        LOGGER.info("调用完成/down/submit");
        return response;
    }

    /**
     * 审批下载.
     *
     * @param down   下载信息
     * @param user   用户
     * @param msg    msg
     * @param reason 原因
     * @return 结果
     * @throws ParseException ParseException
     * @author fangguitang
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approvePunch(Down down, @PathParam("user") String user, @PathParam("msg") String msg,
                                             @PathParam("reason") String reason) throws ParseException {
        LOGGER.info("开始调用/down/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = downService.approveDownTask(down, user, msg, reason);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/down/approve");
        return response;
    }

    /**
     * 直接通过下载流程信息.
     *
     * @param down 下载信息
     * @return 结果
     * @throws ParseException ParseException
     * @author fangguitang
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Down down) throws ParseException {
        LOGGER.info("开始调用/down/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = downService.terminationDownTask(down);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
            //更新状态
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/down/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param down     下载实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Down down, @PathParam("departId") long departId, @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/down/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        List<Staff> list = downService.getNextUser(down, departId, staffId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/down/getNextUser");
        return response;
    }

    /**
     * 删除事项.
     *
     * @param actId actId
     * @return 结果
     */
    @DELETE
    @Path("/{actId}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteDownInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/down/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = downService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除下载信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除下载信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/down/delete");
        return response;
    }

    /**
     * 撤销.
     *
     * @param actId actId
     * @return 结果
     */
    @DELETE
    @Path("/{actId}/revoke")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> revokeDownInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/down/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = downService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/down/revoke");
        return response;
    }

    /**
     * 忽略下载提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/down/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = downService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略下载信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略下载信息异常");
        }
        LOGGER.info("调用完成/down/ignore");
        return response;
    }
}
