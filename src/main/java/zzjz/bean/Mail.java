package zzjz.bean;

import java.io.Serializable;

/**
 * Email 类：添加相关Email信息.
 *
 * @author fgt
 * @version 2017/6/22 13:53
 */
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件内容类型.
     *
     * @author lvyue
     */
    public enum ContentType {
        /**
         * TEXT.
         */
        TEXT,
        /**
         * HTML.
         */
        HTML
    }

    /**
     * 发件人
     */
    private String sender;

    /**
     * 收件人
     */
    private String[] addressees;

    /**
     * 抄送人
     */
    private String[] cc;

    /**
     * 安送人
     */
    private String[] bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 文本内容类型
     */
    private ContentType contentType = ContentType.TEXT;

    /**
     * 附件
     */
    private boolean affix;

    /**
     * 附件地址
     */
    private String[] affixName;

    /**
     * 获取发信人地址.
     *
     * @return String
     */
    public String getSender() {
        return sender;
    }

    /**
     * 设置发信人地址.
     *
     * @param sender sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * 获取收信人地址.
     *
     * @return String[]
     */
    public String[] getAddressees() {
        if (addressees == null) {
            return null;
        }
        return addressees.clone();
    }

    /**
     * 设置收信人地址.
     *
     * @param addressees addressees
     */
    public void setAddressees(String[] addressees) {
        if (addressees == null) {
            this.addressees = null;
        } else {
            this.addressees = addressees.clone();
        }
    }

    /**
     * 获取邮件主题.
     *
     * @return String
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置邮件主题.
     *
     * @param subject subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取邮件内容.
     *
     * @return String
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件内容.
     *
     * @param content content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取邮件类型.
     *
     * @return ContentType
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * 设置邮件类型.
     *
     * @param contentType contentType
     */
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * 是否带有附件.
     *
     * @return boolean
     */
    public boolean hasAffix() {
        return affix;
    }

    /**
     * 获取附件地址.
     *
     * @return String[]
     */
    public String[] getAffixName() {
        if (affixName == null) {
            return null;
        }
        return affixName.clone();
    }

    /**
     * 设置附件地址.
     *
     * @param affixName affixName
     */
    public void setAffixName(String[] affixName) {
        if (affixName == null) {
            this.affixName = null;
        } else {
            this.affixName = affixName.clone();
        }
        affix = (this.affixName != null && this.affixName.length > 0);
    }

    /**
     * 获取抄送人.
     *
     * @return String[]
     */
    public String[] getCc() {
        if (cc == null) {
            return null;
        }
        return cc.clone();
    }

    /**
     * 抄送人.
     *
     * @param cc cc
     */
    public void setCc(String[] cc) {
        if (cc == null) {
            this.cc = null;
        } else {
            this.cc = cc.clone();
        }
    }

    /**
     * 获取暗送.
     *
     * @return String[]
     */
    public String[] getBcc() {
        if (bcc == null) {
            return null;
        }
        return bcc.clone();
    }

    /**
     * 添加暗送.
     *
     * @param bcc bcc
     */
    public void setBcc(String[] bcc) {
        if (bcc == null) {
            this.bcc = null;
        } else {
            this.bcc = bcc.clone();
        }
    }
}
