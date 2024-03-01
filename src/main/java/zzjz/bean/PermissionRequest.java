package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author 曹雪东
 * @version 2015年5月9日 上午11:42:23
 * @ClassName: PermissionRequest
 * @Description: 权限request
 */
@XmlRootElement
public class PermissionRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 系统标示
     */
    private String systemSign;

    /**
     * 目标ID
     */
    private String targetId;

    /**
     * 系统ID
     */
    private long systemId;

    /**
     * getSystemId.
     *
     * @return SYSTEM_ID
     */
    public long getSystemId() {
        return systemId;
    }

    /**
     * setSystemId.
     *
     * @param systemId SYSTEM_ID
     */
    public void setSystemId(long systemId) {
        this.systemId = systemId;
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
     * getPermissionName.
     *
     * @return PERMISSION_NAME
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * setPermissionName.
     *
     * @param permissionName PERMISSION_NAME
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * getSystemName.
     *
     * @return SYSTEM_NAME
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * setSystemName.
     *
     * @param systemName SYSTEM_NAME
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * getSystemSign.
     *
     * @return SYSTEM_SIGN
     */
    public String getSystemSign() {
        return systemSign;
    }

    /**
     * setSystemSign.
     *
     * @param systemSign SYSTEM_SIGN
     */
    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }

    /**
     * getTargetId.
     *
     * @return TARGET_ID
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * setTargetId.
     *
     * @param targetId TARGET_ID
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }


}
 