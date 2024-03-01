package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Down;
import zzjz.bean.DownRequest;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.service.CommonService;
import zzjz.service.DownService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class DownServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer RESOURCE_ID = 3;

    private static final Integer ID = 1;

    private static final Integer DEPART_ID = 41;

    @Autowired
    DownService downService;

    @Autowired
    CommonService commonService;

    /**
     * 测试新增下载信息.
     */
    @Test
    public void testAddDownInfo() {
        Down down = new Down();
        down.setId(ID);
        down.setUserId("62");
        down.setUserName("彭鹏");
        down.setReason("测试");
        down.setResourceId(RESOURCE_ID);
        down.setResourceName("测试");
        down = downService.addDownInfo(down);
        assert down != null;
    }

    /**
     * 测试修改下载信息.
     */
    @Test
    public void testUpdateDownInfo() {
        Down down = downService.getDownInfoById(ID);
        boolean rs = downService.updateDownInfo(down);
        assert rs;
    }

    /**
     * 测试分页获取下载信息列表.
     */
    @Test
    public void testGetDownPage() {
        DownRequest request = new DownRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageSize(PAGE_SIZE);
        pagingEntity.setPageNo(1);
        request.setPaging(pagingEntity);
        Page<Down> page = downService.getDownPage(request);
        assert page != null;
    }

    /**
     * 测试提交下载信息.
     */
    @Test
    public void testSubmitDownTask() {
        Down down = downService.getDownInfoById(ID);
        boolean rs = downService.submitDownTask(down, "27", "62");
        assert rs;
    }

    /**
     * 测试获取下一处理人列表.
     */
    @Test
    public void testGetNextUser() {
        Down down = downService.getDownInfoById(ID);
        List<Staff> list = downService.getNextUser(down, DEPART_ID, "3","agree");
        assert list != null;
    }

    /**
     * 测试根据id获取下载信息.
     */
    @Test
    public void testGetDownInfoById() {
        Down down = downService.getDownInfoById(ID);
        assert down != null;
    }

    /**
     * 测试审批下载.
     */
    @Test
    public void testApproveDownTask() {
        Down down = downService.getDownInfoById(ID);
        boolean rs = downService.approveDownTask(down, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试直接通过下载流程信息.
     */
    @Test
    public void testTerminationDownTask() {
        Down down = downService.getDownInfoById(ID);
        boolean rs = downService.terminationDownTask(down);
        assert rs;
    }

    /**
     * 测试删除下载信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Down down = downService.getDownInfoById(ID);
        boolean rs = downService.deleteRecordByActId(down.getActId());
        assert rs;
    }

    /**
     * 测试撤销下载信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Down down = new Down();
        down.setId(ID);
        down.setUserId("62");
        down.setUserName("彭鹏");
        down.setReason("测试");
        down.setResourceId(RESOURCE_ID);
        down.setResourceName("测试");
        down = downService.addDownInfo(down);
        boolean rs = downService.revokeRecordByActId(down.getActId());
        assert rs;
    }
}

