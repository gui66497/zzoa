package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import zzjz.bean.BaseResponse;
import zzjz.bean.Catalog;
import zzjz.bean.Resource;
import zzjz.bean.ResultCode;
import zzjz.service.CatalogService;
import zzjz.service.ResourceService;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/8/28 13:46
 * @ClassName: ResourceRest
 * @Description: 资源管理rest
 */
@Component
@Path("catalog")
public class CatalogRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogRest.class);

    @Context
    HttpServletRequest httpServletRequest;

    @Autowired
    ResourceService resourceService;

    @Autowired
    CatalogService catalogService;

    /**
     * 获取所有目录信息.
     * @return 目录信息
     */
    @Path("/list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Catalog> getAll() {
        LOGGER.info("开始调用/catalog/list");
        BaseResponse<Catalog> response = new BaseResponse<>();
        List<Catalog> catalogList = catalogService.getAllCatalog();

        response.setData(catalogList);
        response.setMessage("获取成功！");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/catalog/list");
        return response;
    }

    /**
     * 获取指定目录id下的所有目录信息.
     *
     * @param catalogId 目录id
     * @return 目录信息
     */
    @Path("{catalogId}/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Catalog> getByCatalogId(@PathParam("catalogId") int catalogId) {
        LOGGER.info("开始调用/catalog/" + catalogId + "/list");
        BaseResponse<Catalog> response = new BaseResponse<>();
        List<Catalog> catalogList = catalogService.queryCataByCatalogId(catalogId);

        response.setData(catalogList);
        response.setMessage("获取成功！");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/catalog/list");
        return response;
    }

    /**
     * 新增目录信息.
     *
     * @param catalog 目录信息
     * @return 结果
     */
    @Path("/catalogAdd")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addCatalog(Catalog catalog) {
        LOGGER.info("开始调用/catalog/catalogAdd");
        BaseResponse<String> response = new BaseResponse<>();
        Long staffId = (Long) httpServletRequest.getSession().getAttribute("staffId");
        catalog.setCreateStaff(staffId);
        boolean rs = catalogService.add(catalog);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("新增目录信息成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("新增目录信息失败");
        }
        LOGGER.info("调用完成/catalog/catalogAdd");
        return response;
    }

    /**
     * 修改目录信息.
     *
     * @param catalog 目录信息
     * @return 结果
     */
    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateCatalogInfo(Catalog catalog) {
        LOGGER.info("开始调用/catalog/update");
        BaseResponse<String> response = new BaseResponse<>();

        boolean rs = catalogService.updateCatalog(catalog);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("修改成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改失败！");
        }
        LOGGER.info("调用完成/catalog/update");
        return response;
    }

    /**
     * 删除目录信息.
     *
     * @param id id
     * @return 结果
     */
    @Path("{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteCatalog(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/catalog/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        List<Resource> resourceList = resourceService.queryResByCatalogIdAndStaffId(id, null);
        if (CollectionUtils.isEmpty(resourceList)) {
            boolean rs = false;
            try {
                rs = catalogService.deleteById(id);
            } catch (IOException e) {
                e.printStackTrace();
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("文件(夹)不存在！");
                return response;
            }
            if (rs) {
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                response.setMessage("删除成功！");
            } else {
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("删除失败！");
            }
        } else {
            response.setMessage("该目录下存在文件，无法删除！");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/catalog/delete");
        return response;
    }

}
