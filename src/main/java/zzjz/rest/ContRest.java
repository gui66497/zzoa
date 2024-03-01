package zzjz.rest;

import com.github.pagehelper.Page;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Cont;
import zzjz.bean.ContRequest;
import zzjz.bean.ResultCode;
import zzjz.service.ContService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author 彭鹏
 * @version 2017年12月14日17:00
 * @ClassName: ContRest
 * @Description: 合同信息管理rest
 */
@Component
@Path("cont")
public class ContRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContRest.class);

    private static final Integer NUM2048 = 2048;

    @Autowired
    ContService contService;

    /**
     * 分页获取合同信息.
     *
     * @param request request
     * @return 合同列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Cont> getPageList(ContRequest request) {
        LOGGER.info("开始调用/cont/page");
        BaseResponse<Cont> response = new BaseResponse<>();
        Page<Cont> list = contService.getContPageList(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) list.getTotal());
        response.setData(list);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/cont/page");
        return response;
    }

    /**
     * 获取合同信息列表.
     *
     * @param cont 合同信息
     * @return 合同列表
     */
    @Path("/list")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Cont> getContList(Cont cont) {
        LOGGER.info("开始调用/cont/list");
        BaseResponse<Cont> response = new BaseResponse<>();
        List<Cont> list = contService.getContList(cont);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/cont/list");
        return response;
    }

    /**
     * 获取有效合同信息列表.
     *
     * @param userId 员工号
     * @return 合同列表
     */
    @Path("/{userId}/effectiveList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Cont> getEffectiveContList(@PathParam("userId") String userId) {
        LOGGER.info("开始调用/cont/effectiveList");
        BaseResponse<Cont> response = new BaseResponse<>();
        List<Cont> list = contService.getEffectiveContList(userId);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/cont/effectiveList");
        return response;
    }

    /**
     * 获取失效合同信息列表.
     *
     * @param userId 员工号
     * @return 合同列表
     */
    @Path("/{userId}/invalidList")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Cont> getInvalidContList(@PathParam("userId") String userId) {
        LOGGER.info("开始调用/cont/invalidList");
        BaseResponse<Cont> response = new BaseResponse<>();
        List<Cont> list = contService.getInvalidContList(userId);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/cont/invalidList");
        return response;
    }

    /**
     * 根据主键获取合同信息.
     *
     * @param id 主键
     * @return 合同信息
     */
    @Path("/{id}/getContInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Cont getContInfo(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/cont/" + id + "/getContInfo");
        Cont cont = contService.query(id);
        LOGGER.info("调用完成/cont/getContInfo");
        return cont;
    }

    /**
     * 保存合同信息.
     *
     * @param cont 合同信息
     * @return 结果
     */
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(Cont cont) {
        LOGGER.info("开始调用/cont/save");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs;
        if (cont.getId() == null) {
            rs = contService.add(cont);
        } else {
            rs = contService.update(cont);
        }
        if (rs) {
            response.setMessage("合同信息保存成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("合同信息保存异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/cont/save");
        return response;
    }

    /**
     * 根据主键删除合同信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("/{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/cont/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = contService.delete(id);
        if (rs) {
            response.setMessage("合同信息删除成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("合同信息删除异常");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/cont/delete");
        return response;
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
        LOGGER.info("开始调用/cont/" + id + "/" + fileTyp + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Cont cont = contService.query(id);
        String fileName = cont.getUserName() + "_" + cont.getContName() + "_" + sdf.format(new Date()) + fileTyp;
        cont.setFileName(fileName);
        contService.update(cont);
        FileOutputStream out = new FileOutputStream(new File(props.getProperty("project.path") + "cont\\" + fileName));
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
        LOGGER.info("调用完成/cont/upload");
        return response;
    }
}
