package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Dic;
import zzjz.bean.DicRequest;
import zzjz.bean.ResultCode;
import zzjz.service.DicService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年03月31日10:20
 * @ClassName: DicRest
 * @Description: 字典项rest
 */
@Component
@Path("dic")
public class DicRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DicRest.class);

    @Autowired
    DicService dicService;

    /**
     * 根据字典项编号获取字典信息.
     *
     * @param dicId 字典项编号
     * @return 字典信息
     */
    @Path("{dicId}/getDicInfo")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Dic> getDicInfo(@PathParam("dicId") String dicId) {
        LOGGER.info("开始调用/dic/" + dicId + "/getDicInfo");
        BaseResponse<Dic> response = new BaseResponse<>();
        List<Dic> list = dicService.geDicByDicId(dicId);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(list);
        LOGGER.info("调用完成/dic/getDicInfo");
        return response;
    }

    /**
     * 分页获取字典项信息.
     *
     * @param request 请求
     * @return 字典信息
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Dic> getDicPage(DicRequest request) {
        LOGGER.info("开始调用/dic/page");
        BaseResponse<Dic> response = new BaseResponse<>();
        Page<Dic> pageList = dicService.getDicPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) pageList.getTotal());
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(pageList);
        response.setOtherData(otherData);
        LOGGER.info("调用完成/dic/page");
        return response;
    }

    /**
     * 新增字典项信息.
     *
     * @param dic 字典项信息
     * @return 结果
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addDicInfo(Dic dic) {
        LOGGER.info("开始调用/dic/add");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = dicService.addDicInfo(dic);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("字典项新增成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("字典项新增失败！");
        }
        LOGGER.info("调用完成/dic/add");
        return response;
    }

    /**
     * 修改字典项信息.
     *
     * @param dic 字典项信息
     * @return 结果
     */
    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateDicInfo(Dic dic) {
        LOGGER.info("开始调用/dic/upload");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = dicService.updateDicInfo(dic);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("字典项修改成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("字典项修改失败！");
        }
        LOGGER.info("调用完成/dic/upload");
        return response;
    }

    /**
     * 删除字典项信息.
     *
     * @param dicId  字典项编号
     * @param dicKey dicKey
     * @return 结果
     */
    @Path("{dicId}/{dicKey}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteDicInfo(@PathParam("dicId") String dicId, @PathParam("dicKey") String dicKey) {
        LOGGER.info("开始调用/dic/" + dicId + "/" + dicKey + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = dicService.deleteDicInfo(dicId, dicKey);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("字典项删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("字典项删除失败！");
        }
        LOGGER.info("调用完成/dic/delete");
        return response;
    }

    /**
     * 查询字典项所有ID.
     *
     * @return 列表
     */
    @Path("/title")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Dic> getDicTitleList() {
        LOGGER.info("开始调用/dic/title");
        BaseResponse<Dic> response = new BaseResponse<>();
        List<Dic> list = dicService.getDicTitleList();
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("成功！");
        LOGGER.info("调用完成/dic/title");
        return response;
    }
}
