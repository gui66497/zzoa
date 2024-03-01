package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.github.pagehelper.Page;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Punch;
import zzjz.bean.PunchRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;
import zzjz.service.PunchService;
import zzjz.service.StaffService;
import zzjz.util.DateUtil;
import zzjz.util.Office2PDF;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年05月10日10:05
 * @ClassName: PunchRest
 * @Description: 忘打卡
 */
@Component
@Path("/punch")
public class PunchRest {

    private final static Logger LOGGER = LoggerFactory.getLogger(OverRest.class);

    @Autowired
    PunchService punchService;

    @Autowired
    CommonService commonService;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    StaffService staffService;

    /**
     * 分页获取忘打卡信息.
     *
     * @param request request
     * @return 打卡信息
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Punch> getPunchList(PunchRequest request) throws ParseException {
        LOGGER.info("开始调用/punch/page");
        BaseResponse<Punch> response = new BaseResponse<>();
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }

        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        request.setUserId(staffId);
        Page<Punch> pageList = punchService.getPunchPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) pageList.getTotal());
        response.setData(pageList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/punch/page");
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
        LOGGER.info("开始调用/punch/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/punch/getId");
        return integer;
    }

    /**
     * 保存忘打卡信息.
     *
     * @param punch   忘打卡信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addPunch(Punch punch, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/punch/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        punch.setUserId(punch.getUserId() == null ? staffId : punch.getUserId());
        punch.setUserName(punch.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : punch.getUserName());
        String actId = punch.getActId();
        if (actId == null) {
            actId = punchService.addPunchInfo(punch);
        } else {
            punchService.updatePunchInfo(punch);
        }
        if (!StringUtils.isBlank(actId)) {
            Staff staff = staffService.getStaffByUserId(staffId);
            //默认部门
            String defaultDepartId = staff.getDepartId();
            //根据部门和员工号，查询角色信息
            String role = staffService.getStaffByDepartAndUser(staffId, Long.parseLong(defaultDepartId));
            List<Staff> staffList = null;
            if ("1".equals(defaultDepartId) || "secondLeader".equals(role)) {
                staffList = activitiService.getTopStaffList(staffId);
                response.setResultCode(ResultCode.RESULT_NOTICE);
            } else {
                staffList = activitiService.getNextLeaderByRole(staffId, actId, true, "punch", "");
                response.setResultCode(ResultCode.RESULT_SUCCESS);
            }
            response.setData(staffList);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/punch/save");
        return response;
    }

    /**
     * 提交忘打卡信息.
     *
     * @param punch 忘打卡信息
     * @param user  用户
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> submitPunch(Punch punch, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/punch/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.submitPunchTask(punch, user);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/punch/submit");
        return response;
    }

    /**
     * 提交并告知忘打卡信息.
     *
     * @param punch 忘打卡信息
     * @param user  用户
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/notice")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> noticePunch(Punch punch, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/punch/" + user + "/notice");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.noticePunchTask(punch, user);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("告知成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("告知异常！");
        }
        LOGGER.info("调用完成/punch/notice");
        return response;
    }

    /**
     * 审批忘打卡信息.
     *
     * @param punch  忘打卡信息
     * @param user   处理人
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approvePunch(Punch punch, @PathParam("user") String user,
                                             @PathParam("msg") String msg, @PathParam("reason") String reason) throws ParseException {
        LOGGER.info("开始调用/punch/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.approvePunchTask(punch, user, msg, reason);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }
        LOGGER.info("调用完成/punch/approve");
        return response;
    }

    /**
     * 审批并结束忘打卡信息.
     *
     * @param punch 忘打卡信息
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Punch punch) throws ParseException {
        LOGGER.info("开始调用/punch/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.terminationPunchTask(punch);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("结束成功！");
            //更新状态
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("结束异常！");
        }
        LOGGER.info("调用完成/punch/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param punch    打卡实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Punch punch, @PathParam("departId") long departId, @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/punch/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> list = punchService.getNextUser(punch, departId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/punch/getNextUser");
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
    public BaseResponse<String> deletePunchInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/punch/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/punch/delete");
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
    public BaseResponse<String> revokePunchInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/punch/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/punch/revoke");
        return response;
    }

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出忘打卡信息.
     *
     * @param id       主键
     * @param response response
     * @param data     数据
     * @return 文件
     */
    @POST
    @Path("/{id}/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] export(@PathParam("id") int id, @Context HttpServletResponse response, String data) {
        LOGGER.info("开始调用/punch/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Punch punch = punchService.getPunchInfoById(id);
        Map<String, Object> map = punchService.getPunchMapList(punch);

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/忘打卡_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = punch.getUserName() + "_忘打卡_" + id + "_" + dateId + "_export.xls";
            String pdfName = punch.getUserName() + "_忘打卡_" + id + "_" + dateId + "_export.pdf";
            String excelFileName = attendancePath + excelName;
            String pdfFileName = attendancePath + pdfName;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(excelFileName);
                workbook.write(fos);
            } catch (IOException ee) {
                ee.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
            //excel转换为pdf
            Office2PDF.office2PDF(excelFileName, pdfFileName);
            response.addHeader("filename",  URLEncoder.encode(pdfName, "utf-8"));
            File pdfFile = new File(pdfFileName);

            //状态更新97
            punch.setStatus("97");
            punchService.updatePunchInfo(punch);
            return FileUtils.readFileToByteArray(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/punch/export");
        return new byte[0];
    }

    /**
     * 忽略忘打卡提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/punch/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = punchService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略忘打卡信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略忘打卡信息异常");
        }
        LOGGER.info("调用完成/punch/ignore");
        return response;
    }
}
