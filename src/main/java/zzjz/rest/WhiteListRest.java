package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.WhiteList;
import zzjz.bean.WhiteListRequest;
import zzjz.service.WhiteListService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月08日16:45
 * @ClassName: WhiteListRest
 * @Description: 白名单信息管理rest
 */
@Component
@Path("whiteList")
public class WhiteListRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(WhiteListRest.class);

    @Autowired
    WhiteListService whiteListService;

    /**
     * 分页获取白名单信息.
     *
     * @param request 请求信息
     * @return 考勤列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<WhiteList> getWhiteListPage(WhiteListRequest request) {
        LOGGER.info("开始调用/whiteList/page");
        BaseResponse<WhiteList> response = new BaseResponse<>();
        Page<WhiteList> pageList = whiteListService.getWhiteListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) pageList.getTotal());
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(pageList);
        response.setOtherData(otherData);
        LOGGER.info("调用完成/whiteList/page");
        return response;
    }

    /**
     * 保存白名单信息.
     *
     * @param whiteList 白名单信息
     * @return 结果
     */
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> add(WhiteList whiteList) {
        LOGGER.info("开始调用/whiteList/save");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs;
        if (whiteList.getId() == null) {
            rs = whiteListService.add(whiteList);
        } else {
            rs = whiteListService.update(whiteList);
        }
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("白名单保存成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("白名单保存失败！");
        }
        LOGGER.info("调用完成/whiteList/save");
        return response;
    }

    /**
     * 删除白名单信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/whiteList/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = whiteListService.delete(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("白名单删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("白名单删除失败！");
        }
        LOGGER.info("调用完成/whiteList/delete");
        return response;
    }

    /**
     * 根据主键获取白名单信息.
     *
     * @param id 主键
     * @return 白名单信息
     */
    @Path("{id}/getDicInfo")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WhiteList getWhiteListInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/whiteList/" + id + "/getDicInfo");
        WhiteList whiteList = whiteListService.getWhiteListInfoById(id);
        LOGGER.info("调用完成/whiteList/getDicInfo");
        return whiteList;
    }
}
