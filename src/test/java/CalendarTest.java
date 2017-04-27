import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.service.CalendarService;
import zzjz.service.impl.CalendarServiceImpl;

/**
 * @author 彭鹏
 * @ClassName: zoa
 * @Description:
 * @date 2017年04月12日11:08
 */
public class CalendarTest {

    @Autowired
    CalendarService calendarService = new CalendarServiceImpl();

    @Test
    public void test (){
        String curDay = "2017-04-04";
        boolean rs = calendarService.updateHoliday(curDay);
        System.out.println(rs);
    }
}
