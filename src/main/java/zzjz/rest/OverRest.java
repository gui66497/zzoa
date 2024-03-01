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
import zzjz.bean.Over;
import zzjz.bean.OverRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.mapper.OverMapper;
import zzjz.service.ActivitiService;
import zzjz.service.CalendarService;
import zzjz.service.CommonService;
import zzjz.service.OverService;
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
 * @version 2017年05月09日16:46
 * @ClassName: OverRest
 * @Description: 加班处理类
 */
@Component
@Path("/over")
public class OverRest {

    private final static Logger LOGGER = LoggerFactory.getLogger(OverRest.class);

    @Autowired
    OverService overService;

    @Autowired
    OverMapper overMapper;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    CommonService commonService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    StaffService staffService;

    @Autowired
    CalendarService calendarService;

    /**
     * 分页获取加班信息.
     *
     * @param request 请求信息
     * @return 加班列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Over> getOverList(OverRequest request) throws ParseException {
        LOGGER.info("开始调用/over/page");
        BaseResponse<Over> response = new BaseResponse<>();
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        request.setUserId(servletRequest.getSession().getAttribute("staffId").toString());
        Page<Over> overList = overService.getOverList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) overList.getTotal());
        response.setData(overList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/over/page");
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
        LOGGER.info("开始调用/over/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/over/getId");
        return integer;
    }

    /**
     * 保存加班信息.
     *
     * @param over    加班信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addOver(Over over, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/over/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        over.setUserId(over.getUserId() == null ? staffId : over.getUserId());
        over.setUserName(over.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : over.getUserName());
        //判断当天是否已经存在加班申请 若已存在则不允许
        OverRequest request = new OverRequest();
        request.setOverDate(over.getOverDate());
        request.setUserId(over.getUserId());
        List<Over> existOverList = overMapper.getOverPageList(request);
        if (existOverList.size() > 0) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("检测到当天已存在加班申请，无法重复申请！");
            return response;
        }
        String actId = over.getActId();
        if (actId == null) {
            actId = overService.addOverInfo(over);
        } else {
            actId = overService.updateOverInfo(over);
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
                boolean isHoliday = calendarService.isHoliDay(over.getOverDate());
                String condition;
                if (isHoliday) {
                    condition = "isHoliday=='1'";
                } else {
                    condition = "isHoliday=='0'";
                }
                staffList = activitiService.getNextLeaderByRole(staffId, actId, true, "over", condition);
            }

            response.setData(staffList);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else if ("error".equals(actId)) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("非普通员工不能申请加班！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/over/save");
        return response;
    }

    /**
     * 提交加班信息.
     *
     * @param over 加班信息
     * @param user 用户
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> submitOver(Over over, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/over/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = overService.submitOverTask(over, user, staffId);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/over/submit");
        return response;
    }

    /**
     * 审批加班信息.
     *
     * @param over   加班信息
     * @param user   用户
     * @param msg    msg
     * @param reason 原因
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approveOver(Over over, @PathParam("user") String user,
                                            @PathParam("msg") String msg, @PathParam("reason") String reason) throws ParseException {
        LOGGER.info("开始调用/over/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = overService.approveOverTask(over, user, msg, reason);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }
        LOGGER.info("调用完成/over/approve");
        return response;
    }

    /**
     * 审批并结束加班信息.
     *
     * @param over 加班信息
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Over over) throws ParseException {
        LOGGER.info("开始调用/over/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = overService.terminationOverTask(over);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("结束成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("结束异常！");
        }
        LOGGER.info("调用完成/over/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param over     加班实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Over over, @PathParam("departId") long departId, @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/over/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> list = overService.getNextUser(over, departId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/over/getNextUser");
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
    public BaseResponse<String> deleteOverInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/over/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = overService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/over/delete");
        return response;
    }

    /**
     * 撤销事项.
     *
     * @param actId actId
     * @return 结果
     */
    @DELETE
    @Path("/{actId}/revoke")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> revokeOverInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/over/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = overService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/over/revoke");
        return response;
    }

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出加班信息.
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
        LOGGER.info("开始调用/over/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Over over = overService.getOverInfoById(id);
        Map<String, Object> map = overService.getOverMapList(over);

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/加班_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = over.getUserName() + "_加班_" + id + "_" + dateId + "_export.xls";
            String pdfName = over.getUserName() + "_加班_" + id + "_" + dateId + "_export.pdf";
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
            over.setStatus("97");
            overService.updateOverInfo(over);
            return FileUtils.readFileToByteArray(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/over/export");
        return new byte[0];
    }

    /**
     * 忽略加班提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/over/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = overService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略加班信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略加班信息异常");
        }
        LOGGER.info("调用完成/over/ignore");
        return response;
    }
}
