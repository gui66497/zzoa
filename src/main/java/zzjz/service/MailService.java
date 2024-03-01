package zzjz.service;

import org.springframework.stereotype.Service;
import zzjz.bean.TbMail;

/**
 * @author 房桂堂
 * @version 2018年02月27日14:17
 * @ClassName: MailService
 * @Description: 邮件接口
 */
@Service
public interface MailService {

    /**
     * 记录一次邮件发送.
     * @param mail 邮件
     * @return 结果
     */
    boolean record(TbMail mail);

    /**
     * 判断能否发送邮件(邮件间隔为8小时).
     * @param tbMail tbMail
     * @return 结果
     */
    boolean canSend(TbMail tbMail);

    /**
     * 发送考勤提醒邮件到审批人 todo(将remindMail利用本方法精简优化).
     * @param actId 流程id
     * @return 结果
     */
    boolean sendMail(String actId);
}
