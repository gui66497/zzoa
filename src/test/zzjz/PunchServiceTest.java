package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.PagingEntity;
import zzjz.bean.Punch;
import zzjz.bean.PunchRequest;
import zzjz.bean.Staff;
import zzjz.service.PunchService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/29.
 */
public class PunchServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    private static final Integer DEPART_ID = 41;

    @Autowired
    PunchService punchService;

    /**
     * 测试添加忘打卡信息.
     */
    @Test
    public void testAddPunchInfo() {
        Punch punch = new Punch();
        punch.setId(ID);
        punch.setUserId("62");
        punch.setUserName("彭鹏");
        punch.setReason("测试");
        punch.setPunchDate("2016-09-29");
        punch.setPunchTyp("0");
        punch.setWitness("测试");
        String actId = punchService.addPunchInfo(punch);
        assert !"".equals(actId);
    }

    /**
     * 测试分页获取忘打卡信息列表.
     */
    @Test
    public void testGetPunchPageList() {
        PunchRequest request = new PunchRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Punch> page = punchService.getPunchPageList(request);
        assert page != null;
    }

    /**
     * 测试更新忘打卡信息.
     */
    @Test
    public void testUpdatePunchInfo() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.updatePunchInfo(punch);
        assert rs;
    }

    /**
     * 测试提交忘打卡信息.
     */
    @Test
    public void testSubmitPunchTask() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.submitPunchTask(punch, "27");
        assert rs;
    }

    /**
     * 测试审批忘打卡信息.
     */
    @Test
    public void testApprovePunchTask() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.approvePunchTask(punch, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试根据任务主键获取忘打卡信息.
     */
    @Test
    public void testGetPunchInfoById() {
        Punch punch = punchService.getPunchInfoById(ID);
        assert punch != null;
    }

    /**
     * 测试查询忘打卡信息.
     */
    @Test
    public void testSelectPunchInfo() {
        Punch punch = punchService.getPunchInfoById(ID);
        List<Punch> list = punchService.selectPunchInfo(punch);
        assert list != null;
    }

    /**
     * 测试获取下一处理人列表.
     */
    @Test
    public void testGetNextUser() {
        Punch punch = punchService.getPunchInfoById(ID);
        List<Staff> list = punchService.getNextUser(punch, DEPART_ID, "agree");
        assert list != null;
    }

    /**
     * 测试审批并结束忘打卡信息.
     */
    @Test
    public void testTerminationPunchTask() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.terminationPunchTask(punch);
        assert rs;
    }

    /**
     * 测试告知忘打卡信息.
     */
    @Test
    @Rollback()
    public void testNoticePunchTask() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.noticePunchTask(punch, "1");
        assert rs;
    }

    /**
     * 测试根据流程ID删除忘打卡信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Punch punch = punchService.getPunchInfoById(ID);
        boolean rs = punchService.deleteRecordByActId(punch.getActId());
        assert rs;
    }

    /**
     * 测试根据流程ID撤销忘打卡信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Punch punch = new Punch();
        punch.setId(ID);
        punch.setUserId("62");
        punch.setUserName("彭鹏");
        punch.setReason("测试");
        punch.setPunchDate("2016-09-29");
        punch.setPunchTyp("0");
        punch.setWitness("测试");
        String actId = punchService.addPunchInfo(punch);
        boolean rs = punchService.revokeRecordByActId(actId);
        assert rs;
    }
}
