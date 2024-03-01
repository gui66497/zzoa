package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.PagingEntity;
import zzjz.bean.Provider;
import zzjz.bean.ProviderRequest;
import zzjz.service.ProviderService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/11/10.
 */
public class ProviderServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    @Autowired
    ProviderService providerService;

    /**
     * 分页获取供应商列表信息.
     */
    @Test
    @Rollback
    public void testGetProviderPage() {
        ProviderRequest request = new ProviderRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Provider> pageList = providerService.getProviderPage(request);
        assert pageList != null;
    }

    /**
     * 新增供应商信息.
     */
    @Test
    public void testAddProvider() {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setName("123");
        Boolean rs = providerService.addProvider(provider);
        assert rs;
    }

    /**
     * 修改供应商信息.
     */
    @Test
    public void testUpdateProvider() {
        Provider provider = new Provider();
        provider.setId(1);
        provider.setName("124");
        providerService.updateProvider(provider);
    }

    /**
     * 删除供应商信息.
     */
    @Test
    public void testDeleteRecordById() {
        Boolean rs = providerService.deleteProviderById(1);
        assert rs;
    }

    /**
     * 获取所有供应商信息.
     */
    @Test
    @Rollback
    public void testGetAllProviderList() {
        List<Provider> list = providerService.getAllProviderList();
        assert list != null;
    }
}
