package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.StaffAttribute;
import zzjz.bean.StaffAttributeRequest;
import zzjz.service.StaffAttributeService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日17:00
 * @ClassName: StaffAttributeRest
 * @Description: 人员属性信息管理rest
 */
@Component
@Path("staffAttribute")
public class StaffAttributeRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(StaffAttributeRest.class);

    @Autowired
    StaffAttributeService staffAttributeService;

    /**
     * 分页获取人员属性信息.
     *
     * @param request request
     * @return 人员属性列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<StaffAttribute> getPageList(StaffAttributeRequest request) {
        LOGGER.info("开始调用/staffAttribute/page");
        BaseResponse<StaffAttribute> response = new BaseResponse<>();
        Page<StaffAttribute> list = staffAttributeService.getStaffAttributePageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) list.getTotal());
        response.setData(list);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/staffAttribute/page");
        return response;
    }

    /**
     * 获取人员属性信息列表.
     *
     * @param staffAttribute 人员属性信息
     * @return 人员属性列表
     */
    @Path("/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<StaffAttribute> getStaffAttributeList(StaffAttribute staffAttribute) {
        LOGGER.info("开始调用/staffAttribute/list");
        BaseResponse<StaffAttribute> response = new BaseResponse<>();
        List<StaffAttribute> list = staffAttributeService.getStaffAttributeList(staffAttribute);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/staffAttribute/list");
        return response;
    }

    /**
     * 根据主键获取人员属性信息.
     *
     * @param id 主键
     * @return 人员属性信息
     */
    @Path("/{id}/getStaffAttributeInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StaffAttribute getStaffAttributeInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/staffAttribute/" + id + "/getStaffAttributeInfo");
        StaffAttribute staffAttribute = staffAttributeService.query(id);
        LOGGER.info("调用完成/staffAttribute/getStaffAttributeInfo");
        return staffAttribute;
    }

    /**
     * 保存人员属性信息.
     *
     * @param staffAttribute 人员属性信息
     * @return 结果
     */
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(StaffAttribute staffAttribute) {
        LOGGER.info("开始调用/staffAttribute/save");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs;
        if (staffAttribute.getId() == null) {
            rs = staffAttributeService.add(staffAttribute);
        } else {
            rs = staffAttributeService.update(staffAttribute);
        }
        if (rs) {
            response.setMessage("人员属性信息保存成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("人员属性信息保存异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staffAttribute/save");
        return response;
    }


    /**
     * 根据主键删除人员属性信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("/{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/staffAttribute/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = staffAttributeService.delete(id);
        if (rs) {
            response.setMessage("人员属性信息删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("人员属性信息删除异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/staffAttribute/delete");
        return response;
    }
}
