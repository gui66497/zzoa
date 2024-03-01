package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 房桂堂
 * @version 2016/7/26 13:41
 * @ClassName: PermissionOutRequest
 * @Description: 权限请求实体
 */
@XmlRootElement
public class PermissionOutRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 系统ID
     */
    private long systemId;

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
}
