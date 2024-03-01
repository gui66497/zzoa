package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.github.pagehelper.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.AttendanceRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.Bean;
import zzjz.bean.Mail;
import zzjz.bean.MainDesk;
import zzjz.bean.Out;
import zzjz.bean.Over;
import zzjz.bean.Punch;
import zzjz.bean.PunchedCard;
import zzjz.bean.PunchedInfo;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.bean.TbMail;
import zzjz.bean.Trip;
import zzjz.bean.Vacation;
import zzjz.service.ActivitiService;
import zzjz.service.AttendanceService;
import zzjz.service.MailService;
import zzjz.service.OutService;
import zzjz.service.OverService;
import zzjz.service.PunchService;
import zzjz.service.StaffService;
import zzjz.service.TripService;
import zzjz.service.VacationService;
import zzjz.service.ZkemService;
import zzjz.task.TaskTest;
import zzjz.util.DateUtil;
import zzjz.util.MD5Util;
import zzjz.util.MailUtil;
import zzjz.util.Office2PDF;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
import java.net.ConnectException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:18
 * @ClassName: AttendanceRest
 * @Description: 考勤信息管理
 */
@Component
@Path("/attendance")
public class AttendanceRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AttendanceRest.class);

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    StaffService staffService;

    @Autowired
    ZkemService zkemService;

    @Autowired
    TaskTest taskTest;

    @Context
    HttpServletRequest servletRequest;

    @Value("${punchIp}")
    String punchIp;

    @Autowired
    TripService tripService;

    @Autowired
    VacationService vacationService;

    @Autowired
    OverService overService;

    @Autowired
    OutService outService;

    @Autowired
    PunchService punchService;

    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    MailService mailService;

    /**
     * 分页获取考勤信息.
     *
     * @param request 请求信息
     * @return 考勤列表
     * @throws ParseException ParseException
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Attendance> getAttendanceList(AttendanceRequest request) throws ParseException {
        LOGGER.info("开始调用/attendance/page");
        BaseResponse<Attendance> response = new BaseResponse<>();
        Page<Attendance> attendanceList = attendanceService.getAttendanceList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) attendanceList.getTotal());
        response.setData(attendanceList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/attendance/page");
        return response;
    }

    /**
     * 上传考勤信息.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        LOGGER.info("开始调用/attendance/upload");
        BaseResponse<String> response = new BaseResponse<>();

        ImportParams importParams = new ImportParams();
        List<AttendanceExcel> res = null;
        try {
            res = ExcelImportUtil.importExcel(fileInputStream, AttendanceExcel.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert res != null;
        for (AttendanceExcel attendance : res) {
            attendance = attendanceService.updateAttentionInfo(attendance);
            attendanceService.checkAttendanceData(attendance);
        }

        boolean result = attendanceService.addAttendanceList(res);

        if (result) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("导入成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("导入失败！");
        }
        LOGGER.info("调用完成/attendance/upload");
        return response;
    }


    /**
     * 添加考勤信息.
     *
     * @param punchedInfo 忘打卡信息
     * @param headers     HttpHeaders
     * @return 结果
     */
    @POST
    @Path("addPunchedCard")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addPunchedCard(PunchedInfo punchedInfo, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/attendance/addPunchedCard");
        BaseResponse<String> response = new BaseResponse<>();
        List<AttendanceExcel> attendanceExcelList = new ArrayList<>();

        for (PunchedCard punchedCard : punchedInfo.getLogs()) {
            String date = punchedCard.getYear() + "-" + punchedCard.getMonth() + "-" + punchedCard.getDay();
            String time = punchedCard.getHour() + ":" + punchedCard.getMinute();
            if (attendanceExcelList.size() > 0) {
                for (AttendanceExcel attendanceExcel : attendanceExcelList) {
                    if (attendanceExcel.getUserId().equals(punchedCard.getUid()) && attendanceExcel.getCheckDate().equals(date)) {
                        if (time.compareTo(attendanceExcel.getSignTime()) < 0) {
                            attendanceExcel.setSignTime(time);
                        }
                        if (time.compareTo(attendanceExcel.getLeaveTime()) > 0) {
                            attendanceExcel.setLeaveTime(time);
                        }
                        Staff staff = staffService.getStaffByUserId(punchedCard.getUid());
                        if (staff != null) {
                            attendanceExcel.setUserName(staff.getName());
                            attendanceExcel.setDepart(staff.getDepartId());
                        }
                    } else {
                        AttendanceExcel attendance = new AttendanceExcel();
                        attendance.setUserId(punchedCard.getUid());
                        attendance.setCheckDate(date);
                        attendance.setSignTime(time);
                        attendance.setLeaveTime(time);
                        attendanceExcelList.add(attendance);
                    }
                }
            } else {
                AttendanceExcel attendance = new AttendanceExcel();
                attendance.setUserId(punchedCard.getUid());
                attendance.setCheckDate(date);
                attendance.setSignTime(time);
                attendance.setLeaveTime(time);
                attendanceExcelList.add(attendance);
            }
        }
        for (AttendanceExcel attendance : attendanceExcelList) {
            attendance = attendanceService.updateAttentionInfo(attendance);
            attendanceService.checkAttendanceData(attendance);
        }

        boolean result = attendanceService.addAttendanceList(attendanceExcelList);

        if (result) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("导入成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("导入失败！");
        }
        LOGGER.info("调用完成/attendance/upload");
        return response;
    }


    /**
     * 录入事项.
     *
     * @param request 请求信息
     * @param headers 头信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("input")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputAtt(AttendanceRequest request, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/attendance/input");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputAtt(request);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("插入成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("插入失败！");
        }
        LOGGER.info("调用完成/attendance/input");
        return response;
    }

    /**
     * 清除事项信息.
     *
     * @param request 请求信息
     * @param headers 头信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("deleteAttInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteAttInfo(AttendanceRequest request, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/attendance/deleteAttInfo");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.deleteAttInfo(request);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        LOGGER.info("调用完成/attendance/deleteAttInfo");
        return response;
    }

    /**
     * 添加签到信息.
     *
     * @param request 请求信息
     * @param headers 头信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("inputSignTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputSignTime(AttendanceRequest request, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/attendance/inputSignTime");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputSignTime(request);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("添加成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("添加失败！");
        }
        LOGGER.info("调用完成/attendance/inputSignTime");
        return response;
    }

    /**
     * 添加签退信息.
     *
     * @param request 请求信息
     * @param headers 头信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("inputLeaveTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputLeaveTime(AttendanceRequest request, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/attendance/inputLeaveTime");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputLeaveTime(request);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("添加成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("添加失败！");
        }
        LOGGER.info("调用完成/attendance/inputLeaveTime");
        return response;
    }

    /**
     * 获取考勤异常提醒信息.
     *
     * @return 列信息
     * @throws ParseException ParseException
     */
    @Path("/remindList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Bean> getRemindList() throws ParseException {
        LOGGER.info("开始调用/attendance/remindList");
        BaseResponse<Bean> response = new BaseResponse<>();
        List<Bean> beanList = new ArrayList<Bean>();
        String userId = servletRequest.getSession().getAttribute("staffId").toString();
        Staff staff = staffService.getStaffByUserId(userId);
        //判断是否需要考勤异常提醒
        if (staff.getIsRemind() == 1) {
            List<Attendance> remindList = attendanceService.getRemindList(userId); //转正
            for (Attendance attendance : remindList) {
                Bean bean = new Bean();
                bean.setCol(attendance.getCheckDate() + "的考勤异常，原因：" + attendance.getErrorDesc());
                beanList.add(bean);
            }
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(beanList);
        LOGGER.info("调用完成/attendance/remindList");
        return response;
    }

    /**
     * 考勤补录.
     *
     * @param dateStr 日期
     * @return 结果
     */
    @Path("/recordAgain/{date}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> recordAgain(@PathParam("date") String dateStr) {
        LOGGER.info("开始调用/attendance/recordAgain");
        BaseResponse<String> response = new BaseResponse<>();
        LOGGER.info("date:" + dateStr);
        if (StringUtils.isBlank(dateStr)) {
            response.setMessage("日期错误");
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            return response;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse(dateStr);
            Date today = DateUtil.getTodayZero();
            if (date.getTime() >= today.getTime()) {
                response.setMessage("请选择今日之前的日期");
                response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
                return response;
            }
        } catch (ParseException e) {
            response.setMessage("日期格式错误");
            response.setResultCode(ResultCode.RESULT_BAD_REQUEST);
            e.printStackTrace();
            return response;
        }
        List<Attendance> oldAttendance = attendanceService.getAttendanceByCheckDate(dateStr);
        if (oldAttendance != null && oldAttendance.size() > 0) {
            response.setMessage(dateStr + "的考勤数据已存在！");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }

        List<PunchedCard> punchedCards;
        try {
            punchedCards = zkemService.getOneDayLog(date);
        } catch (ConnectException e) {
            e.printStackTrace();
            response.setMessage("获取考勤机数据失败: " + e.getMessage());
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }

        if (punchedCards == null || punchedCards.size() < 1) {
            response.setMessage("没有找到 " + dateStr + " 当日的考勤数据！");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }

        boolean res = taskTest.punchCardsToAttendance(punchedCards, false);
        if (res) {
            response.setMessage("考勤数据补录成功！");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("考勤数据补录失败！");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/attendance/recordAgain");
        return response;
    }

    /**
     * 通用的导出考勤信息.
     * @param type 考勤种类
     * @param id id
     * @param response response
     * @return 结果
     */
    @POST
    @Path("/{type}/{id}/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] attendanceExport(@PathParam("type") String type, @PathParam("id") int id, @Context HttpServletResponse response) {
        LOGGER.info("开始调用/punch/" + id + "/export");
        String name = null;
        //pdf上显示id
        String key = null;
        Map<String, Object> map = new HashMap<>();
        Trip trip = new Trip();
        Out out = new Out();
        Vacation vacation = new Vacation();
        Over over = new Over();
        Punch punch = new Punch();
        switch (type) {
            case "trip":
                trip = tripService.getTripInfoById(id);
                trip.setStatus("97");
                name = trip.getUserName() + "_出差";
                key = "trip_" + trip.getId();
                map = tripService.getTripMapList(trip);
                break;
            case "out":
                out = outService.getOutInfoById(id);
                out.setStatus("97");
                name = out.getUserName() + "_外出";
                key = "out_" + out.getId();
                map = outService.getOutMapList(out);
                break;
            case "vacation":
                vacation = vacationService.getVacationInfoById(id);
                vacation.setStatus("97");
                name = vacation.getUserName() + "_请假";
                key = "vacation_" + vacation.getId();
                map = vacationService.getVacationMapList(vacation);
                break;
            case "over":
                over = overService.getOverInfoById(id);
                over.setStatus("97");
                name = over.getUserName() + "_加班";
                key = "over_" + over.getId();
                map = overService.getOverMapList(over);
                break;
            case "punch":
                punch = punchService.getPunchInfoById(id);
                punch.setStatus("97");
                name = punch.getUserName() + "_忘打卡";
                key = "punch_" + punch.getId();
                map = punchService.getPunchMapList(punch);
                break;
            default:
                LOGGER.error("参数错误");
                break;
        }
        map.put("key", key);
        try {
            assert name != null;
            String templatePath = "WEB-INF/classes/docs/" + name.substring(name.lastIndexOf("_") + 1) + "_template.xls";
            TemplateExportParams params = new TemplateExportParams(templatePath);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(attendancePath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            String dateId = DateUtil.getIdByDate();
            String excelName = name + id + "_" + dateId + "_export.xls";
            String pdfName = name + id + "_" + dateId + "_export.pdf";
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

            String md5 = MD5Util.getFileMd5(pdfFileName);
            // 状态更新97 将md5值也更新进去
            switch (type) {
                case "trip":
                    trip.setMd5(md5);
                    tripService.updateTripInfo(trip);
                    break;
                case "out":
                    out.setMd5(md5);
                    outService.updateOutInfo(out);
                    break;
                case "vacation":
                    vacation.setMd5(md5);
                    vacationService.updateVacationInfo(vacation);
                    break;
                case "over":
                    over.setMd5(md5);
                    overService.updateOverInfo(over);
                    break;
                case "punch":
                    punch.setMd5(md5);
                    punchService.updatePunchInfo(punch);
                    break;
                default:
                    LOGGER.error("参数错误");
                    break;
            }

            return FileUtils.readFileToByteArray(pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/punch/export");
        return new byte[0];
    }

    /**
     * 邮件提醒下一级处理人.
     * @param mainDesk mainDesk
     * @return 结果
     */
    @Path("/remindMail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> remindMail(MainDesk mainDesk) {
        BaseResponse<String> response = new BaseResponse<>();
        Task task = activitiService.findTaskById(mainDesk.getActId());
        String staffId = task.getAssignee();
        if (StringUtils.isBlank(staffId)) {
            response.setMessage("没有找到下一级处理人,邮件发送失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        Staff leader = staffService.getStaffByUserId(staffId);
        assert leader != null;

        String applyer = staffService.getStaffByUserId(mainDesk.getUserId()).getName();
        String type = getApplyType(mainDesk.getApplyTyp());

        //判断能否发送
        TbMail mailTemp = new TbMail();
        mailTemp.setEmail(leader.getEmail());
        mailTemp.setType("考勤处理提醒");
        mailTemp.setrId(mainDesk.getActId());
        if (!mailService.canSend(mailTemp)) {
            response.setMessage("系统检测到您最近已经进行过邮件提醒,请明天再试!");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        MailUtil mailUtil = new MailUtil();
        mailUtil.init();
        Mail mail = new Mail();
        mail.setSender("hr@zzjunzhi.com");
        mail.setAddressees(new String[]{leader.getEmail()});
        //mail.setAddressees(new String[]{"guitang.fang@zzjunzhi.com"});
        mail.setSubject("考勤处理提醒");
        String content = "您好，" + leader.getName() + "：</br>" + applyer + "的[" + type + "]申请需要您的审批，请登录" +
                "<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\">OA系统</a>" +
                "及时处理。";
        content += "</br></br><span style=\"font-size:10px;color:#908f8f;\">本邮件为";
        content += "<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\" style=\"font-size:10px;color:#908f8f;\">OA系统</a>";
        content += "<span style=\"font-size:10px;color:#908f8f;\">自动发送，请勿回复。</span>";
        mail.setContent(content);
        mail.setContentType(Mail.ContentType.HTML);
        try {
            mailUtil.send(mail);
            TbMail tbMail = new TbMail(leader.getName(), leader.getEmail(), "考勤处理提醒", mainDesk.getActId(), content);
            mailService.record(tbMail);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("邮件发送失败:" + e.getMessage());
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage("发送邮件成功!");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }

    String getApplyType(String num) {
        String[] vacationArr = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08"};
        if (Arrays.asList(vacationArr).contains(num)) {
            return "请假";
        } else if ("20".equals(num)) {
            return "出差";
        } else if ("21".equals(num)) {
            return "外出";
        } else if ("22".equals(num)) {
            return "加班";
        } else if ("23".equals(num)) {
            return "忘打卡";
        } else if ("30".equals(num)) {
            return "下载";
        } else if ("31".equals(num)) {
            return "付款信息";
        } else {
            return "未知";
        }
    }

}
