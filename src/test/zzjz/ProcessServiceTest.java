package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.PagingEntity;
import zzjz.bean.Process;
import zzjz.bean.ProcessRequest;
import zzjz.service.ProcessService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/11/10.
 */
public class ProcessServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final int INT999 = 999;

    @Autowired
    ProcessService processService;

    /**
     * 分页获取流程配置信息.
     */
    @Test
    @Rollback
    public void testGetPageList() {
        ProcessRequest request = new ProcessRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Process> pageList = processService.getPageList(request);
        assert pageList != null;
    }

    /**
     * 获取流程配置信息.
     */
    @Test
    @Rollback
    public void testGetSelections() {
        Process process = new Process();
        List<Process> list = processService.getSelections(process);
        assert list != null;
    }

    /**
     * 根据流程信息获取用户列表.
     */
    @Test
    @Rollback
    public void testGetStaffList() {
        List<Process> list = processService.getStaffList("provider", "apply");
        assert list != null;
    }

    /**
     * 新增.
     */
    @Test
    @Rollback
    public void testInsert() {
        Process process = new Process();
        process.setId(INT999);
        process.setProcessId("provider");
        process.setProcessSign("供应商");
        process.setNodeId("apply");
        process.setNodeSign("经办人");
        process.setUserId("3");
        process.setUserName("李旭东");
        process.setCreateTime("2017-11-10 15:11:11");
        Boolean rs = processService.insert(process);
        assert rs;
    }

    /**
     * 修改.
     */
    @Test
    @Rollback
    public void testUpdate() {
        Process process = new Process();
        process.setId(INT999);
        process.setProcessId("provider");
        process.setProcessSign("供应商");
        process.setNodeId("apply");
        process.setNodeSign("经办人");
        process.setUserId("3");
        process.setUserName("李旭东");
        process.setCreateTime("2017-11-10 15:11:11");
        Boolean rs = processService.update(process);
        assert rs;
    }

    /**
     * 查询.
     */
    @Test
    @Rollback
    public void testSelectById() {
        Process process = processService.selectById(INT999);
        assert process != null;
    }

    /**
     * 删除.
     */
    @Test
    @Rollback
    public void testDelete() {
        Boolean rs = processService.delete(INT999);
        assert rs;
    }
}
