package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Calendar;
import zzjz.bean.ResultCode;
import zzjz.service.CalendarService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * @author 彭鹏
 * @version 2017年04月12日10:49
 * @ClassName: CalendarRest
 * @Description: 日历
 */
@Component
@Path("calendar")
public class CalendarRest {

    private final static Logger LOGGER = LoggerFactory.getLogger(CalendarRest.class);

    @Autowired
    CalendarService calendarService;

    /**
     * 获取日期信息.
     *
     * @param curDay 日期
     * @return 信息
     */
    @Path("{curDay}/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCalendarList(@PathParam("curDay") String curDay) {
        LOGGER.info("开始调用/calendar/" + curDay + "/list");
        Calendar calendar = new Calendar();
        calendar.setCurDay(curDay);
        Object obj = calendarService.getCalendarList(calendar);
        LOGGER.info("开始调用/calendar/list");
        return obj;
    }

    /**
     * 修改为节假日.
     *
     * @param curDay 日期
     * @param hTyp   hTyp
     * @return 结果
     */
    @PUT
    @Path("{curDay}/{hTyp}/toHoliday")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateHoliday(@PathParam("curDay") String curDay, @PathParam("hTyp") String hTyp) {
        LOGGER.info("开始调用/calendar/" + curDay + "/" + hTyp + "/toHoliday");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = calendarService.updateHoliday(curDay, hTyp);
        if (!rs) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        } else {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }
        LOGGER.info("调用完成/calendar/toHoliday");
        return response;
    }

    /**
     * 修改为工作日.
     *
     * @param curDay 日期
     * @param wTyp   wTyp
     * @return 结果
     */
    @PUT
    @Path("{curDay}/{wTyp}/toWorkday")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateWorkday(@PathParam("curDay") String curDay, @PathParam("wTyp") String wTyp) {
        LOGGER.info("开始调用/calendar/" + curDay + "/" + wTyp + "/toWorkday");
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = calendarService.updateWorkday(curDay, wTyp);
        if (!rs) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        } else {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }
        LOGGER.info("调用完成/calendar/toWorkday");
        return response;
    }
}
