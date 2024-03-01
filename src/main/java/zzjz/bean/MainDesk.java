package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 彭鹏
 * @version 2017年05月09日11:24
 */
@XmlRootElement
public class MainDesk {
    private Integer id;

    private String actId;

    private String userId;

    private String userName;

    private String departId;

    private String departName;

    private String name;

    private String applyDate;

    private String applyTyp;

    private String applyDay;

    private String createTime;

    private String status;

    /**
     * 结束时间
     */
    private String endTime;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

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
     * getActId.
     *
     * @return ACT_ID
     */
    public String getActId() {
        return actId;
    }

    /**
     * setActId.
     *
     * @param actId ACT_ID
     */
    public void setActId(String actId) {
        this.actId = actId;
    }

    /**
     * getUserId.
     *
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setUserId.
     *
     * @param userId USER_ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
        this.userName = userName;
    }

    /**
     * getDepartId.
     *
     * @return DEPART_ID
     */
    public String getDepartId() {
        return departId;
    }

    /**
     * setDepartId.
     *
     * @param departId DEPART_ID
     */
    public void setDepartId(String departId) {
        this.departId = departId;
    }

    /**
     * getName.
     *
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * setName_.
     *
     * @param name NAME
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getApplyDate.
     *
     * @return APPLY_DATE
     */
    public String getApplyDate() {
        return applyDate;
    }

    /**
     * setApplyDate.
     *
     * @param applyDate APPLY_DATE
     */
    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    /**
     * getApplyTyp.
     *
     * @return APPLY_TYP
     */
    public String getApplyTyp() {
        return applyTyp;
    }

    /**
     * setApplyTyp.
     *
     * @param applyTyp APPLY_TYP
     */
    public void setApplyTyp(String applyTyp) {
        this.applyTyp = applyTyp;
    }

    /**
     * getApplyDay.
     *
     * @return APPLY_Day
     */
    public String getApplyDay() {
        return applyDay;
    }

    /**
     * setApplyDay.
     *
     * @param applyDay APPLY_DAY
     */
    public void setApplyDay(String applyDay) {
        this.applyDay = applyDay;
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
        this.createTime = createTime;
    }

    /**
     * getDepartName.
     *
     * @return DEPART_NAME
     */
    public String getDepartName() {
        return departName;
    }

    /**
     * setDepartName.
     *
     * @param departName DEPART_NAME
     */
    public void setDepartName(String departName) {
        this.departName = departName;
    }

    /**
     * getStatus.
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus.
     *
     * @param status STATUS
     */
    public void setStatus(String status) {
        this.status = status;
    }
}