package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Over;
import zzjz.bean.OverRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.service.OverService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/29.
 */
public class OverServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    private static final Double HALF_DAY = 0.5;

    private static final Integer DEPART_ID = 41;

    @Autowired
    OverService overService;

    /**
     * 测试添加加班信息.
     */
    @Test
    public void testAddOverInfo() {
        Over over = new Over();
        over.setId(ID);
        over.setUserId("62");
        over.setUserName("彭鹏");
        over.setOverDate("2016-09-29");
        over.setOverDay(HALF_DAY);
        over.setReason("测试");
        String actId = overService.addOverInfo(over);
        assert !"".equals(actId);
    }

    /**
     * 测试分页获取加班信息.
     */
    @Test
    public void testGetOverList() {
        OverRequest over = new OverRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        over.setPaging(pagingEntity);
        Page<Over> page = overService.getOverList(over);
        assert page != null;
    }

    /**
     * 测试更新加班信息.
     */
    @Test
    public void testUpdateOverInfo() {
        Over over = overService.getOverInfoById(ID);
        String actId = overService.updateOverInfo(over);
        assert !"".equals(actId);
    }

    /**
     * 测试提交加班信息.
     */
    @Test
    public void testSubmitOverTask() {
        Over over = overService.getOverInfoById(ID);
        boolean rs = overService.submitOverTask(over, "27", "62");
        assert rs;
    }

    /**
     * 测试审批加班信息.
     */
    @Test
    @Rollback()
    public void testApproveOverTask() {
        Over over = overService.getOverInfoById(ID);
        boolean rs = overService.approveOverTask(over, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试根据主键查询加班信息.
     */
    @Test
    public void testGetOverInfoById() {
        Over over = overService.getOverInfoById(ID);
        assert over != null;
    }

    /**
     * 测试查询下一处理人.
     */
    @Test
    public void testGetNextUser() {
        Over over = overService.getOverInfoById(ID);
        List<Staff> list = overService.getNextUser(over, DEPART_ID, "agree");
        assert list != null;
    }

    /**
     * 测试审批并结束加班信息.
     */
    @Test
    public void testTerminationOverTask() {
        Over over = overService.getOverInfoById(ID);
        boolean rs = overService.terminationOverTask(over);
        assert rs;
    }

    /**
     * 测试查询加班信息.
     */
    @Test
    public void testSelectOverInfo() {
        Over over = overService.getOverInfoById(ID);
        over = overService.selectOverInfo(over);
        assert over != null;
    }

    /**
     * 测试删除关联的考勤信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Over over = overService.getOverInfoById(ID);
        boolean rs = overService.deleteRecordByActId(over.getActId());
        assert rs;
    }

    /**
     * 测试根据流程ID撤销加班信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Over over = new Over();
        over.setId(ID);
        over.setUserId("62");
        over.setUserName("彭鹏");
        over.setOverDate("2016-09-29");
        over.setOverDay(HALF_DAY);
        over.setReason("测试");
        String actId = overService.addOverInfo(over);
        boolean rs = overService.revokeRecordByActId(actId);
        assert rs;
    }
}
