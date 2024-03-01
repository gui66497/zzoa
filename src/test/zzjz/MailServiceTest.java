package zzjz;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zzjz.bean.TbMail;
import zzjz.service.MailService;

/**
 * @author 房桂堂
 * @version  2018/2/28 8:43
 */
public class MailServiceTest extends JunitBaseTest {

    @Autowired
    MailService mailService;

    /**
     *
     */
    @Test
    public void testRecord() {
        TbMail mail = new TbMail();
        mail.setReceiver("房桂堂");
        mail.setEmail("guitang.fang@zzjunzhi.com");
        mail.setType("aaa");
        mail.setrId("111");
        mail.setContent("12345");
        mailService.record(mail);
    }

    /**
     *
     */
    @Test
    public void testCanSend() {
        TbMail mail = new TbMail();
        mail.setEmail("xudong.li@zzjunzhi.com");
        mail.setType("考勤处理提醒");
        mail.setrId("305001");
        mailService.canSend(mail);
    }

    @Test
    public void testSendMail() {
        String actId = "305016";
        mailService.sendMail(actId);
    }

}
