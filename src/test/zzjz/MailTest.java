package zzjz;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.google.common.io.Files;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import zzjz.bean.DepartRole;
import zzjz.bean.Staff;
import zzjz.bean.StaffRequest;
import zzjz.service.CatalogService;
import zzjz.service.GroupService;
import zzjz.service.StaffService;
import zzjz.task.TaskTest;
import zzjz.util.RoleUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MailTest
 * @Description: 考勤、邮件发送测试类
 * @author 房桂堂
 * @version  2017/6/22 14:50
 */
public class MailTest extends JunitBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailTest.class);

    private static final int EIGHT = 8;

    @Value("${jdbc.url}")
    String url;

    @Autowired
    TaskTest taskTest;

    @Autowired
    StaffService staffService;

    @Autowired
    CatalogService catalogService;

    @Autowired
    GroupService groupService;

    /**
     * 每日23:58触发 记录当日考勤到数据库.
     */
    @Test
    public void testAddAttendance() {
        taskTest.addAttendance();
    }

    /**
     * 补齐指定日期之后的所有考勤记录.
     */
    @Test
    public void testAttendanceSupply() {
        try {
            taskTest.attendanceSupply("2022-07-01");
        } catch (ConnectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邮件提醒指定日期的考勤异常情况. 5.15号到5-22没发 6.29~7.6没发
     */
    @Test
    public void testMailRemindAttendance() {
        taskTest.mailRemindAttendance("2019-07-11", true);
    }

    /**
     * 根据用户号获取员工信息(包括默认部门信息).
     */
    @Test
    public void testGet() {
        Staff staff = staffService.getStaffWithDefDepartByUserId("44");
    }

    /**f
     * 获取系统属性.
     */
    @Test
    public void getProperty() {
        LOGGER.info(System.getProperty("user.dir"));
    }

    /**
     * 获取用户包括角色信息.
     */
    @Test
    public void testGetWithRole() {
        StaffRequest staffRequest = new StaffRequest();
        staffRequest.setOnJob(1);
        List<Staff> staffList = staffService.getWithRole(staffRequest);
        for (Staff staff : staffList) {
            List<DepartRole> departRoles = staff.getDepartRoles();
            String topRole = RoleUtil.getTopRole(departRoles);
        }
    }

    /**
     * test2.
     * @throws Exception Exception
     */
    @Test
    public void test2() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "tem.xls");
        /*params.setHeadingRows(2);
        params.setHeadingStartRow(3);*/
        Map<String, Object> map = new HashMap<String, Object>();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int i = 0; i < EIGHT; i++) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("staffId", "id" + "1");
            m.put("depart", "depart" + "1");
            m.put("userName", i + "");

            m.put("over03", i + "");
            m.put("month03", i + "");
            m.put("left03", i + "");

            m.put("over04", i + "");
            m.put("month04", i + "");
            m.put("left04", i + "");

            m.put("over05", i + "");
            m.put("month05", i + "");
            m.put("left05", i + "");
            list.add(m);
        }
        map.put("list", list);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("D:/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/excel/merge_test2.xls");
        workbook.write(fos);
        fos.close();
    }

    /**
     * 测试获取所有组.
     */
    @Test
    public void testList() {
        groupService.queryAllGroup();
    }

    /**
     * 测试邮件提醒所有具有没审批的单子的用户.
     */
    @Test
    public void testMailRemindApprove() {
        taskTest.mailRemindApprove();
    }

    /**
     * 测试计算司龄 累计工龄.
     */
    @Test
    public void testCountSeniority() {
        taskTest.countSeniorityScheduled();
    }

    @Test
    public void testttt() throws IOException, InterruptedException {
        while (true) {
            //读取移动硬盘中的文件 防止休眠
            String fileName = "F:\\fgt\\soc\\bh.txt";
            Files.write(new DateTime().toString().getBytes(StandardCharsets.UTF_8),new File(fileName));
            //5分钟
            Thread.sleep(180000);
        }
    }

}