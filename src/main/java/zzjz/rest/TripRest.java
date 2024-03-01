package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.github.pagehelper.Page;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import zzjz.bean.Trip;
import zzjz.bean.TripRequest;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
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
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月13日15:17
 * @ClassName: TripRest
 * @Description: 出差信息
 */
@Component
@Path("trip")
public class TripRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripRest.class);

    @Autowired
    TripService tripService;

    @Autowired
    CommonService commonService;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Autowired
    StaffService staffService;

    /**
     * 分页获取出差信息.
     *
     * @param request request
     * @return 出差列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Trip> getTripPage(TripRequest request) throws ParseException {
        LOGGER.info("开始调用/trip/page");
        BaseResponse<Trip> response = new BaseResponse<>();
        request.setUserId(servletRequest.getSession().getAttribute("staffId").toString());

        //时区转换
        /*if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(DateUtil.getAttendanceTime(request.getStartTime()) + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(DateUtil.getAttendanceTime(request.getEndTime()) + " 23:59:59");
        }*/

        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        Page<Trip> tripList = tripService.getTripListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) tripList.getTotal());
        response.setData(tripList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/trip/page");
        return response;
    }

    /**
     * 根据日期（时间模块）计算出差信息.
     *
     * @param trip    出差信息
     * @param headers headers
     * @return 出差信息
     */
    @Path("init")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Trip> getTripInfo(Trip trip, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/trip/init");
        BaseResponse<Trip> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        trip = tripService.initTripInfo(trip, staffId);
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        response.setData(tripList);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("假期查询成功！");
        LOGGER.info("调用完成/trip/init");
        return response;
    }

    /**
     * 根据日期（时分）计算出差信息.
     *
     * @param trip    出差信息
     * @param headers headers
     * @return 出差信息
     */
    @Path("forInit")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Trip> getForTripInfo(Trip trip, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/trip/forInit");
        BaseResponse<Trip> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        trip = tripService.initForTripInfo(trip, staffId);
        List<Trip> tripList = new ArrayList<>();
        tripList.add(trip);
        response.setData(tripList);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("假期查询成功！");
        LOGGER.info("调用完成/trip/forInit");
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
        LOGGER.info("开始调用/trip/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/trip/getId");
        return integer;
    }

    /**
     * 获取出差信息.
     *
     * @param id 主键
     * @return 出差信息
     */
    @Path("{id}/load")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Trip getTripInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/trip/" + id + "/load");
        Trip trip = tripService.getTripInfoById(id);
        LOGGER.info("调用完成/trip/load");
        return trip;
    }

    /**
     * 保存出差申请.
     *
     * @param trip    出差信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> addTrip(Trip trip, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/trip/save");
        BaseResponse<Staff> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        trip.setUserId(trip.getUserId() == null ? staffId : trip.getUserId());
        trip.setUserName(trip.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : trip.getUserName());

        String actId = trip.getActId();
        if (actId == null) {
            actId = tripService.addTripInfo(trip);
        } else {
            tripService.updateTripInfo(trip);
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
                staffList = activitiService.getNextLeaderByRole(staffId, actId, true, "trip", "");
            }
            response.setData(staffList);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/trip/save");
        return response;
    }

    /**
     * 销差.
     *
     * @param trip    出差信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("forTrip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> forTrip(Trip trip, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/trip/forTrip");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        trip.setUserId(trip.getUserId() == null ? staffId : trip.getUserId());
        trip.setUserName(trip.getUserName() == null ? servletRequest.getSession().getAttribute("name").toString() : trip.getUserName());
        boolean rs = tripService.forTrip(trip);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("销差成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("销差异常！");
        }
        LOGGER.info("调用完成/trip/forTrip");
        return response;
    }

    /**
     * 人事修改人员出差信息.
     *
     * @param trip    出差信息
     * @param headers headers
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("hrSave")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> hrSave(Trip trip, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/trip/hrSave");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.hrSave(trip);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("人事修改人员出差信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("人事修改人员出差信息异常！");
        }
        LOGGER.info("调用完成/trip/hrSave");
        return response;
    }

    /**
     * 提交出差申请.
     *
     * @param user 用户
     * @param trip 出差信息
     * @return 结果
     * @throws ParseException ParseException
     * @author 彭鹏
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> saveAndSubmit(Trip trip, @PathParam("user") String user) throws ParseException {
        LOGGER.info("开始调用/trip/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = tripService.submitTripTask(trip, user, staffId);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }
        LOGGER.info("调用完成/trip/submit");
        return response;
    }

    /**
     * 审批出差信息.
     *
     * @param trip   出差信息
     * @param user   处理人信息
     * @param msg    操作
     * @param reason 原因
     * @return 结果
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approveSave(Trip trip, @PathParam("user") String user,
                                            @PathParam("msg") String msg, @PathParam("reason") String reason) {
        LOGGER.info("开始调用/trip/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.approveTripTask(trip, user, msg, reason);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }
        LOGGER.info("调用完成/trip/approve");
        return response;
    }

    /**
     * 审批并结束出差信息.
     *
     * @param trip 出差信息
     * @return 结果
     */
    @POST
    @Path("termination")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> termination(Trip trip) {
        LOGGER.info("开始调用/trip/termination");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.terminationTripTask(trip);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("结束成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("结束异常！");
        }
        LOGGER.info("调用完成/trip/termination");
        return response;
    }

    /**
     * 获取下一级处理人.
     *
     * @param trip     出差实体
     * @param departId 部门id
     * @param msg      msg
     * @return 员工信息
     * @throws ParseException ParseException
     */
    @POST
    @Path("/{msg}/{departId}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Staff> getNextUser(Trip trip, @PathParam("departId") long departId, @PathParam("msg") String msg) throws ParseException {
        LOGGER.info("开始调用/trip/" + msg + "/" + departId + "/getNextUser");
        BaseResponse<Staff> response = new BaseResponse<>();
        List<Staff> list = tripService.getNextUser(trip, departId, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/trip/getNextUser");
        return response;
    }

    /**
     * 获取流程图片.
     *
     * @param actId 流程id
     * @return 路径
     */
    @GET
    @Path("/getImage/{actId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getActImg(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/trip/getImage/" + actId);
        String path;
        String relativePath;
        String httpPath = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" +
                servletRequest.getServerPort() + servletRequest.getContextPath();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = taskService.createTaskQuery().processInstanceId(actId).singleResult();
        if (task == null) {
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(actId).singleResult();
            String pKey = hpi.getProcessDefinitionKey();
            InputStream stream = TripRest.class.getClassLoader().getResourceAsStream("diagrams/" + pKey + "/" + pKey + ".png");
            relativePath = "\\images\\activiti\\" + pKey + ".png";
            path = servletRequest.getRealPath("") + relativePath;
            File file = new File(path);
            try {
                FileUtils.copyInputStreamToFile(stream, file);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(stream);
            }
            httpPath += "/images/activiti/" + pKey + ".png";
            return httpPath;
        }

        //流程定义
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        //正在活动节点
        List<String> activeActiviyIds = runtimeService.getActiveActivityIds(task.getExecutionId());

        ProcessDiagramGenerator pdg = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        //生成流图片
        InputStream inputStream = pdg.generateDiagram(bpmnModel, "PNG", activeActiviyIds, activeActiviyIds,
                "宋体", "宋体", "宋体",
                processEngine.getProcessEngineConfiguration().getProcessEngineConfiguration().getClassLoader(), 1.0);
        httpPath += "/images/activiti/activiti_" + actId + ".png";
        //生成本地图片
        try {
            relativePath = "\\images\\activiti\\activiti_" + actId + ".png";
            path = servletRequest.getRealPath("") + relativePath;
            LOGGER.info(path);
            File file = new File(path);
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        LOGGER.info("调用完成/trip/getImage");
        return httpPath;
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
    public BaseResponse<String> deleteTripInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/trip/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/trip/delete");
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
    public BaseResponse<String> revokeTripInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/trip/" + actId + "/revoke");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.revokeRecordByActId(actId);
        if (rs) {
            response.setMessage("撤销成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("撤销失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/trip/revoke");
        return response;
    }

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出出差信息.
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
        LOGGER.info("开始调用/trip/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Trip trip = tripService.getTripInfoById(id);
        Map<String, Object> map = tripService.getTripMapList(trip);

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/出差_template.xls");
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = trip.getUserName() + "_出差_" + id + "_" + dateId + "_export.xls";
            String pdfName = trip.getUserName() + "_出差_" + id + "_" + dateId + "_export.pdf";
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
            response.addHeader("filename", URLEncoder.encode(pdfName, "utf-8"));
            File pdfFile = new File(pdfFileName);

            //状态更新97
            trip.setStatus("97");
            tripService.updateTripInfo(trip);
            return FileUtils.readFileToByteArray(pdfFile);
            /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            response.addHeader("filename", URLEncoder.encode(id + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
            return bos.toByteArray();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/trip/export");
        return new byte[0];
    }

    /**
     * 获取未核销的出差信息.
     *
     * @return 列表
     */
    @POST
    @Path("getTripRemindList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Trip> getTripRemindList() {
        LOGGER.info("开始调用/trip/getTripRemindList");
        BaseResponse<Trip> response = new BaseResponse<>();
        List<Trip> list = tripService.getTripRemindList();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        LOGGER.info("调用完成/trip/getTripRemindList");
        return response;
    }

    /**
     * 忽略出差提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/trip/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = tripService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略出差信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略出差信息异常");
        }
        LOGGER.info("调用完成/trip/ignore");
        return response;
    }

}
