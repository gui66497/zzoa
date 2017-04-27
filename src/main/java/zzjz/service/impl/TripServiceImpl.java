package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Trip;
import zzjz.bean.TripRequest;
import zzjz.mapper.TripMapper;
import zzjz.service.TripService;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月13日14:38
 */
@Service
public class TripServiceImpl implements TripService {

    @Autowired
    TripMapper tripMapper;

    @Override
    public Page<Trip> getTripListPage(TripRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Trip> tripPage = (Page<Trip>) tripMapper.getTripList(request);
        long total = tripPage.getTotal();
        return tripPage;
    }
}
