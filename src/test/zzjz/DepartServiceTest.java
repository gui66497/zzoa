package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Depart;
import zzjz.bean.DepartRequest;
import zzjz.bean.PagingEntity;
import zzjz.service.DepartService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class DepartServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer DEPART_ID = 19;

    private static final Integer PAR_DEPART_ID = 1;

    @Autowired
    DepartService departService;

    /**
     * 测试分页获取部门信息.
     */
    @Test
    public void testGetDepartListPage() {
        DepartRequest request = new DepartRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageSize(PAGE_SIZE);
        pagingEntity.setPageNo(1);
        request.setPaging(pagingEntity);
        Page<Depart> page = departService.getDepartListPage(request);
        assert page != null;
    }

    /**
     * 测试获取所有子部门信息列表.
     */
    @Test
    public void testGetDepartList() {
        List<Depart> list = departService.getDepartList();
        assert list != null;

    }

    /**
     * 测试获取所有部门信息列表.
     */
    @Test
    public void testGetAllDepartList() {
        List<Depart> list = departService.getAllDepartList();
        assert list != null;
    }

    /**
     * 测试新增部门信息.
     */
    @Test
    public void testAddDepart() {
        Depart depart = new Depart();
        depart.setCreateTime("2017-09-28 10:42:55");
        depart.setDepartId(Long.valueOf(DEPART_ID));
        depart.setDepartName("测试部门");
        depart.setLevel("2");
        depart.setParentDepart(Long.valueOf(PAR_DEPART_ID));
        boolean rs = departService.addDepart(depart);
        assert rs;
    }

    /**
     * 测试修改部门信息.
     */
    @Test
    public void testUpdateDepart() {
        Depart depart = new Depart();
        depart.setCreateTime("2017-09-28 10:42:55");
        depart.setDepartId(Long.valueOf(DEPART_ID));
        depart.setDepartName("测试部门");
        depart.setLevel("2");
        depart.setParentDepart(Long.valueOf(PAR_DEPART_ID));
        boolean rs = departService.updateDepart(depart);
        assert rs;
    }

    /**
     * 测试获取部门信息.
     */
    @Test
    public void testGetDepartIdByName() {
        String id = departService.getDepartIdByName("测试部门");
        assert "19".equals(id);
    }

    /**
     * 测试获取部门信息.
     */
    @Test
    public void testSelectByPrimaryKey() {
        Depart depart = departService.selectByPrimaryKey(DEPART_ID);
        assert depart != null;
    }

    /**
     * 测试删除部门信息.
     */
    @Test
    public void testDelByDepartId() {
        boolean rs = departService.delByDepartId(Long.valueOf(DEPART_ID));
        assert rs;
    }
}
