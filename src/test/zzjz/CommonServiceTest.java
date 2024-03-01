package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import zzjz.service.CommonService;

import java.text.ParseException;

/**
 * @author 彭鹏
 * @version 2017/9/28.
 */
public class CommonServiceTest extends JunitBaseTest {
    @Autowired
    CommonService commonService;

    /**
     * 测试获取主键.
     */
    @Test
    @Rollback()
    public void testGetActSeq() {
        Integer i = commonService.getActSeq();
        assert i > 0;
    }

    /**
     * 测试校验事项.
     * @throws ParseException ParseException
     */
    @Test
    @Rollback()
    public void testCheckOption() throws ParseException {
        boolean rs = commonService.checkOption("62", "2016-09-28", "2016-09-28", "mor", "mor");
        assert rs;
    }

    /**
     * 测试核销时校验事项.
     * @throws ParseException ParseException
     */
    @Test
    @Rollback()
    public void testForCheckOption() throws ParseException {
        boolean rs = commonService.forCheckOption("62", "2016-09-28 08:20:20", "2016-09-28 11:45:23", 1);
        assert rs;
    }
}
