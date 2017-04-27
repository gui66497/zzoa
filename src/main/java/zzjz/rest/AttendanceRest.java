package zzjz.rest;

import com.github.pagehelper.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.*;
import zzjz.service.AttendanceService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: AttendanceRest
 * @Description: 考勤信息
 * @date 2017年04月12日15:18
 */
@Component
@Path("/attendance")
public class AttendanceRest {
    private final static Logger logger = LoggerFactory.getLogger(AttendanceRest.class);

    @Autowired
    AttendanceService attendanceService;

    /**
     * 分页获取出差信息
     * @param request
     * @return 出差列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Attendance> getAttendanceList(AttendanceRequest request){
        BaseResponse<Attendance> response = new BaseResponse<>();
        Page<Attendance> attendanceList = attendanceService.getAttendanceList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) attendanceList.getTotal());
        response.setData(attendanceList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }


    /**
     * 上传考勤信息
     * @param fileInputStream 输入文件流
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
        logger.info("开始调用/attendance/upload");
        BaseResponse<String> response = new BaseResponse<>();

        ImportParams importParams = new ImportParams();
        List<AttendanceExcel> res = null;
        try {
            res = ExcelImportUtil.importExcel(fileInputStream, AttendanceExcel.class, importParams);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert res != null;
        for(AttendanceExcel attendance:res){
            attendanceService.checkAttendanceData(attendance);
        }

        boolean result = attendanceService.addAttendanceList(res);

        if(result){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("导入成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("导入失败！");
        }

        return response;
    }


    /**
     * 录入事项
     * @param request
     * @param headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("input")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputAtt(AttendanceRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputAtt(request);
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("插入成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("插入失败！");
        }
        return response;
    }


    /**
     * 清除事项信息
     * @param request
     * @param headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("deleteAttInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteAttInfo(AttendanceRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.deleteAttInfo(request);
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        return response;
    }


    /**
     * 添加签到信息
     * @param request
     * @param headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("inputSignTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputSignTime(AttendanceRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputSignTime(request);
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        return response;
    }


    /**
     * 添加签退信息
     * @param request
     * @param headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("inputLeaveTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> inputLeaveTime(AttendanceRequest request, @Context HttpHeaders headers) {
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = attendanceService.inputLeaveTime(request);
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        return response;
    }
}
