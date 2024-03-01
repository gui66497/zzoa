package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.AptitudeBorrow;
import zzjz.bean.AptitudeBorrowRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.AptitudeBorrowService;
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
 * @ClassName: AptitudeBorrowRest
 * @Description: 资质证书借阅信息管理rest
 */
@Component
@Path("aptitudeBorrow")
public class AptitudeBorrowRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AptitudeBorrowRest.class);

    @Autowired
    AptitudeBorrowService aptitudeBorrowService;

    /**
     * 分页获取资质证书借阅信息.
     *
     * @param request request
     * @return 资质证书借阅列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AptitudeBorrow> getPageList(AptitudeBorrowRequest request) {
        LOGGER.info("开始调用/aptitudeBorrow/page");
        BaseResponse<AptitudeBorrow> response = new BaseResponse<>();
        Page<AptitudeBorrow> list = aptitudeBorrowService.getAptitudeBorrowPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) list.getTotal());
        response.setData(list);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitudeBorrow/page");
        return response;
    }

    /**
     * 获取资质证书借阅信息列表.
     *
     * @param aptitudeBorrow 资质证书借阅信息
     * @return 资质证书借阅列表
     */
    @Path("/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<AptitudeBorrow> getAptitudeBorrowList(AptitudeBorrow aptitudeBorrow) {
        LOGGER.info("开始调用/aptitudeBorrow/list");
        BaseResponse<AptitudeBorrow> response = new BaseResponse<>();
        List<AptitudeBorrow> list = aptitudeBorrowService.getAptitudeBorrowList(aptitudeBorrow);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitudeBorrow/list");
        return response;
    }

    /**
     * 根据主键获取资质证书借阅信息.
     *
     * @param id 主键
     * @return 资质证书借阅信息
     */
    @Path("/{id}/getAptitudeBorrowInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AptitudeBorrow getAptitudeBorrowInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/aptitudeBorrow/" + id + "/getAptitudeBorrowInfo");
        AptitudeBorrow aptitudeBorrow = aptitudeBorrowService.query(id);
        LOGGER.info("调用完成/aptitudeBorrow/getAptitudeBorrowInfo");
        return aptitudeBorrow;
    }

    /**
     * 保存资质证书借阅信息.
     *
     * @param aptitudeBorrow 资质证书借阅信息
     * @return 结果
     */
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(AptitudeBorrow aptitudeBorrow) {
        LOGGER.info("开始调用/aptitudeBorrow/save");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs;
        if (aptitudeBorrow.getId() == null) {
            rs = aptitudeBorrowService.add(aptitudeBorrow);
        } else {
            rs = aptitudeBorrowService.update(aptitudeBorrow);
        }
        if (rs) {
            response.setMessage("资质证书借阅信息保存成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("资质证书借阅信息保存异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/aptitudeBorrow/save");
        return response;
    }


    /**
     * 根据主键删除资质证书借阅信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("/{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/aptitudeBorrow/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = aptitudeBorrowService.delete(id);
        if (rs) {
            response.setMessage("资质证书借阅信息删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("资质证书借阅信息删除异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/aptitudeBorrow/delete");
        return response;
    }

}
