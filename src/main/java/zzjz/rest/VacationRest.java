package zzjz.rest;

import com.github.pagehelper.Page;
import com.vaadin.terminal.gwt.client.ui.VCalendarPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.Vacation;
import zzjz.bean.VacationRequest;
import zzjz.mapper.VacationMapper;
import zzjz.service.VacationService;
import zzjz.util.PageUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: VacationRest
 * @Description: 出差管理
 * @date 2017年04月13日16:39
 */
@Component
@Path("vacation")
public class VacationRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VacationRest.class);

    @Autowired
    VacationService vacationService;

    /**
     * 分页获取假期信息
     * @param request
     * @return 假期列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Vacation> getVacationPage (VacationRequest request){
        BaseResponse<Vacation> response = new BaseResponse<>();
        Page<Vacation> vacationList = vacationService.getVacationListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) vacationList.getTotal());
        response.setData(vacationList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }

    @Path("/getId")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Integer getInitId() {
        Integer integer = vacationService.getActSeq();
        return integer;
    }

    /**
     * 获取假期信息
     * @param id
     * @return 假期信息
     */
    @Path("{id}/load")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Vacation getVacationInfo (@PathParam("id") Integer id){
        return vacationService.getVacationInfoById(id);
    }

    /**
     * 保存请假申请
     * @param vacation 请假信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addVacation(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs;
        if(vacation.getCreateTime()!=null){
            rs = vacationService.updateVacationInfo(vacation);
        } else {
            rs = vacationService.addVacationInfo(vacation);
        }

        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        }else{
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }

        return response;
    }

    /**
     * 保存并提交请假申请
     * @param vacation 请假信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("saveAndSubmit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> saveAndSubmit(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs;
        if(vacation.getCreateTime()!=null){
            rs = vacationService.updateVacationInfo(vacation);
        } else {
            rs = vacationService.addVacationInfo(vacation);
        }
        if(rs){
            rs = vacationService.submitVacationInfo(vacation,"");
        }
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("提交成功！");
        }else{
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("提交异常！");
        }

        return response;
    }

    /**
     * 保存请假审批
     * @param vacation 请假信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("approveSave")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approveSave(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = vacationService.updateVacationInfo(vacation);
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("保存成功！");
        }else{
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("保存异常！");
        }

        return response;
    }

    /**
     * 审批请假信息
     * @param vacation 请假信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> approveVacation(Vacation vacation, @Context HttpHeaders headers) throws ParseException {
        BaseResponse<String> response = new BaseResponse<String>();
        boolean rs = vacationService.updateVacationInfo(vacation);
        if(rs){
            rs = vacationService.submitVacationInfo(vacation,"");
        }
        if(rs){
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("审批成功！");
        }else{
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("审批异常！");
        }

        return response;
    }


}
