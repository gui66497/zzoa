package zzjz.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.PagingEntity;
import zzjz.bean.Provider;
import zzjz.bean.ProviderRequest;
import zzjz.mapper.ProviderMapper;
import zzjz.service.ProviderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ProviderServiceImpl.
 *
 * @author 彭鹏
 * @version 2017/11/10 17:19
 */
@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    ProviderMapper providerMapper;

    @Override
    public Page<Provider> getProviderPage(ProviderRequest request) {
        PagingEntity pagingEntity = request.getPaging();
        PageHelper.startPage(pagingEntity.getPageNo(), pagingEntity.getPageSize());
        return (Page<Provider>) providerMapper.getProviderList(request);
    }

    @Override
    public boolean addProvider(Provider provider) {
        String userId = provider.getCreateUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = sdf.format(new Date());
        provider.setCreateTime(createTime);
        provider.setCreateUser(userId);
        return providerMapper.insert(provider) > 0;
    }

    @Override
    public boolean updateProvider(Provider provider) {
        return providerMapper.updateByPrimaryKey(provider) > 0;
    }

    @Override
    public boolean deleteProviderById(Integer id) {
        return providerMapper.deleteProviderById(id);
    }

    @Override
    public boolean batchInsert(List<Provider> providerList) {
        return providerMapper.batchInsert(providerList);
    }

    @Override
    public List<Provider> getAllProviderList() {
        return providerMapper.getAllProviderList();
    }
}
