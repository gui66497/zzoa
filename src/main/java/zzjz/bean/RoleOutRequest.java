package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 * @version 2016/7/26 15:20
 * @ClassName: RoleOutRequest
 * @Description: 外部角色request
 */
@XmlRootElement
public class RoleOutRequest extends BaseRequest {

    /**
     * 系统id
     */
    long systemId;

    /**
     * 用户id
     */
    String userId;

    /**
     * 系统标识
     */
    String systemSign;

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
     * getUserId.
     *
     * @param userId USER_ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
}
