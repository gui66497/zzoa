package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.bean.Annual;
import zzjz.service.AnnualService;

/**
 * @author 彭鹏
 * @version 2017/9/27.
 */
public class AnnualServiceTest extends JunitBaseTest {

    private static final Integer STAFF_ID = 3;

    private static final String YEAR = "2016";

    private static final Double DAY10 = 10.0;

    @Autowired
    AnnualService annualService;

    /**
     * 测试根据用户ID查询年假天数.
     */
    @Test
    @Rollback()
    public void testAnnualDays() {
        Double days = annualService.annualDays(STAFF_ID + "", YEAR);
        assert days >= 0;
    }

    /**
     * 测试初始化用户年假信息.
     */
    @Test
    @Rollback()
    public void testInitAnnual() {
        boolean rs = annualService.initAnnual(STAFF_ID + "", YEAR);
        assert rs;
    }

    /**
     * 测试初始化所有用户年假信息.
     */
    @Test
    @Rollback()
    public void testInitBatchAnnual() {
        boolean rs = annualService.initBatchAnnual(YEAR);
        assert rs;
    }

    /**
     * 测试更新年假信息.
     */
    @Test
    @Rollback()
    public void testUpdateAnnualInfo() {
        Annual annual = new Annual();
        annual.setUserId(STAFF_ID + "");
        annual.setYear(YEAR);
        annual.setAnnualAll(DAY10);
        annual.setAnnualUse(0.0);
        annual.setAnnualLeft(DAY10);
        boolean rs = annualService.updateAnnualInfo(annual);
        assert rs;
    }

    /**
     * 测试查询年假信息.
     */
    @Test
    @Rollback()
    public void testQueryAndInitAnnual() {
        boolean rs = annualService.queryAndInitAnnual(STAFF_ID);
        assert rs;
    }

}
