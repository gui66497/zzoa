package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.PagingEntity;
import zzjz.bean.Vacation;
import zzjz.bean.VacationDetail;
import zzjz.bean.VacationRequest;
import zzjz.bean.Staff;
import zzjz.service.VacationService;

import java.text.ParseException;
import java.util.List;

/**
 * @author 彭鹏
 * @version 2017/9/30.
 */
public class VacationServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer DEPART_ID = 41;

    private static final Integer ID = 1;

    @Autowired
    VacationService vacationService;

    /**
     * 测试新增假期信息.
     */
    @Test
    public void testAddVacationInfo() {
        Vacation vacation = new Vacation();
        vacation.setId(ID);
        vacation.setActId(null);
        vacation.setUserId("62");
        vacation.setUserName("彭鹏");
        vacation.setLeaveTyp("02");
        vacation.setReason("测试");
        vacation.setLeaveDate("2016-09-30");
        vacation.setBackDate("2016-09-30");
        vacation.setLeavePart("0");
        vacation.setBackPart("1");
        vacation.setLeaveDay(1.0);
        vacation.setCreateTime("2017-09-30 10:46:55");
        vacation.setStatus("00");
        String actId = vacationService.addVacationInfo(vacation);
        assert !"".equals(actId);
    }

    /**
     * 测试分页获取假期信息.
     */
    @Test
    public void testGetVacationListPage() {
        VacationRequest request = new VacationRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(PAGE_SIZE);
        request.setPaging(pagingEntity);
        Page<Vacation> page = vacationService.getVacationListPage(request);
        assert page != null;
    }

    /**
     * 测试修改假期信息.
     */
    @Test
    public void testUpdateVacationInfo() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.updateVacationInfo(vacation);
        assert rs;
    }

    /**
     * 测试计算请假天数.
     */
    @Test
    public void testBetweenDays() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        Double days = vacationService.betweenDays(vacation);
        assert days >= 0;
    }

    /**
     * 测试初始化计算假期信息.
     * @throws ParseException ParseException
     */
    @Test
    public void testInitVacationInfo() throws ParseException {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        vacation = vacationService.initVacationInfo(vacation, "62");
        assert vacation != null;
    }

    /**
     * 测试提交假期申请.
     */
    @Test
    public void testSubmitVacationInfo() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.submitVacationInfo(vacation, "27", "62");
        assert rs;
    }

    /**
     * 测试审批假期信息.
     */
    @Test
    public void testApproveVacationTask() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.approveVacationTask(vacation, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试获取下一处理人列表.
     */
    @Test
    public void testGetNextUser() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        List<Staff> list = vacationService.getNextUser(vacation, DEPART_ID, "agree");
        assert list != null;
    }

    /**
     * 测试审批并结束假期信息.
     */
    @Test
    public void testTerminationVacationTask() {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.terminationVacationTask(vacation);
        assert rs;
    }

    /**
     * 测试销假.
     * @throws ParseException ParseException
     */
    @Test
    public void testForVacationInfo() throws ParseException {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.forVacationInfo(vacation);
        assert rs;
    }

    /**
     * 测试计算并生成假期明细表.
     * @throws ParseException ParseException
     */
    @Test
    public void testInitVacationDetailInfo() throws ParseException {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.initVacationDetailInfo(vacation);
        assert rs;
    }

    /**
     * 测试根据假期明细请求信息查询假期明细信息.
     */
    @Test
    public void testSelectVacationInfo() {
        VacationDetail vacation = new VacationDetail();
        vacation.setLeaveTyp("02");
        List<VacationDetail> list = vacationService.selectVacationInfo(vacation);
        assert list != null;
    }

    /**
     * 测试获取剩余调休天数.
     */
    @Test
    public void testGetLeftOffDays() {
        Double days = vacationService.getLeftOffDays("62");
        assert days >= 0;
    }

    /**
     * 测试获取剩余年假天数.
     */
    @Test
    public void testGetLeftAnnualDays() {
        Double days = vacationService.getLeftAnnualDays("62");
        assert days >= 0;
    }

    /**
     * 测试删除假期信息.
     * @throws ParseException ParseException
     */
    @Test
    public void testDeleteRecordByActId() throws ParseException {
        Vacation vacation = vacationService.getVacationInfoById(ID);
        boolean rs = vacationService.deleteRecordByActId(vacation.getActId());
        assert rs;
    }

    /**
     * 测试撤销假期信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Vacation vacation = new Vacation();
        vacation.setId(ID);
        vacation.setActId(null);
        vacation.setUserId("62");
        vacation.setUserName("彭鹏");
        vacation.setLeaveTyp("02");
        vacation.setReason("测试");
        vacation.setLeaveDate("2016-09-30");
        vacation.setBackDate("2016-09-30");
        vacation.setLeavePart("0");
        vacation.setBackPart("1");
        vacation.setLeaveDay(1.0);
        vacation.setCreateTime("2017-09-30 10:46:55");
        vacation.setStatus("00");
        String actId = vacationService.addVacationInfo(vacation);
        boolean rs = vacationService.revokeRecordByActId(actId);
        assert rs;
    }
}
