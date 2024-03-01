package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.StaffExcel;
import zzjz.service.DepartService;
import zzjz.service.StaffService;
import zzjz.service.ZkemService;
import zzjz.util.PinyinUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelRest.
 *
 * @author fgt
 * @version 2017/2/20 17:04
 */
@Component
@Path("/excel")
public class ExcelRest {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExcelRest.class);

    private String downPath = "D:/excel/";

    @Autowired
    StaffService staffService;

    @Autowired
    DepartService departService;

    @Autowired
    ZkemService zkemService;

    /**
     * 上传测试.
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
    public BaseResponse<String> upload(@FormDataParam("file") InputStream fileInputStream,
                                       @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) throws IOException {
        LOGGER.info("开始调用/excel/upload");
        BaseResponse<String> response = new BaseResponse<>();
        /*System.out.println(contentDispositionHeader.getSize());
        System.out.println(contentDispositionHeader.getType());*/

        ImportParams importParams = new ImportParams();
        List<StaffExcel> res = null;
        try {
            res = ExcelImportUtil.importExcel(fileInputStream, StaffExcel.class, importParams);
            /*System.out.println(res);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert res != null;
        for (StaffExcel staff : res) {
            //时间戳
            staff.setStaffId(new Date().getTime());
            //根据身份证信息补全
            staff = staffService.completeStaffByIdNum(staff);
            //计算转正日期
            String entryDateStr = staff.getEntryDate();
            if (StringUtils.isNotBlank(entryDateStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    Date entryDate = sdf.parse(entryDateStr);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(entryDate);
                    calendar.add(Calendar.MONTH, 2);
                    String formalDateStr = sdf.format(calendar.getTime());
                    staff.setFormalDate(formalDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String name = staff.getName();
            //将名字的全拼作为用户名
            String userName = PinyinUtil.getInstance().getStringPinyin(name);
            staff.setUserName(userName);
            //默认密码12345
            staff.setPassword("12345");
            //设置staffId
            //String staffIdStr = StaffIdUtil.resMap.get(staff.getName());
            String staffIdStr = zkemService.getUserIdByName(staff.getName());
            if (StringUtils.isNotBlank(staffIdStr)) {
                staff.setStaffId(Long.parseLong(staffIdStr));
            }
            //设置部门
            String departName = staff.getDepartName();
            if (StringUtils.isNotBlank(departName)) {
                String departId = departService.getDepartIdByName(departName);
                staff.setDepartId(departId);
            }

        }
        boolean result = staffService.addStaffList(res);
        if (result) {
            response.setResultCode(ResultCode.RESULT_SUCCESS);
            response.setMessage("上传成功！");
        } else {
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage("上传失败！");
        }
        LOGGER.info("调用完成/excel/upload");
        return response;
    }

    /**
     * 下载测试.
     *
     * @param request  请求
     * @param response 结果
     * @return 结果
     * @throws IOException IOException
     */
    @POST
    @Path("down")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] downloadExcel(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
        LOGGER.info("开始调用/excel/down");
        TemplateExportParams params = new TemplateExportParams("D:\\project\\zzoa\\src\\main\\resources\\docs\\专项支出用款申请书.xls");
        Map<String, Object> map = new HashMap<>();
        map.put("date", "2017.2.20");
        map.put("person", "小龙1");
        map.put("phone", "12345");
        map.put("company", "直真");
        Workbook book = ExcelExportUtil.exportExcel(params, map);
        File file = new File(downPath);
        if (!file.exists()) {
            boolean rs = file.mkdirs();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        book.write(bos);

        response.addHeader("filename", "ttt3.xls"); //文件命名
        LOGGER.info("调用完成/excel/down");
        return bos.toByteArray();
    }
}
