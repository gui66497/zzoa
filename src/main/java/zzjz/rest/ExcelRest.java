package zzjz.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.util.DateUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelRest
 *
 * @author fgt
 * @version 2017/2/20 17:04
 */
@Component
@Path("/excel")
public class ExcelRest {

    private final static Logger logger = LoggerFactory.getLogger(ExcelRest.class);

    /**
     * 上传测试
     * @param fileInputStream
     * @param contentDispositionHeader
     * @return
     * @throws IOException
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        logger.info("开始调用/excel/down");
        BaseResponse<String> response = new BaseResponse<>();
        String fileName = contentDispositionHeader.getFileName();
        System.out.println(contentDispositionHeader.getSize());
        System.out.println(contentDispositionHeader.getType());

        ImportParams importParams = new ImportParams();
        try {
            List<StaffExcel> res = ExcelImportUtil.importExcel(fileInputStream, StaffExcel.class, importParams);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int pos = fileName.lastIndexOf(".");
        String newFileName = fileName.substring(0, pos) + "_" + DateUtil.getRandomFileName() + fileName.substring(pos);
        String path = "D:/excel/" + newFileName;
        File distFile = new File(path);
        FileUtils.copyInputStreamToFile(fileInputStream, distFile);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("上传成功！");
        return response;
    }

    /**
     * 下载测试
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @POST
    @Path("down")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        logger.info("开始调用/excel/down");
        TemplateExportParams params = new TemplateExportParams("D:\\zzjz_work\\zoa3\\src\\main\\resources\\docs\\专项支出用款申请书.xls");
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2017.2.20");
        map.put("person", "小龙1");
        map.put("phone", "12345");
        map.put("company", "直真");
        Workbook book = ExcelExportUtil.exportExcel(params, map);
        File file = new File("D:/excel/");
        if (!file.exists()) {
            file.mkdirs();
        }
        //FileOutputStream fos = new FileOutputStream("D:/excel/ttt2.xls");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        book.write(bos);

        //response.setHeader("Content-Disposition","attachment;filename=ttt2.xls");//为文件命名
        //response.addHeader("content-type","application/vnd.ms-excel");
        response.addHeader("filename","ttt3.xls");//文件命名
        return bos.toByteArray();
        //fos.close();
    }
}
