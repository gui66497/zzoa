package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.Aptitude;
import zzjz.bean.AptitudeRequest;
import zzjz.bean.BaseResponse;
import zzjz.bean.Column;
import zzjz.bean.ResultCode;
import zzjz.service.AptitudeService;
import zzjz.util.PageUtil;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 彭鹏
 * @version 2017年12月14日17:00
 * @ClassName: AptitudeRest
 * @Description: 资质证书信息管理rest
 */
@Component
@Path("aptitude")
public class AptitudeRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AptitudeRest.class);

    private static final Integer NUM2048 = 2048;

    private String exportPath = "D:/downloadFiles/";

    @Autowired
    AptitudeService aptitudeService;

    /**
     * 分页获取资质证书信息.
     *
     * @param request request
     * @return 资质证书表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Aptitude> getPageList(AptitudeRequest request) {
        LOGGER.info("开始调用/aptitude/page");
        BaseResponse<Aptitude> response = new BaseResponse<>();
        Page<Aptitude> list = aptitudeService.getAptitudePageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) list.getTotal());
        response.setData(list);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitude/page");
        return response;
    }

    /**
     * 获取资质证书信息列表.
     *
     * @param aptitude 资质证书信息
     * @return 资质证书列表
     */
    @Path("/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Aptitude> getAptitudeList(Aptitude aptitude) {
        LOGGER.info("开始调用/aptitude/list");
        BaseResponse<Aptitude> response = new BaseResponse<>();
        List<Aptitude> list = aptitudeService.getAptitudeList(aptitude);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitude/list");
        return response;
    }

    /**
     * 获取有效资质证书信息列表.
     *
     * @param userId 员工号
     * @return 资质证书信息列表
     */
    @Path("/{userId}/effectiveList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Aptitude> getEffectiveAptitudeList(@PathParam("userId") String userId) {
        LOGGER.info("开始调用/aptitude/effectiveList");
        BaseResponse<Aptitude> response = new BaseResponse<>();
        List<Aptitude> list = aptitudeService.getEffectiveAptitudeList(userId);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitude/effectiveList");
        return response;
    }

    /**
     * 获取失效资质证书信息列表.
     *
     * @param userId 员工号
     * @return 资质证书信息列表
     */
    @Path("/{userId}/invalidList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Aptitude> getInvalidAptitudeList(@PathParam("userId") String userId) {
        LOGGER.info("开始调用/aptitude/invalidList");
        BaseResponse<Aptitude> response = new BaseResponse<>();
        List<Aptitude> list = aptitudeService.getInvalidAptitudeList(userId);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/aptitude/invalidList");
        return response;
    }

    /**
     * 根据主键获取资质证书信息.
     *
     * @param id 主键
     * @return 资质证书信息
     */
    @Path("/{id}/getAptitudeInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Aptitude getAptitudeInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/aptitude/" + id + "/getAptitudeInfo");
        Aptitude aptitude = aptitudeService.query(id);
        LOGGER.info("调用完成/aptitude/getAptitudeInfo");
        return aptitude;
    }

    /**
     * 保存资质证书信息.
     *
     * @param aptitude 资质证书信息
     * @return 结果
     */
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(Aptitude aptitude) {
        LOGGER.info("开始调用/aptitude/save");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs;
        if (aptitude.getId() == null) {
            rs = aptitudeService.add(aptitude);
        } else {
            rs = aptitudeService.update(aptitude);
        }
        if (rs) {
            response.setMessage("资质证书信息保存成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("资质证书信息保存异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/aptitude/save");
        return response;
    }

    /**
     * 根据主键删除资质证书信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("/{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/aptitude/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = aptitudeService.delete(id);
        if (rs) {
            response.setMessage("资质证书信息删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("资质证书信息删除异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/aptitude/delete");
        return response;
    }

    /**
     * 获取所有列.
     *
     * @return 列信息
     */
    @Path("/allCol")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Column> getAllColumn() {
        LOGGER.info("开始调用/aptitude/allCol");
        BaseResponse<Column> response = new BaseResponse<>();
        List<Column> allColumn = aptitudeService.getAllColumn();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(allColumn);
        LOGGER.info("调用完成/aptitude/allCol");
        return response;
    }

    /**
     * 导出资质信息(支持列定制).
     *
     * @param request      请求信息
     * @param response     返回信息
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletResponse response, AptitudeRequest request) throws IOException {
        LOGGER.info("开始调用/aptitude/export");
        String exportStr = request.getExportStr();
        assert StringUtils.isNotBlank(exportStr);
        String[] exportArr = exportStr.split(",");
        List<ExcelExportEntity> entities = aptitudeService.getExcelExportEntityByArr(exportArr);
        List<Map<String, Object>> aptitudeMap = aptitudeService.getAptitudeMap(request);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(
                "资质信息", "资质信息", "资质信息"), entities, aptitudeMap);
        File file = new File(exportPath);
        if (!file.exists()) {
            boolean rs = file.mkdirs();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        response.addHeader("filename", URLEncoder.encode("资质信息导出.xls", "utf-8"));
        LOGGER.info("调用完成/aptitude/export");
        return bos.toByteArray();
    }

    /**
     * 上传.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @param id                       主键
     * @param fileTyp                  文件类型
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("{id}/{fileTyp}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                       @PathParam("id") Integer id, @PathParam("fileTyp") String fileTyp) throws IOException {
        LOGGER.info("开始调用/aptitude/" + id + "/" + fileTyp + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Aptitude aptitude = aptitudeService.query(id);
        String fileName = aptitude.getUserName() + "_" + aptitude.getCertificateName() + "_" + sdf.format(new Date()) + fileTyp;
        aptitude.setFileName(fileName);
        aptitudeService.update(aptitude);
        FileOutputStream out = new FileOutputStream(new File(props.getProperty("project.path")
                + "src\\main\\resources\\Files\\certificate\\" + fileName));
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(fileInputStream);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[NUM2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        out.close();
        LOGGER.info("调用完成/aptitude/upload");
        return response;
    }


}
