package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Trip;
import zzjz.bean.TripRequest;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月13日14:15
 */
@Service
public interface TripService {
    /**
     * 分页获取出差列表信息
     * @param request request
     * @return 出差列表信息
     */
    Page<Trip> getTripListPage(TripRequest request);
}
