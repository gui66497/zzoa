package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.PagingEntity;
import zzjz.bean.Turnover;
import zzjz.bean.TurnoverRequest;
import zzjz.service.TurnoverService;

/**
 * @author 彭鹏
 * @version 2017/9/30.
 */
public class TurnoverServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 02;

    private static final Integer STAFF_ID = 62;

    @Autowired
    TurnoverService turnoverService;

    /**
     * 测试分页获取员工的异动记录.
     */
    @Test
    public void testQueryPage() {
        TurnoverRequest request = new TurnoverRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Turnover> page = turnoverService.queryPage(request);
        assert page != null;
    }

    /**
     * 测试添加异动信息.
     */
    @Test
    public void testAddTurnover() {
        Turnover turnover = new Turnover();
        turnover.setId(ID);
        turnover.setStaffId(STAFF_ID);
        turnover.setType("职位");
        turnover.setAction("变化");
        turnover.setOldValue("交服工程师");
        turnover.setNewValue("算法工程师");
        turnover.setEffectTime("2017-09-27");
        turnover.setCreateTime("2017-09-27 15:15:28");
        boolean rs = false;
        try {
            rs = turnoverService.addTurnover(turnover);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert rs;
    }

    /**
     * 测试修改异动信息.
     * @throws Exception Exception
     */
    @Test
    public void testUpdateTurnover() throws Exception {
        Turnover turnover = new Turnover();
        turnover.setId(ID);
        turnover.setStaffId(STAFF_ID);
        turnover.setType("职位");
        turnover.setAction("变化");
        turnover.setOldValue("交服工程师");
        turnover.setNewValue("算法工程师");
        turnover.setEffectTime("2017-09-27");
        turnover.setCreateTime("2017-09-27 15:15:28");
        boolean rs = turnoverService.updateTurnover(turnover);
        assert rs;
    }

    /**
     * 测试删除异动信息.
     */
    @Test
    public void testDelById() {
        boolean rs = turnoverService.delById(ID);
        assert rs;
    }
}
