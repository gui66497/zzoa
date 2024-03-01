package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.Group;
import zzjz.service.GroupService;

import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class GroupServiceTest extends JunitBaseTest {

    private static final Integer GROUP_ID = 1;

    @Autowired
    GroupService groupService;

    /**
     * 测试新增组信息.
     */
    @Test
    public void testAddGroup() {
        Group group = new Group();
        group.setGroupId(GROUP_ID);
        group.setCreateTime(null);
        group.setGroupName("测试组");
        boolean rs = groupService.addGroup(group);
        assert rs;
    }

    /**
     * 测试更新组信息.
     */
    @Test
    public void testUpdateGroup() {
        Group group = new Group();
        group.setGroupId(GROUP_ID);
        group.setCreateTime(null);
        group.setGroupName("测试组");
        boolean rs = groupService.updateGroup(group);
        assert rs;
    }

    /**
     * 测试查询所有组信息列表.
     */
    @Test
    public void testQueryAllGroup() {
        List<Group> list = groupService.queryAllGroup();
        assert list != null;
    }

    /**
     * 测试根据组名查询组信息.
     */
    @Test
    public void testQueryByName() {
        Group group = groupService.queryByName("测试组");
        assert group != null;
    }

    /**
     * 测试根据组ID查询组信息.
     */
    @Test
    public void testQueryByIdWithStaff() {
        Group group = groupService.queryByIdWithStaff(GROUP_ID);
        assert group != null;
    }

    /**
     * 测试根据用户ID查询组列表.
     */
    @Test
    public void testQueryGroupByStaffId() {
        List<Group> list = groupService.queryGroupByStaffId("62");
        assert list != null;
    }

    /**
     * 测试根据组ID删除组信息.
     */
    @Test
    public void testDeleteByGroupId() {
        boolean rs = groupService.deleteByGroupId(GROUP_ID);
        assert rs;
    }
}
