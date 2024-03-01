package zzjz.rest;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.github.pagehelper.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Column;
import zzjz.bean.ResultCode;
import zzjz.bean.Turnover;
import zzjz.bean.TurnoverRequest;
import zzjz.service.TurnoverService;
import zzjz.util.PageUtil;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * TurnoverRest 人员异动rest.
 *
 * @author fgt
 * @version 2017/6/14 8:49
 */
@Component
@Path("turnover")
public class TurnoverRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TurnoverRest.class);

    private static final String URI = "";

    private String message = "";

    private String exportPath = "D:/downloadFiles/";

    @Context
    HttpServletRequest servletRequest;

    @Autowired
    TurnoverService turnoverService;

    /**
     * 分页获取员工的异动记录.
     *
     * @param request request
     * @return 异动列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Turnover> getTurnoverPage(TurnoverRequest request) {
        LOGGER.info("开始调用/turnover/page");
        BaseResponse<Turnover> response = new BaseResponse<>();
        Page<Turnover> page = turnoverService.queryPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) page.getTotal());
        response.setData(page);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/turnover/page");
        return response;
    }

    /**
     * 添加异动信息.
     * @param turnover 异动信息
     * @param headers headers
     * @return 结果
     */
    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> addTurnover(Turnover turnover, @Context HttpHeaders headers) {
        LOGGER.info("开始调用/turnover/add");
        BaseResponse<String> response = new BaseResponse<String>();
        long staffId = turnover.getStaffId();
        if (staffId < 1) {
            message = "staffId不正确!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        String type = turnover.getType();
        if (StringUtils.isBlank(type)) {
            message = "异动类型不能为空!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }

        boolean res = false;
        try {
            res = turnoverService.addTurnover(turnover);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        if (res) {
            message = "异动信息添加成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "异动信息添加失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        response.setMessage(message);
        LOGGER.info("调用完成/turnover/add");
        return response;
    }

    /**
     * 修改异动信息.
     * @param turnover 异动信息
     * @return 结果
     */
    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> updateTurnover(Turnover turnover) {
        LOGGER.info("开始调用/turnover/update");
        BaseResponse<String> response = new BaseResponse<String>();
        long staffId = turnover.getStaffId();
        if (staffId < 1) {
            message = "staffId值不正确!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        String type = turnover.getType();
        if (StringUtils.isBlank(type)) {
            message = "异动事项不能为空!";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }
        boolean res;
        try {
            res = turnoverService.updateTurnover(turnover);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        if (res) {
            message = "异动信息修改成功";
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            message = "异动信息修改失败";
            response.setResultCode(ResultCode.RESULT_ERROR);
            return response;
        }
        response.setMessage(message);
        LOGGER.info("调用完成/turnover/update");
        return response;

    }

    /**
     * 删除异动信息.
     * @param id 异动主键
     * @return 结果
     */
    @DELETE
    @Path("{id}/del")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> delete(@PathParam("id") int id) {
        LOGGER.info("开始调用/turnover/" + id + "/del");
        BaseResponse<String> response = new BaseResponse<>();
        if (id < 0) {
            message = "参数不合法";
            response.setResultCode(ResultCode.RESULT_ERROR);
            response.setMessage(message);
            LOGGER.debug(message);
            return response;
        }

        boolean res = turnoverService.delById(id);
        if (res) {
            response.setMessage("删除异动信息成功");
            response.setResultCode(ResultCode.RESULT_SUCCESS);
        } else {
            response.setMessage("删除异动信息失败");
            response.setResultCode(ResultCode.RESULT_ERROR);
        }
        LOGGER.info("调用完成/turnover/del");
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
        LOGGER.info("开始调用/turnover/allCol");
        BaseResponse<Column> response = new BaseResponse<>();
        List<Column> allColumn = turnoverService.getAllColumn();
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        response.setData(allColumn);
        LOGGER.info("调用完成/turnover/allCol");
        return response;
    }

    /**
     * 导出异动信息(支持列定制).
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
    public byte[] downloadExcel(@Context HttpServletResponse response, TurnoverRequest request) throws IOException {
        LOGGER.info("开始调用/turnover/export");
        String exportStr = request.getExportStr();
        assert StringUtils.isNotBlank(exportStr);
        String[] exportArr = exportStr.split(",");
        List<ExcelExportEntity> entities = turnoverService.getExcelExportEntityByArr(exportArr);
        List<Map<String, Object>> staffMap = turnoverService.getTurnoverMap(request);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(
                "异动信息", "异动信息", "异动信息"), entities, staffMap);
        File file = new File(exportPath);
        if (!file.exists()) {
            boolean rs = file.mkdirs();
            LOGGER.info("创建文件:" + file + (rs == true ? "成功" : "失败"));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        response.addHeader("filename", URLEncoder.encode("异动信息导出.xls", "utf-8"));
        LOGGER.info("调用完成/turnover/export");
        return bos.toByteArray();
    }


}
