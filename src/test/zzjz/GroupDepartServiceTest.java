package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.DepartRole;
import zzjz.bean.GroupDepartInfo;
import zzjz.bean.GroupDepartRequest;
import zzjz.service.GroupDepartService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class GroupDepartServiceTest extends JunitBaseTest {

    private static final Integer DEPART_ID = 41;

    private static final Integer ID = 19;

    @Autowired
    GroupDepartService groupDepartService;

    /**
     * 测试获取部门组信息列表.
     */
    @Test
    @Rollback()
    public void testGetGroupListByDepartId() {
        GroupDepartInfo groupDepartInfo = groupDepartService.getGroupListByDepartId(DEPART_ID);
        assert groupDepartInfo != null;
    }

    /**
     * 测试根据用户ID获取该用户的角色list.
     */
    @Test
    @Rollback()
    public void testGetGroupList() {
        List<DepartRole> list = groupDepartService.getGroupList("62");
        assert list != null;
    }

    /**
     * 测试新增部门组角色.
     */
    @Test
    @Rollback()
    public void testAddGroupDepart() {
        GroupDepartRequest request = new GroupDepartRequest();
        List<String> groupList = new ArrayList<>();
        groupList.add("cs");
        request.setDepartId(DEPART_ID);
        request.setGroupList(groupList);
        request.setId(ID);
        boolean rs = groupDepartService.addGroupDepart(request);
        assert rs;
    }
}
