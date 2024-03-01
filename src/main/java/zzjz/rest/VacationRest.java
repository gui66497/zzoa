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
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.bean.Vacation;
import zzjz.bean.VacationRequest;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;
import zzjz.service.StaffService;
import zzjz.service.VacationService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日16:39
 * @ClassName: VacationRest
 * @Description: 请假管理
 */
@Component
@Path("vacation")
public class VacationRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VacationRest.class);

    @Autowired
    VacationService vacationService;

    @Autowired
    CommonService commonService;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    StaffService staffService;

    private static final int TWO = 2;

    private static final Double DOU5 = 5.0;

    /**
     * 分页获取假期信息.
     *
     * @param request request
     * @return 假期列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Vacation> getVacationPage(VacationRequest request) throws ParseException {
        LOGGER.info("开始调用/vacation/page");
        BaseResponse<Vacation> response = new BaseResponse<>();
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        request.setUserId(servletRequest.getSession().getAttribute("staffId").toString());
        Page<Vacation> vacationList = vacationService.getVacationListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) vacationList.getTotal());
        response.setData(vacationList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/vacation/page");
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
        LOGGER.info("开始调用/vacation/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/vacation/getId");
        return integer;
    }

    /**
     * 获取假期信息.
     *
     * @param id 主键
     * @return 假期信息
     */
    @Path("{id}/load")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Vacation getVacationInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/vacation/" + id + "/load");
        Vacation vacation = vacationService.getVacationInfoById(id);
        LOGGER.info("调用完成/vacation/load");
        return vacation;
    }

    /**
     * 计算假期信息.
     *
     * @param vacation 请假信息
     * @param headers  headers
     * @return 假期信息
     */
    @Path("init")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Vacation> getVacationInfo(Vacation vacation, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/vacation/init");
        BaseResponse<Vacation> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        List<Vacation> vacationList = new ArrayList<>();
        Staff staff = staffService.getStaffWithDefDepartByUserId(staffId);
        // 调休和年假 20200224 研发中心特殊处理 默认先使用调休再使用年假
        if (staff.getDepartName().contains("研发") && "01".equals(vacation.getLeaveTyp())) {
            //查询剩余调休是否为0
            Double days = vacationService.getLeftOffDays(staffId);
            if (days > 0) {
                vacation.setLeaveDay(null);
                vacation.setBackDate(null);
                vacation.setBackPart(null);
                vacationList.add(vacation);
                response.setData(vacationList);
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("您还有" + days + "天调休，请优先使用调休！");
                return response;
            }
        }
        try {
            vacation = vacationService.initVacationInfo(vacation, staffId);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        vacationList.add(vacation);
        response.setData(vacationList);
        if (vacation.getLeaveDay() != null && vacation.getLeaveDay() == -1.0) {
            vacation.setLeaveDay(null);
            vacation.setBackDate(null);
            vacation.setBackPart(null);
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("剩余假期不足！");
        } else if (vacation.getLeaveDay() != null && vacation.getLeaveDay() == -TWO) {
            vacation.setLeaveDay(null);
            vacation.setBackDate(null);
            vacation.setBackPart(null);
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("试用期期间不能请年假！");
        } else if (vacation.getLeaveDay() != null && vacation.getLeaveDay() >= 0.0) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("假期查询成功！");
        } else {
            vacation.setLeaveDay(null);
            vacation.setBackDate(null);
            vacation.setBackPart(null);
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("开始日期必须是工作日！");
        }
        LOGGER.info("调用完成/vacation/init");
        return response;
    }

    /**
     * 保存请假申请.
     *
     * @param vacation 请假信息
     * @param headers  headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addVacation(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/vacation/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        vacation.setUserId(vacation.getUserId() == null ? staffId : vacation.getUserId());
        vacation.setUserName(vacation.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : vacation.getUserName());
        String actId = vacation.getActId();
        if (actId != null) {
            vacationService.updateVacationInfo(vacation);
        } else {
            actId = vacationService.addVacationInfo(vacation);
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
            } else {
                String condition;
                if (vacation.getLeaveDay() < DOU5) {
                    condition = "day < 5";
                } else {
                    condition = "day >= 5";
                }
                staffList = activitiService.getNextLeaderByRole(staffId, actId, true, "vacation", condition);
            }
            response.setData(staffList);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/vacation/save");
        return response;
    }


    /**
     * 保存请假申请.
     *
     * @param vacation 请假信息
     * @param headers  headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("forVacation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> forVacation(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/vacation/forVacation");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        vacation.setUserId(vacation.getUserId() == null ? staffId : vacation.getUserId());
        vacation.setUserName(vacation.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : vacation.getUserName());
        boolean rs = vacationService.forVacationInfo(vacation);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("销假成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("销假异常！");
        }
        LOGGER.info("调用完成/vacation/forVacation");
        return response;
    }

    /**
     * 提交请假申请.
     *
     * @param vacation 请假信息
     * @param user     用户
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> saveAndSubmit(Vacation vacation, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/vacation/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = vacationService.submitVacationInfo(vacation, user, staffId);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/vacation/submit");
        return response;
    }

    /**
     * 审批请假信息.
     *
     * @param vacation 假期信息
     * @param user     处理人
     * @param msg      操作
     * @param reason   原因
     * @return 结果
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approveSave(Vacation vacation, @PathParam("user") String user,
                                            @PathParam("msg") String msg, @PathParam("reason") String reason) {
        LOGGER.info("开始调用/vacation/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = vacationService.approveVacationTask(vacation, user, msg, reason);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/vacation/approve");
        return response;
    }

    /**
     * 审批并结束请假信息.
     *
     * @param vacation 请假信息
     * @return 结果
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Vacation vacation) {
        LOGGER.info("开始调用/vacation/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = vacationService.terminationVacationTask(vacation);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }
        LOGGER.info("调用完成/vacation/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param vacation 请假实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Vacation vacation, @PathParam("departId") long departId,
                                           @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/vacation/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> list = vacationService.getNextUser(vacation, departId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/vacation/getNextUser");
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
    public BaseResponse<String> deleteVacationInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/vacation/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = false;
        try {
            rs = vacationService.deleteRecordByActId(actId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rs) {
            response.setMessage("删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/vacation/delete");
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
    public BaseResponse<String> revokeVacationInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/vacation/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = vacationService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/vacation/revoke");
        return response;
    }

    /**
     * 获取剩余调休天数.
     *
     * @return 天数
     */
    @Path("/getLeftOffDays")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Double getLeftOffDays() {
        LOGGER.info("开始调用/vacation/getLeftOffDays");
        Double days = vacationService.getLeftOffDays(servletRequest.getSession().getAttribute("staffId").toString());
        LOGGER.info("调用完成/vacation/getLeftOffDays");
        return days;
    }

    /**
     * 获取剩余年假天数.
     *
     * @return 天数
     */
    @Path("/getLeftAnnualDays")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Double getLeftAnnualDays() {
        LOGGER.info("开始调用/vacation/getLeftAnnualDays");
        Double days = vacationService.getLeftAnnualDays(servletRequest.getSession().getAttribute("staffId").toString());
        LOGGER.info("调用完成/vacation/getLeftAnnualDays");
        return days;
    }

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出请假信息.
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
        LOGGER.info("开始调用/vacation/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Vacation vacation = vacationService.getVacationInfoById(id);
        Map<String, Object> map = vacationService.getVacationMapList(vacation);

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/请假_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = vacation.getUserName() + "_请假_" + id + "_" + dateId + "_export.xls";
            String pdfName = vacation.getUserName() + "_请假_" + id + "_" + dateId + "_export.pdf";
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
            vacation.setStatus("97");
            vacationService.updateVacationInfo(vacation);
            return FileUtils.readFileToByteArray(pdfFile);
            /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            response.addHeader("filename", URLEncoder.encode(id + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
            return bos.toByteArray();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/vacation/export");
        return new byte[0];
    }

    /**
     * 忽略请假提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/vacation/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = vacationService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略请假信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略请假信息异常");
        }
        LOGGER.info("调用完成/vacation/ignore");
        return response;
    }
}
