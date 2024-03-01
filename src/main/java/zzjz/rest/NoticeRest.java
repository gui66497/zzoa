package zzjz.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.Notice;
import zzjz.bean.NoticeRequest;
import zzjz.bean.ResultCode;
import zzjz.service.NoticeService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年08月30日17:00
 * @ClassName: NoticeRest
 * @Description: 告知rest
 */
@Component
@Path("notice")
public class NoticeRest {
    private final static Logger LOGGER = LoggerFactory.getLogger(NoticeRest.class);

    @Autowired
    NoticeService noticeService;

    @Context
    HttpServletRequest servletRequest;

    /**
     * 初始化流程信息.
     *
     * @param request NoticeRequest
     * @return BaseResponse
     * @author 彭鹏
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Notice> getNoticeList(NoticeRequest request) {
        LOGGER.info("开始调用/notice/list");
        BaseResponse<Notice> response = new BaseResponse<>();
        request.setNoticeId(Long.valueOf(servletRequest.getSession().getAttribute("staffId").toString()));
        List<Notice> list = noticeService.getNoticeList(request);
        response.setData(list);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        LOGGER.info("调用完成/notice/list");
        return response;
    }
}
