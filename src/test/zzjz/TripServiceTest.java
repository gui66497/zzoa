package zzjz;

import com.github.pagehelper.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.PagingEntity;
import zzjz.bean.Trip;
import zzjz.bean.TripDetail;
import zzjz.bean.TripRequest;
import zzjz.bean.Staff;
import zzjz.service.TripService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017/9/30.
 */
public class TripServiceTest extends JunitBaseTest {

    private static final Integer PAGE_SIZE = 50;

    private static final Integer ID = 1;

    private static final Integer DEPART_ID = 41;

    @Autowired
    TripService tripService;

    /**
     * 测试添加出差信息.
     */
    @Test
    public void testAddTripInfo() {
        Trip trip = new Trip();
        trip.setId(ID);
        trip.setUserId("62");
        trip.setUserName("彭鹏");
        trip.setAddress("无锡");
        trip.setTripDate("2016-09-30");
        trip.setBackDate("2016-09-30");
        trip.setTripPart("0");
        trip.setBackPart("1");
        trip.setTripDay(1.0);
        trip.setStatus("00");
        trip.setTools("火车");
        trip.setIntends("测试");
        String actId = tripService.addTripInfo(trip);
        assert !"".equals(actId);
    }

    /**
     * 测试分页获取出差信息列表.
     */
    @Test
    public void testGetTripListPage() {
        TripRequest request = new TripRequest();
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageSize(PAGE_SIZE);
        pagingEntity.setPageNo(1);
        request.setPaging(pagingEntity);
        Page<Trip> page = tripService.getTripListPage(request);
        assert page != null;
    }

    /**
     * 测试修改出差信息.
     */
    @Test
    public void testUpdateTripInfo() {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.updateTripInfo(trip);
        assert rs;
    }

    /**
     * 测试计算出差天数.
     */
    @Test
    public void testBetweenDays() {
        Trip trip = tripService.getTripInfoById(ID);
        Double days = tripService.betweenDays(trip);
        assert days >= 0.0;
    }

    /**
     * 测试提交出差申请.
     * @throws ParseException ParseException
     */
    @Test
    public void testSubmitTripTask() throws ParseException {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.submitTripTask(trip, "27", "62");
        assert rs;
    }

    /**
     * 测试审批出差信息.
     */
    @Test
    public void testApproveTripTask() {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.approveTripTask(trip, "3", "agree", "同意");
        assert rs;
    }

    /**
     * 测试获取下一处理人列表.
     */
    @Test
    public void testGetNextUser() {
        Trip trip = tripService.getTripInfoById(ID);
        List<Staff> list = tripService.getNextUser(trip, DEPART_ID, "agree");
        assert list != null;
    }

    /**
     * 测试审批并结束出差信息.
     */
    @Test
    public void testTerminationTripTask() {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.terminationTripTask(trip);
        assert rs;
    }

    /**
     * 测试计算出差信息.
     */
    @Test
    public void testInitTripInfo() {
        Trip trip = tripService.getTripInfoById(ID);
        trip = tripService.initTripInfo(trip, "62");
        assert trip != null;
    }

    /**
     * 测试根据主键获取出差信息.
     */
    @Test
    public void testGetTripInfoById() {
        Trip trip = tripService.getTripInfoById(ID);
        assert trip != null;
    }

    /**
     * 测试计算并生成出差明细表.
     * @throws ParseException ParseException
     */
    @Test
    public void testInitTripDetailInfo() throws ParseException {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.initTripDetailInfo(trip);
        assert rs;
    }

    /**
     * 测试查询出差明细信息列表.
     */
    @Test
    public void testSelectTripInfo() {
        TripDetail trip = new TripDetail();
        trip.setUserId("62");
        List<TripDetail> list = tripService.selectTripInfo(trip);
        assert list != null;
    }

    /**
     * 测试计算出差核销信息.
     */
    @Test
    public void testInitForTripInfo() {
        Trip trip = tripService.getTripInfoById(ID);
        trip = tripService.initForTripInfo(trip, "62");
        assert trip != null;
    }

    /**
     * 测试出差核销.
     * @throws ParseException ParseException
     */
    @Test
    public void testForTrip() throws ParseException {
        Trip trip = tripService.getTripInfoById(ID);
        trip.setStartTime("2017-09-30 08:20:00");
        trip.setEndTime("2017-09-30 18:20:00");
        boolean rs = tripService.forTrip(trip);
        assert rs;
    }

    /**
     * 测试查询出差信息.
     */
    @Test
    public void testGetTripList() {
        Trip trip = tripService.getTripInfoById(ID);
        List<TripDetail> list = tripService.getTripList("62", trip.getTripDate(), "all");
        assert list != null;
    }

    /**
     * 测试出差信息导出内容.
     */
    @Test
    public void testGetTripMapList() {
        Trip trip = tripService.getTripInfoById(ID);
        Map<String, Object> map = tripService.getTripMapList(trip);
        assert map != null;
    }

    /**
     * 测试根据流程ID删除出差信息和流程信息.
     */
    @Test
    public void testDeleteRecordByActId() {
        Trip trip = tripService.getTripInfoById(ID);
        boolean rs = tripService.deleteRecordByActId(trip.getActId());
        assert rs;
    }

    /**
     * 测试撤销出差信息.
     */
    @Test
    public void testRevokeRecordByActId() {
        Trip trip = new Trip();
        trip.setId(ID);
        trip.setUserId("62");
        trip.setUserName("彭鹏");
        trip.setAddress("无锡");
        trip.setTripDate("2016-09-30");
        trip.setBackDate("2016-09-30");
        trip.setTripPart("0");
        trip.setBackPart("1");
        trip.setTripDay(1.0);
        trip.setStatus("00");
        trip.setTools("火车");
        trip.setIntends("测试");
        String actId = tripService.addTripInfo(trip);
        boolean rs = tripService.revokeRecordByActId(actId);
        assert rs;
    }
}
