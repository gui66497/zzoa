package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.pagehelper.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Provider;
import zzjz.bean.ProviderRequest;
import zzjz.bean.ResultCode;
import zzjz.service.ProviderService;
import zzjz.util.DateUtil;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * 供应商rest.
 *
 * @author fangguitang
 * @version 2017/11/2 9:38
 */
@Component
@Path("/provider")
public class ProviderRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderRest.class);

    private String exportPath = "D:/downloadFiles/";

    @Autowired
    ProviderService providerService;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 分页获取供应商信息.
     *
     * @param request 请求信息
     * @return 结果
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Provider> getProviderPage(ProviderRequest request) {
        LOGGER.info("开始调用/provider/page");
        BaseResponse<Provider> response = new BaseResponse<>();
        Page<Provider> providerPage = providerService.getProviderPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) providerPage.getTotal());
        response.setData(providerPage);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/provider/page");
        return response;
    }

    /**
     * 获取所有供应商信息列表.
     *
     * @return 结果列表
     */
    @Path("/allList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Provider> getAllProviderList() {
        LOGGER.info("开始调用/provider/AllList");
        BaseResponse<Provider> response = new BaseResponse<>();
        List<Provider> providerPage = providerService.getAllProviderList();
        response.setData(providerPage);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/provider/AllList");
        return response;
    }

    /**
     * 保存供应商信息.
     *
     * @param provider 供应商信息
     * @param headers  头信息
     * @return 列表
     */
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addProvider(Provider provider, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/provider/save");
        BaseResponse<String> response = new BaseResponse<>();
        String staffId = servletRequest.getSession().getAttribute("staffId").toString();
        provider.setCreateUser(staffId);
        Integer id = provider.getId();
        if (id == null) {
            providerService.addProvider(provider);
        } else {
            providerService.updateProvider(provider);
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("保存成功！");
        LOGGER.info("调用完成/provider/save");
        return response;
    }

    /**
     * 删除供应商信息.
     *
     * @param id id
     * @return 结果
     */
    @DELETE
    @Path("/{id}/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteProvider(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/provider/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = providerService.deleteProviderById(id);
        if (rs) {
            response.setMessage("删除供应商信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除供应商信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/provider/delete");
        return response;
    }

    /**
     * 导入供应商信息.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Provider> upload(@FormDataParam("file") InputStream fileInputStream,
                                         @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        LOGGER.info("开始调用/provider/upload");
        BaseResponse<Provider> response = new BaseResponse<>();
        ImportParams importParams = new ImportParams();
        List<Provider> providerList = null;
        try {
            providerList = ExcelImportUtil.importExcel(fileInputStream, Provider.class, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert providerList != null;
        //过滤掉空的数据
        Iterator<Provider> it = providerList.iterator();
        while (it.hasNext()) {
            Provider provider = it.next();
            if (StringUtils.isBlank(provider.getName())) {
                it.remove();
            }
        }
        boolean rs = providerService.batchInsert(providerList);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("上传成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("上传失败！");
        }
        LOGGER.info("调用完成/staff/upload");
        return response;
    }

    /**
     * 导出供应商信息.
     *
     * @param request       请求信息
     * @param response      返回信息
     * @param providerRequest providerRequest
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                ProviderRequest providerRequest) throws IOException {
        LOGGER.info("开始调用/provider/export");
        List<Provider> providerList = providerService.getProviderPage(providerRequest);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(
                "供应商信息", "供应商信息", "供应商信息"), Provider.class, providerList);
        File file = new File(exportPath);
        if (!file.exists()) {
            boolean rs = file.mkdirs();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        response.addHeader("filename", URLEncoder.encode("供应商信息_" + DateUtil.getIdByDate() + "_export.xls", "utf-8"));
        LOGGER.info("调用完成/provider/export");
        return bos.toByteArray();
    }
}
