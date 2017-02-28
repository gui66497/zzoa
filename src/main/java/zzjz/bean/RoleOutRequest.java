package zzjz.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @ClassName: RoleOutRequest
 * @Description: 外部角色request
 * @author 房桂堂
 * @date 2016/7/26 15:20
 */
@XmlRootElement
public class RoleOutRequest extends BaseRequest {

    long systemId;      //系统id
    String userId;      //用户id
    String systemSign;  //系统标识

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSystemSign() {
        return systemSign;
    }

    public void setSystemSign(String systemSign) {
        this.systemSign = systemSign;
    }
}
