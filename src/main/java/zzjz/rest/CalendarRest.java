package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Calendar;
import zzjz.bean.ResultCode;
import zzjz.service.CalendarService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: CalendarRest
 * @Description: 日历
 * @date 2017年04月12日10:49
 */
@Component
@Path("calendar")
public class CalendarRest {

    private final static Logger logger = LoggerFactory.getLogger(CalendarRest.class);

    @Autowired
    CalendarService calendarService;

    /**
     * 获取日期信息
     * @param curDay
     * @return
     */
    @Path("{curDay}/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Calendar> getCalendarList(@PathParam("curDay") String curDay){
        BaseResponse<Calendar> response = new BaseResponse<>();
        Calendar calendar = new Calendar();
        calendar.setCurDay(curDay);
        List<Calendar> list = calendarService.getCalendarList(calendar);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }

    /**
     * 修改为节假日
     * @param curDay 日期
     * @return 结果
     * @throws IOException IOException
     */
    @PUT
    @Path("{curDay}/toHoliday")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateHoliday(@PathParam("curDay") String curDay){
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = calendarService.updateHoliday(curDay);
        if(!rs){
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        }else{
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }

        return response;
    }

    /**
     * 取消节假日
     * @param curDay 日期
     * @return 结果
     * @throws IOException IOException
     */
    @PUT
    @Path("{curDay}/toHolidayCancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateHolidayCancel(@PathParam("curDay") String curDay){
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = calendarService.updateHolidayCancel(curDay);
        if(!rs){
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        }else{
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }
        return response;
    }

    /**
     * 修改为工作日
     * @param curDay 日期
     * @return 结果
     * @throws IOException IOException
     */
    @PUT
    @Path("{curDay}/toWorkday")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateWorkday(@PathParam("curDay") String curDay){
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = calendarService.updateWorkday(curDay);
        if(!rs){
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        }else{
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }

        return response;
    }

    /**
     * 取消工作日
     * @param curDay 日期
     * @return 结果
     * @throws IOException IOException
     */
    @PUT
    @Path("{curDay}/toWorkdayCancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateWorkdayCancel(@PathParam("curDay") String curDay){
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = calendarService.updateWorkdayCancel(curDay);
        if(!rs){
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("更新失败");
        }else{
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("更新成功");
        }
        return response;
    }
}
