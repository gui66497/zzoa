package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.PagingEntity;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;
import zzjz.bean.Column;
import zzjz.bean.DepartRole;
import zzjz.bean.Bean;
import zzjz.service.StaffService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/30.
 */
public class StaffServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    private static final Integer STAFF_ID = 9999999;

    private static final Integer AGE = 24;

    @Autowired
    StaffService staffService;

    /**
     * 测试添加员工信息.
     */
    @Test
    public void testAddStaff() {
        Staff staff = new Staff();
        staff.setId(ID);
        staff.setStaffId(STAFF_ID);
        staff.setName("测试");
        staff.setUserName("ceshi");
        staff.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
        staff.setEmail("ceshi@zzjunzhi.com");
        staff.setDepartId("41");
        staff.setPosition("测试人员");
        staff.setEntryDate("2017-09-30");
        staff.setIsFormal(0);
        staff.setFormalDate("2017-11-30");
        staff.setCompanyAge((float) 0);
        staff.setIdNumber("658958199302154518");
        staff.setBirth("1993-02-15");
        staff.setAge(AGE);
        staff.setGender(1);
        staff.setPreSeniority((float) 0);
        staff.setSeniority("0年0月");
        staff.setPayCard(null);
        staff.setPoliticalStatus("群众");
        staff.setJobNumber("99999");
        staff.setContractStart("2017-09-30");
        staff.setContractEnd("2019-09-30");
        staff.setTel("12345678978");
        staff.setGraduateSchool("测试大学");
        staff.setMajor("测试专业");
        staff.setEducation("本科");
        staff.setGraduateDate("2017-06-22");
        staff.setNativePlace("dfdswafrrws");
        staff.setHomeAddress("123423432");
        staff.setCensusRegister("34523454365");
        staff.setCompany("直真君智");
        staff.setOnJob(1);
        staff.setItAttributes("科技人员");
        staff.setResearchStaff("研发人员");
        boolean rs = staffService.addStaff(staff);
        assert rs;
    }

    /**
     * 测试更新用户信息.
     */
    @Test
    public void testEditStaff() {
        Staff staff = staffService.getStaffByUserId(STAFF_ID + "");
        boolean rs = staffService.editStaff(staff);
        assert rs;
    }

    /**
     * 测试分页获取员工信息列表.
     */
    @Test
    public void testGetStaffListPage() {
        StaffRequest request = new StaffRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Staff> page = staffService.getStaffListPage(request);
        assert page != null;
    }

    /**
     * 测试获取所有在职员工信息.
     */
    @Test
    public void testGetAllSimpleStaffOnJob() {
        List<Staff> list = staffService.getAllSimpleStaffOnJob();
        assert list != null;
    }

    /**
     * 测试获取所有员工信息（只包含简单信息）.
     */
    @Test
    public void testGetAllSimpleStaff() {
        List<Staff> list = staffService.getAllSimpleStaff();
        assert list != null;
    }

    /**
     * 测试获取员工信息列表.
     */
    @Test
    public void testGetStaffList() {
        StaffRequest request = new StaffRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        List<StaffExcel> list = staffService.getStaffList(request);
        assert list != null;
    }

    /**
     * 测试获取总经办员工信息列表.
     */
    @Test
    public void testGetTopStaffList() {
        List<Staff> list = staffService.getTopStaffList();
        assert list != null;
    }

    /**
     * 测试获取所有展示列.
     */
    @Test
    public void testGetAllColumn() {
        List<Column> list = staffService.getAllColumn();
        assert list != null;
    }

    /**
     * 测试更新展示列.
     */
    @Test
    public void testUpdateCol() {
        Column column = new Column();
        column.setId(1);
        column.setColumnName("姓名");
        column.setColumnSign("name");
        column.setIsShow(1);
        boolean rs = staffService.updateCol(column);
        assert rs;
    }

    /**
     * 测试登陆.
     */
    @Test
    public void testLogin() {
        boolean rs = staffService.login("ceshi", "827ccb0eea8a706c4c34a16891f84e7b");
        assert rs;
    }

    /**
     * 测试删除员工信息.
     */
    @Test
    public void testDelByStaffId() {
        boolean rs = staffService.delByStaffId(STAFF_ID);
        assert rs;
    }

    /**
     * 测试获取转正提醒信息.
     */
    @Test
    public void testGetFormalRemindList() {
        List<Staff> list = staffService.getformalRemindList();
        assert list != null;
    }

    /**
     * 测试获取生日提醒信息.
     */
    @Test
    public void testGetBirthRemindList() {
        List<Staff> list = staffService.getbirthRemindList();
        assert list != null;
    }

    /**
     * 测试获取合同到期提醒信息.
     */
    @Test
    public void testGetContRemindList() {
        List<Staff> list = staffService.getcontRemindList();
        assert list != null;
    }

    /**
     * 测试获取资格证书到期提醒信息.
     */
    @Test
    public void testGetCertificateRemindList() {
        List<Staff> list = staffService.getcertificateRemindList();
        assert list != null;
    }

    /**
     * 测试批量添加员工信息.
     */
    @Test
    public void testAddStaffList() {
        List<StaffExcel> res = new ArrayList<>();
        StaffExcel staff = new StaffExcel();
        staff.setId(ID);
        staff.setStaffId(STAFF_ID);
        staff.setName("测试");
        staff.setUserName("ceshi");
        staff.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
        staff.setDepartId("41");
        staff.setPosition("测试人员");
        staff.setEntryDate("2017-09-30");
        staff.setIsFormal(0);
        staff.setFormalDate("2017-11-30");
        staff.setCompanyAge((float) 0);
        staff.setIdNumber("658958199302154518");
        staff.setBirth("1993-02-15");
        staff.setAge(AGE);
        staff.setGender(1);
        staff.setPreSeniority((float) 0);
        staff.setSeniority("0年0月");
        staff.setPayCard(null);
        staff.setPoliticalStatus("群众");
        staff.setJobNumber("99999");
        staff.setContractStart("2017-09-30");
        staff.setContractEnd("2019-09-30");
        staff.setTel("12345678978");
        staff.setGraduateSchool("测试大学");
        staff.setMajor("测试专业");
        staff.setEducation("本科");
        staff.setGraduateDate("2017-06-22");
        staff.setNativePlace("dfdswafrrws");
        staff.setHomeAddress("123423432");
        staff.setCensusRegister("34523454365");
        staff.setCompany("直真君智");
        staff.setOnJob(1);
        staff.setItAttributes("科技人员");
        staff.setResearchStaff("研发人员");
        res.add(staff);
        boolean rs = staffService.addStaffList(res);
        assert rs;
    }

    /**
     * 测试批量更新.
     */
    @Test
    public void testUploadStaffs() {
        List<StaffExcel> res = new ArrayList<>();
        StaffExcel staff = new StaffExcel();
        staff.setId(ID);
        staff.setStaffId(STAFF_ID);
        staff.setName("测试");
        staff.setUserName("ceshi");
        staff.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
        staff.setDepartId("41");
        staff.setPosition("测试人员");
        staff.setEntryDate("2017-09-30");
        staff.setIsFormal(0);
        staff.setFormalDate("2017-11-30");
        staff.setCompanyAge((float) 0);
        staff.setIdNumber("658958199302154518");
        staff.setBirth("1993-02-15");
        staff.setAge(AGE);
        staff.setGender(1);
        staff.setPreSeniority((float) 0);
        staff.setSeniority("0年0月");
        staff.setPayCard(null);
        staff.setPoliticalStatus("群众");
        staff.setJobNumber("99999");
        staff.setContractStart("2017-09-30");
        staff.setContractEnd("2019-09-30");
        staff.setTel("12345678978");
        staff.setGraduateSchool("测试大学");
        staff.setMajor("测试专业");
        staff.setEducation("本科");
        staff.setGraduateDate("2017-06-22");
        staff.setNativePlace("dfdswafrrws");
        staff.setHomeAddress("123423432");
        staff.setCensusRegister("34523454365");
        staff.setCompany("直真君智");
        staff.setOnJob(1);
        staff.setItAttributes("科技人员");
        staff.setResearchStaff("研发人员");
        res.add(staff);
        boolean rs = staffService.uploadStaffs(res);
        assert rs;
    }

    /**
     * 测试根据用户名获取用户信息.
     */
    @Test
    public void testGetStaffByUserName() {
        Staff staff = staffService.getStaffByUserName("ceshi", false);
        assert staff != null;
    }

    /**
     * 测试根据用户登录名获取用户信息.
     */
    @Test
    public void testGetStaffByName() {
        Staff staff = staffService.getStaffByName("测试");
        assert staff != null;
    }

    /**
     * 测试根据用户ID获取用户信息.
     */
    @Test
    public void testGetStaffByUserId() {
        Staff staff = staffService.getStaffByUserId("62");
        assert staff != null;
    }

    /**
     * 测试根据用户ID和机构获取用户机构信息.
     */
    @Test
    public void testGetStaffWithDefDepartByUserId() {
        Staff staff = staffService.getStaffWithDefDepartByUserId("62");
        assert staff != null;
    }

    /**
     * 测试根据用户ID和角色获取用户机构信息.
     */
    @Test
    public void testGetStaffWithRoleByUserId() {
        Staff staff = staffService.getStaffWithRoleByUserId("62");
        assert staff != null;
    }

    /**
     * 测试同步用户信息到打卡机.
     */
    /*    @Test
    @Rollback()
    public void testSynStaffToAct() {
        boolean rs = staffService.synStaffToAct();
        assert rs;
    }*/

    /**
     * 测试计算用户信息.
     */
    @Test
    public void testCompleteStaffByIdNum() {
        Staff staff = staffService.getStaffByUserId("62");
        staff = staffService.completeStaffByIdNum(staff);
        assert staff != null;
    }

    /**
     * 测试计算用户信息(导入).
     */
    @Test
    public void testCompleteStaffByIdNumExcel() {
        StaffExcel staff = new StaffExcel();
        staff.setId(ID);
        staff.setStaffId(STAFF_ID);
        staff.setName("测试");
        staff.setUserName("ceshi");
        staff.setPassword("827ccb0eea8a706c4c34a16891f84e7b");
        staff.setDepartId("41");
        staff.setPosition("测试人员");
        staff.setEntryDate("2017-09-30");
        staff.setIsFormal(0);
        staff.setFormalDate("2017-11-30");
        staff.setCompanyAge((float) 0);
        staff.setIdNumber("658958199302154518");
        staff.setBirth("1993-02-15");
        staff.setAge(AGE);
        staff.setGender(1);
        staff.setPreSeniority((float) 0);
        staff.setSeniority("0年0月");
        staff.setPayCard(null);
        staff.setPoliticalStatus("群众");
        staff.setJobNumber("99999");
        staff.setContractStart("2017-09-30");
        staff.setContractEnd("2019-09-30");
        staff.setTel("12345678978");
        staff.setGraduateSchool("测试大学");
        staff.setMajor("测试专业");
        staff.setEducation("本科");
        staff.setGraduateDate("2017-06-22");
        staff.setNativePlace("dfdswafrrws");
        staff.setHomeAddress("123423432");
        staff.setCensusRegister("34523454365");
        staff.setCompany("直真君智");
        staff.setOnJob(1);
        staff.setItAttributes("科技人员");
        staff.setResearchStaff("研发人员");
        staff = staffService.completeStaffByIdNum(staff);
        assert staff != null;
    }

    /**
     * 测试设置部门角色.
     */
    @Test
    public void testSetDepartRole() {
        List<DepartRole> departRoles = new ArrayList<>();
        DepartRole departRole = new DepartRole();
        departRole.setId(ID);
        departRole.setStaffId(STAFF_ID);
        departRole.setDepartId(1);
        departRole.setGroupId("firstLeader");
        departRoles.add(departRole);
        boolean rs = staffService.setDepartRole(STAFF_ID, departRoles);
        assert rs;
    }

    /**
     * 测试根据部门和角色查询用户信息.
     */
    @Test
    public void testGetStaffByDepartAndRole() {
        List<Staff> list = staffService.getStaffByDepartAndRole("", Long.parseLong("41"));
        assert list != null;
    }

    /**
     * 测试根据部门和用户查询用户信息.
     */
    @Test
    public void testGetStaffByDepartAndUser() {
        String result = staffService.getStaffByDepartAndUser("62", Long.parseLong("41"));
        assert !"".equals(result);

    }

    /**
     * 测试获取所有双软人员属性.
     */
    @Test
    public void testGetAllPostList() {
        List<Bean> list = staffService.getAllPostList();
        assert list != null;
    }

    /**
     * 测试根据用户ID获取邮箱信息.
     */
    @Test
    public void testGetEmailByStaffId() {
        String email = staffService.getEmailByStaffId(Long.parseLong("62"));
        assert !"".equals(email);
    }

    /**
     * 测试根据机构获取用户信息列表.
     */
    @Test
    public void testGetStaffByDepart() {
        List<Staff> list = staffService.getStaffByDepart(Long.parseLong("41"));
        assert list != null;
    }

    /**
     * 测试根据角色获取用户信息列表.
     */
    @Test
    public void testGetWithRole() {
        StaffRequest staffRequest = new StaffRequest();
        List<Staff> list = staffService.getWithRole(staffRequest);
        assert list != null;
    }

    /**
     * 测试批量删除用户信息.
     */
    @Test
    public void testDelByStaffIds() {
        String[] staffIds = {STAFF_ID + ""};
        boolean rs = staffService.delByStaffIds(staffIds);
        assert rs;
    }
}
