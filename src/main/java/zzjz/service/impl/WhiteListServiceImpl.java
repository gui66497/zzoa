package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.WhiteList;
import zzjz.bean.WhiteListRequest;
import zzjz.mapper.WhiteListMapper;
import zzjz.service.WhiteListService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 彭鹏
 * @version 2017年12月08日15:25
 * @ClassName: WhiteListServiceImpl
 * @Description: 白名单信息方法
 */
@Service
public class WhiteListServiceImpl implements WhiteListService {

    @Autowired
    WhiteListMapper whiteListMapper;

    @Override
    public Page<WhiteList> getWhiteListPage(WhiteListRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<WhiteList> pageList = (Page<WhiteList>) whiteListMapper.getWhiteList(request);
        return pageList;
    }

    @Override
    public boolean add(WhiteList whiteList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        whiteList.setCreateTime(sdf.format(new Date()));
        return whiteListMapper.insertSelective(whiteList) > 0;
    }

    @Override
    public boolean update(WhiteList whiteList) {
        return whiteListMapper.updateByPrimaryKeySelective(whiteList) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return whiteListMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public WhiteList getWhiteListInfoById(Integer id) {
        return whiteListMapper.selectByPrimaryKey(id);
    }

    @Override
    public WhiteList getWhiteListInfoByUserIdAndType(String userId, String type, String checkDate) {
        return whiteListMapper.getWhiteListInfoByUserIdAndType(userId, type, checkDate);
    }
}
