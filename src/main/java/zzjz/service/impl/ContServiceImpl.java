package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Cont;
import zzjz.bean.ContRequest;
import zzjz.bean.PagingEntity;
import zzjz.mapper.ContMapper;
import zzjz.service.ContService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: ContService
 * @Description: 合同信息服务接口实现类
 */
@Service
public class ContServiceImpl implements ContService {

    @Autowired
    ContMapper contMapper;

    @Override
    public Page<Cont> getContPageList(ContRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Cont> pageList = (Page<Cont>) contMapper.getContPageList(request);
        return pageList;
    }

    @Override
    public List<Cont> getContList(Cont cont) {
        return contMapper.getContList(cont);
    }

    @Override
    public Cont query(Integer id) {
        return contMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean add(Cont cont) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cont.setCreateTime(sdf.format(new Date()));
        return contMapper.insertSelective(cont) > 0;
    }

    @Override
    public boolean update(Cont cont) {
        return contMapper.updateByPrimaryKeySelective(cont) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return contMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<Cont> getEffectiveContList(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkDate = sdf.format(new Date());
        return contMapper.getEffectiveContList(userId, checkDate);
    }

    @Override
    public List<Cont> getInvalidContList(String userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String checkDate = sdf.format(new Date());
        return contMapper.getInvalidContList(userId, checkDate);
    }
}
