package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.MainDesk;
import zzjz.bean.MainDeskRequest;
import zzjz.bean.PagingEntity;
import zzjz.mapper.MainDeskMapper;
import zzjz.service.MainDeskService;
import java.util.List;

/**
 * @author 房桂堂
 * @version 2017/5/15 9:14
 * @ClassName: MainDeskServiceImpl
 * @Description: 待办事项service实现类
 */
@Service
public class MainDeskServiceImpl implements MainDeskService {

    @Autowired
    MainDeskMapper mainDeskMapper;

    @Override
    public Page<MainDesk> getMainDeskPageList(MainDeskRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<MainDesk> pageList = (Page<MainDesk>) mainDeskMapper.getMainDeskPageList(request);
        return pageList;
    }

    @Override
    public Page<MainDesk> getMainDeskDoneList(MainDeskRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<MainDesk> pageList = (Page<MainDesk>) mainDeskMapper.getMainDeskDoneList(request);
        return pageList;
    }

    @Override
    public Page<MainDesk> getCommitAttentionList(MainDeskRequest request, String beginDate,
                                                 String endDate, String userName, String applyTyp, String departName, boolean finish) {
        if (beginDate == null || "undefined".equals(beginDate)) {
            beginDate = null;
        }
        if (endDate == null || "undefined".equals(endDate)) {
            endDate = null;
        }
        if (userName == null || "undefined".equals(userName)) {
            userName = null;
        }
        if (departName == null || "undefined".equals(departName)) {
            departName = null;
        }
        if (applyTyp == null || "undefined".equals(applyTyp)) {
            applyTyp = "all";
        }
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<MainDesk> pageList;
        if (finish) {
            pageList = (Page<MainDesk>) mainDeskMapper.getCommitAttentionList(request, beginDate, endDate, userName, applyTyp, departName);
        } else {
            pageList = (Page<MainDesk>) mainDeskMapper.getUnCommitAttentionList(request, beginDate, endDate, userName, applyTyp, departName);
        }
        return pageList;
    }

    @Override
    public List<MainDesk> getRunningDesk() {
        return mainDeskMapper.getRunningDesk();
    }

    @Override
    public Page<MainDesk> getProgressList(MainDeskRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<MainDesk> pageList = (Page<MainDesk>) mainDeskMapper.getProgressList(request);
        return pageList;
    }
}
