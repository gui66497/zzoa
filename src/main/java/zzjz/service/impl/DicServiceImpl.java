package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Dic;
import zzjz.bean.DicRequest;
import zzjz.bean.PagingEntity;
import zzjz.mapper.DicMapper;
import zzjz.service.DicService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017年10月18日15:28
 * @ClassName: DicServiceImpl
 * @Description: 字典项管理实现类
 */
@Service
public class DicServiceImpl implements DicService {

    @Autowired
    DicMapper dicMapper;

    /**
     * 根据字典项编号获取字典项信息.
     *
     * @param dicId
     * @return
     */
    @Override
    public List<Dic> geDicByDicId(String dicId) {
        return dicMapper.getDicByDicId(dicId);
    }

    @Override
    public Page<Dic> getDicPageList(DicRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        Page<Dic> listPage = (Page<Dic>) dicMapper.getDicList(request);
        return listPage;
    }

    @Override
    public boolean addDicInfo(Dic dic) {
        dic.setStatus("1");
        return dicMapper.insert(dic) > 0;
    }

    @Override
    public boolean updateDicInfo(Dic dic) {
        return dicMapper.updateByPrimaryKey(dic) > 0;
    }

    @Override
    public boolean deleteDicInfo(String dicId, String dicKey) {
        Dic dic = new Dic();
        dic.setDicId(dicId);
        dic.setDicKey(dicKey);
        return dicMapper.deleteByPrimaryKey(dic) > 0;
    }

    @Override
    public List<Dic> getDicTitleList() {
        return dicMapper.getDicTitleList();
    }
}
