package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.AttenSingleSummary;
import zzjz.bean.AttenSummary;
import zzjz.bean.NumbersSummaryRequest;
import zzjz.bean.SummaryYear;
import zzjz.bean.AttenSummaryDetails;
import zzjz.bean.NumbersSummary;
import zzjz.service.AttenSummaryService;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 彭鹏
 * @version 2017/9/27.
 */
public class AttenSummaryServiceTest extends JunitBaseTest {
    @Autowired
    AttenSummaryService attenSummaryService;

    /**
     * 测试查询汇总列表.
     */
    @Test
    @Rollback()
    public void testInitSummaryList() {
        List<AttenSummary> list = attenSummaryService.initSummaryList("2017-08", "1");
        assert list != null;
    }

    /**
     * 测试查询单项汇总信息列表.
     */
    @Test
    @Rollback()
    public void testGetSummerySingleList() {
        Map<String, String> map = new HashMap<>();
        map.put("year", "2017");
        map.put("nextyear", Integer.parseInt("2017") + 1 + "");
        map.put("leaveTyp", "02");
        map.put("userName", null);
        map.put("userId", null);
        map.put("departId", null);
        List<AttenSingleSummary> list = attenSummaryService.getSummerySingleList(map);
        assert list != null;
    }

    /**
     * 测试根据日期区间查询汇总信息.
     */
    @Test
    @Rollback()
    public void testGetSummaryYearList() {
        List<SummaryYear> list = attenSummaryService.getSummaryYearList(null, "2017-04-16", "2017-09-15", "11111111");
        assert list != null;
    }

    /**
     * 测试查询人数统计信息.
     *
     * @throws ParseException ParseException
     */
    @Test
    @Rollback()
    public void testGetNumbersSummary() throws ParseException {
        NumbersSummaryRequest request = new NumbersSummaryRequest();
        request.setBeginMonth("2017-05");
        request.setEducation(null);
        request.setEndMonth("2017-09");
        request.setNextMonth(null);
        request.setPost(null);
        request.setRule("0");
        request.setThisMonth(null);
        List<NumbersSummary> list = attenSummaryService.getNumbersSummary(request);
        assert list != null;
    }

    /**
     * 测试查询汇总详情.
     */
    @Test
    @Rollback()
    public void testGetSummaryDetails() {
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryDetails("37", "2017-05-06", "2017-09-28");
        assert list != null;
    }

    /**
     * 测试查询加班汇总详情.
     */
    @Test
    @Rollback()
    public void testGetSummaryOverDetails() {
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryOverDetails("37", "2016", "09");
        assert list.size() == 0;
    }

    /**
     * 测试查询调休汇总详情.
     */
    @Test
    @Rollback()
    public void testGetSummaryOffDetails() {
        List<AttenSummaryDetails> list = attenSummaryService.getSummaryOffDetails("37", "2016", "09");
        assert list.size() == 0;
    }

}
