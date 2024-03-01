package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.github.pagehelper.Page;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.MainDesk;
import zzjz.bean.MainDeskRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.Punch;
import zzjz.bean.Over;
import zzjz.bean.Out;
import zzjz.bean.Trip;
import zzjz.bean.Vacation;
import zzjz.bean.Down;
import zzjz.bean.Payment;
import zzjz.service.MainDeskService;
import zzjz.service.OutService;
import zzjz.service.OverService;
import zzjz.service.PunchService;
import zzjz.service.TripService;
import zzjz.service.VacationService;
import zzjz.service.DownService;
import zzjz.service.PaymentService;
import zzjz.service.StaffService;
import zzjz.util.DateUtil;
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
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 * @author 彭鹏
 * @version 2017年05月15日10:56
 * @ClassName: MainDeskRest
 * @Description: 首页信息
 */
@Component
@Path("/mainDesk")
public class MainDeskRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MainDeskRest.class);

    private final static Integer FINT400 = -400;

    @Autowired
    MainDeskService mainDeskService;

    @Autowired
    PunchService punchService;

    @Autowired
    OverService overService;

    @Autowired
    OutService outService;

    @Autowired
    TripService tripService;

    @Autowired
    VacationService vacationService;

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    DownService downService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    StaffService staffService;

    private String getStaffIdFromSession() {
        return servletRequest != null ? servletRequest.getSession().getAttribute("staffId").toString() : "";
    }

    /**
     * 分页获取待办信息.
     *
     * @param request 请求信息
     * @return 待办信息
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<MainDesk> getMainDeskPageList(MainDeskRequest request) {
        LOGGER.info("开始调用/mainDesk/page");
        BaseResponse<MainDesk> response = new BaseResponse<>();
        String userId = getStaffIdFromSession();
        request.setUserId(userId);
        Page<MainDesk> mainDeskList = mainDeskService.getMainDeskPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) mainDeskList.getTotal());
        response.setData(mainDeskList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/mainDesk/page");
        return response;
    }

    /**
     * 分页获取已办信息.
     *
     * @param request 请求信息
     * @return 已办信息
     */
    @Path("/pageDone")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<MainDesk> getMainDeskDoneList(MainDeskRequest request) {
        LOGGER.info("开始调用/mainDesk/pageDone");
        BaseResponse<MainDesk> response = new BaseResponse<>();
        String userId = getStaffIdFromSession();
        request.setUserId(userId);
        Page<MainDesk> mainDeskList = mainDeskService.getMainDeskDoneList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) mainDeskList.getTotal());
        response.setData(mainDeskList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/mainDesk/pageDone");
        return response;
    }

    /**
     * 分页获取已提交信息.
     *
     * @param request   请求信息
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param userName  用户姓名
     * @param applyTyp  申请类型
     * @param departName  部门名称
     * @param finish  是否是结束流程
     * @return 已提交信息
     */
    @Path("{beginDate}/{endDate}/{userName}/{applyTyp}/{departName}/{finish}/commitPage")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<MainDesk> getCommitAttentionList(MainDeskRequest request,
                                                         @PathParam("beginDate") String beginDate, @PathParam("endDate") String endDate,
                                                         @PathParam("userName") String userName, @PathParam("applyTyp") String applyTyp,
                                                         @PathParam("departName") String departName, @PathParam("finish") boolean finish) {
        LOGGER.info("开始调用/mainDesk/" + beginDate + "/" + endDate + "/" + userName + "/" + applyTyp + "/" + departName + "/commitPage");
        BaseResponse<MainDesk> response = new BaseResponse<>();
        Page<MainDesk> mainDeskList = mainDeskService.getCommitAttentionList(request, beginDate, endDate, userName, applyTyp, departName, finish);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) mainDeskList.getTotal());
        response.setData(mainDeskList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/mainDesk/commitPage");
        return response;
    }

    private ProcessDefinition getProcessDefinition(String processDefinitionId) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }

    /**
     * 根据ID获取忘打卡信息.
     *
     * @param id id
     * @return 忘打卡信息
     */
    @GET
    @Path("{id}/getPunchInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Punch getPunchInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getPunchInfo");
        Punch punch = punchService.getPunchInfoById(id);
        LOGGER.info("调用完成/mainDesk/getPunchInfo");
        return punch;
    }

    /**
     * 根据ID获取加班信息.
     *
     * @param id id
     * @return 加班信息
     */
    @GET
    @Path("{id}/getOverInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Over getOverInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getOverInfo");
        Over over = overService.getOverInfoById(id);
        LOGGER.info("调用完成/mainDesk/getOverInfo");
        return over;
    }

    /**
     * 根据ID获取外出信息.
     *
     * @param id id
     * @return 外出信息
     */
    @GET
    @Path("{id}/getOutInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Out getOutInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getOutInfo");
        Out out = outService.getOutInfoById(id);
        LOGGER.info("调用完成/mainDesk/getOutInfo");
        return out;
    }

    /**
     * 根据ID获取出差信息.
     *
     * @param id id
     * @return 出差信息
     */
    @GET
    @Path("{id}/getTripInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Trip getTripInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getTripInfo");
        Trip trip = tripService.getTripInfoById(id);
        LOGGER.info("调用完成/mainDesk/getTripInfo");
        return trip;
    }

    /**
     * 根据ID获取请假信息.
     *
     * @param id id
     * @return 请假信息
     */
    @GET
    @Path("{id}/getVacationInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Vacation getVacationInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getVacationInfo");
        Vacation vacation = vacationService.getVacationInfoById(id);
        LOGGER.info("调用完成/mainDesk/getVacationInfo");
        return vacation;
    }

    /**
     * 根据ID获取下载信息.
     *
     * @param id id
     * @return 下载信息
     */
    @GET
    @Path("{id}/getDownInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Down getDownInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getDownInfo");
        Down down = downService.getDownInfoById(id);
        LOGGER.info("调用完成/mainDesk/getDownInfo");
        return down;
    }

    /**
     * 根据ID获取付款申请信息.
     *
     * @param id id
     * @return 付款申请信息
     */
    @GET
    @Path("{id}/getPaymentInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Payment getPaymentInfoById(@PathParam("id") int id) {
        LOGGER.info("开始调用/mainDesk/" + id + "/getPaymentInfo");
        Payment payment = paymentService.getPaymentInfoById(id);
        LOGGER.info("调用完成/mainDesk/getPaymentInfo");
        return payment;
    }

    /**
     * 获取申请进度列表.
     *
     * @param request 请求信息
     * @return 进度列表
     */
    @POST
    @Path("getProgress")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<MainDesk> getProgressList(MainDeskRequest request) {
        LOGGER.info("开始调用/mainDesk/getProgress");
        BaseResponse<MainDesk> response = new BaseResponse<>();
        MainDeskRequest request1 = new MainDeskRequest();
        request1.setPaging(request.getPaging());
        String userId = getStaffIdFromSession();
        request1.setUserId(userId);
        Page<MainDesk> list = mainDeskService.getProgressList(request1);
        List<Object> otherData = PageUtil.dealPaging(request1.getPaging(), (int) list.getTotal());
        response.setData(list);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/mainDesk/getProgress");
        return response;
    }


    //考勤导出路径
    @Value("${attendance.path}")
    String attendancePath;

    /**
     * 导出.
     *
     * @param param    参数
     * @param response response
     * @return 文件
     */
    @POST
    @Path("/{param}/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] export(@PathParam("param") String param, @Context HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String[] arr = param.split("\\|");
        Map<String, Object> map = new HashedMap();
        String fileName = "";
        String userName = "";
        List<PdfReader> list = new ArrayList<>();
        String key = null;
        for (int i = 0; i < arr.length; i++) {
            String id = arr[i].split(":")[0];
            String type = arr[i].split(":")[1];
            switch (type) {
                case "out" :
                    Out out = outService.getOutInfoById(Integer.parseInt(id));
                    map = outService.getOutMapList(out);
                    fileName = "外出";
                    userName = out.getUserName();
                    key = "out_" + out.getId();
                    break;
                case "over" :
                    Over over = overService.getOverInfoById(Integer.parseInt(id));
                    map = overService.getOverMapList(over);
                    fileName = "加班";
                    userName = over.getUserName();
                    key = "over_" + over.getId();
                    break;
                case "punch" :
                    Punch punch = punchService.getPunchInfoById(Integer.parseInt(id));
                    map = punchService.getPunchMapList(punch);
                    fileName = "忘打卡";
                    userName = punch.getUserName();
                    key = "punch_" + punch.getId();
                    break;
                case "trip2" :
                    Trip trip = tripService.getTripInfoById(Integer.parseInt(id));
                    map = tripService.getTripMapList(trip);
                    fileName = "出差";
                    userName = trip.getUserName();
                    key = "trip_" + trip.getId();
                    break;
                case "vacation" :
                    Vacation vacation = vacationService.getVacationInfoById(Integer.parseInt(id));
                    map = vacationService.getVacationMapList(vacation);
                    fileName = "请假";
                    userName = vacation.getUserName();
                    key = "vacation_" + vacation.getId();
                    break;
                default :
                    break;
            }
            map.put("key", key);
            try {
                TemplateExportParams params = new TemplateExportParams(
                        "WEB-INF/classes/docs/" + fileName + "_template.xls");
                Workbook workbook = ExcelExportUtil.exportExcel(params, map);
                File file = new File(attendancePath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                String dateId = DateUtil.getIdByDate();
                String excelName = userName + "_" + fileName + "_" + id + "_" + dateId + "_export.xls";
                String pdfName = userName + "_" + fileName + "_" + id + "_" + dateId + "_export.pdf";
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
                list.add(new PdfReader(pdfFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Rectangle rectPageSize = new Rectangle(PageSize.A4); // A4纸张
        Document document = new Document(rectPageSize); // 上、下、左、右间距
        String exportName = attendancePath + DateUtil.getIdByDate() + "_exportCombin.pdf";
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(exportName));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            for (int i = 0; i < list.size(); i++) {
                page = writer.getImportedPage(list.get(i), 1);
                if (i % 2 == 0) {
                    document.newPage();
                    cb.addTemplate(page, 0, 0);
                } else {
                    cb.addTemplate(page, 0, FINT400);
                }
            }
            document.close();
            writer.close();
            File pdfFile = new File(exportName);
            response.addHeader("filename",  URLEncoder.encode(exportName, "utf-8"));
            return FileUtils.readFileToByteArray(pdfFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

}
