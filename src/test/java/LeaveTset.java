import org.junit.Test;
import zzjz.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * @author 彭鹏
 * @ClassName: LeaveTset
 * @Description: 计算年假测试类
 * @date 2017年04月07日10:43
 */
public class LeaveTset {
    @Test
    public void YearLeaveTest(){

        Double pastYear = 1.5; //加入公司前已工作年限
        Double inYear = 0.0; //司龄
        Double workYear = pastYear+inYear;
        String inDate = "2017-03-17";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int leaveDays = 0;
        if(workYear>=1&&workYear<=10){
            leaveDays = 5;
        } else if (workYear>10&&workYear<=20) {
            leaveDays = 10;
        } else if (workYear>20){
            leaveDays = 15;
        }
        try {
            Date joinDate = sdf.parse(inDate);
            if(inYear<1){
                Date beginDate;
                Date endDate;

                Calendar now = Calendar.getInstance();
                if((now.get(Calendar.MONDAY+1)<3)||(now.get(Calendar.MONDAY+1)==3&&(now.get(Calendar.DAY_OF_MONTH)<=15))){
                    beginDate = sdf.parse((now.get(Calendar.YEAR)-1)+"-03-16");
                    endDate = sdf.parse(now.get(Calendar.YEAR)+"-03-15");
                } else {
                    beginDate = sdf.parse(now.get(Calendar.YEAR)+"-03-16");
                    endDate = sdf.parse((now.get(Calendar.YEAR)+1)+"-03-15");
                }
                System.out.println(sdf.format(beginDate));
                System.out.println(sdf.format(endDate));
                if(joinDate.after(beginDate)&&joinDate.before(endDate)){
                    int leftDays = DateUtil.daysBetween(joinDate,endDate);
                    int allDays = DateUtil.daysBetween(beginDate,endDate);
                    leaveDays = Math.round(leaveDays*leftDays/allDays);
                }
            }
            System.out.println(leaveDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 迟到扣款
     * 迟到15分钟以内，扣款10元
     * 迟到15到30分钟，扣款20元
     * 迟到30到60分钟，扣款50元
     * 迟到60分钟以上，记为旷工一天
     */
    @Test
    public void KK(){

    }

    /**
     * 需要导入日历表
     */
    @Test
    public void DD(){

        String input = "2017-02";

        if(!input.matches("\\d{4}-\\d{2}")){
            System.out.println("Error input in format, exit!");
            System.exit(0);
        }

        int count = 0;

        int month = Integer.parseInt(input.substring(5, 7));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(input.substring(0, 4)));
        cal.set(Calendar.MONTH,  month - 1);
        cal.set(Calendar.DATE, 1);
        while(cal.get(Calendar.MONTH) < month){
            int day = cal.get(Calendar.DAY_OF_WEEK);

            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){
                count++;
            }

            cal.add(Calendar.DATE, 1);
        }

        System.out.println(count);
    }
}
