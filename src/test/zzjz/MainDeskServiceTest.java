package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.MainDesk;
import zzjz.bean.MainDeskRequest;
import zzjz.bean.PagingEntity;
import zzjz.service.MainDeskService;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class MainDeskServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    @Autowired
    MainDeskService mainDeskService;

    /**
     * 测试分页获取待办信息.
     */
    @Test
    @Rollback()
    public void testGetMainDeskPageList() {
        MainDeskRequest request = new MainDeskRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<MainDesk> page = mainDeskService.getMainDeskPageList(request);
        assert page != null;
    }

    /**
     * 测试分页获取已办信息.
     */
    @Test
    @Rollback()
    public void testGetMainDeskDoneList() {
        MainDeskRequest request = new MainDeskRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<MainDesk> page = mainDeskService.getMainDeskDoneList(request);
        assert page != null;
    }

    /**
     * 测试分页获取已提交信息.
     */
    @Test
    @Rollback()
    public void testGetCommitAttentionList() {
        MainDeskRequest request = new MainDeskRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<MainDesk> page = mainDeskService.getCommitAttentionList(request, null, null, null, null, null, true);
        assert page != null;
    }

    @Test
    @Rollback()
    public void testGetRunningDesk() {
        List<MainDesk> mainDesks = mainDeskService.getRunningDesk();
        System.out.println(1);
    }
}
