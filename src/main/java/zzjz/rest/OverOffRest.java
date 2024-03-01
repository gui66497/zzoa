package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.OverOffService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * @author 彭鹏
 * @version 2017年06月19日16:09
 * @ClassName: OverOffRest
 * @Description: 加班调休信息管理
 */
@Component
@Path("/overOff")
public class OverOffRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(OverOffRest.class);

    @Autowired
    OverOffService overOffService;

    /**
     * 清除加班结余.
     *
     * @param monthDate 年月
     * @param userId    员工号
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("/{monthDate}/{userId}/clearLastDay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> clearLastLeftDay(@PathParam("monthDate") String monthDate, @PathParam("userId") String userId) {
        LOGGER.info("开始调用/overOff/" + monthDate + "/" + userId + "/clearLastDay");
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = overOffService.clearLastLeftDay(userId, monthDate);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("清空成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("清空失败！");
        }
        LOGGER.info("调用完成/overOff/clearLastDay");
        return response;
    }
}
