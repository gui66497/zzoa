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
import zzjz.bean.Out;
import zzjz.bean.OutRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;
import zzjz.service.OutService;
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
 * @ClassName: OutRest
 * @Description: 外出处理类
 */
@Component
@Path("/out")
public class OutRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(OutRest.class);

    @Autowired
    OutService outService;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    CommonService commonService;

    @Autowired
    ActivitiService activitiService;

    /**
     * 分页获取外出信息.
     *
     * @param request request
     * @return 外出列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Out> getOutList(OutRequest request) throws ParseException {
        LOGGER.info("开始调用/out/page");
        BaseResponse<Out> response = new BaseResponse<>();
        request.setUserId(servletRequest.getSession().getAttribute("staffId").toString());
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }

        Page<Out> outList = outService.getOutList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) outList.getTotal());
        response.setData(outList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/out/page");
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
        LOGGER.info("开始调用/out/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/out/getId");
        return integer;
    }

    /**
     * 保存外出信息.
     *
     * @param out     外出信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addOut(Out out, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/out/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        out.setUserId(out.getUserId() == null ? staffId : out.getUserId());
        out.setUserName(out.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : out.getUserName());
        String actId = out.getActId();
        if (actId == null) {
            actId = outService.addOutInfo(out);
        } else {
            outService.updateOutInfo(out);
        }

        if (!StringUtils.isBlank(actId)) {
            List<Staff> staffList = activitiService.getNextLeaderByRole(staffId, actId, true, "out", "");
            response.setData(staffList);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/out/save");
        return response;
    }

    /**
     * 提交外出信息.
     *
     * @param user 用户
     * @param out  外出信息
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> submitOut(Out out, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/out/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.submitOutTask(out, user);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/out/submit");
        return response;
    }

    /**
     * 审批外出信息.
     *
     * @param out    外出信息
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
    public BaseResponse<String> approveOut(Out out, @PathParam("user") String user,
                                           @PathParam("msg") String msg, @PathParam("reason") String reason) throws ParseException {
        LOGGER.info("开始调用/out/" + user + "/" + msg + "/" + reason + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.approveOutTask(out, user, msg, reason);

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }
        LOGGER.info("调用完成/out/approve");
        return response;
    }

    /**
     * 审批并结束外出信息.
     *
     * @param out 外出信息
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Out out) throws ParseException {
        LOGGER.info("开始调用/out/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.terminationOutTask(out);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("结束成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("结束异常！");
        }
        LOGGER.info("调用完成/out/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param out      外出实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Out out, @PathParam("departId") long departId, @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/out/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> list = outService.getNextUser(out, departId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/out/getNextUser");
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
    public BaseResponse<String> deleteOutInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/out/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/out/delete");
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
    public BaseResponse<String> revokeOutInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/out/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/out/revoke");
        return response;
    }

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出外出信息.
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
        LOGGER.info("开始调用/out/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Out out = outService.getOutInfoById(id);
        Map<String, Object> map = outService.getOutMapList(out);
        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/外出_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = out.getUserName() + "_外出_" + id + "_" + dateId + "_export.xls";
            String pdfName = out.getUserName() + "_外出_" + id + "_" + dateId + "_export.pdf";
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
            out.setStatus("97");
            outService.updateOutInfo(out);
            return FileUtils.readFileToByteArray(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/out/export");
        return new byte[0];
    }

    /**
     * 忽略外出提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/out/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = outService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略外出信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略外出信息异常");
        }
        LOGGER.info("调用完成/out/ignore");
        return response;
    }
}
