package zzjz.task;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zzjz.bean.Attendance;
import zzjz.bean.AttendanceExcel;
import zzjz.bean.Depart;
import zzjz.bean.Mail;
import zzjz.bean.Mail.ContentType;
import zzjz.bean.MainDesk;
import zzjz.bean.PagingEntity;
import zzjz.bean.PunchedCard;
import zzjz.bean.PunchedInfo;
import zzjz.bean.Staff;
import zzjz.bean.StaffExcel;
import zzjz.bean.StaffRequest;
import zzjz.bean.TbMail;
import zzjz.service.AnnualService;
import zzjz.service.AttendanceService;
import zzjz.service.BackUpService;
import zzjz.service.CalendarService;
import zzjz.service.DepartService;
import zzjz.service.MailService;
import zzjz.service.MainDeskService;
import zzjz.service.OverOffService;
import zzjz.service.StaffService;
import zzjz.service.ZkemService;
import zzjz.util.DateUtil;
import zzjz.util.MailUtil;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author 彭鹏
 * @version 2017年04月10日14:39
 * @ClassName: TaskTest
 * @Description: 定时任务测试
 */
@Component
public class TaskTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskTest.class);

    private String bACKPATH = "F:\\project";

    @Autowired
    StaffService staffService;

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    AnnualService annualService;

    @Autowired
    OverOffService overOffService;

    @Autowired
    ZkemService zkemService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    DepartService departService;

    @Autowired
    BackUpService backUpService;

    @Autowired
    TaskService taskService;

    @Autowired
    MainDeskService mainDeskService;

    @Autowired
    MailService mailService;

    @Value("${punchIp}")
    String punchIp;

    /**
     * 需要发送考勤邮件的部门
     */
    @Value("${mail.depart}")
    String mailDepart;

    /**
     * 需要排除的人
     */
    @Value("${mail.except}")
    String mailExcept;

    /**
     * 特定需要发送邮件人员
     */
    @Value("${mail.special}")
    String mailSpecial;

    /**
     * 每日23:58触发 记录当日考勤到数据库.
     */
    @Scheduled(cron = "0 58 23 * * ? ")//每日23:58触发
    public void addAttendance() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date date = new Date();

        LOGGER.info("###本定时任务为添加考勤定时任务，当前时间为：" + sdf.format(date));

        try {
            List<PunchedCard> punchedCards = zkemService.getLogsToday();
            punchCardsToAttendance(punchedCards, true);

        } catch (ConnectException e) {
            e.printStackTrace();
        }

    }

    /**
     * 每年月1号0点触发 年假初始化.
     */
    @Scheduled(cron = "0 0 0 1 4 ? ")//每年4月1号0点触发
    public void initAnnualInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        Date date = new Date();

        LOGGER.info("###本定时任务为计算年假定时任务，当前时间为：" + sdf.format(date));
        annualService.initBatchAnnual(sdf1.format(date));
    }

    /**
     * 每月1号0点触发  批量初始化加班调休.
     */
    @Scheduled(cron = "0 0 0 1 * ? ")//每月1号0点触发
    public void initOverOffInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();

        LOGGER.info("###本定时任务为计算加班调休定时任务，当前时间为：" + sdf.format(date));
        overOffService.batchInitOverOffInfo(sdf1.format(date));

    }

    /**
     * 每天早上八点钟 查询昨日有异常的考勤人员并发送邮件.
     */
    //@Scheduled(cron = "0 00 08 * * ? ")
    public void yesterdayAttendance() {
        //昨天日期
        String yesterdayStr = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        mailRemindAttendance(yesterdayStr, true);
    }

    /**
     * 邮件提醒指定日期的考勤异常情况.
     *
     * @param dateStr  日期（yyyy-MM-dd）
     * @param realSend 是否真的发送
     */
    public void mailRemindAttendance(String dateStr, boolean realSend) {
        LOGGER.info("查询 " + dateStr + " 考勤情况，并给有异常人员发送邮件");
        if (DateUtil.isToday(dateStr)) {
            LOGGER.error("邮件提醒的指定日期不能是当日！");
            return;
        }
        if (!calendarService.isWorkDay(dateStr)) {
            LOGGER.info("由于指定日期不是工作日，任务退出");
            return;
        }
        DateTime dateTime = new DateTime(dateStr);
        Attendance attendance = new Attendance();
        attendance.setBeginTime(dateStr);
        attendance.setEndTime(dateStr);
        //得到昨天所有考勤异常的信息
        List<Attendance> errAttendances = attendanceService.getErrorByDate(attendance);
        if (errAttendances.isEmpty()) {
            return;
        }
        MailUtil mailUtil = new MailUtil();
        mailUtil.init();
        LOGGER.info("总共有" + errAttendances.size() + "条考勤异常");
        //遍历并逐个发送邮件
        for (Attendance one : errAttendances) {
            if (!isNeedAttendance(one.getUserId())) {
                LOGGER.info("员工 " + one.getUserName() + " 不需要参与考勤异常提醒！");
                continue;
            }
            String emailAddress = staffService.getEmailByStaffId(Long.parseLong(one.getUserId()));
            /*if(!"xudong.li@zzjunzhi.com".equals(emailAddress)) {
                continue;
            }*/
            //测试用 暂时将email都设置为自己
            //emailAddress = "guitang.fang@zzjunzhi.com";
            if (StringUtils.isBlank(emailAddress)) {
                LOGGER.info(one.getUserName() + "的邮箱为空，无法发送");
                continue;
            }

            Mail mail = new Mail();
            mail.setSender("hr@zzjunzhi.com");
            mail.setAddressees(new String[]{emailAddress});
            mail.setSubject("考勤异常通知");
            String content = "您好: " + one.getUserName() + "</br>您" + dateTime.toString("yyyy年MM月dd日") + "的考勤存在异常：" + one.getErrorDesc();
            if (one.getSignTime() == null) {
                content += "</br>您的签到时间为： 无 ,签退时间为： 无";
            } else {
                content += "</br>您的签到时间为：" + one.getSignTime() + " ,签退时间为：";
                if (one.getSignTime().equals(one.getLeaveTime())) {
                    content += "无";
                } else {
                    content += one.getLeaveTime();
                }
            }
            content += " ，请留意并及时到<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\">OA系统</a>进行处理";
            content += "</br></br><span style=\"font-size:10px;color:#908f8f;\">本邮件为";
            content += "<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\" style=\"font-size:10px;color:#908f8f;\">OA系统</a>";
            content += "<span style=\"font-size:10px;color:#908f8f;\">自动发送,请勿回复。</span>";
            mail.setContent(content);
            // mail.setContent("<!DOCTYPE html><html lang=\"zh-CN\"><head><title>您的礼品已收到</title><meta charset=\"utf-8\" /></head>
            // <body>您的礼品我们已收到，礼品号33001;请点击下方连接查看您的礼品投递情况.<br /><a href=\"http://www.baidu.com?d=33001\">
            // http://www.baidu.com?d=33001<a></body></html>");
            mail.setContentType(ContentType.HTML);
            LOGGER.info("开始向 " + one.getUserName() + "(" + emailAddress + ")发送邮件，内容为：" + content);
            if (realSend) {
                try {
                    mailUtil.send(mail);
                    LOGGER.error("真的发送了");
                    TbMail tbMail = new TbMail(one.getUserName(), emailAddress, "考勤异常提醒", null, content);
                    mailService.record(tbMail);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("发送邮件出错:" + e.getMessage());
                }
            }
        }
    }

    /**
     * 补齐指定日期之后的所有考勤记录.
     *
     * @param dateStr 日期（yyyy-MM-dd）
     * @throws ConnectException ConnectException
     */
    public void attendanceSupply(String dateStr) throws ConnectException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = DateUtil.getTodayZero();
        if (date.getTime() >= today.getTime()) {
            LOGGER.error("请选择今日之前的日期");
            return;
        }
        List<PunchedCard> punchedCards = zkemService.getLogAfter(date);
        punchCardsToAttendance(punchedCards, false);
    }

    /**
     * 通过打卡记录生成当日的考勤.
     *
     * @param punchedCards 打卡记录
     * @param isNeedToday  是否需要当日
     * @return 结果
     */
    public boolean punchCardsToAttendance(List<PunchedCard> punchedCards, boolean isNeedToday) {
        Map<String, List<PunchedCard>> punchMap = convertPunch(punchedCards);

        for (Map.Entry<String, List<PunchedCard>> entry : punchMap.entrySet()) {
            String dateStrTemp = entry.getKey();
            if (!isNeedToday) {
                //排除今日
                if (DateUtil.isToday(dateStrTemp)) {
                    continue;
                }
            }
            List<PunchedCard> todayPunchCards = entry.getValue();
            PunchedInfo punchedInfo = new PunchedInfo();
            punchedInfo.setLogs(todayPunchCards);
            attendanceService.addPunchInfo(punchedInfo);

            //将其它未打卡人员的记录补齐
            Set<String> userIds = new HashSet<>();
            for (PunchedCard p : todayPunchCards) {
                userIds.add(p.getUid());
            }
            List<Staff> staffs = staffService.getAllSimpleStaffOnJob();
            List<AttendanceExcel> otherAttendances = new ArrayList<>();
            for (Staff staff : staffs) {
                if (!userIds.contains(String.valueOf(staff.getStaffId()))) {
                    AttendanceExcel attendanceExcel = new AttendanceExcel();
                    attendanceExcel.setUserId(String.valueOf(staff.getStaffId()));
                    attendanceExcel.setUserName(staff.getName());
                    attendanceExcel.setCheckDate(dateStrTemp);
                    if (StringUtils.isNotBlank(staff.getDepartId())) {
                        Depart depart = departService.selectByPrimaryKey(Long.parseLong(staff.getDepartId()));
                        attendanceExcel.setDepart(depart.getDepartName());
                    }
                    otherAttendances.add(attendanceExcel);
                }
            }

            for (AttendanceExcel attendance : otherAttendances) {
                attendance = attendanceService.updateAttentionInfo(attendance);
                attendanceService.checkAttendanceData(attendance);
            }
            attendanceService.addAttendanceList(otherAttendances);
        }
        return true;
    }

    /**
     * 将考勤记录按照日期整理成 Map形式
     *
     * @param punchedCards 打卡记录
     * @return 结果
     */
    Map<String, List<PunchedCard>> convertPunch(List<PunchedCard> punchedCards) {
        Map<String, List<PunchedCard>> resMap = new HashMap<>();

        for (PunchedCard punchedCard : punchedCards) {
            String date = punchedCard.getYear() + "-" + (punchedCard.getMonth().length() == 1 ? "0" +
                    punchedCard.getMonth() : punchedCard.getMonth()) + "-" + (punchedCard.getDay().length() == 1 ?
                    "0" + punchedCard.getDay() : punchedCard.getDay());
            if (resMap.containsKey(date)) {
                List<PunchedCard> oldList = resMap.get(date);
                oldList.add(punchedCard);
                resMap.put(date, oldList);
            } else {
                List<PunchedCard> punchedCardsTemp = new ArrayList<>();
                punchedCardsTemp.add(punchedCard);
                resMap.put(date, punchedCardsTemp);
            }
        }
        return sortMapByKey(resMap);
    }

    /**
     * 按照日期升序排列.
     *
     * @param oriMap 源打卡记录
     * @return 打卡记录
     */
    public static Map<String, List<PunchedCard>> sortMapByKey(Map<String, List<PunchedCard>> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, List<PunchedCard>> sortedMap = new TreeMap<>(new Comparator<String>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            public int compare(String key1, String key2) {
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = sdf.parse(key1);
                    date2 = sdf.parse(key2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    return date1.getTime() - date2.getTime() > 0 ? 1 : -1;
                } catch (NullPointerException e) {
                    return 1;
                }
            }
        });
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    /**
     * 判断一个员工是否需要参与考勤邮件提醒
     *
     * @param userId 员工id
     * @return 结果
     */
    boolean isNeedAttendance(String userId) {
        Staff staff = staffService.getStaffWithDefDepartByUserId(userId);
        if (0 == staff.getIsRemind()) {
            LOGGER.info(staff.getName() + "设置了考勤异常不提醒");
            return false;
        }
        if (Arrays.asList(mailSpecial.split("#")).contains(staff.getName())) {
            LOGGER.info(staff.getName() + "是特定必须发送人员");
            return true;
        }
        //是特定排除人员
        if (Arrays.asList(mailExcept.split("#")).contains(staff.getName())) {
            LOGGER.info(staff.getName() + "是特定排除人员");
            return false;
        }
        if (Arrays.asList(mailDepart.split("#")).contains(staff.getDepartName())) {
            return true;
        }
        LOGGER.info(staff.getName() + "不在邮件发送部门");
        return false;
    }

    /*public static void main(String[] args) {
        Map<String, List<PunchedCard>> resMap = new HashMap<>();
        List<PunchedCard> punchedCards = new ArrayList<>();
        resMap.put("2017-06-10", punchedCards);
        resMap.put("2017-06-09", punchedCards);
        resMap.put("2017-06-19", punchedCards);
        resMap.put("2017-06-02", punchedCards);
        resMap.put("2017-06-15", punchedCards);
        resMap.put("2017-06-20", punchedCards);
        resMap = sortMapByKey(resMap);
        System.out.println(resMap);
    }*/

    /**
     * 邮件提醒所有具有没审批的单子的用户.
     */
    public void mailRemindApprove() {
        List<MainDesk> mainDesks = mainDeskService.getRunningDesk();
        Map<String, Integer> staffMap = new HashMap<>();
        for (MainDesk mainDesk : mainDesks) {
            Task task = taskService.createTaskQuery().executionId(mainDesk.getActId()).singleResult();
            if ("apply".equals(task.getTaskDefinitionKey())) {
                continue;
            }
            String assign = task.getAssignee();
            Staff assignStaff = staffService.getStaffByUserId(assign);
            if (assignStaff != null) {
                if (staffMap.get(assignStaff.getName()) != null) {
                    staffMap.put(assignStaff.getName(), staffMap.get(assignStaff.getName() + 1));
                } else {
                    staffMap.put(assignStaff.getName(), 1);
                }
            }
        }
        Task task = taskService.createTaskQuery().executionId("12525").singleResult();
        LOGGER.info("getAssignee: " + task.getAssignee());
        LOGGER.info("getTaskDefinitionKey: " + task.getTaskDefinitionKey());
    }

    /**
     * 备份.
     */
    //@Scheduled(cron = "0 0 22 ? * Sun ")//每周末晚上10点触发
    public void backup() {
        LOGGER.info("开始备份");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String time = sdf.format(new Date());
        boolean rs = backUpService.backup("E:\\backup\\backup_" + time + ".zip", new File(bACKPATH));
        if (rs) {
            LOGGER.info("备份完成");
        } else {
            LOGGER.info("备份异常");
        }
    }

    /**
     * 数据库备份.
     */
    @Scheduled(cron = "0 30 23 ? * *")
    public void mysqlBackUp() {
        LOGGER.info("开始备份");
        Properties props = new Properties();
        try {
            props.load(TaskTest.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }
        String mysqlPath = props.getProperty("mysqlPath");
        String mysqlBackPath = props.getProperty("mysqlBackPath");
        String jdbcUsername = props.getProperty("jdbc.username");
        String jdbcPassword = props.getProperty("jdbc.password");

        boolean rs = backUpService.mysqlBackUp(mysqlPath, mysqlBackPath, jdbcUsername, jdbcPassword);

        if (rs) {
            LOGGER.info("备份完成");
        } else {
            LOGGER.info("备份异常");
        }
    }

    private static final int FIVE_HUNDRED = 500;

    private static final int TWELVE = 12;

    /**
     * 每月1号0点10分触发  计算司龄 累计工龄.
     */
    @Scheduled(cron = "0 10 0 1 * ? ")
    public void countSeniorityScheduled() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date date = new Date();
        LOGGER.info("###本定时任务为累加，当前时间为：" + sdf.format(date));

        //累计工龄=入职前工龄+司龄  单位应该都为月份  司龄为入职日期与现在的月份差,入职前工龄必填,否则累计工龄也没有
        StaffRequest request = new StaffRequest();
        request.setOnJob(1);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setPageNo(1);
        pagingEntity.setPageSize(FIVE_HUNDRED);
        request.setPaging(pagingEntity);
        List<Staff> staffList = staffService.getStaffListPage(request);
        for (Staff staff : staffList) {
            staff = countSeniority(staff);
            staffService.editStaff(staff);
        }
    }

    /**
     * 重新计算工龄和司龄的单独接口.
     * @param staff 员工
     * @return 结果
     */
    public Staff countSeniority(Staff staff) {
        assert staff != null;
        if (StringUtils.isNotBlank(staff.getEntryDate())) {
            DateTime entryTime = new DateTime(staff.getEntryDate());
            DateTime now = new DateTime();
            Period period = new Period(entryTime, now);
            int months = period.getYears() * TWELVE + period.getMonths();
            staff.setCompanyAge(Float.valueOf(months));
            if (staff.getPreSeniority() == null) {
                //如果入职前工龄为空 则默认置为0
                staff.setPreSeniority(0F);
            }
            staff.setSeniority(String.valueOf(staff.getPreSeniority() + months));
            return staff;
        }
        return null;
    }

    /**
     * 重新计算工龄和司龄的单独接口.
     * @param staff 员工
     * @return 结果
     */
    public StaffExcel countSeniority(StaffExcel staff) {
        assert staff != null;
        if (StringUtils.isNotBlank(staff.getEntryDate())) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd");
            DateTime entryTime = DateTime.parse(staff.getEntryDate(), dateTimeFormatter);
            DateTime now = new DateTime();
            Period period = new Period(entryTime, now);
            int months = period.getYears() * TWELVE + period.getMonths();
            staff.setCompanyAge((float) months);
            if (staff.getPreSeniority() == null) {
                //如果入职前工龄为空 则默认置为0
                staff.setPreSeniority(0F);
            }
            staff.setSeniority(String.valueOf(staff.getPreSeniority() + months));
            return staff;
        }
        return null;
    }
}
