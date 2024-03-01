package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zzjz.bean.Mail;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import zzjz.bean.Mail.ContentType;

/**
 * 邮件发送工具类.
 *
 * @author fgt
 * @version 2017/6/22 13:56
 */
@Component
public class MailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

    private Session session;

    /*public MailUtil() {
        init();
    }*/

    /**
     * 初始化.
     */
    public void init() {
        LOGGER.info("初始化邮件服务。。。");
        // 定义属性对象
        Properties props = new Properties();
        try {
            props.load(MailUtil.class.getClassLoader().getResourceAsStream("props/system.properties"));
        } catch (IOException e) {
            LOGGER.info("加载配置文件出错。。。");
            e.printStackTrace();
        }

        // 设置属性
        // 设置smtp服务器
        props.put("mail.smtp.host", props.getProperty("mail.smtp"));
        // 设置客户端是否向邮件服务器提交验证
        props.put("mail.smtp.auth", props.getProperty("mail.auth"));
        // 定义验证对象
        Authenticator authenticator = null;
        // 设置验证信息
        if ("true".equals(props.getProperty("mail.auth"))) {
            authenticator = new LocalAuthenticator(props.getProperty("mail.username"), props.getProperty("mail.password"));
        }
        session = Session.getInstance(props, authenticator);
    }

    /**
     * 发送邮件.
     *
     * @param email email
     * @return 结果
     * @throws MessagingException MessagingException
     * @throws IOException        IOException
     */
    public boolean send(Mail email) throws MessagingException, IOException {

        // 定义发送信息的对象
        Message msg = new MimeMessage(session);
        // 设置发信人地址
        if (StringUtils.isNotEmpty(email.getSender())) {
            msg.setFrom(new InternetAddress(email.getSender()));
        } else {
            msg.setFrom();
        }
        // 设置标题
        msg.setSubject(email.getSubject());
        // 获取收信人列表
        String[] addressees = email.getAddressees();
        // 非空验证
        Assert.assertNotNull(addressees);
        // 设置收信人
        InternetAddress[] toInternetAddressees = new InternetAddress[addressees.length];
        for (int i = 0; i < addressees.length; i++) {
            toInternetAddressees[i] = new InternetAddress(addressees[i]);
        }
        msg.setRecipients(RecipientType.TO, toInternetAddressees);
        // 设置抄送人
        String[] cc = email.getCc();
        if (cc != null && cc.length > 0) {
            // 设置收信人
            InternetAddress[] ccInternetAddressees = new InternetAddress[cc.length];
            for (int i = 0; i < cc.length; i++) {
                ccInternetAddressees[i] = new InternetAddress(cc[i]);
            }
            msg.setRecipients(RecipientType.CC, ccInternetAddressees);
        }
        // 设置暗送
        String[] bcc = email.getBcc();
        if (bcc != null && bcc.length > 0) {
            // 设置收信人
            InternetAddress[] bccInternetAddressees = new InternetAddress[bcc.length];
            for (int i = 0; i < bcc.length; i++) {
                bccInternetAddressees[i] = new InternetAddress(bcc[i]);
            }
            msg.setRecipients(RecipientType.BCC, bccInternetAddressees);
        }
        if (email.getContentType().equals(ContentType.TEXT)) {
            msg.setText(email.getContent());
            // 添加附件
            if (email.hasAffix()) {
                Multipart mp = new MimeMultipart();
                String[] affixName = email.getAffixName();
                for (int i = 0; i < affixName.length; i++) {
                    MimeBodyPart affix = new MimeBodyPart();
                    affix.attachFile(affixName[i]);
                    mp.addBodyPart(affix, i);
                }
                msg.setContent(mp);
            }
        } else if (email.getContentType().equals(ContentType.HTML)) {
            // 添加邮件内容
            BodyPart body = new MimeBodyPart();
            body.setContent(email.getContent(), "text/html;charset=utf-8");
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(body, 0);
            // 添加附件
            if (email.hasAffix()) {
                String[] affixName = email.getAffixName();
                for (int i = 0; i < affixName.length; i++) {
                    MimeBodyPart affix = new MimeBodyPart();
                    affix.attachFile(affixName[i]);
                    mp.addBodyPart(affix, i + 1);
                }
            }
            msg.setContent(mp);
        }
        // 设置发送时间
        msg.setSentDate(new Date(System.currentTimeMillis()));
        // 发送邮件
        Transport.send(msg);
        return true;

    }

    /*public static void main(String[] args) throws IOException {
        //Replace PDFConvert directory, source PDF file name and output directory
        String command = "\"D:\\software\\PDFConvert\\pdfconvert.exe\" " +
                "/cs 10000 /i \"C:\\Users\\Administrator\\Desktop\\22222.pdf\" /o \"C:\\Users\\Administrator\\Desktop\" /pwo 2";
        Process p = Runtime.getRuntime().exec(command);
        System.out.println("Conversion complete!");
    }*/
}
