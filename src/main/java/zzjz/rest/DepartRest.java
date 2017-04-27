package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;
import zzjz.bean.ResultCode;
import zzjz.service.DepartService;
import zzjz.util.PageUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: DepartRest
 * @Description: 部门rest
 * @date 2017年03月31日10:20
 */
@Component
@Path("depart")
public class DepartRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartRest.class);

    private String URI = "";
    private String message = "";

    @Autowired
    private DepartService departService;

    String[] pattern = new String[]{"yyyy-MM","yyyyMM","yyyy/MM",
            "yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd",
            "yyyyMMddHHmmss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss"};

    /**
     * 分页获取部门信息
     * @param request
     * @return
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Depart> getDepartPage(DepartRequest request){
        BaseResponse<Depart> response = new BaseResponse<>();
        Page<Depart> departList = departService.getDepartListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) departList.getTotal());
        response.setData(departList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }

    /**
     * 新增部门信息
     * @param depart 部门信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addDepart (Depart depart, @Context HttpHeaders headers) throws ParseException{
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "depart/add";
        LOGGER.debug("开始调用接口：" + URI);
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.addDepart(depart);
        if(res){
            message = "添加新部门成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "添加新部门失败!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        return response;
    }

    /**
     * 修改部门信息
     * @param depart 部门信息
     * @param headers headers
     * @author 彭鹏
     * @return
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateDepart (Depart depart,@Context HttpHeaders headers) throws ParseException{
        //获取当前操作用户ID，如果为空，则提示当前用户未登录
        URI = "depart/update";
        LOGGER.debug("开始调用接口："+URI);
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.updateDepart(depart);
        if(res){
            message = "修改部门信息成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "修改部门信息失败！";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        return response;
    }

    /**
     * 删除部门信息
     * @param Id 主键
     * @author 彭鹏
     * @return
     */
    @DELETE
    @Path("${Id}/Del")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> DelDepart (@PathParam("Id") Integer Id){
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.delByDepartId(Id);
        if(res){
            message = "删除部门成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "删除部门失败！";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        return response;
    }

}
