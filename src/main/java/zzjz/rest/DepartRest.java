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
import java.text.ParseException;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年03月31日10:20
 * @ClassName: DepartRest
 * @Description: 部门rest
 */
@Component
@Path("depart")
public class DepartRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartRest.class);

    private static final String URI = "";

    private String message = "";

    @Autowired
    private DepartService departService;

    String[] pattern = new String[]{"yyyy-MM", "yyyyMM", "yyyy/MM", "yyyyMMdd", "yyyy-MM-dd", "yyyy/MM/dd",
        "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};

    /**
     * 分页获取部门信息.
     *
     * @param request 请求信息
     * @return 结果
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Depart> getDepartPage(DepartRequest request) {
        LOGGER.info("开始调用/depart/page");
        BaseResponse<Depart> response = new BaseResponse<>();
        Page<Depart> departList = departService.getDepartListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) departList.getTotal());
        response.setData(departList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/depart/page");
        return response;
    }

    /**
     * 获取所有子部门信息列表.
     *
     * @return 部门列表
     */
    @Path("/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Depart> getDepartList() {
        LOGGER.info("开始调用/depart/list");
        List<Depart> departList = departService.getDepartList();
        LOGGER.info("调用完成/depart/list");
        return departList;
    }

    /**
     * 获取所有部门信息列表.
     *
     * @return 部门列表
     */
    @Path("/allList")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Depart> getAllDepartList() {
        LOGGER.info("开始调用/depart/allList");
        List<Depart> departList = departService.getAllDepartList();
        LOGGER.info("调用完成/depart/allList");
        return departList;
    }

    /**
     * 新增部门信息.
     *
     * @param depart  部门信息
     * @param headers headers
     * @return 结果
     * @author 彭鹏
     * @throws ParseException ParseException
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addDepart(Depart depart, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/depart/add");
        // 获取当前操作用户ID，如果为空，则提示当前用户未登录
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.addDepart(depart);
        if (res) {
            message = "添加新部门成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "添加新部门失败!";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        response.setMessage(message);
        LOGGER.info("调用完成/depart/add");
        return response;
    }

    /**
     * 修改部门信息.
     *
     * @param depart  部门信息
     * @param headers headers
     * @return 结果
     * @author 彭鹏
     * @throws ParseException ParseException
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateDepart(Depart depart, @Context HttpHeaders headers) throws ParseException {
        LOGGER.info("开始调用/depart/update");
        //获取当前操作用户ID，如果为空，则提示当前用户未登录
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.updateDepart(depart);
        if (res) {
            message = "修改部门信息成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "修改部门信息失败！";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        response.setMessage(message);
        LOGGER.info("调用完成/depart/update");
        return response;
    }

    /**
     * 删除部门信息.
     *
     * @param departId 主键
     * @return 结果
     * @author 彭鹏
     */
    @DELETE
    @Path("{departId}/Del")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delDepart(@PathParam("departId") Long departId) {
        LOGGER.info("开始调用/depart/" + departId + "/Del");
        BaseResponse<String> response = new BaseResponse<>();
        boolean res = departService.delByDepartId(departId);
        if (res) {
            message = "删除部门成功！";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "删除部门失败！";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        response.setMessage(message);
        LOGGER.info("调用完成/depart/Del");
        return response;
    }

}
