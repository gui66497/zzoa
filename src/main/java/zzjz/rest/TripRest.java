package zzjz.rest;

import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import zzjz.bean.Trip;
import zzjz.bean.TripRequest;
import zzjz.service.TripService;
import zzjz.util.PageUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: TripRest
 * @Description: 出差信息
 * @date 2017年04月13日15:17
 */
@Component
@Path("trip")
public class TripRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripRest.class);

    @Autowired
    TripService tripService;

    /**
     * 分页获取出差信息
     * @param request
     * @return 出差列表
     */
    @Path("/page")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<Trip> getTripPage (TripRequest request){
        BaseResponse<Trip> response = new BaseResponse<>();
        Page<Trip> tripList = tripService.getTripListPage(request);
        List<Object> otherData = PageUtil.dealPaging(request.getPaging(), (int) tripList.getTotal());
        response.setData(tripList);
        response.setOtherData(otherData);
        response.setResultCode(ResultCode.RESULT_SUCCESS);
        return response;
    }



}
