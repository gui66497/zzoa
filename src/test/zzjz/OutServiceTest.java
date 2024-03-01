package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Out;
import zzjz.bean.OutRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.service.OutService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class OutServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    private static final Integer DEPART_ID = 41;

    private static final Double OUT_DAY = 0.5;

    @Autowired
    OutService outService;

    /**
     * 测试新增外出信息.
     */
    @Test
    public void testAddOutInfo() {
        Out out = new Out();
        out.setId(ID);
        out.setUserId("62");
        out.setUserName("彭鹏");
        out.setOutDate("2016-09-29");
        out.setOutPart("mor");
        out.setOutDay(OUT_DAY);
        out.setReason("测试");
        String actId = outService.addOutInfo(out);
        assert !"".equals(actId);
    }

    /**
     * 测试分页获取外出信息.
     */
    @Test
    public void testGetOutList() {
        OutRequest request = new OutRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Out> page = outService.getOutList(request);
        assert page != null;
    }

    /**
     * 测试修改外出信息.
     */
    @Test
    public void testUpdateOutInfo() {
        Out out = outService.getOutInfoById(ID);
        boolean rs = outService.updateOutInfo(out);
        assert rs;
    }

    /**
     * 测试根据主键查询外出信息.
     */
    @Test
    public void testGetOutInfoById() {
        Out out = outService.getOutInfoById(ID);
        assert out != null;
    }

    /**
     * 测试提交外出信息.
     */
    @Test
    public void testSubmitOutTask() {
        Out out = outService.getOutInfoById(ID);
        boolean rs = outService.submitOutTask(out, "27");
        assert rs;
    }

    /**
     * 测试审批外出信息.
     */
    @Test
    public void testApproveOutTask() {
        Out out = outService.getOutInfoById(ID);
        boolean rs = outService.approveOutTask(out, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试获取下一处理人.
     */
    @Test
    public void testGetNextUser() {
        Out out = outService.getOutInfoById(ID);
        List<Staff> list = outService.getNextUser(out, DEPART_ID, "agree");
        assert list != null;
    }

    /**
     * 测试查询外出信息.
     */
    @Test
    public void testSelectOutInfo() {
        Out out = outService.getOutInfoById(ID);
        List<Out> list = outService.selectOutInfo(out);
        assert list != null;
    }

    /**
     * 测试审批并结束外出信息.
     */
    @Test
    public void testTerminationOutTask() {
        Out out = outService.getOutInfoById(ID);
        boolean rs = outService.terminationOutTask(out);
        assert rs;
    }

    /**
     * 测试根据流程ID删除外出信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Out out = outService.getOutInfoById(ID);
        boolean rs = outService.deleteRecordByActId(out.getActId());
        assert rs;
    }

    /**
     * 测试根据流程ID撤销外出信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Out out = new Out();
        out.setId(ID);
        out.setUserId("62");
        out.setUserName("彭鹏");
        out.setOutDate("2016-09-29");
        out.setOutPart("mor");
        out.setOutDay(OUT_DAY);
        out.setReason("测试");
        String actId = outService.addOutInfo(out);
        boolean rs = outService.revokeRecordByActId(actId);
        assert rs;
    }
}
