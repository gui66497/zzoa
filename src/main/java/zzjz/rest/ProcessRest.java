package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Process;
import zzjz.bean.ProcessRequest;
import zzjz.bean.ResultCode;
import zzjz.service.ProcessService;
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
 * @version 2017年11月02日14:19
 * @ClassName: ProcessRest
 * @Description: 流程配置服务处理类
 */
@Component
@Path("/process")
public class ProcessRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessRest.class);

    @Autowired
    ProcessService processService;

    /**
     * 分页获取流程配置信息.
     *
     * @param request 请求信息
     * @return 流程配置列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Process> getProcessPageList(ProcessRequest request) {
        LOGGER.info("开始调用/process/page");
        BaseResponse<Process> response = new BaseResponse<>();
        Page<Process> pageList = processService.getPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) pageList.getTotal());
        response.setData(pageList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/process/page");
        return response;
    }

    /**
     * 获取流程配置信息.
     *
     * @param process 请求信息
     * @return 流程配置列表
     */
    @Path("/getSelections")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Process> getSelections(Process process) {
        LOGGER.info("开始调用/process/getSelections");
        BaseResponse<Process> response = new BaseResponse<>();
        List<Process> list = processService.getSelections(process);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/process/getSelections");
        return response;
    }

    /**
     * 查询流程配置服务信息.
     *
     * @param id 主键
     * @return 流程配置信息
     * @author 彭鹏
     */
    @POST
    @Path("/{id}/getProcessInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Process getProcessInfo(@PathParam("id") Integer id) {
        return processService.selectById(id);
    }

    /**
     * 新增流程配置服务信息.
     *
     * @param process 流程配置服务信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addProcessInfo(Process process) {
        LOGGER.info("开始调用/process/addProcessInfo");
        BaseResponse<String> response = new BaseResponse<>();
        Boolean rs = processService.insert(process);
        if (rs) {
            response.setMessage("新增成功！");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("新增失败！");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/process/addProcessInfo");
        return response;
    }

    /**
     * 修改流程配置服务信息.
     *
     * @param process 流程配置服务信息
     * @return 结果
     * @author 彭鹏
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateProcessInfo(Process process) {
        LOGGER.info("开始调用/process/updateProcessInfo");
        BaseResponse<String> response = new BaseResponse<>();
        Boolean rs = processService.update(process);
        if (rs) {
            response.setMessage("修改成功！");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("修改失败！");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/process/updateProcessInfo");
        return response;
    }

    /**
     * 删除流程配置服务信息.
     *
     * @param id 流程配置服务ID
     * @return 结果
     * @author 彭鹏
     */
    @DELETE
    @Path("/{id}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteProcessInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/process/deleteProcessInfo");
        BaseResponse<String> response = new BaseResponse<>();
        Boolean rs = processService.delete(id);
        if (rs) {
            response.setMessage("删除成功！");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除失败！");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/process/deleteProcessInfo");
        return response;
    }

}
