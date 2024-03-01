package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.mapper.DepartMapper;
import zzjz.mapper.StaffMapper;
import zzjz.service.DepartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 彭鹏
 * @version 2017年03月31日10:09
 * @ClassName: DepartServiceImpl
 * @Description: 部门服务实现类
 */
@Service
public class DepartServiceImpl implements DepartService {
    @Autowired
    private DepartMapper departMapper;

    @Autowired
    private StaffMapper staffMapper;


    @Override
    public Page<Depart> getDepartListPage(DepartRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Depart> departPage = (Page<Depart>) departMapper.getDepartList(request);
        return departPage;
    }

    @Override
    public List<Depart> getDepartList() {
        List<Depart> departList = list(0L);
        return departList;
    }

    @Override
    public List<Depart> getAllDepartList() {
        return departMapper.getAllDepartList();
    }

    @Override
    public boolean addDepart(Depart depart) {
        return departMapper.insertSelective(depart) > 0;
    }

    @Override
    public boolean updateDepart(Depart depart) {
        return departMapper.updateByPrimaryKeySelective(depart) > 0;
    }

    @Override
    public boolean delByDepartId(Long departId) {
        Map map = new HashMap();
        map.put("flag", true);
        Map retMap = deleteById(departId, new ArrayList<Long>(), map);
        if ((boolean) retMap.get("flag")) {
            departMapper.deleteDepartList((List) retMap.get("list"));
            return true;
        }
        return false;
    }

    @Override
    public String getDepartIdByName(String departName) {
        List<Depart> departs = departMapper.getDepartByName(departName);
        if (CollectionUtils.isNotEmpty(departs)) {
            return String.valueOf(departs.get(0).getDepartId());
        }
        return "";
    }

    @Override
    public Depart selectByPrimaryKey(long departId) {
        return departMapper.selectByPrimaryKey(departId);
    }

    private Map deleteById(Long departId, List<Long> list, Map map) {
        List<Staff> staffList = staffMapper.getStaffListByDepartId(departId);
        if (staffList.size() > 0) {
            map.put("flag", false);
            return map;
        } else {
            list.add(departId);
            map.put("list", list);
        }
        List<Depart> departs = departMapper.getDepartByParentDepart(departId);
        for (int i = 0; i < departs.size(); i++) {
            deleteById(departs.get(i).getDepartId(), list, map);
        }
        return map;
    }

    private List<Depart> list(Long parentDepart) {

        List<Depart> departs = departMapper.getDepartByParentDepart(parentDepart);
        for (int i = 0; i < departs.size(); i++) {
            departs.get(i).setNodes(list(departs.get(i).getDepartId()));
        }
        return departs;
    }

}
