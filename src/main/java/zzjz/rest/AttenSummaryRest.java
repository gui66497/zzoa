package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.AttenSummary;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.AttenSummaryService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: AttenSummaryRest
 * @Description: 考勤汇总
 * @date 2017年04月19日9:54
 */
@Component
@Path("/attenSummary")
public class AttenSummaryRest {

    private final static Logger logger = LoggerFactory.getLogger(AttendanceRest.class);

    @Autowired
    AttenSummaryService attenSummaryService;

    /**
     * 查询汇总信息
     * @param month
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("{month}/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AttenSummary> inputSignTime(@PathParam("month") String month) {
        BaseResponse<AttenSummary> response = new BaseResponse<>();
        List<AttenSummary> list =  attenSummaryService.initSummaryList(month);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }
}
