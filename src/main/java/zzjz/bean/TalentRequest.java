package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class TalentRequest extends BaseRequest {
    private Integer id;

    private String userName;

    private String sex;

    private String phone;

    private String email;

    private String type;

    private String keyword;

    private String source;

    private String area;

    private String resumeName;

    private String wordDesc;

    private String createTime;

    private String note;

    /**
     * getId.
     *
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * setId.
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * getUserName.
     *
     * @return USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setUserName.
     *
     * @param userName USER_NAME
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * getSex.
     *
     * @return SEX
     */
    public String getSex() {
        return sex;
    }

    /**
     * setSex.
     *
     * @param sex SEX
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * getPhone.
     *
     * @return PHONE
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setPhone.
     *
     * @param phone PHONE
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * getEmail.
     *
     * @return EMAIL
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail.
     *
     * @param email EMAIL
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getType.
     *
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    /**
     * setType.
     *
     * @param type TYPE
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * getKeyword.
     *
     * @return KEYWORD
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * setKeyword.
     *
     * @param keyword KEYWORD
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    /**
     * getSource.
     *
     * @return SOURCE
     */
    public String getSource() {
        return source;
    }

    /**
     * setSource.
     *
     * @param source SOURCE
     */
    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    /**
     * getArea.
     *
     * @return AREA
     */
    public String getArea() {
        return area;
    }

    /**
     * setArea.
     *
     * @param area AREA
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * getResumeName.
     *
     * @return RESUME_NAME
     */
    public String getResumeName() {
        return resumeName;
    }

    /**
     * setResumeName.
     *
     * @param resumeName RESUME_NAME
     */
    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    /**
     * getWordDesc.
     *
     * @return WORD_DESC
     */
    public String getWordDesc() {
        return wordDesc;
    }

    /**
     * setWordDesc.
     *
     * @param wordDesc WORD_DESC
     */
    public void setWordDesc(String wordDesc) {
        this.wordDesc = wordDesc;
    }

    /**
     * getCreateTime.
     *
     * @return CREATE_TIME
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime.
     *
     * @param createTime CREATE_TIME
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * getNote.
     *
     * @return NOTE
     */
    public String getNote() {
        return note;
    }

    /**
     * setNote.
     *
     * @param note NOTE
     */
    public void setNote(String note) {
        this.note = note;
    }
}