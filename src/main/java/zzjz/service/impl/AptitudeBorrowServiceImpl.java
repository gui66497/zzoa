package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.AptitudeBorrow;
import zzjz.bean.AptitudeBorrowRequest;
import zzjz.bean.PagingEntity;
import zzjz.mapper.AptitudeBorrowMapper;
import zzjz.service.AptitudeBorrowService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年12月14日15:04
 * @ClassName: AptitudeBorrowService
 * @Description: 资质证书借阅信息服务接口实现类
 */
@Service
public class AptitudeBorrowServiceImpl implements AptitudeBorrowService {

    @Autowired
    AptitudeBorrowMapper aptitudeBorrowMapper;

    @Override
    public Page<AptitudeBorrow> getAptitudeBorrowPageList(AptitudeBorrowRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<AptitudeBorrow> pageList = (Page<AptitudeBorrow>) aptitudeBorrowMapper.getAptitudeBorrowPageList(request);
        return pageList;
    }

    @Override
    public List<AptitudeBorrow> getAptitudeBorrowList(AptitudeBorrow aptitudeBorrow) {
        return aptitudeBorrowMapper.getAptitudeBorrowList(aptitudeBorrow);
    }

    @Override
    public AptitudeBorrow query(Integer id) {
        return aptitudeBorrowMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean add(AptitudeBorrow aptitudeBorrow) {
        return aptitudeBorrowMapper.insertSelective(aptitudeBorrow) > 0;
    }

    @Override
    public boolean update(AptitudeBorrow aptitudeBorrow) {
        return aptitudeBorrowMapper.updateByPrimaryKeySelective(aptitudeBorrow) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return aptitudeBorrowMapper.deleteByPrimaryKey(id) > 0;
    }
}
