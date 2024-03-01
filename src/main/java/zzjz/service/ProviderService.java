package zzjz.service;

import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import zzjz.bean.Provider;
import zzjz.bean.ProviderRequest;

import java.util.List;

/**
 * ProviderService.
 *
 * @author 彭鹏
 * @version 2017/11/2 9:41
 */
@Service
public interface ProviderService {

    /**
     * 分页获取供应商列表信息.
     *
     * @param request request
     * @return 供应商列表信息
     */
    Page<Provider> getProviderPage(ProviderRequest request);

    /**
     * 新增供应商信息.
     *
     * @param provider 供应商信息
     * @return actId
     */
    boolean addProvider(Provider provider);

    /**
     * 修改供应商信息.
     *
     * @param provider 供应商信息
     * @return 结果
     */
    boolean updateProvider(Provider provider);

    /**
     * 删除供应商信息.
     *
     * @param id id
     * @return 结果
     */
    boolean deleteProviderById(Integer id);

    /**
     * 批量新增供应商信息.
     *
     * @param providerList 供应商
     * @return 结果
     */
    boolean batchInsert(List<Provider> providerList);

    /**
     * 获取所有供应商信息.
     *
     * @return List
     */
    List<Provider> getAllProviderList();
}
