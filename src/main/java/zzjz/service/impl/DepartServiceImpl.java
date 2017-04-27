package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;
import zzjz.bean.PagingEntity;
import zzjz.mapper.DepartMapper;
import zzjz.service.DepartService;

/**
 * @author 彭鹏
 * @ClassName: DepartServiceImpl
 * @Description: 部门服务实现类
 * @date 2017年03月31日10:09
 */
@Service
public class DepartServiceImpl implements DepartService{
    @Autowired
    private DepartMapper departMapper;

    @Override
    public Page<Depart> getDepartListPage(DepartRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Depart> departPage = (Page<Depart>) departMapper.getDepartList(request);
        long total = departPage.getTotal();
        return departPage;
    }

    @Override
    public boolean addDepart(Depart depart) {
        return departMapper.insert(depart)>0;
    }

    @Override
    public boolean updateDepart(Depart depart) {
        return departMapper.updateByPrimaryKey(depart)>0;
    }

    @Override
    public boolean delByDepartId(Integer Id) {
        return departMapper.deleteByPrimaryKey(Id)>0;
    }
}
