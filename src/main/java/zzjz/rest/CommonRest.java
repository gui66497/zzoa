package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.ActHis;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.ActivitiService;
import zzjz.service.CommonService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年04月12日15:18
 * @ClassName: CommonRest
 * @Description: 公共信息
 */
@Component
@Path("/common")
public class CommonRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommonRest.class);

    @Autowired
    CommonService commonService;

    @Autowired
    ActivitiService activitiService;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 获取冲突信息.
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param beginPart 开始时段
     * @param endPart   结束时段
     * @return 结果
     */
    @Path("/{beginDate}/{endDate}/{beginPart}/{endPart}/check")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> check(@PathParam("beginDate") String beginDate, @PathParam("endDate") String endDate,
                                      @PathParam("beginPart") String beginPart, @PathParam("endPart") String endPart) {
        LOGGER.info("开始调用/common/" + beginDate + "/" + endDate + "/" + beginPart + "/" + endPart + "/check");
        BaseResponse<String> response = new BaseResponse<>();
        String userId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = false;
        try {
            rs = commonService.checkOption(userId, beginDate, endDate, beginPart, endPart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("事项冲突！");
        }
        LOGGER.info("调用完成/common/check");
        return response;
    }

    /**
     * 获取冲突信息.
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param beginPart 开始时段
     * @param endPart   结束时段
     * @param userId    员工号
     * @param id        id
     * @return 结果
     */
    @Path("/{beginDate}/{endDate}/{beginPart}/{endPart}/{userId}/{id}/check")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> check2(@PathParam("beginDate") String beginDate, @PathParam("endDate") String endDate,
                                       @PathParam("beginPart") String beginPart, @PathParam("endPart") String endPart,
                                       @PathParam("userId") String userId, @PathParam("id") Integer id) {
        LOGGER.info("开始调用/common/" + beginDate + "/" + endDate + "/" + beginPart + "/" + endPart + "/" + userId + "/" + id + "/check");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = false;
        try {
            rs = commonService.checkOptionWithId(id, userId, beginDate, endDate, beginPart, endPart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("事项冲突！");
        }
        LOGGER.info("调用完成/common/check");
        return response;
    }

    /**
     * 获取冲突信息.
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param id        id
     * @return 结果
     */
    @Path("/{startTime}/{endTime}/{id}/forCheck")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> forCheck(@PathParam("startTime") String startTime, @PathParam("endTime") String endTime, @PathParam("id") int id) {
        LOGGER.info("开始调用/common/" + startTime + "/" + endTime + "/" + id + "/forCheck");
        BaseResponse<String> response = new BaseResponse<>();
        String userId = servletRequest.getSession().getAttribute("staffId").toString();
        boolean rs = false;
        try {
            rs = commonService.forCheckOption(userId, startTime, endTime, id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("所选日期（整天）内存在其他事项，是否继续？");
        }
        LOGGER.info("调用完成/common/forCheck");
        return response;
    }

    /**
     * 获取历史记录.
     *
     * @param actId 流程编号
     * @return 结果
     */
    @Path("/{actId}/getHisInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<ActHis> getHisInfo(@PathParam("actId") String actId) {
        LOGGER.info("开始调用/common/" + actId + "/getHisInfo");
        BaseResponse<ActHis> response = new BaseResponse<>();
        List<ActHis> list = activitiService.getActHisList(actId);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("");
        LOGGER.info("调用完成/common/getHisInfo");
        return response;
    }

}
