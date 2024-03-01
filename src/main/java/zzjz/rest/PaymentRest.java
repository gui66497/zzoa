package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.github.pagehelper.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Payment;
import zzjz.bean.PaymentRequest;
import zzjz.bean.Process;
import zzjz.bean.ResultCode;
import zzjz.service.CommonService;
import zzjz.service.PaymentService;
import zzjz.util.DateUtil;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款申请rest.
 *
 * @author 彭鹏
 * @version 2017/11/13 9:38
 */
@Component
@Path("/payment")
public class PaymentRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRest.class);

    private String exportPath = "D:/downloadFiles/";

    @Autowired
    PaymentService paymentService;

    @Autowired
    CommonService commonService;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 分页获取付款申请信息.
     *
     * @param request 请求信息
     * @return 结果
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Payment> getPaymentPage(PaymentRequest request) {
        LOGGER.info("开始调用/payment/page");
        BaseResponse<Payment> response = new BaseResponse<>();
        if (StringUtils.isNotBlank(request.getStartTime())) {
            request.setStartTime(request.getStartTime() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(request.getEndTime())) {
            request.setEndTime(request.getEndTime() + " 23:59:59");
        }
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        request.setCreateUser(staffId);
        Page<Payment> paymentPage = paymentService.getPaymentPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) paymentPage.getTotal());
        response.setData(paymentPage);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/payment/page");
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
        LOGGER.info("开始调用/payment/getId");
        Integer integer = commonService.getActSeq();
        LOGGER.info("调用完成/payment/getId");
        return integer;
    }

    /**
     * 保存付款申请信息.
     *
     * @param payment 付款申请信息
     * @param headers  头信息
     * @return 列表
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Process> addPayment(Payment payment, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/payment/save");
        BaseResponse<Process> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        payment.setCreateUser(staffId);
        String actId = payment.getActId();
        if (actId == null) {
            actId = paymentService.addPayment(payment);
        } else {
            paymentService.updatePayment(payment);
        }
        if (!StringUtils.isBlank(actId)) {
            List<Process> list = paymentService.getNextUser(payment, "agree");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setData(list);
            response.setMessage("保存付款申请成功！");
        }
        LOGGER.info("调用完成/payment/save");
        return response;
    }

    /**
     * 提交付款申请信息.
     *
     * @param payment 付款申请信息
     * @param user     用户
     * @return 结果
     */
    @POST
    @Path("/{user}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> submitPayment(Payment payment, @PathParam("user") String user) {
        LOGGER.info("开始调用/payment/" + user + "/submit");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = paymentService.submitPayment(payment, user);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("付款申请信息提交成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("付款申请信息提交异常！");
        }
        LOGGER.info("调用完成/payment/submit");
        return response;
    }

    /**
     * 审批付款申请信息.
     *
     * @param payment 付款申请信息
     * @param user     用户
     * @param msg      操作
     * @param reason   原因
     * @return 结果
     */
    @POST
    @Path("/{user}/{msg}/{reason}/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approvePayment(Payment payment, @PathParam("user") String user,
                                                @PathParam("msg") String msg, @PathParam("reason") String reason) {
        LOGGER.info("开始调用/payment/" + user + "/" + msg + "/" + reason + "/approve");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = paymentService.approvePayment(payment, user, msg, reason);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("付款申请信息审批成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("付款申请信息审批异常！");
        }
        LOGGER.info("调用完成/payment/approve");
        return response;
    }

    /**
     * 获取下一处理人.
     *
     * @param payment 付款申请信息
     * @param msg      操作
     * @return 列表
     */
    @POST
    @Path("/{msg}/getNextUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Process> getNextUser(Payment payment, @PathParam("msg") String msg) {
        LOGGER.info("开始调用/payment/" + msg + "/getNextUser");
        BaseResponse<Process> response = new BaseResponse<>();
        List<Process> list = paymentService.getNextUser(payment, msg);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/payment/getNextUser");
        return response;
    }

    /**
     * 删除付款申请信息.
     *
     * @param actId actId
     * @return 结果
     */
    @DELETE
    @Path("/{actId}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deletePayment(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/payment/" + actId + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = paymentService.deleteRecordByActId(actId);
        if (rs) {
            response.setMessage("删除付款申请信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除付款申请信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/payment/delete");
        return response;
    }

    /**
     * 导出付款申请单.
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
        LOGGER.info("开始调用/payment/" + id + "/export");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.getTypeFactory().constructCollectionType(ArrayList.class, PaymentRest.class);

        Payment payment = paymentService.getPaymentInfoById(id);

        List<Map<String, Object>> list = paymentService.getPaymentMapList(payment);

        try {
            TemplateExportParams params = new TemplateExportParams(
                    "WEB-INF/classes/docs/付款申请单.xls");
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File file = new File(exportPath);
            if (!file.exists()) {
                boolean rs = file.mkdirs();
                LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            response.addHeader("filename", URLEncoder.encode(id + "_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));

            //状态更新97
            payment.setStatus("97");
            paymentService.updatePayment(payment);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("调用完成/payment/export");
        return new byte[0];
    }

    /**
     * 忽略支付提醒.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/ignore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> ignore(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/payment/" + id + "/ignore");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = paymentService.ignore(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("忽略付款信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("忽略付款信息异常");
        }
        LOGGER.info("调用完成/payment/ignore");
        return response;
    }
}
