package zzjz.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.service.ZipService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 彭鹏
 * @version 2017年03月31日10:20
 * @ClassName: ActivityRest
 * @Description: 流程rest
 */
@Component
@Path("zip")
public class ZipRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZipRest.class);

    @Autowired
    ZipService zipService;

    /**
     * 压缩加密文件.
     *
     * @param fileInputStream          InputStream
     * @param contentDispositionHeader FormDataContentDisposition
     * @return BaseResponse
     * @throws IOException IOException
     * @author 彭鹏
     */
    @POST
    @Path("zip")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> zip(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        LOGGER.info("开始调用/zip/zip");
        BaseResponse<String> response = new BaseResponse<>();
        try {
            zipService.zip("F:", "scala01a.mp4", fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setMessage("压缩成功");
        LOGGER.info("调用完成/zip/zip");
        return response;
    }

    /**
     * 解压解密文件.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return byte
     * @author 彭鹏
     */
    @POST
    @Path("unzip")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] unZip(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        LOGGER.info("开始调用/zip/unzip");
        byte[] bytes = null;
        try {
            bytes = zipService.unZip("F:", "scala01a.mp4");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.addHeader("filename", "scala01a.mp4");
        LOGGER.info("调用完成/zip/unzip");
        return bytes;
    }
}
