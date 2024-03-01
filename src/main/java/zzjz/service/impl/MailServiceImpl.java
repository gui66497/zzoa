package zzjz.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zzjz.bean.Mail;
import zzjz.bean.Out;
import zzjz.bean.Over;
import zzjz.bean.Punch;
import zzjz.bean.Staff;
import zzjz.bean.TbMail;
import zzjz.bean.Trip;
import zzjz.bean.Vacation;
import zzjz.mapper.MailMapper;
import zzjz.mapper.OutMapper;
import zzjz.mapper.OverMapper;
import zzjz.mapper.PunchMapper;
import zzjz.mapper.TripMapper;
import zzjz.mapper.VacationMapper;
import zzjz.service.ActivitiService;
import zzjz.service.MailService;
import zzjz.service.StaffService;
import zzjz.util.MailUtil;

import java.util.Arrays;

/**
 * @author 房桂堂
 * @version 2018年02月28日14:31
 * @ClassName: MailServiceImpl
 * @Description: 邮件实现类
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private static final int EIGHT = 8;

    @Autowired
    MailMapper mailMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    StaffService staffService;

    @Autowired
    IdentityService identityService;

    @Autowired
    HistoryService historyService;

    @Autowired
    OutMapper outMapper;

    @Autowired
    PunchMapper punchMapper;

    @Autowired
    VacationMapper vacationMapper;

    @Autowired
    TripMapper tripMapper;

    @Autowired
    OverMapper overMapper;

    @Override
    public boolean record(TbMail mail) {
        return mailMapper.insertSelective(mail) > 0;
    }

    @Override
    public boolean canSend(TbMail tbMail) {
        TbMail mail = mailMapper.queryLatest(tbMail);
        if (mail == null) {
            return true;
        }
        String createTime = mail.getCreateTime();
        DateTime dateTime = DateTime.parse(createTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.sss"));
        //八小时之后才能再次发送
        return dateTime.plusHours(EIGHT).isBeforeNow();
    }

    @Override
    public boolean sendMail(String actId) {
        Task task = activitiService.findTaskById(actId);
        String taskName = task.getName();
        String applyName = taskName.substring(taskName.indexOf("[") + 1, taskName.length() - 1);
        System.out.println("测试获取申请人" + historyService.createHistoricProcessInstanceQuery().processInstanceId(actId).singleResult().getStartUserId());

        String applyer = null;
        switch (applyName) {
            case "外出":
                Out out = outMapper.queryByActId(actId);
                applyer = out.getUserName();
                break;
            case "忘打卡":
                Punch punch = punchMapper.queryByActId(actId);
                applyer = punch.getUserName();
                break;
            case "请假":
                Vacation vacation = vacationMapper.queryByActId(actId);
                applyer = vacation.getUserName();
                break;
            case "出差":
                Trip trip = tripMapper.queryByActId(actId);
                applyer = trip.getUserName();
                break;
            case "加班":
                Over over = overMapper.queryByActId(actId);
                applyer = over.getUserName();
                break;
            default:
                LOGGER.error("参数错误");
                break;
        }
        String staffId = task.getAssignee();
        if (StringUtils.isBlank(staffId)) {
            return false;
        }
        Staff leader = staffService.getStaffByUserId(staffId);
        assert leader != null;

        //判断能否发送
        TbMail mailTemp = new TbMail();
        mailTemp.setEmail(leader.getEmail());
        mailTemp.setType("考勤处理提醒");
        mailTemp.setrId(actId);

        MailUtil mailUtil = new MailUtil();
        mailUtil.init();
        Mail mail = new Mail();
        mail.setSender("hr@zzjunzhi.com");
        // 特殊处理 如果是发送给项君的 就发给孙艳
        if ("jun.xiang@zzjunzhi.com".equals(leader.getEmail())) {
            mail.setAddressees(new String[]{"yan.sun@zzjunzhi.com"});
        } else {
            mail.setAddressees(new String[]{leader.getEmail()});
        }
        if ("房桂堂".equals(applyer)) {
            mail.setAddressees(new String[]{"guitang.fang@zzjunzhi.com"});
        }
        // todo 去掉注释
        //mail.setAddressees(new String[]{"guitang.fang@zzjunzhi.com"});
        mail.setSubject("考勤处理提醒");
        String content = "您好，" + leader.getName() + "：</br>" + applyer + "的[" + applyName + "]申请需要您的审批，请登录" +
                "<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\">OA系统</a>" +
                "及时处理。";
        content += "</br></br><span style=\"font-size:10px;color:#908f8f;\">本邮件为";
        content += "<a href='http://192.168.1.9:8080/zzoa' target=\"_blank\" style=\"font-size:10px;color:#908f8f;\">OA系统</a>";
        content += "<span style=\"font-size:10px;color:#908f8f;\">自动发送，请勿回复。</span>";
        mail.setContent(content);
        mail.setContentType(Mail.ContentType.HTML);

        try {
            mailUtil.send(mail);
            TbMail tbMail = new TbMail(leader.getName(), leader.getEmail(), "考勤处理提醒", actId, content);
            record(tbMail);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    String getApplyType(String num) {
        String[] vacationArr = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08"};
        if (Arrays.asList(vacationArr).contains(num)) {
            return "请假";
        } else if ("20".equals(num)) {
            return "出差";
        } else if ("21".equals(num)) {
            return "外出";
        } else if ("22".equals(num)) {
            return "加班";
        } else if ("23".equals(num)) {
            return "忘打卡";
        } else if ("30".equals(num)) {
            return "下载";
        } else if ("31".equals(num)) {
            return "付款信息";
        } else {
            return "未知";
        }
    }
}
