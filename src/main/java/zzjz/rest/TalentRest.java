package zzjz.rest;

import com.github.pagehelper.Page;
import com.google.common.io.Files;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.Talent;
import zzjz.bean.TalentRequest;
import zzjz.service.TalentService;
import zzjz.util.Office2PDF;
import zzjz.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author 彭鹏.
 * @version 2017/7/6
 */
@Component
@Path("talent")
public class TalentRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TalentRest.class);

    private static final Integer NUM3 = 3;

    private static final Integer NUM1024 = 1024;

    private static final Integer NUM2048 = 2048;

    @Context
    HttpServletRequest httpServletRequest;

    @Autowired
    TalentService talentService;

    @Value("${resume.path}")
    String resumePath;

    @Value("${resumeHtml.path}")
    String resumeHtml;

    /**
     * 分页获取人才信息列表.
     *
     * @param request 人才信息请求
     * @return 人才信息列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Talent> getTalentPage(TalentRequest request) {
        LOGGER.info("开始调用/talent/page");
        BaseResponse<Talent> response = new BaseResponse<>();
        Page<Talent> pageList = talentService.queryTalentPageList(request);

        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) pageList.getTotal());
        response.setData(pageList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/talent/page");
        return response;
    }

    /**
     * 根据主键获取人才信息.
     *
     * @param id 主键
     * @return 人才信息
     */
    @Path("{id}/query")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Talent getTalentById(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/talent/" + id + "/query");
        Talent talent = talentService.queryTalentById(id);
        LOGGER.info("调用完成/talent/query");
        return talent;
    }

    /**
     * 添加人才信息.
     *
     * @param talent 人才信息
     * @return 结果
     * @throws IOException IOException
     */
    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addTalentInfo(Talent talent) throws IOException {
        LOGGER.info("开始调用/talent/add");
        BaseResponse<String> response = new BaseResponse<>();
        Talent rs = talentService.addTalentInfo(talent);
        if (rs != null) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage(rs.getResumeName());
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("添加失败！");
        }
        LOGGER.info("调用完成/talent/add");
        return response;
    }

    /**
     * 修改人才信息.
     *
     * @param talent 人才信息
     * @return 结果
     */
    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateTalentInfo(Talent talent) {
        LOGGER.info("开始调用/talent/update");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = talentService.updateTalentInfo(talent);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("修改成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("修改失败！");
        }
        LOGGER.info("调用完成/talent/update");
        return response;
    }

    /**
     * 根据主键删除人才信息.
     *
     * @param id 主键
     * @return 结果
     */
    @Path("{id}/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> deleteTalent(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/talent/" + id + "/delete");
        BaseResponse<String> response = new BaseResponse<>();
        boolean rs = talentService.deleteTalentById(id);
        if (rs) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("删除成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("删除失败！");
        }
        LOGGER.info("调用完成/talent/delete");
        return response;
    }

    /**
     * 上传.
     *
     * @param fileName                 文件名
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("{fileName}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                       @PathParam("fileName") String fileName) throws IOException {
        LOGGER.info("开始调用/talent/" + fileName + "/upload");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        FileOutputStream out = new FileOutputStream(new File(props.getProperty("resume.path") + fileName));
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
        LOGGER.info("调用完成/talent/upload");
        return response;
    }

    /**
     * 批量上传.
     *
     * @param fileInputStream          输入文件流
     * @param contentDispositionHeader 文件属性信息
     * @param source                   简历来源
     * @param type                     简历类型
     * @param keyword                  关键字
     * @param note                     备注
     * @param fileTyp                  文件类型
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("{source}/{type}/{keyword}/{note}/{fileTyp}/batchUpload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> batchUpload(@FormDataParam("file") InputStream fileInputStream,
                                            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
                                            @PathParam("source") String source, @PathParam("type") String type, @PathParam("keyword") String keyword,
                                            @PathParam("note") String note, @PathParam("fileTyp") String fileTyp) throws IOException {
        LOGGER.info("开始调用/talent/" + source + "/" + type + "/" + keyword + "/" + note + "/" + fileTyp + "/batchUpload");
        BaseResponse<String> response = new BaseResponse<>();
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        String fileName = props.getProperty("resume.path") + "简历." + fileTyp;
        FileOutputStream out = new FileOutputStream(new File(fileName));
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

        //如果是zip文件，解压缩
        if ("zip".equals(fileTyp)) {
            Charset gbk = Charset.forName("gbk");
            String zipFileName = fileName;
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFileName), gbk); //输入源zip路径
            BufferedInputStream bin = new BufferedInputStream(zin);
            File fout = null;
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
                fileName = props.getProperty("resume.path") + "简历." + entry.getName().split("\\.")[1];
                fout = new File(fileName);
                if (!fout.exists()) {
                    boolean rs = (new File(fout.getParent())).mkdirs();
                    LOGGER.info("创建文件:" + fout.getParent() + (rs == true ? "成功" : "失败"));
                }
                FileOutputStream out1 = new FileOutputStream(fout);
                BufferedOutputStream bout = new BufferedOutputStream(out1);
                int b;
                while ((b = bin.read()) != -1) {
                    bout.write(b);
                }
                bout.close();
                out1.close();
                LOGGER.info(fout + "解压成功");

                String rename = talentService.addFile(fileName, source, type, keyword, note);
                File file = new File(fileName);
                if (!"".equals(rename)) {
                    boolean rs = file.renameTo(new File(props.getProperty("resume.path") + rename));
                    LOGGER.info("重命名:" + rs);
                } else {
                    boolean rs = file.delete();
                    LOGGER.info("删除文件:" + file + (rs == true ? "成功" : "失败"));
                }
            }
            bin.close();
            zin.close();
            //删除压缩文件
            File file = new File(zipFileName);
            if (file.exists()) {
                boolean rs = file.delete();
                LOGGER.info("删除文件:" + file + (rs == true ? "成功" : "失败"));
            }
        } else {
            String rename = talentService.addFile(fileName, source, type, keyword, note);
            File file = new File(fileName);
            if (!"".equals(rename)) {
                boolean rs = file.renameTo(new File(props.getProperty("resume.path") + rename));
                LOGGER.info("重命名:" + rs);
            } else {
                boolean rs = file.delete();
                LOGGER.info("删除文件:" + file + (rs == true ? "成功" : "失败"));
            }
        }
        LOGGER.info("调用完成/talent/batchUpload");
        return response;
    }

    /**
     * 下载.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param id       主键
     * @return byte
     * @throws IOException IOException
     */
    @POST
    @Path("{id}/down")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response,
                                @PathParam("id") Integer id) throws IOException {
        LOGGER.info("开始调用/talent/" + id + "/down");
        Properties props = new Properties();
        try {
            props.load(TalentRest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }

        Talent talent = talentService.queryTalentById(id);
        String fileName = talent.getResumeName();
        String fileTyp = fileName.split("\\.")[1];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        InputStream fis = new BufferedInputStream(new FileInputStream(props.getProperty("resume.path") + fileName));
        byte[] buffer = new byte[NUM3];
        int len = 0;
        while ((len = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        fis.close();

        response.addHeader("filename", fileTyp); //文件命名
        LOGGER.info("调用完成/talent/down");
        return bos.toByteArray();
    }

    /**
     * 目前支持的转换类型
     */
    private static String[] officeTypes = new String[]{"doc", "docx", "txt"};

    private static String[] imgTypes = new String[]{"jpg", "png"};

    private static String[] htmlTypes = new String[]{"html"};

    /**
     * 预览.
     *
     * @param id 输入文件流
     * @return 结果
     */
    @GET
    @Path("{id}/preview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> preview(@PathParam("id") Integer id) {
        LOGGER.info("开始调用/talent/" + id + "/preview");
        BaseResponse<String> response = new BaseResponse<>();

        //容器路径
        String containerPath = httpServletRequest.getSession().getServletContext().getRealPath("/");
        Talent talent = talentService.queryTalentById(id);

        String fileName = talent.getResumeName();
        //文件绝对路径
        String sourceFileStr = resumePath + fileName;
        //文件后缀
        String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
        //源文件
        File sourceFile = new File(sourceFileStr);
        if (!sourceFile.exists()) {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("源文件不存在，请核实！");
            return response;
        }
        //是图片类型
        if (Arrays.asList(imgTypes).contains(postfix)) {
            //将文件拷贝到容器中
            File imgOutFile = new File(containerPath + "images\\resume\\" + fileName);
            try {
                Files.copy(sourceFile, imgOutFile);
            } catch (IOException e) {
                e.printStackTrace();
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("图片拷贝失败：" + e.getMessage());
                return response;
            }
            response.setObj(fileName);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("该文件是图片类型，无需转换");
            return response;
        } else if (Arrays.asList(htmlTypes).contains(postfix)) { //html文件，复制文件至指定文件夹下
            boolean res = false;
            File destFile = new File(containerPath + "resumeHtml\\" + talent.getId() + "." + fileName.split("\\.")[1]);
            if (destFile.exists()) {
                boolean rs = new File(resumeHtml + fileName).delete();
                LOGGER.info("删除文件:" + resumeHtml + fileName + (rs == true ? "成功" : "失败"));
            } else {
                // 如果目标文件所在目录不存在，则创建目录
                if (!destFile.getParentFile().exists()) {
                    // 目标文件所在目录不存在
                    if (!destFile.getParentFile().mkdirs()) {
                        // 复制文件失败：创建目标文件所在目录失败
                        response.setResultCode(ResultCode.RESULT_ERROR);
                        response.setMessage("移动失败！");
                        return response;
                    }
                }
            }

            // 复制文件
            int byteread = 0; // 读取的字节数
            InputStream in = null;
            OutputStream out = null;

            try {
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                byte[] buffer = new byte[NUM1024];

                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                }
                res = true;
            } catch (FileNotFoundException e) {
                res = false;
            } catch (IOException e) {
                res = false;
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (res) {
                response.setObj(talent.getId() + "." + fileName.split("\\.")[1]);
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                response.setMessage("移动成功！");
            } else {
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("移动失败！");
            }
            return response;
        } else if (Arrays.asList(officeTypes).contains(postfix)) { //文档类型 使用openoffice转换
            String pdfFileName = fileName.split("\\.")[0] + ".pdf";

            String destFile = containerPath + "pdf\\" + pdfFileName;
            boolean res;
            try {
                res = Office2PDF.office2PDF(sourceFileStr, destFile);
            } catch (IOException e) {
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("转换失败：" + e.getMessage());
                e.printStackTrace();
                return response;
            }
            if (res) {
                response.setObj(pdfFileName);
                response.setResultCode(ResultCode.RESULT_SUCCESS);
                response.setMessage("转换成功！");
            } else {
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("转换失败！");
            }
            return response;
        } else if ("pdf".equals(postfix)) { //本身就是pdf类型
            File pdfOutFile = new File(containerPath + "pdf\\" + fileName);
            try {
                Files.copy(sourceFile, pdfOutFile);
            } catch (IOException e) {
                e.printStackTrace();
                response.setResultCode(ResultCode.RESULT_ERROR);
                response.setMessage("转换失败！");
                return response;
            }
            response.setObj(fileName);
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("转换成功！");
            return response;

        } else { //不支持的类型
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("预览失败，不支持的文件后缀：" + postfix);
            return response;
        }
    }


}
