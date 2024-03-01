package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 * @version 2017/9/6 8:43
 * @ClassName: DownRequest
 * @Description:
 */
@XmlRootElement
public class DownRequest extends BaseRequest {

    private Integer id;

    private String actId;

    private String userId;

    private String userName;

    private String reason;

    private Integer resourceId;

    private String resourceName;

    private String createTime;

    private String startTime;

    private String endTime;

    private String status;

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
     * getReason.
     *
     * @return REASON
     */
    public String getReason() {
        return reason;
    }

    /**
     * setReason.
     *
     * @param reason REASON
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * getResourceId.
     *
     * @return RESOURCE_ID
     */
    public Integer getResourceId() {
        return resourceId;
    }

    /**
     * setResourceId.
     *
     * @param resourceId RESOURCE_ID
     */
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * getResourceName.
     *
     * @return RESOURCE_NAME
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * setResourceName.
     *
     * @param resourceName RESOURCE_NAME
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
     * getStatus.
     *
     * @return STATUS
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

    /**
     * getStartTime.
     *
     * @return START_TIME
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * setStartTime.
     *
     * @param startTime START_TIME
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * getEndTime.
     *
     * @return END_TIME
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * setEndTime.
     *
     * @param endTime END_TIME
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
