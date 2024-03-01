package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Dic;
import zzjz.bean.DicRequest;
import zzjz.bean.PagingEntity;
import zzjz.service.DicService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class DicServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    @Autowired
    DicService dicService;

    /**
     * 测试新增字典项信息.
     */
    @Test
    @Rollback()
    public void testAddDicInfo() {
        Dic dic = new Dic();
        dic.setDicId("RESUME_TYPE");
        dic.setDicName("简历类型");
        dic.setDicKey("测试");
        dic.setDicValue("研发中心");
        dic.setStatus("1");
        boolean rs = dicService.addDicInfo(dic);
        assert rs;
    }

    /**
     * 测试修改字典项信息.
     */
    @Test
    @Rollback()
    public void testUpdateDicInfo() {
        Dic dic = new Dic();
        dic.setDicId("RESUME_TYPE");
        dic.setDicName("简历类型");
        dic.setDicKey("测试");
        dic.setDicValue("研发中心");
        dic.setStatus("1");
        boolean rs = dicService.updateDicInfo(dic);
        assert rs;
    }

    /**
     * 测试根据字典项编号获取字典项信息.
     */
    @Test
    @Rollback()
    public void testGeDicByDicId() {
        List<Dic> list = dicService.geDicByDicId("RESUME_TYPE");
        assert list != null;
    }

    /**
     * 测试分页获取字典项信息列表.
     */
    @Test
    @Rollback()
    public void testGetDicPageList() {
        DicRequest request = new DicRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Dic> page = dicService.getDicPageList(request);
        assert page != null;
    }

    /**
     * 测试查询字典项所有ID.
     */
    @Test
    @Rollback()
    public void testGetDicTitleList() {
        List<Dic> list = dicService.getDicTitleList();
        assert list != null;
    }

    /**
     * 测试删除字典项信息.
     */
    @Test
    @Rollback()
    public void testDeleteDicInfo() {
        boolean rs = dicService.deleteDicInfo("RESUME_TYPE", "测试");
        assert rs;
    }
}
