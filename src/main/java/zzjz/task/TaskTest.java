package zzjz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zzjz.bean.Staff;
import zzjz.service.StaffService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 彭鹏
 * @ClassName: TaskTest
 * @Description: 定时任务测试
 * @date 2017年04月10日14:39
 */
@Component
public class TaskTest {

    @Autowired
    StaffService staffService;

    @Scheduled(cron="0 0/10 *  * * ? ")//0 15 10 L * ?	每月最后一天的10点15分触发
    public void taskCycle(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date date = new Date();
        System.out.println("本定时任务为测试任务，当前时间为："+sdf.format(date));
        List<Staff> list = staffService.getbirthRemindList();
        List<Staff> list1 = staffService.getformalRemindList();

    }
}
