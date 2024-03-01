package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.Annual;
import zzjz.bean.AttenSingleSummary;
import zzjz.bean.AttenSummary;
import zzjz.bean.AttenSummaryDetails;
import zzjz.bean.Attendance;
import zzjz.bean.BaseResponse;
import zzjz.bean.NumbersSummary;
import zzjz.bean.NumbersSummaryRequest;
import zzjz.bean.ResultCode;
import zzjz.bean.SummaryYear;
import zzjz.service.AnnualService;
import zzjz.service.AttenSummaryService;
import zzjz.util.DateUtil;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017年04月19日9:54
 * @ClassName: AttenSummaryRest
 * @Description: 考勤汇总
 */
@Component
@Path("/attenSummary")
public class AttenSummaryRest {

    private final static Logger LOGGER = LoggerFactory.getLogger(AttenSummaryRest.class);

    private String exportPath = "D:/downloadFiles/";

    @Autowired
    AttenSummaryService attenSummaryService;

    @Autowired
    AnnualService annualService;

    /**
     * 查询汇总信息.
     *
     * @param month      年月
     * @param attendance Attendance
     * @return 列表
     * @author 彭鹏
     */
    @POST
    @Path("{month}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSummary> getAttendanceSumList(@PathParam("month") String month, Attendance attendance) {
        LOGGER.info("开始调用/attenSummary/" + month + "/list");
        BaseResponse<AttenSummary> response = new BaseResponse<>();
        String departName = "null".equals(attendance.getDepart()) ? null : attendance.getDepart();
        DateTime beginTime = new DateTime(attendance.getBeginTime());
        DateTime endTime = new DateTime(attendance.getEndTime());
        if (beginTime.equals(endTime)) {
            //日期相等 代表只查当月的
            String month2 = beginTime.toString("yyyy-MM");
            List<AttenSummary> list = attenSummaryService.initSummaryList(month2, departName);
            response.setData(list);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else if (beginTime.isAfter(endTime)) {
            response.setMessage("开始日期必须小于等于结束日期！");
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        } else {
            //1.年份相同
            if (beginTime.getYear() == endTime.getYear()) {
                List<AttenSummary> attenSummaryTotal = null;
                for (; beginTime.isBefore(endTime) || beginTime.equals(endTime); beginTime = beginTime.plusMonths(1)) {
                    String month2 = beginTime.toString("yyyy-MM");
                    List<AttenSummary> attenSummaryTemp = attenSummaryService.initSummaryList(month2, departName);
                    if (attenSummaryTotal == null) {
                        attenSummaryTotal = attenSummaryTemp;
                    } else {
                        attenSummaryTotal = combineAttenSummary(attenSummaryTotal, attenSummaryTemp);
                    }
                }
                response.setData(attenSummaryTotal);
                response.setResultCode(ResultCode.RESULT_SUCCESS);

            } else if ((beginTime.getYear() + 1) == endTime.getYear()) {    //2.相差一年
                List<AttenSummary> attenSummaryTotal = null;
                for (; beginTime.isBefore(endTime) || beginTime.equals(endTime); beginTime = beginTime.plusMonths(1)) {
                    String month2 = beginTime.toString("yyyy-MM");
                    List<AttenSummary> attenSummaryTemp = attenSummaryService.initSummaryList(month2, departName);
                    if (attenSummaryTotal == null) {
                        attenSummaryTotal = attenSummaryTemp;
                    } else {
                        attenSummaryTotal = combineAttenSummary(attenSummaryTotal, attenSummaryTemp);
                    }
                }
                response.setData(attenSummaryTotal);
                response.setResultCode(ResultCode.RESULT_SUCCESS);

            } else {    //3.相差一年以上
                response.setMessage("开始日期和结束日期之间跨度太大，暂不支持！");
                response.setResultCode(ResultCode.RESULT_ERROR);
                return response;
            }
        }
        LOGGER.info("调用完成/attenSummary/" + month + "/list");
        return response;
    }

    /**
     * 将单月考勤汇总到总考勤
     *
     * @param total 总考勤
     * @param one   单月考勤
     * @return 总考勤
     */
    List<AttenSummary> combineAttenSummary(List<AttenSummary> total, List<AttenSummary> one) {
        for (AttenSummary attenSummaryOne : one) {
            total = addOne(total, attenSummaryOne);
        }
        return total;
    }

    List<AttenSummary> addOne(List<AttenSummary> total, AttenSummary summary) {
        boolean notIn = true;
        for (AttenSummary attenSummaryTotal : total) {
            if (summary.getUserId().equals(attenSummaryTotal.getUserId())) {
                notIn = false;
                attenSummaryTotal.setWorkday(attenSummaryTotal.getWorkday() + summary.getWorkday());
                attenSummaryTotal.setActual(attenSummaryTotal.getActual() + summary.getActual());
                attenSummaryTotal.setLate(attenSummaryTotal.getLate() + summary.getLate());
                attenSummaryTotal.setEarly(attenSummaryTotal.getEarly() + summary.getEarly());
                attenSummaryTotal.setAbsent(attenSummaryTotal.getAbsent() + summary.getAbsent());
                attenSummaryTotal.setOvertime(attenSummaryTotal.getOvertime() + summary.getOvertime());
                attenSummaryTotal.setOff(attenSummaryTotal.getOff() + summary.getOff());
                attenSummaryTotal.setAnnual(attenSummaryTotal.getAnnual() + summary.getAnnual());
                attenSummaryTotal.setCompassionate(attenSummaryTotal.getCompassionate() + summary.getCompassionate());
                attenSummaryTotal.setMarriage(attenSummaryTotal.getMarriage() + summary.getMarriage());
                attenSummaryTotal.setSick(attenSummaryTotal.getSick() + summary.getSick());
                attenSummaryTotal.setFuneral(attenSummaryTotal.getFuneral() + summary.getFuneral());
                attenSummaryTotal.setMaternity(attenSummaryTotal.getMaternity() + summary.getMaternity());
                attenSummaryTotal.setProject(attenSummaryTotal.getProject() + summary.getProject());
                attenSummaryTotal.setTrip(attenSummaryTotal.getTrip() + summary.getTrip());
                attenSummaryTotal.setMeal(attenSummaryTotal.getMeal() + summary.getMeal());
                attenSummaryTotal.setDeduction(attenSummaryTotal.getDeduction() + summary.getDeduction());
                break;
            }
        }
        if (notIn) {
            total.add(summary);
        }
        return total;
    }


    /**
     * 查询汇总信息.
     *
     * @param year     年
     * @param leaveTyp 类型
     * @param userId   用户ID
     * @param userName 用户名
     * @param departId 机构
     * @return 列表
     * @author 彭鹏
     */
    @POST
    @Path("{year}/{leaveTyp}/{userId}/{userName}/{departId}/singleList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSingleSummary> selectSingleList(@PathParam("year") String year,
                                                             @PathParam("leaveTyp") String leaveTyp, @PathParam("userId") String userId,
                                                             @PathParam("userName") String userName, @PathParam("departId") String departId) {
        LOGGER.info("开始调用/attenSummary/" + year + "/" + leaveTyp + "/" + userId + "/" + userName + "/" + departId + "/singleList");
        BaseResponse<AttenSingleSummary> response = new BaseResponse<>();
        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("nextyear", Integer.parseInt(year) + 1 + "");
        map.put("leaveTyp", leaveTyp);
        if ("undefined".equals(userName)) {
            map.put("userName", null);
        } else {
            map.put("userName", userName);
        }
        map.put("leaveTyp", leaveTyp);
        if ("undefined".equals(userId)) {
            map.put("userId", null);
        } else {
            map.put("userId", userId);
        }
        if ("null".equals(departId)) {
            map.put("departId", null);
        } else {
            map.put("departId", departId);
        }
        List<AttenSingleSummary> list = attenSummaryService.getSummerySingleList(map);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/attenSummary/singleList");
        return response;
    }

    /**
     * 查询汇总信息.
     *
     * @param departName 部门名称
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param total      total
     * @return 列表
     * @author 彭鹏
     */
    @POST
    @Path("{departName}/{beginDate}/{endDate}/{total}/summaryYear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<SummaryYear> getSummaryYearList(@PathParam("departName") String departName,
                                                        @PathParam("beginDate") String beginDate, @PathParam("endDate") String endDate,
                                                        @PathParam("total") String total) {
        LOGGER.info("开始调用/attenSummary/" + departName + "/" + beginDate + "/" + endDate + "/" + total + "/summaryYear");
        BaseResponse<SummaryYear> response = new BaseResponse<>();
        if ("null".equals(departName)) {
            departName = null;
        }
        List<SummaryYear> list = attenSummaryService.getSummaryYearList(departName, beginDate, endDate, total);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/attenSummary/summaryYear");
        return response;
    }

    /**
     * 汇总公司人数信息.
     *
     * @param request 查询条件
     * @return 列表
     * @author 彭鹏
     */
    @POST
    @Path("numbersSummary")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<NumbersSummary> getNumberSummaryList(NumbersSummaryRequest request) {
        LOGGER.info("开始调用/attenSummary/numbersSummary");
        BaseResponse<NumbersSummary> response = new BaseResponse<>();
        List<NumbersSummary> list = null;
        try {
            list = attenSummaryService.getNumbersSummary(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/attenSummary/numbersSummary");
        return response;
    }

    /**
     * 保存年假信息.
     *
     * @param annual 年假信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("changeAnnual")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> changeAnnual(Annual annual) {
        LOGGER.info("开始调用/attenSummary/changeAnnual");
        BaseResponse<String> response = new BaseResponse<>();
        Boolean rs = annualService.updateAnnualInfo(annual);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }
        LOGGER.info("调用完成/attenSummary/changeAnnual");
        return response;
    }

    /**
     * 统计结果导出为Excel（包括考勤统计、单项统计、年假统计、调休统计、合并统计、人数计算）.
     *
     * @param type     类型
     * @param response response
     * @param data     数据
     * @return 文件
     */
    @POST
    @Path("{type}/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] export(@PathParam("type") String type, @Context HttpServletResponse response, String data) {
        LOGGER.info("开始调用/attenSummary/" + type + "/export");
        if (StringUtils.isBlank(type) || StringUtils.isBlank(data)) {
            LOGGER.error("参数错误！");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if ("考勤统计".equals(type)) {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AttenSummary.class);
            List<AttenSummary> attenSummaryList = null;
            try {
                attenSummaryList = mapper.readValue(data, collectionType);

                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("考勤统计", "考勤统计", "考勤统计"), AttenSummary.class, attenSummaryList);
                File file = new File(exportPath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);

                response.addHeader("filename", URLEncoder.encode(type + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("单项统计".equals(type) || "年假统计".equals(type)) {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AttenSingleSummary.class);
            List<AttenSingleSummary> attenSingleSummaryList;
            try {
                attenSingleSummaryList = mapper.readValue(data, collectionType);

                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(type, type, type), AttenSingleSummary.class, attenSingleSummaryList);
                File file = new File(exportPath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);

                response.addHeader("filename", URLEncoder.encode(type + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("调休统计".equals(type)) {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AttenSingleSummary.class);
            List<AttenSingleSummary> attenSingleSummaryList;
            try {
                attenSingleSummaryList = mapper.readValue(data, collectionType);

                TemplateExportParams params = new TemplateExportParams("WEB-INF/classes/docs/调休统计_template.xls");

                Map<String, Object> map = new HashMap<>();

                map.put("list", attenSingleSummaryList);
                Workbook workbook = ExcelExportUtil.exportExcel(params, map);

                File file = new File(exportPath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);

                response.addHeader("filename", URLEncoder.encode(type + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("合并统计".equals(type)) {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, SummaryYear.class);
            List<SummaryYear> summaryYearList;
            try {
                summaryYearList = mapper.readValue(data, collectionType);

                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(type, type, type), SummaryYear.class, summaryYearList);
                File file = new File(exportPath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);
                response.addHeader("filename", URLEncoder.encode(type + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("人数计算".equals(type)) {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, NumbersSummary.class);
            List<NumbersSummary> numbersSummaryList;
            try {
                numbersSummaryList = mapper.readValue(data, collectionType);

                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(type, type, type), NumbersSummary.class, numbersSummaryList);
                File file = new File(exportPath);
                if (!file.exists()) {
                    boolean rs = file.mkdirs();
                    LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);

                response.addHeader("filename", URLEncoder.encode(type + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("调用完成/attenSummary/export");
        return new byte[0];
    }

    /**
     * 查询汇总详情.
     *
     * @param userId    用户ID
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 列表
     * @author 彭鹏
     */
    @GET
    @Path("/{userId}/{beginDate}/{endDate}/details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSummaryDetails> getSummaryDetails(@PathParam("userId") String userId,
                                                               @PathParam("beginDate") String beginDate, @PathParam("endDate") String endDate) {
        BaseResponse<AttenSummaryDetails> response = new BaseResponse<>();
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryDetails(userId, beginDate, endDate);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("查询成功！");
        return response;
    }

    /**
     * 查询加班汇总详情.
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     * @return 列表
     * @author 彭鹏
     */
    @GET
    @Path("/{userId}/{year}/{month}/overDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSummaryDetails> getSummaryOverDetails(@PathParam("userId") String userId,
                                                                   @PathParam("year") String year, @PathParam("month") String month) {
        BaseResponse<AttenSummaryDetails> response = new BaseResponse<>();
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryOverDetails(userId, year, month);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("查询成功！");
        return response;
    }

    /**
     * 查询调休汇总详情.
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     * @return 列表
     * @author 彭鹏
     */
    @GET
    @Path("/{userId}/{year}/{month}/offDetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSummaryDetails> getSummaryOffDetails(@PathParam("userId") String userId,
                                                                  @PathParam("year") String year, @PathParam("month") String month) {
        BaseResponse<AttenSummaryDetails> response = new BaseResponse<>();
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryOffDetails(userId, year, month);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("查询成功！");
        return response;
    }

}
