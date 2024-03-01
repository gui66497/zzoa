package zzjz.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Catalog;
import zzjz.bean.Group;
import zzjz.bean.Resource;
import zzjz.bean.ResultCode;
import zzjz.service.CatalogService;
import zzjz.service.GroupService;
import zzjz.service.ResourceService;
import zzjz.service.ZipService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/8/28 13:46
 * @ClassName: ResourceRest
 * @Description: 资源管理rest
 */
@Component
@Path("resource")
public class ResourceRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRest.class);

    @Context
    HttpServletRequest httpServletRequest;

    @Autowired
    ResourceService resourceService;

    @Autowired
    ZipService zipService;

    @Autowired
    GroupService groupService;

    @Autowired
    CatalogService catalogService;

    @Value("${resource.path}")
    String rootPath;

    /**
     * 获取指定目录id下的所有资源信息.
     *
     * @param catalogId 目录ID
     * @param staffId   用户ID
     * @return 资源列表
     */
    @Path("{catalogId}/{staffId}/resourceList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Resource> getResourcePage(@PathParam("catalogId") int catalogId,
                                                  @PathParam("staffId") String staffId) {
        LOGGER.info("开始调用/resource/" + catalogId + "/" + staffId + "/resourceList");
        if (StringUtils.isBlank(staffId)) {
            staffId = null;
        }
        BaseResponse<Resource> response = new BaseResponse<>();
        //改为显示所有文件 不是是显示自己上传的
        /*List<Resource> resourceList = resourceService.queryResByCatalogIdAndStaffId(catalogId, staffId);*/
        List<Resource> resourceList = resourceService.queryResByCatalogIdAndStaffId(catalogId, null);
        response.setData(resourceList);
        response.setMessage("获取成功！");
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/resource/resourceList");
        return response;
    }

    /**
     * 根据任务主键获取资源信息.
     *
     * @param id 任务主键
     * @return 资源信息
     */
    @Path("{id}/query")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Resource getResourceById(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/resource/" + id + "/query");
        Resource resource = resourceService.queryResourceById(id);
        LOGGER.info("调用完成/resource/query");
        return resource;
    }

    /**
     * 新增资源文件信息.
     *
     * @param resource 资源文件信息
     * @return 结果
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Resource> addResource(Resource resource) {
        LOGGER.info("开始调用/resource/add");
        BaseResponse<Resource> response = new BaseResponse<>();
        Long staffId = (Long) httpServletRequest.getSession().getAttribute("staffId");
        resource.setStaffId(staffId);
        Resource result = resourceService.add(resource);
        if (result != null) {
            response.setObj(result);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("新增资源文件成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("新增资源文件失败");
        }
        LOGGER.info("调用完成/resource/add");
        return response;
    }

    /**
     * 修改资源(目前只修改了文件名).
     *
     * @param resource 资源实体
     * @return 结果
     */
    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateResource(Resource resource) {
        LOGGER.info("开始调用/resource/update");
        BaseResponse<String> response = new BaseResponse<>();

        String newFileName = null;
        //先修改真实文件名
        try {
            newFileName = resourceService.renameResource(resource);
        } catch (IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改资源信息失败:" + e.getMessage());
            return response;
        }
        if (newFileName != null) {
            //再修改数据库中的资源信息
            resource.setFileName(newFileName);
            boolean rs = resourceService.updateResource(resource);
            if (rs) {
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                response.setMessage("修改资源信息成功！");
            } else {
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("修改资源信息失败！");
            }
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改资源信息失败！");
        }

        LOGGER.info("调用完成/resource/update");
        return response;
    }

    /**
     * 修改资源(修改全部信息).
     *
     * @param resource 资源实体
     * @return 结果
     */
    @Path("/updateRes")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateResourceAll(Resource resource) {
        LOGGER.info("开始调用/resource/updateRes");
        BaseResponse<String> response = new BaseResponse<>();

        //如果修改了文件名 先要走修改文件名的那一套
        Resource originResource = resourceService.queryResourceById(resource.getId());
        String newFileName = "";
        if (originResource.getName().equals(resource.getName())) {
            newFileName = "";
        } else {
            try {
                newFileName  = resourceService.renameResource(resource);
            } catch (IOException e) {
                e.printStackTrace();
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("修改资源信息失败！");
                return response;
            }
        }
        if (newFileName == null) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改资源信息失败！");
            return response;
        } else if (!"".equals(newFileName)) {
            resource.setFileName(newFileName);
        }
        boolean rs = resourceService.updateResourceAll(resource);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("修改资源信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改资源信息失败！");
        }
        LOGGER.info("调用完成/resource/update");
        return response;
    }

    /**
     * 上传.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @param resourceId  资源id
     * @return 结果
     */
    @POST
    @Path("{resourceId}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                       @PathParam("resourceId") int resourceId) {
        LOGGER.info("开始调用/resource/" + resourceId + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        Resource resource = resourceService.queryResourceById(resourceId);
        boolean res = false;

        File file = new File(resource.getFileName());
        //文件目录
        String path = file.getParent();
        File parentCatalog = new File(path);

        if (!parentCatalog.exists()) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("上传失败，文件目录缺失！");
            return response;
        }
        try {
            res = zipService.zip(path, file.getName(), fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("上传失败：" + e.getMessage());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("上传成功");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("上传失败");
        }
        LOGGER.info("调用完成/resource/upload");
        return response;
    }

    /**
     * 删除资源文件.
     * @param id id
     * @return 结果
     */
    @Path("{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteResource(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/resource/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = resourceService.deleteById(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        LOGGER.info("调用完成/resource/delete");
        return response;
    }

    /**
     * 下载前校验.
     *
     * @param resourceId 资源id
     * @return 结果
     */
    @Path("{resourceId}/preDown")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> preDown(@PathParam("resourceId") int resourceId) {
        LOGGER.info("开始调用/resource/" + resourceId + "/preDown");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = String.valueOf(httpServletRequest.getSession().getAttribute("staffId"));
        if (StringUtils.isBlank(staffId)) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("未登录！");
            return response;
        }
        Resource resource = resourceService.queryResourceById(resourceId);
        //是公开文件
        if ("public".equals(resource.getType())) {
            response.setMessage("是公开文件,可以下载");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            //是上传者
            if (staffId.equals(String.valueOf(resource.getStaffId()))) {
                response.setMessage("是上传者,可以下载");
                response.setResultCode(ResultCode.RESULT_SUCCESS);
            } else {
                if ("secret".equals(resource.getType())) {
                    response.setMessage("非公开文件,不可以下载");
                    response.setResultCode(ResultCode.RESULT_ERROR);
                } else {
                    //是指定组公开的情况
                    //判断是否在可下载组中
                    boolean inGroup = false;
                    String[] downGroup = resource.getType().split(",");
                    List<Group> staffGroups = groupService.queryGroupByStaffId(staffId);
                    for (Group group : staffGroups) {
                        if (Arrays.binarySearch(downGroup, String.valueOf(group.getGroupId())) > -1) {
                            inGroup = true;
                            break;
                        }
                    }
                    if (inGroup) {
                        response.setMessage("在下载组中,可以下载");
                        response.setResultCode(ResultCode.RESULT_SUCCESS);
                    } else {
                        response.setMessage("不在下载组中,不可以下载");
                        response.setResultCode(ResultCode.RESULT_ERROR);
                    }
                }
            }
        }
        LOGGER.info("调用完成/resource/preDown");
        return response;
    }

    /**
     * 下载.
     *
     * @param request    request
     * @param response   response
     * @param resourceId 资源ID
     * @return 文件流
     */
    @POST
    @Path("{resourceId}/down")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                @PathParam("resourceId") int resourceId) {
        LOGGER.info("开始调用/resource/" + resourceId + "/down");
        Resource resource = resourceService.queryResourceById(resourceId);
        String filePath = resource.getFileName();
        File file = new File(filePath);
        String path = file.getParent();
        byte[] resBytes = null;
        try {
            resBytes = zipService.unZip(path, file.getName());
            response.addHeader("filename", URLEncoder.encode(file.getName(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("下载出错：" + e.getMessage());
        }
        LOGGER.info("调用完成/resource/down");
        return resBytes;
    }

    /**
     * 移动资源.
     * @param resourceId    资源实体id
     * @param catalogId     目标目录id
     * @return 结果
     */
    @Path("{resourceId}/{catalogId}/move")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> moveResource(@PathParam("resourceId") int resourceId, @PathParam("catalogId") int catalogId) {
        LOGGER.info("开始调用/resource/{resourceId}/{catalogId}/move");
        BaseResponse<String> response = new BaseResponse<>();

        //先移动资源文件 成功的话再修改数据库巨记录
        Resource resource = resourceService.queryResourceById(resourceId);
        Catalog targetCatalog = catalogService.queryById(catalogId);

        String targetPath = catalogService.recursion(targetCatalog, new ArrayList<String>());
        if (StringUtils.isNotBlank(targetPath)) {
            targetPath = rootPath + File.separator + targetPath;
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("移动资源信息失败,计算目标文件夹路径失败");
            return response;
        }

        boolean rs = false;

        boolean moveRs = false;
        try {
            moveRs = resourceService.moveResource(resource, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("移动资源信息失败:" + e.getMessage());
            return response;
        }
        if (moveRs) {
            //修改belong和filename
            resource.setBelong(targetCatalog.getId());
            resource.setFileName(targetPath + File.separator + resource.getName());
            rs = resourceService.updateResourceAll(resource);
        }

        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("移动资源信息成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("移动资源信息失败！");
        }
        LOGGER.info("调用完成/resource/{resourceId}/{catalogId}/move");
        return response;
    }

}
